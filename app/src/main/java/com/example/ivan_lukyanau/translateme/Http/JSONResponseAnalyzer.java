package com.example.ivan_lukyanau.translateme.Http;
import android.provider.Telephony;

import com.example.ivan_lukyanau.translateme.Models.TranslationModelResult;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 * Created by Ivan_Lukyanau on 4/5/2017.
 */
/**  there is some method to interact with yandex api and lingualeo api here **/
public class JSONResponseAnalyzer {
    private final JSONParser parser;

    public JSONResponseAnalyzer(){
        parser = new JSONParser();
    }

    ///GET TRANSLATION EN-RU THROUGH THE LINGUALEO API
    public String getEN2RUTranslationResult(String externalJsonString) throws ParseException {
        JSONObject jsonObject = (JSONObject) parser.parse(externalJsonString);
        JSONArray arrayOfResults = (JSONArray)jsonObject.get("translate");

        int idxInResultArray = getRandomNumberInRange(arrayOfResults.size() /* result array LENGTH*/);
        JSONObject resultObject = (JSONObject) arrayOfResults.get(idxInResultArray);
        return resultObject.get("value").toString();
    }

    ///GET TRANSLATION RU-EN THROUGH THE LINGUALEO API
    public String getRU2ENTranslationResult(String externalJsonString) throws ParseException {
        //result as JSON Object
        JSONObject jsonObject = (JSONObject) parser.parse(externalJsonString);
        // object of translation result
        return jsonObject.get("translation").toString();

    }

    ///GET IMAGE URL THROUGH THE MICROSOFT COGNITIVE SERVICE
    public String getImageResult(String externalJsonString) throws ParseException{
        JSONObject jsonObject = (JSONObject) parser.parse(externalJsonString);
        JSONArray arrayOfResults = (JSONArray)jsonObject.get("value");

        int idxInResultArray = getRandomNumberInRange(arrayOfResults.size() /* result array LENGTH*/);
        JSONObject resultObject = (JSONObject) arrayOfResults.get(idxInResultArray);
        return resultObject.get("thumbnailUrl").toString();
    }

    /// GET AUDIO URL RESULT USING LINGUALEO API
    public String getAudioResult(String externalJsonString) throws ParseException{
        JSONObject jsonObject = (JSONObject) parser.parse(externalJsonString);
        return jsonObject.get("sound_url").toString();
    }

    /*
    * YANDEX RESPONSE ANALYZER
    *
    * */

    ///GET TRANSLATION using YANDEX API
    public TranslationModelResult getYandexTranslationResult(String externalJsonString) throws ParseException {
        TranslationModelResult result = null;

        // посмотреть код ответа
        //ParseException = проверка на ошибки
        //NumberFormatException
        JSONObject jsonObject = (JSONObject) parser.parse(externalJsonString);
        int codeResponse = Integer.parseInt(jsonObject.get("code").toString());

        switch (codeResponse){
            case 200: { /* Операция выполнена успешно */
                JSONArray arrayOfResults = (JSONArray)jsonObject.get("text");
                result = new TranslationModelResult(
                        (String) arrayOfResults.get(0),
                        true);
            }break;
            case 413: { /* Превышен максимально допустимый размер текста */
                result = new TranslationModelResult(false, "Text is toooooo long :-/. Please, try again!");
            }break;
            case 422: { /* Текст не может быть переведен */
                result = new TranslationModelResult(false, "Sorry, there is no translation for this text.");
            }break;
            case 401: /* Неправильный API-ключ */
            case 402: /* API-ключ заблокирован */
            case 404: /* Превышено суточное ограничение на объем переведенного текста */
            {
                result = new TranslationModelResult(false, "Sorry, something went wrong. Please, try again!");
            }break;
        }

        return  result;
    }

    /// GET RANDOM IF MIN EQUALS ZERO
    private Integer getRandomNumberInRange( int max ){
        return (int)(Math.random() * max); //formula : R * (max - min) + min
    }
}
