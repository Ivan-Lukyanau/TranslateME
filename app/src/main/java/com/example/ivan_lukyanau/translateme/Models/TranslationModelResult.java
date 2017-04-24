package com.example.ivan_lukyanau.translateme.Models;

/**
 * Created by Ivan_Lukyanau on 4/15/2017.
 */

public class TranslationModelResult {
    private  String result;
    private final boolean isResolved;
    private String errorMessage;

    public TranslationModelResult(String result, boolean isResolved){
        this.isResolved = isResolved;
        this.result = result;
    }

    public TranslationModelResult(boolean isResolved, String errorMessage){
        this.isResolved = isResolved;
        this.errorMessage = errorMessage;
    }

    public String getResult(){ return this.result;}
    public String getErrorMessage(){ return this.errorMessage;}
    public boolean isResolved(){ return this.isResolved;}



}
