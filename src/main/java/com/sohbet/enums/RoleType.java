package com.sohbet.enums;

public enum RoleType {

    ROLE_ADMIN( "Administrator"),
    ROLE_ANONYMOUS( "Anonymous");

   
    private String name;

    private RoleType(String name) {
       
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

