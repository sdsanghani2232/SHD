package com.shd.halperclass.validationclass;

public class FormValidation {

    private final String designCode,mainType,subtype,customerCode,tempCode,workBy,workPlace;
    public interface Result{
        void onResult(String result);
    }

    public FormValidation(String designCode, String customerCode, String tempCode, String mainType, String subtype,String workBy,String workPlace) {
        this.designCode = designCode;
        this.mainType = mainType;
        this.subtype = subtype;
        this.customerCode = customerCode;
        this.tempCode = tempCode;
        this.workBy = workBy;
        this.workPlace =workPlace;
    }

    public void validate(Result result)
    {
        if(!designCode.isEmpty() && designCode.trim().length() <5)
        {
            result.onResult("Small Design Code");
            return;
        }
        if (!customerCode.isEmpty() && customerCode.trim().length() <5) {
            result.onResult("Small Customer Code");
           return;
        }
        if(designCode.isEmpty() && customerCode.isEmpty() && tempCode.isEmpty()) {
            result.onResult("Empty Temp Code");
            return;
        } else if (designCode.isEmpty() && customerCode.isEmpty() && tempCode.trim().length() < 5) {
            result.onResult("Small Temp Code");
            return;
        }

        if (mainType.isEmpty()) {
            result.onResult("Empty Main Type");
            return;
        }else if((mainType.equals("Ring") || mainType.equals("NKC") || mainType.equals("ER") || mainType.equals("BR"))&& (subtype.isEmpty() || subtype.equals("Sub Type")))
        {
            result.onResult("Empty sub Type");
            return;
        }

        if (workBy.isEmpty()) result.onResult("work by empty");
        if (workPlace.isEmpty()) result.onResult("work place empty");

        result.onResult("NO Error");

    }
}
