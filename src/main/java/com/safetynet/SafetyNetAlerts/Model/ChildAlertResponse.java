package com.safetynet.SafetyNetAlerts.Model;

import lombok.Data;


import java.util.List;


@Data
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


    @Data
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




