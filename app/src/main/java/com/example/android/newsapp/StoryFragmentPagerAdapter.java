package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class StoryFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private int tabTitles[] = new int[]{R.string.news, R.string.sport,
            R.string.culture, R.string.life_and_style};

    public StoryFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new NewsFragment();
        } else if (position == 1) {
            return new SportFragment();
        } else if (position == 2) {
            return new CultureFragment();
        } else {
            return new LifeAndStyleFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(tabTitles[position]);
    }
}
