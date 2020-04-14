package com.foi_bois.zisprojekt.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.foi_bois.zisprojekt.firebase.Baza;
import com.foi_bois.zisprojekt.R;
import com.foi_bois.zisprojekt.model.Lokacija;

public class TabSettings extends Fragment implements View.OnClickListener {

    public TabSettings() { }

    private Button btnSpremi;
    private EditText tbLang;
    private EditText tbLat;
    private Baza baza;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_settings, container, false);

        btnSpremi = (Button)view.findViewById(R.id.btnSpremi);
        btnSpremi.setOnClickListener(this); //treba i implements!

        tbLang = (EditText)view.findViewById(R.id.tbLang);
        tbLat = (EditText)view.findViewById(R.id.tbLat);

        baza = new Baza();

        return view;
    }

    @Override
    public void onClick(View v){
        Toast.makeText(getActivity(), "Isprobavamo bazu", Toast.LENGTH_SHORT).show();

        Lokacija lokacija = new Lokacija(Double.valueOf(tbLang.getText().toString()), Double.valueOf(tbLat.getText().toString()));
        //Lokacija lokacija = new Lokacija(49.9, 14.9);
        baza.osvjeziLokaciju(getContext(), lokacija, "400543054098");
    }
}
