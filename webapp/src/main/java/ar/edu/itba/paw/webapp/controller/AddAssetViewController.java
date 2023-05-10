package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.InternalErrorException;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.userContext.implementations.Behaviour;
import ar.edu.itba.paw.webapp.formFactories.FormFactoryAddAssetView;
import ar.edu.itba.paw.webapp.form.AddAssetForm;
import ar.edu.itba.paw.webapp.form.SnackbarControl;
import ar.edu.itba.paw.interfaces.AssetExistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collection;

@Controller
final public class AddAssetViewController {


    private final UserService userService;
    private final AssetExistanceService assetExistanceService;
    private final static String viewName = "views/addAssetView";

    private final static String SUCESS_MSG = "Libro agregado exitosamente!";

    @RequestMapping(value = "/addAsset", method = RequestMethod.POST)
    public ModelAndView addAsset(@RequestParam(name ="file") MultipartFile image,
                                 @Valid @ModelAttribute final AddAssetForm addAssetForm,
                                 final BindingResult errors,
                                 Model model, HttpServletResponse response) {

        response.setContentType("text/html; charset=UTF-8");

        if(errors.hasErrors() || image.isEmpty())
            return addAssetView(addAssetForm)
                    .addObject("showSnackbarInvalid", true)
                    .addObject("snackBarInvalidTextTitle",  image.isEmpty() ? "Falta la imagen \n" : "");

        String email = userService.getCurrentUser();
        try {
            assetExistanceService.addAssetInstance(FormFactoryAddAssetView.createAssetInstance(addAssetForm, email), handleImage(image));
            ModelAndView addAssetView = addAssetView(addAssetForm);
            SnackbarControl.displaySuccess(addAssetView, SUCESS_MSG);
            return addAssetView;

        }catch (InternalErrorException e) {
            return addAssetView(addAssetForm)
                    .addObject("showSnackbarInvalid", true)
                    .addObject("snackBarInvalidTextTitle", "Hubo un error guardando el libro");
        }
    }

    private static byte[] handleImage(MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                return file.getBytes();
            } catch (Exception e) {
                //
            }
        }
        return null;
    }

    @Autowired
    public AddAssetViewController(UserService userService, AssetExistanceService assetExistanceService){
        this.userService = userService;
        this.assetExistanceService = assetExistanceService;
    }
    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("path", "addAsset");
    }
    @RequestMapping( value = "/addAssetView",  method = RequestMethod.GET)
    public ModelAndView addAssetView(@ModelAttribute("addAssetForm") final AddAssetForm addAssetForm){

        final ModelAndView mav = new ModelAndView(viewName);
        Collection<? extends GrantedAuthority> auth =  userService.getCurrentRoles();
        if(auth.contains(new SimpleGrantedAuthority("ROLE_BORROWER"))){
            mav.addObject("borrowerUser","true");
        }else {
            mav.addObject("borrowerUser","false");
        }
        return  mav;
    }

}
