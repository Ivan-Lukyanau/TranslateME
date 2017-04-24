package com.example.ivan_lukyanau.translateme.Storage;

/**
 * Created by Ivan_Lukyanau on 4/5/2017.
 */

public class StringHelper {

    public boolean DetectCyrillic(String text){
        for(int i = 0; i < text.length(); i++) {
            if(!Character.UnicodeBlock.of(text.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC)) {
                return false;
            }
        }

        return true;
    }

    public boolean StartWithCyrillic(String word){
        if(word == null){
            throw new IllegalArgumentException("word is null");
        }
        return Character.UnicodeBlock.of(word.charAt(0)).equals(Character.UnicodeBlock.CYRILLIC);
    }
}