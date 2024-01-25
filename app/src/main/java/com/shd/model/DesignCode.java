package com.shd.model;


import java.util.ArrayList;
import java.util.List;

public class DesignCode {
    List<String> codes = new ArrayList<>();

    public DesignCode() {
    }

    public DesignCode(List<String> codes) {
        this.codes = codes;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }
}
