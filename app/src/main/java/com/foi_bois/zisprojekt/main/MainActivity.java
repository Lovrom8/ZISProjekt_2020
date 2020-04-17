package com.foi_bois.zisprojekt.main;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.foi_bois.zisprojekt.R;
import com.foi_bois.zisprojekt.main.PageAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends FragmentActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tabMain, tabSettings;
    public PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout)findViewById(R.id.TabLayoutMain);
        tabMain = (TabItem) findViewById(R.id.TabMain);
        tabSettings = (TabItem) findViewById(R.id.TabSettings);
        viewPager = findViewById(R.id.ViewPager);

        pagerAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0)
                    pagerAdapter.notifyDataSetChanged();
                else if(tab.getPosition() == 1)
                    pagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab){}

            @Override
            public void onTabReselected(TabLayout.Tab tab){}
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}
