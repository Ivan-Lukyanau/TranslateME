package com.example.ivan_lukyanau.translateme.Providers;

import com.example.ivan_lukyanau.translateme.Storage.StringHelper;

/**
 * Created by Ivan_Lukyanau on 4/12/2017.
 */

public abstract class BaseTranslatorAPIProvider {

    protected String inputText;
    protected StringHelper stringHelper;

    public BaseTranslatorAPIProvider(String inputText)
    {
        this.inputText = inputText;
        this.stringHelper = new StringHelper();
    }
}
