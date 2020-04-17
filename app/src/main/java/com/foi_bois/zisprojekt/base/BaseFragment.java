package com.foi_bois.zisprojekt.base;

import android.content.Context;

import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.DaggerFragment;

public class BaseFragment extends DaggerFragment {

    @Override
    public void onAttach(Context context) {
        //AndroidSupportInjection.inject(this); whatif
        super.onAttach(context);
    }
}
