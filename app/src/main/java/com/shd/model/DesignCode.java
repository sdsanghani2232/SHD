package com.shd.model;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DesignCode {
    String designCode;
    private static DesignCode Instance;
    List<String> codeList = new ArrayList<>();
    public DesignCode(String designCode) {
        this.designCode = designCode;
    }

    public DesignCode() {
    }

    public static synchronized DesignCode getInstance()
    {
        if (Instance == null)
        {
            Instance = new DesignCode();
        }
        return Instance;
    }

//    get list on data base

}
