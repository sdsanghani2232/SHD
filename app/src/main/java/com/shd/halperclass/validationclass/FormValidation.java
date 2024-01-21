package com.shd.halperclass.validationclass;

public class FormValidation {

    String designCode,mainType,subtype,customerCode,tempCode;
    public interface Result{
        void onResult(String result);
    }

    public FormValidation(String designCode, String customerCode, String tempCode, String mainType, String subtype) {
        this.designCode = designCode;
        this.mainType = mainType;
        this.subtype = subtype;
        this.customerCode = customerCode;
        this.tempCode = tempCode;
    }

    public void validate(Result result)
    {
        boolean isError = false;
        if(!designCode.isEmpty() && designCode.trim().length() <5)
        {
            result.onResult("Small Design Code");
            isError = true;
        }
        if (!customerCode.isEmpty() && customerCode.trim().length() <5) {
            result.onResult("Small Customer Code");
            isError = true;
        }
        if(designCode.isEmpty() && customerCode.isEmpty() && tempCode.isEmpty()) {
            result.onResult("Empty Temp Code");
            isError = true;
        } else if (designCode.isEmpty() && customerCode.isEmpty() && tempCode.trim().length() < 5) {
            result.onResult("Small Temp Code");
            isError = true;
        }

        if (mainType.isEmpty()) {
            result.onResult("Empty Main Type");
            isError = true;
        }else if((mainType.equals("Ring") || mainType.equals("NKC") || mainType.equals("ER") || mainType.equals("BR"))&& (subtype.isEmpty() || subtype.equals("Sub Type")))
        {
            result.onResult("Empty sub Type");
            isError = true;
        }

        if(!isError)
        {
            result.onResult("NO Error");
        }
    }
}
