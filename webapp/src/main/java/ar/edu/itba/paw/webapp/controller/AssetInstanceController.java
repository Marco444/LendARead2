package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.assetExistanceContext.implementations.AssetInstanceImpl;
import ar.edu.itba.paw.models.assetExistanceContext.implementations.AssetInstanceReview;
import ar.edu.itba.paw.models.assetExistanceContext.implementations.PhysicalCondition;
import ar.edu.itba.paw.models.assetLendingContext.implementations.AssetState;
import ar.edu.itba.paw.models.viewsContext.implementations.PagingImpl;
import ar.edu.itba.paw.models.viewsContext.implementations.SearchQueryImpl;
import ar.edu.itba.paw.models.viewsContext.implementations.Sort;
import ar.edu.itba.paw.models.viewsContext.implementations.SortDirection;
import ar.edu.itba.paw.models.viewsContext.interfaces.Page;
import ar.edu.itba.paw.webapp.dto.AssetInstanceReviewDTO;
import ar.edu.itba.paw.webapp.dto.AssetsInstancesDTO;
import ar.edu.itba.paw.webapp.form.AssetInstanceForm;
import ar.edu.itba.paw.webapp.form.AssetInstancePatchForm;
import ar.edu.itba.paw.webapp.form.AssetInstanceReviewForm;
import ar.edu.itba.paw.webapp.miscellaneous.PaginatedData;
import ar.edu.itba.paw.webapp.miscellaneous.Vnd;
import com.sun.istack.internal.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@Path("assetInstance")
public class AssetInstanceController {


    private final UserAssetInstanceService uais;
    private final AssetInstanceService ais;

    private final AssetExistanceService aes;

    private final LocationsService ls;

    private final UserService us;
    private final AssetInstanceReviewsService air;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public AssetInstanceController(final UserAssetInstanceService uais, final AssetInstanceService ais,final AssetExistanceService aes,final LocationsService ls,final UserService us,final AssetInstanceReviewsService air) {
        this.uais = uais;
        this.ais = ais;
        this.aes = aes;
        this.ls = ls;
        this.us = us;
        this.air = air;
    }

    @GET
    @Path("/{id}")
    @Produces(value = {Vnd.VND_ASSET_INSTANCE})
    public Response getUserAssetsInstances(@PathParam("id") final int id) throws AssetInstanceNotFoundException {
        final AssetInstanceImpl assetInstance = ais.getAssetInstance(id);
        AssetsInstancesDTO assetDTO = AssetsInstancesDTO.fromAssetInstance(uriInfo,assetInstance);
        return Response.ok(assetDTO).build();
    }
    @GET
    @Path("/{id}/image")
    @Produces(value = {"image/webp"})
    public Response getImage(@PathParam("id") final int id) throws AssetInstanceNotFoundException {
        final AssetInstanceImpl assetInstance = ais.getAssetInstance(id);

        EntityTag eTag = new EntityTag(String.valueOf(assetInstance.getImage()));

        byte[] profileImage =   assetInstance.getImage().getPhoto();
        return Response.ok(profileImage).tag(eTag).build();
    }

    @GET
    @Produces(value = {Vnd.VND_ASSET_INSTANCE_SEARCH})
    public Response getAssetsInstances( @QueryParam("search") @Nullable @Size(min = 1, max = 100) String search,
                                         @QueryParam("physicalConditions")@Nullable  List<String> physicalConditions,
                                         @QueryParam("languages") @Nullable List<String> languages,
                                         @QueryParam("sort")  @Nullable @Pattern(regexp = "AUTHOR_NAME|TITLE_NAME|RECENT|DEFAULT") String sort,
                                         @QueryParam("sortDirection")  @Nullable @Pattern(regexp = "ASCENDING|DESCENDING|DEFAULT") String sortDirection,
                                         @QueryParam("page") @Nullable @DefaultValue("1")  @Min(1) int currentPage,
                                         @QueryParam("minRating") @Nullable @DefaultValue("1")@Min(1) @Max(5) int minRating,
                                         @QueryParam("maxRating") @Nullable @DefaultValue("5") @Min(1) @Max(5)int maxRating,
                                         @QueryParam("itemsPerPage")@Nullable @DefaultValue("10") int itemsPerPage,
                                        @QueryParam("userId")  @DefaultValue("-1") int userId) throws  InternalErrorException, LocationNotFoundException {
        Page page = ais.getAllAssetsInstances(
                currentPage, itemsPerPage,
                new SearchQueryImpl(
                        (languages != null) ? languages : new ArrayList<>(),
                        (physicalConditions != null) ? physicalConditions : new ArrayList<>(),
                        (search != null) ? search : "",
                        (sort != null) ? Sort.fromString(sort) : Sort.RECENT,
                        (sortDirection != null) ? SortDirection.fromString(sort) : SortDirection.DESCENDING,
                        minRating,
                        maxRating,
                        userId
                        )
        );
        List<AssetsInstancesDTO> assetsInstancesDTO = AssetsInstancesDTO.fromAssetInstanceList(uriInfo, page.getBooks());
        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<AssetsInstancesDTO>>(assetsInstancesDTO) {});
        PaginatedData.paginatedData(response, page, uriInfo);

