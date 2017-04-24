package com.example.ivan_lukyanau.translateme.Storage;

import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;
import com.example.ivan_lukyanau.translateme.MainActivity;
import com.example.ivan_lukyanau.translateme.Models.WordDescription;


/**
 * Created by Ivan_Lukyanau on 4/1/2017.
 */

public class StorageHelper {
    SharedPreferences myPreferences;
    public StorageHelper(SharedPreferences preferences){
        this.myPreferences = preferences;
    }

    public void cleanHistory(){
        SharedPreferences.Editor editor = myPreferences.edit();
        editor.remove(MainActivity.APP_PREFERENCES_HISTORY);
        editor.apply();
    }

    public void cleanFavorites(){
        SharedPreferences.Editor editor = myPreferences.edit();
        editor.remove(MainActivity.APP_PREFERENCES_FAVORITES);
        editor.apply();
    }

    public String getPreferencesString(String appPreferencesKey){
        String result = "";

        if (myPreferences.contains(appPreferencesKey)) {
            result = myPreferences.getString(appPreferencesKey, "");
        }

        return result;
    }

    public String[] getStringArray(String appPreferencesKey){
        String initialStr = getPreferencesString(appPreferencesKey);
        if(!initialStr.isEmpty()){
            String[] resultArray = initialStr.split(":");
            return resultArray;
        } else{
            return null;
        }
    }

    public List<WordDescription> getList(String appPreferencesKey){
        String[] stringArray = this.getStringArray(appPreferencesKey);
        List<WordDescription> newList = new ArrayList<WordDescription>();
        if(stringArray != null){
            String[] elementAsArray;
            WordDescription item;

            for (int i = 0; i < stringArray.length; i++){
                String element = stringArray[i];
                elementAsArray = element.split("[|]");

                item = new WordDescription(
                        elementAsArray[0],
                        elementAsArray[1],
                        elementAsArray[2],
                        elementAsArray[3]
                );

                newList.add(item);
            }
        }

        return newList;
    }

    private void editUpdatePreferences(List<WordDescription> collection, String collectionName){
        SharedPreferences.Editor editor = myPreferences.edit();
        String toPush = GetResultStringToPush(collection);
        editor.putString(collectionName, toPush);
        editor.apply();
    }

    public void addToHistory(String word, String translation){

        // define cyrillic
        StringHelper strHelper = new StringHelper();
        boolean isCyrillic = strHelper.StartWithCyrillic(word);
        String direction = isCyrillic ? "RU-EN" : "EN-RU";

        List<WordDescription> list = this.getList(MainActivity.APP_PREFERENCES_HISTORY);

        if(list.size() > 0){
            if(!isExists(list, word)){
                // check whether thi word already in favorites
                boolean alreadyExistsInFavorites = this.isExistsInCollection(MainActivity.APP_PREFERENCES_FAVORITES, word);
                list.add(new WordDescription(word, translation, direction, alreadyExistsInFavorites));
                this.editUpdatePreferences(list, MainActivity.APP_PREFERENCES_HISTORY);
            }
        }
        else{
            SharedPreferences.Editor editor = myPreferences.edit();

            // le't check whether exists the word already in favorites
            boolean alreadyExistsInFavorites = isExistsInCollection(MainActivity.APP_PREFERENCES_FAVORITES, word);
            //declare result string
            String resultString = String.format("%s|%s|%s|%s:", word, translation, direction, alreadyExistsInFavorites);
            editor.putString(MainActivity.APP_PREFERENCES_HISTORY, resultString);
            editor.apply();
        }
    }

    public void updateHistory(WordDescription word){
        List<WordDescription> list = this.getList(MainActivity.APP_PREFERENCES_HISTORY);

        for (WordDescription w:list) {
            if(w.getWord().equalsIgnoreCase(word.getWord())){
                w.setFav(word.getFav());
                break;
            }
        }

        this.editUpdatePreferences(list, MainActivity.APP_PREFERENCES_HISTORY);
    }

    public void addToFavorites(WordDescription word){

        List<WordDescription> list = this.getList(MainActivity.APP_PREFERENCES_FAVORITES);
        if(list.size() > 0){
            if(!isExists(list, word)){
                //push new
                list.add(word);
                // it needs to change state in preferences
                editUpdatePreferences(list, MainActivity.APP_PREFERENCES_FAVORITES);
            }
        }
        else{
            SharedPreferences.Editor editor = myPreferences.edit();
            String resultString = String.format("%s:", word.toString());
            editor.putString(MainActivity.APP_PREFERENCES_FAVORITES, resultString);
            editor.apply();
        }
    }

    public void removeFromFavorites(WordDescription word){

        List<WordDescription> list = getList(MainActivity.APP_PREFERENCES_FAVORITES);

        if(isExists(list, word)){
            List<WordDescription> listUpdated = new ArrayList<WordDescription>();
            for (WordDescription w:list) {
                if(!w.getWord().equalsIgnoreCase(word.getWord())){
                    listUpdated.add(w);
                }
            }

            editUpdatePreferences(listUpdated, MainActivity.APP_PREFERENCES_FAVORITES);
        }
    }

    public void untickAllInCollection(String collectionName){
        // get collection
        List<WordDescription> list = getList(collectionName);
        // create new list to push
        List<WordDescription> listUpdated = new ArrayList<WordDescription>();
        for (WordDescription w:list) {
            w.setFav(false); // set flag to false and push into new list
            listUpdated.add(w);
        }
        // update collection we are interested in
        editUpdatePreferences(listUpdated, collectionName);
    }

    private boolean isExistsInCollection(String collectionName, String word){
        List<WordDescription> collection = getList(collectionName);
        return isExists(collection, word);
    }

    private boolean isExists(List<WordDescription> collection, WordDescription word){
        for (WordDescription w:collection) {
            if(w.getWord().equalsIgnoreCase(word.getWord())){
                return true;
            }
        }
        return false;
    }

    private boolean isExists(List<WordDescription> collection, String word){
        for (WordDescription w:collection) {
            if(w.getWord().equalsIgnoreCase(word)){
                return true;
            }
        }
        return false;
    }

    private String GetResultStringToPush(List<WordDescription> collection){
        StringBuilder toPush = new StringBuilder();
        for (WordDescription w : collection){
            toPush.append(w.toString()+":");
        }
        return toPush.toString();
    }

}

