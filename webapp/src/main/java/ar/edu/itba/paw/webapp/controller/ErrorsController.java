package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Locale;

@ControllerAdvice
public class ErrorsController {


    private final MessageSource messageSource;

    @Autowired
    public ErrorsController(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @ExceptionHandler({
            UserNotFoundException.class,
            AssetInstanceNotFoundException.class,
            ImageNotFoundException.class,
            NoHandlerFoundException.class
    })
    public ModelAndView notFoundException() {
        ModelAndView mav = new ModelAndView("views/error");
        mav.addObject("errorTitle",messageSource.getMessage("error.notFound.Title",null, LocaleContextHolder.getLocale()));
        mav.addObject("errorSubtitle",messageSource.getMessage("error.notFound.Subtitle",null, LocaleContextHolder.getLocale()));
        return mav;
    }
    @ExceptionHandler({AssetInstanceBorrowException.class})
    public ModelAndView borrowAssetException(){
        ModelAndView mav = new ModelAndView("views/error");
        mav.addObject("errorTitle",messageSource.getMessage("error.borrowAsset.Title",null,LocaleContextHolder.getLocale()));
        mav.addObject("errorSubtitle",messageSource.getMessage("error.borrowAsset.Subtitle",null,LocaleContextHolder.getLocale()));
        return mav;
    }
    @ExceptionHandler({InternalErrorException.class, LendingCompletionUnsuccessfulException.class})
    public ModelAndView internalErrorException(){
        ModelAndView mav = new ModelAndView("views/error");
        mav.addObject("errorTitle",messageSource.getMessage("error.internalError.Title",null,LocaleContextHolder.getLocale()));
        mav.addObject("errorSubtitle",messageSource.getMessage("error.internalError.Subtitle",null,LocaleContextHolder.getLocale()));
        return mav;
    }
}
