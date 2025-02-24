package com.safetynet.safetynetalerts.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public  class FireStationInfo {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;

}