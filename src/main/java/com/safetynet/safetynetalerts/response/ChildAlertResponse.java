package com.safetynet.safetynetalerts.response;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.util.List;


@ToString
@Getter
@Setter
public class ChildAlertResponse {

    private String firstName;
    private String lastName;
    private int age;
    private List<FamilyMemberInfo> familyMembers;

    public ChildAlertResponse(String firstName, String lastName, int age, List<FamilyMemberInfo> familyMembers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.familyMembers = familyMembers;
    }

}




