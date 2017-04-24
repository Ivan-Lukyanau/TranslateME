package com.example.ivan_lukyanau.translateme.Providers;

import com.example.ivan_lukyanau.translateme.Storage.EnDecoderComponent;

/**
 * Created by Ivan_Lukyanau on 4/12/2017.
 */

public class YandexTranslatorAPIProvider extends BaseTranslatorAPIProvider implements TranslatorAPIProvider {

    private final String yandexKey = "trnsl.1.1.20170411T130048Z.59d5288f310cda94.1c119da83a982da932aae3007d5df658ae361b06";
    private final String baseUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=";

    public YandexTranslatorAPIProvider(String inputText) {
        super(inputText);
    }

    ///
    /// RETURN AUTO DETECTED DIRECTION OF TRANSLATION API LINK
    ///
    @Override
    public String getEndpoint() {
        String readyString = EnDecoderComponent.encodeURI(inputText);

        ///  IF CYRILLIC RETURN APPROPRIATE LINK TO HTTP CLIENT
        if (stringHelper.DetectCyrillic(this.inputText)){
            return this.baseUrl + yandexKey +"&text="+ readyString +"&lang=ru-en";
        }
        else {
            return this.baseUrl + yandexKey +"&text="+ readyString +"&lang=en-ru";
        }
    }
}
