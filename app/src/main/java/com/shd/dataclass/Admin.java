package com.shd.dataclass;

import org.checkerframework.checker.units.qual.A;

public class Admin {
    private String email;

    public Admin() {
    }


    public Admin(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
