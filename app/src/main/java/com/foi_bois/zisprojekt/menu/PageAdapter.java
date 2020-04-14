package com.foi_bois.zisprojekt.menu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter{
    private int numOfTabs;

    public PageAdapter(FragmentManager fm, int numOfTabs){
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position){
        switch(position){
            case 0:
                return new TabMain();
            case 1:
                return new TabSettings();
            default:
                return null;
        }
    }

    @Override
    public int getCount(){
        return numOfTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object){
        return POSITION_NONE;
    }
}
