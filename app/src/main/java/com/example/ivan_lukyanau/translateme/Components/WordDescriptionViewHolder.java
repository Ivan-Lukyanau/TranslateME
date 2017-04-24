package com.example.ivan_lukyanau.translateme.Components;

/**
 * Created by Ivan_Lukyanau on 4/6/2017.
 */

import android.widget.CheckBox;
import android.widget.TextView;

/** Holds child views for one row. */
public class WordDescriptionViewHolder {
    private CheckBox checkBox;
    private TextView wordView;
    private TextView translationView;
    private TextView directionView;

    public WordDescriptionViewHolder() {
    }

    public WordDescriptionViewHolder(TextView wordView, TextView translationView, TextView directionView, CheckBox checkBox) {
        this.checkBox = checkBox;
        this.wordView = wordView;
        this.translationView = translationView;
        this.directionView = directionView;
    }

    public CheckBox getCheckBox() {
        return this.checkBox;
    }

    public TextView getTranslationView() {
        return this.translationView;
    }

    public TextView getWordView() {
        return this.wordView;
    }

    public TextView getDirectionView() {
        return this.directionView;
    }

    public void setDirectionView(TextView directionView) {
        this.directionView = directionView;
    }

    public void setWordView(TextView wordView) {
        this.wordView = wordView;
    }

    public void setTranslationView(TextView translationView) {
        this.translationView = translationView;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }
}

