package com.example.ivan_lukyanau.translateme.Validator;

import com.example.ivan_lukyanau.translateme.Models.InputValidationModelResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ivan_Lukyanau on 4/14/2017.
 */

public class InputTextValidator {

    public InputValidationModelResult Validate(String inputString){
        // если ничего не введено
        if(inputString.isEmpty() || inputString == null){
            return new InputValidationModelResult(false, "Please, type text to translate.");
        }

        // если введены только цифры
        Pattern patternNumbers = Pattern.compile("^[0-9]+$");
        Matcher matcher = patternNumbers.matcher(inputString);
        if(matcher.matches()){
            return new InputValidationModelResult(false, "Sorry, there is no translation only for numbers.");
        }

        //если введены латиница и кириллица вместе
        Pattern patternLatin = Pattern.compile("\\w*[a-zA-Z]+\\w*");
        Pattern patternCyrillic = Pattern.compile("\\w*[а-яА-ЯёЁ]+\\w*");
        Matcher matcherLatin = patternLatin.matcher(inputString);
        Matcher matcherCyrillic = patternCyrillic.matcher(inputString);
        if(matcherLatin.find() && matcherCyrillic.find()){
            return new InputValidationModelResult(false, "Cannot translate. Please enter only one language.");
        }

        // если введены цифры и буквы без пробелов
        Pattern pattern = Pattern.compile("^([0-9]+[a-zA-Z]+)|([a-zA-Z]+[0-9]+)|([0-9]+[а-яА-ЯёЁ]+)|([а-яА-ЯёЁ]+[0-9]+)$");
        Matcher matcherNumALph = pattern.matcher(inputString);
        if(matcherNumALph.find()){
            return new InputValidationModelResult(false, "Cannot translate. Please, try again.");
        }

        return null;
    }
}
