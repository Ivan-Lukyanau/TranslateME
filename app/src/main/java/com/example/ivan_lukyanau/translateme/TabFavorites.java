package com.example.ivan_lukyanau.translateme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.ivan_lukyanau.translateme.Components.WordArrayAdapter;
import com.example.ivan_lukyanau.translateme.Components.BaseFragment;

/**
 * Created by Ivan_Lukyanau on 4/5/2017.
 */

public class TabFavorites extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.tab_favorites, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        listView = (ListView) getView().findViewById(R.id.favorites_list);
        populateCollection(MainActivity.APP_PREFERENCES_FAVORITES);
        adapter = new WordArrayAdapter(getView().getContext(), collection);
        listView.setAdapter(adapter);
    }
}
