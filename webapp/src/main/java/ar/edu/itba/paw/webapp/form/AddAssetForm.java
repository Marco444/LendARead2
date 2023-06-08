package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.annotations.interfaces.Isbn;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AddAssetForm {

    @Isbn
    private String isbn;

    @Size(min = 1, max = 100)
    private String language;

    @Size(min = 1, max = 100)
    private String author;

    @Size(min = 1, max = 1000)
    private String title;

    @Size(min = 1, max = 100)
    private String physicalCondition;

    @Size(min = 1, max = 100)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String zipcode;

    @Size(min = 20, max = 300)
    private String description;

    @Size(min = 1, max = 100)
    private String locality;

    @Size(min = 1, max = 100)
    private String province;

    @Size(min = 1, max = 100)
    private String country;

    @Min(value = 1)
    private int maxDays;

    private String languageSelect;
}
