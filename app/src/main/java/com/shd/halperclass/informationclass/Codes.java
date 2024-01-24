package com.shd.halperclass.informationclass;

import com.shd.model.DesignCode;
import com.shd.model.TempCode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Codes {

    private List<DesignCode> designCodes = new ArrayList<>();
    private List<TempCode> tempCodes = new ArrayList<>();
    private static Codes instance;
    public static synchronized Codes getInstance()
    {
        if(instance == null)
        {
            instance = new Codes();
        }
        return instance;
    }

    public void updateDesignCodes(List<DesignCode> designCodes)
    {
        this.designCodes = designCodes;
    }

    public void updateTempCodes(List<TempCode> tempCodes)
    {
        this.tempCodes = tempCodes;
    }

    public boolean isDesignCodeExists(String code)
    {
        AtomicBoolean exits = new AtomicBoolean(false);
            designCodes.forEach(code1 -> code1.getCodes().forEach(s -> {
                if(s.equals(code))
                {
                    exits.set(true);
                }
            }));
        return exits.get();
    }

    public boolean isTempCodeExits(String code)
    {
        AtomicBoolean exits = new AtomicBoolean(false);
            tempCodes.forEach(code1 -> code1.getCodes().forEach(s -> {
                if(s.equals(code))
                {
                    exits.set(true);
                }
            }));
        return exits.get();
    }
}
