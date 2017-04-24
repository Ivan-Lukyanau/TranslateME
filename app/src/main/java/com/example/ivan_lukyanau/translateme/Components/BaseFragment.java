package com.example.ivan_lukyanau.translateme.Components;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.example.ivan_lukyanau.translateme.Components.WordArrayAdapter;
import com.example.ivan_lukyanau.translateme.Models.WordDescription;
import com.example.ivan_lukyanau.translateme.Storage.StorageHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan_Lukyanau on 4/13/2017.
 */

public class BaseFragment extends Fragment {

    protected List<WordDescription> collection;
    protected ListView listView;
    protected WordArrayAdapter adapter;

    protected void populateCollection(String collectionName){
        StorageHelper storage = new StorageHelper(getActivity().getPreferences(Context.MODE_PRIVATE));

        List<WordDescription> array = storage.getList(collectionName);
        if (array == null || array.size() == 0){
            this.collection = new ArrayList<WordDescription>();
        } else {
            this.collection = array;
        }

    }
}