        return response.build();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(value = {Vnd.VND_ASSET_INSTANCE})
    public Response createAssetInstance( @Valid @BeanParam final AssetInstanceForm assetInstanceForm) throws UserNotFoundException, InternalErrorException, LocationNotFoundException {
        AssetInstanceImpl assetInstance = aes.addAssetInstance(PhysicalCondition.fromString(assetInstanceForm.getPhysicalCondition()),assetInstanceForm.getDescription(),assetInstanceForm.getMaxDays(),assetInstanceForm.getIsReservable(), AssetState.fromString(assetInstanceForm.getState()),assetInstanceForm.getLocationId(),assetInstanceForm.getAssetId(),assetInstanceForm.getImageBytes());
        final URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(assetInstance.getId())).build();
        return Response.created(uri).build();
    }

    @PATCH
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(value = {Vnd.VND_ASSET_INSTANCE})
    @Path("/{id}")
    public Response updateAssetInstance(@PathParam("id") final int id, @Valid @BeanParam final AssetInstancePatchForm assetInstancePatchForm) throws  AssetInstanceNotFoundException, ImageNotFoundException, LocationNotFoundException {
        ais.changeAssetInstance(id, Optional.ofNullable(assetInstancePatchForm.getPhysicalCondition()!= null? PhysicalCondition.fromString(assetInstancePatchForm.getPhysicalCondition()):null), Optional.ofNullable(assetInstancePatchForm.getMaxDays()), Optional.ofNullable(assetInstancePatchForm.getLocationId()), assetInstancePatchForm.getImageBytes(), Optional.ofNullable(assetInstancePatchForm.getDescription()), Optional.ofNullable(assetInstancePatchForm.getIsReservable()), Optional.ofNullable(assetInstancePatchForm.getState()));
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/reviews")
    @Produces(value ={Vnd.VND_ASSET_INSTANCE_REVIEW})
    public Response getAssetInstanceReviews(@PathParam("id") final int id, @QueryParam("page") @Nullable @DefaultValue("1") final int page, @QueryParam("itemsPerPage")@Nullable @DefaultValue("4") final int itemsPerPage) throws AssetInstanceNotFoundException {
        PagingImpl<AssetInstanceReview> reviews = air.getAssetInstanceReviewsById(page, itemsPerPage,id);
        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<AssetInstanceReviewDTO>>(AssetInstanceReviewDTO.fromAssetInstanceReviews(reviews.getList(),uriInfo)) {});
        PaginatedData.paginatedData(response, reviews, uriInfo);
        return response.build();
    }
    @POST
    @Path("/{id}/reviews")
    @Consumes(value = {Vnd.VND_ASSET_INSTANCE_REVIEW})
    @Produces(value = {Vnd.VND_ASSET_INSTANCE_REVIEW})
    public Response createAssetInstanceReview(@PathParam("id") final int id, @Valid @RequestBody final AssetInstanceReviewForm assetInstanceReviewForm) throws AssetInstanceNotFoundException, UserNotFoundException, LendingNotFoundException {
        AssetInstanceReview review = air.addReview(id,assetInstanceReviewForm.getLendingId(),assetInstanceReviewForm.getReview(),assetInstanceReviewForm.getRating());
        final URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(review.getId())).build();
        return Response.created(uri).build();
    }

    @GET
    @Path("/{id}/reviews/{idReview}")
    @Produces(value = {Vnd.VND_ASSET_INSTANCE_REVIEW})
    public Response getAssetInstanceReview(@PathParam("id") final int id, @PathParam("idReview") final int idReview) throws AssetInstanceNotFoundException, AssetInstanceReviewNotFoundException {
        AssetInstanceReview review = air.getReviewById(idReview);
        return Response.ok(AssetInstanceReviewDTO.fromAssetInstanceReview(review,uriInfo)).build();
    }
    @DELETE
    @Path("/{id}/reviews/{idReview}")
    public Response deleteReviewById(@PathParam("id") final int id, @PathParam("idReview") final int idReview) throws AssetInstanceNotFoundException, AssetInstanceReviewNotFoundException {
        air.deleteReviewById(idReview);
        return Response.noContent().build();
    }
}
