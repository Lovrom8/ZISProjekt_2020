package com.foi_bois.zisprojekt.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.foi_bois.zisprojekt.main.map.ui.LokacijeFragment;
import com.foi_bois.zisprojekt.main.settings.ui.SettingsFragment;

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
                return new LokacijeFragment();
            case 1:
                return new SettingsFragment();
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
