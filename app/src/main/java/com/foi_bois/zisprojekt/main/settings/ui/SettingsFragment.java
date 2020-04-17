package com.foi_bois.zisprojekt.main.settings.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.foi_bois.zisprojekt.R;
import com.foi_bois.zisprojekt.base.BaseFragment;
import com.foi_bois.zisprojekt.firebase.BazaHelper;
import com.foi_bois.zisprojekt.main.map.LokacijePresenter;
import com.foi_bois.zisprojekt.main.map.ui.LokacijeView;
import com.foi_bois.zisprojekt.main.settings.SettingsPresenter;
import com.foi_bois.zisprojekt.model.Lokacija;

import javax.inject.Inject;

public class SettingsFragment extends BaseFragment implements SettingsView, View.OnClickListener {
    @Inject
    SettingsPresenter<SettingsView> presenter;

    public SettingsFragment() { }

    private Button btnSpremi;
    private EditText tbLang;
    private EditText tbLat;
    private BazaHelper baza;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_settings, container, false);

        btnSpremi = (Button)view.findViewById(R.id.btnSpremi);
        btnSpremi.setOnClickListener(this); //treba i implements!

        tbLang = (EditText)view.findViewById(R.id.tbLang);
        tbLat = (EditText)view.findViewById(R.id.tbLat);

        baza = new BazaHelper();
        presenter.attach(this);

        return view;
    }

    @Override
    public void onDestroy(){ //izgleda da nije protected onDestroy kod fragmenata :(
        presenter.detach();
        super.onDestroy();
    }

    @Override
    public void onClick(View v){
        Toast.makeText(getActivity(), "Isprobavamo bazu", Toast.LENGTH_SHORT).show();

        //Lokacija lokacija = new Lokacija(Double.valueOf(tbLang.getText().toString()), Double.valueOf(tbLat.getText().toString()));
        //Lokacija lokacija = new Lokacija(49.9, 14.9);
        //baza.osvjeziLokaciju(getContext(), lokacija, "400543054098");
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
