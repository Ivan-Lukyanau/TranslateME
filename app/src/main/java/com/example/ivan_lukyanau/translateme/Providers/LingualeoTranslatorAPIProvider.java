package com.example.ivan_lukyanau.translateme.Providers;

import com.example.ivan_lukyanau.translateme.Storage.EnDecoderComponent;

/**
 * Created by Ivan_Lukyanau on 4/12/2017.
 */

public class LingualeoTranslatorAPIProvider extends BaseTranslatorAPIProvider implements TranslatorAPIProvider {

    public LingualeoTranslatorAPIProvider(String inputText) {
        super(inputText);
    }

    @Override
    public String getEndpoint() {
        String readyString = EnDecoderComponent.encodeURI(inputText);

        if (stringHelper.DetectCyrillic(inputText)){
            return "http://api.lingualeo.com/translate.php?q="+ readyString +"&source=ru&target=en";
        }
        else {
            return "http://api.lingualeo.com/gettranslates?word="+ readyString;
        }
    }
}
