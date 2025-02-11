package com.safetynet.SafetyNetAlerts.Response;

import lombok.Data;
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


    @ToString
    @Getter
    @Setter
    public static class FamilyMemberInfo {
        private String firstName;
        private String lastName;


        public FamilyMemberInfo(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }


    @Data
    public static class ChildInfo {
        private String firstName;
        private String lastName;
        private int age;


        public ChildInfo(String firstName, String lastName, int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }
    }
}




