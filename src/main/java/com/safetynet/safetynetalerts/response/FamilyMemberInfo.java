package com.safetynet.safetynetalerts.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class FamilyMemberInfo {

    private String firstName;
    private String lastName;


    public FamilyMemberInfo(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
