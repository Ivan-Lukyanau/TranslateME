package com.example.ivan_lukyanau.translateme.Models;

/**
 * Created by Ivan_Lukyanau on 4/5/2017.
 */

public class WordDescription {
    private String word, translation, direction;
    boolean isFav;

    public WordDescription(String word, String translation, String direction, boolean isFav){
        this.word = word;
        this.translation = translation;
        this.direction = direction;
        this.isFav = isFav;
    }

    public WordDescription(String word, String translation, String direction, String isFav){
        this.word = word;
        this.translation = translation;
        this.direction = direction;
        this.isFav = Boolean.parseBoolean(isFav);
    }

    public void setWord(String word){
        this.word = word;
    }

    public void setTranslation(String translation){
        this.translation = translation;
    }
    public void setDirection(String direction){
        this.direction = direction;
    }
    public void setFav(String isFav){
        this.isFav = Boolean.parseBoolean(isFav);
    }
    public void setFav(boolean isFav){
        this.isFav = isFav;
    }

    @Override
    public String toString(){
        return String.format("%s|%s|%s|%s", this.word, this.translation, this.direction, this.isFav);
    }

    public String getWord(){
        return this.word;
    }

    public String getTranslation() {
        return this.translation;
    }

    public String getDirection() {
        return this.direction;
    }

    public boolean getFav(){
        return this.isFav;
    }
}
