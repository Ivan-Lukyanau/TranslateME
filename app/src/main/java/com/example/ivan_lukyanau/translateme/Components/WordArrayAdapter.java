package com.example.ivan_lukyanau.translateme.Components;

/**
 * Created by Ivan_Lukyanau on 4/6/2017.
 */
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivan_lukyanau.translateme.MainActivity;
import com.example.ivan_lukyanau.translateme.Models.WordDescription;
import com.example.ivan_lukyanau.translateme.R;
import com.example.ivan_lukyanau.translateme.Storage.StorageHelper;

/** Custom adapter for displaying an array of WordDescription objects. */
public class WordArrayAdapter extends ArrayAdapter<WordDescription> {

    List<WordDescription> items;
    private LayoutInflater inflater;

    public WordArrayAdapter(Context context, List<WordDescription> words) {
        super(context, R.layout.custom_row, words);
        //do cache the LayoutInflate to avoid asking for a new one each time.
        this.inflater = LayoutInflater.from(context);
        this.items = words;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        WordDescription word = (WordDescription) this.getItem(position);
        final CheckBox checkBox;
        TextView wordView;
        TextView translationView;
        TextView directionView;
        // Create a new row view
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_row, null);

            wordView = (TextView) convertView.findViewById(R.id.word);
            translationView = (TextView) convertView.findViewById(R.id.translation);
            directionView = (TextView) convertView.findViewById(R.id.direction);
            checkBox = (CheckBox) convertView.findViewById(R.id.CheckBox01);

            convertView.setTag(new WordDescriptionViewHolder(wordView, translationView, directionView, checkBox));

            checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    WordDescription word = (WordDescription) cb.getTag();
                    StorageHelper storage = new StorageHelper(getContext()
                            .getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE));

                    // MESSAGE FOR TOAST
                    String message = word.getFav() ? "removed from Favorites" : "added to Favorites";

                    word.setFav(checkBox.isChecked());

                    if(!cb.isChecked()){
                        storage.removeFromFavorites(word);
                    } else{
                        storage.addToFavorites(word);
                    }

                    storage.updateHistory(word);

                    Toast.makeText(v.getContext(),
                            message, Toast.LENGTH_SHORT).show();
                }
            });

        }
        // Re-use existing row view
        else {

            WordDescriptionViewHolder viewHolder = (WordDescriptionViewHolder) convertView
                    .getTag();
            checkBox = viewHolder.getCheckBox();
            wordView = viewHolder.getWordView();
            translationView = viewHolder.getTranslationView();
            directionView = viewHolder.getDirectionView();
        }
        checkBox.setTag(word);

        // Display words data
        checkBox.setChecked(word.getFav());
        wordView.setText(word.getWord());
        translationView.setText(word.getTranslation());
        directionView.setText(word.getDirection());

        return convertView;
    }
}
