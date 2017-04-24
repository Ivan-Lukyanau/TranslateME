package com.example.ivan_lukyanau.translateme.Models;

/**
 * Created by Ivan_Lukyanau on 4/14/2017.
 */

public class InputValidationModelResult {
    private String message;
    private boolean isValid;

    public String GetMessage(){
        return this.message;
    }
    public boolean IsValid(){
        return this.isValid;
    }

    public InputValidationModelResult(boolean isValid, String message){
        this.isValid = isValid;
        this.message = message;
    }
}
