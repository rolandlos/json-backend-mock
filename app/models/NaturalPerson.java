package models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NaturalPerson {

    private final String firstName;
    private final String lastName;
    private final String callName;
    private final String originalName;
    private final String birthDate;
    private final BaseData nationality;
    private final BaseData placeOfOrigin;
    private final String profession;
    private final BaseData identificationType;
    private final String identificationNumber;
    private final String street;
    private final String houseNumber;
    private final BaseData place;
    private final BaseData country;
    private final BaseData phone1Type;
    private final String phone1Number;
    private final BaseData phone2Type;
    private final String phone2Number;
    private final String eMail;
    private final String additionalInformation;

}
