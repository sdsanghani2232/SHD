package com.shd.model;

import java.util.ArrayList;
import java.util.List;

public class TempCode {
    List<String> codes = new ArrayList<>();

    public TempCode() {
    }

    public TempCode(List<String> codes) {
        this.codes = codes;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }
}
