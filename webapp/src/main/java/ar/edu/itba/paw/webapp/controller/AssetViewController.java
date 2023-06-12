package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.AssetInstanceBorrowException;
import ar.edu.itba.paw.exceptions.AssetInstanceNotFoundException;
import ar.edu.itba.paw.exceptions.DayOutOfRangeException;
import ar.edu.itba.paw.exceptions.UserNotFoundException;
import ar.edu.itba.paw.interfaces.AssetAvailabilityService;
import ar.edu.itba.paw.interfaces.AssetInstanceReviewsService;
import ar.edu.itba.paw.interfaces.AssetInstanceService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.assetExistanceContext.implementations.AssetInstanceImpl;
import ar.edu.itba.paw.models.assetExistanceContext.implementations.AssetInstanceReview;
import ar.edu.itba.paw.models.viewsContext.implementations.PagingImpl;
import ar.edu.itba.paw.webapp.form.BorrowAssetForm;
import ar.edu.itba.paw.webapp.form.SnackbarControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Controller
public class AssetViewController {
    private final static String SUCESS_MSG = "Libro agregado exitosamente!";

    private final AssetInstanceService assetInstanceService;
    private final AssetInstanceReviewsService assetInstanceReviewsService;
    private final AssetAvailabilityService assetAvailabilityService;
    private final UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AddAssetViewController.class);


    @Autowired
    public AssetViewController(final AssetInstanceService assetInstanceService, final AssetAvailabilityService assetAvailabilityService, final UserService userService, final AssetInstanceReviewsService assetInstanceReviewsService) {
        this.assetInstanceService = assetInstanceService;
        this.assetAvailabilityService = assetAvailabilityService;
        this.userService = userService;
        this.assetInstanceReviewsService = assetInstanceReviewsService;
    }

    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public ModelAndView assetInfoView(@PathVariable(name = "id") int id,
                                      @RequestParam(name = "reviewPage", required = false) Integer reviewPage,
                                      @ModelAttribute("borrowAssetForm") final BorrowAssetForm borrowAssetForm,
                                      @RequestParam(required = false, name = "success") final boolean success) throws AssetInstanceNotFoundException {
        AssetInstanceImpl assetInstanceOpt = assetInstanceService.getAssetInstance(id);

        if (assetInstanceService.isOwner(assetInstanceOpt, userService.getCurrentUser()))
            return new ModelAndView("redirect:/myBookDetails/" + assetInstanceOpt.getId());


        final ModelAndView mav = new ModelAndView("/views/assetView");
        if (success)
            SnackbarControl.displaySuccess(mav, SUCESS_MSG);


        PagingImpl<AssetInstanceReview> assetInstanceReviewPage = assetInstanceReviewsService.getAssetInstanceReviews((reviewPage != null) ? reviewPage : 1, 2, assetInstanceOpt);
        mav.addObject("assetInstanceReviewPage", assetInstanceReviewPage);
        mav.addObject("hasDescription", !assetInstanceOpt.getDescription().isEmpty());
        mav.addObject("hasReviews", !assetInstanceReviewPage.getList().isEmpty());
        mav.addObject("assetInstanceReviewAverage", assetInstanceReviewsService.getRatingById(assetInstanceOpt.getId()));
        mav.addObject("assetInstance", assetInstanceOpt);

        return mav;
    }

    @RequestMapping(value = "/requestAsset/{id}", method = RequestMethod.POST)
    public ModelAndView requestAsset(@PathVariable(name = "id") int id,
                                     @Valid @ModelAttribute final BorrowAssetForm borrowAssetForm,
                                     final BindingResult errors) throws AssetInstanceBorrowException, UserNotFoundException, AssetInstanceNotFoundException {
        if (errors.hasErrors()) {
            return assetInfoView(id, 1, borrowAssetForm, false);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        try {
            assetAvailabilityService.borrowAsset(id, userService.getCurrentUser(), LocalDate.parse(borrowAssetForm.getDate(), formatter));
            LOGGER.info("Borrow asset correct");

        } catch (DayOutOfRangeException ex) {
            LOGGER.warn("Cannot borrow asset because the de dayFormat");
            ModelAndView assetInfoView = assetInfoView(id, 1, borrowAssetForm, false);
            assetInfoView.addObject("dayError", true);
            return assetInfoView;
        }

        return new ModelAndView(String.format("redirect:/info/%d?success=%s", id, "true"));
    }


}
