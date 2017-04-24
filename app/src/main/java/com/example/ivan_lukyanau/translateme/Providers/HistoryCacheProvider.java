package com.example.ivan_lukyanau.translateme.Providers;

import android.content.SharedPreferences;

import com.example.ivan_lukyanau.translateme.MainActivity;
import com.example.ivan_lukyanau.translateme.Models.WordDescription;
import com.example.ivan_lukyanau.translateme.Storage.StorageHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ivan_Lukyanau on 4/14/2017.
 */

public class HistoryCacheProvider {
    private final SharedPreferences myPreferences;
    private final StorageHelper storageHelper;
    public HistoryCacheProvider(SharedPreferences myPreferences){
        this.myPreferences = myPreferences;
        this.storageHelper = new StorageHelper(this.myPreferences);
    }

    public WordDescription FindInCache(String inputText){
        // get full history
        List<WordDescription> history = storageHelper.getList(MainActivity.APP_PREFERENCES_HISTORY);
        List<WordDescription> favorites = storageHelper.getList(MainActivity.APP_PREFERENCES_FAVORITES);
        List<WordDescription> mergedList = mergeLists(history, favorites);

        for (WordDescription word:
                mergedList) {
            if(word.getWord().equalsIgnoreCase(inputText)){
                return word; // if we found the words then return it
            }
        }

        return null;
    }

    private List<WordDescription> mergeLists(final List<WordDescription> list1, final List<WordDescription> list2) {
        final Map<String, WordDescription> headersMap = new LinkedHashMap<String, WordDescription>();

        for (final WordDescription defHeader : list1) {
            headersMap.put(defHeader.getWord().toUpperCase(), defHeader);
        }

        for (final WordDescription ovrdHeader : list2) {
            headersMap.put(ovrdHeader.getWord().toUpperCase(), ovrdHeader);
        }

        return new ArrayList<WordDescription>(headersMap.values());
    }
}
