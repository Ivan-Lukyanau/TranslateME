package com.example.ivan_lukyanau.translateme;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Ivan_Lukyanau on 4/5/2017.
 */

public class CustomPagerAdapter extends FragmentStatePagerAdapter {

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Translator", "History", "Favorites" };
    private Context context;

    public CustomPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0: {
                TabTranslator tt = new TabTranslator();
                return tt;
            }
            case 1: {
                TabHistory th = new TabHistory();
                return th;
            }
            case 2: {
                TabFavorites tf = new TabFavorites();
                return tf;
            }
            default: return new TabTranslator();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // title based on item position
        return tabTitles[position];
    }

    @Override
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        // when we will click on the appropriate tab
        return POSITION_NONE;
    }
}
