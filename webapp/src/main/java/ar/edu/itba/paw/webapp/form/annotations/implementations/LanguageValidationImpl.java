package ar.edu.itba.paw.webapp.form.annotations.implementations;

import ar.edu.itba.paw.interfaces.LanguagesService;
import ar.edu.itba.paw.webapp.form.annotations.interfaces.Language;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LanguageValidationImpl implements ConstraintValidator<Language,String> {
    private final LanguagesService languagesService;
    @Autowired
    public LanguageValidationImpl(LanguagesService languagesService) {
        this.languagesService = languagesService;
    }


    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            languagesService.getLanguage(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
