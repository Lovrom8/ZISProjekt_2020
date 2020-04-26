package com.foi_bois.zisprojekt.main.settings.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foi_bois.zisprojekt.R;
import com.foi_bois.zisprojekt.auth.ui.LoginActivity;
import com.foi_bois.zisprojekt.base.BaseFragment;
import com.foi_bois.zisprojekt.firebase.Auth;
import com.foi_bois.zisprojekt.firebase.BazaHelper;
import com.foi_bois.zisprojekt.lib.AuthHelper;
import com.foi_bois.zisprojekt.lib.DownloadImageTask;
import com.foi_bois.zisprojekt.lib.ImgHelper;
import com.foi_bois.zisprojekt.lib.PermsHelper;
import com.foi_bois.zisprojekt.main.MainActivity;
import com.foi_bois.zisprojekt.main.settings.SettingsPresenter;
import com.foi_bois.zisprojekt.model.Location;
import com.foi_bois.zisprojekt.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;

import javax.inject.Inject;

public class SettingsFragment extends BaseFragment implements SettingsView {
    @Inject
    SettingsPresenter<SettingsView> presenter;

    public SettingsFragment() { }

    private Button btnSave;
    private Button btnLogout;
    private TextView tvUsername;
    private EditText tbUsername;
    private EditText tbEmail;
    private BazaHelper baza;
    private ImageView ivAvatar;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_settings, container, false);

        btnSave = (Button)view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onSaveSettingsClick(v);
            }
        });
        btnLogout = (Button)view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogoutClick();
            }
        });

        ivAvatar = (ImageView)view.findViewById(R.id.ivAvatar);
        ivAvatar.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onAvatarClick(v);
            }
        });

        tvUsername = (TextView)view.findViewById(R.id.tvSettingsUsername);
        tbUsername = (EditText)view.findViewById(R.id.editSettingsUsername);
        tbEmail = (EditText)view.findViewById(R.id.editSettingsEmail);

        presenter.attach(this);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        loadCurrentSettings();

        return view;
    }

    @Override
    public void onDestroy(){ //izgleda da nije protected onDestroy kod fragmenata :(
        presenter.detach();
        super.onDestroy();
    }

    private void loadCurrentSettings() {
        tvUsername.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        BazaHelper.getInstance().getCustomDataForUser(currentUser, new BazaHelper.FirebaseUserLoadCallback() {
            @Override
            public void onCallback(boolean isSuccessful, User userData) {
                if(userData.getAvatar().length() == 0){
                    ivAvatar.setImageDrawable(getResources().getDrawable(R.drawable.default_avatar));
                    return;
                }

                new DownloadImageTask(new DownloadImageTask.Listener() {
                    @Override
                    public void onImageDownloaded(final Bitmap bitmap) {
                        ivAvatar.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onImageDownloadError() {
                        Log.d("Settings", "Neuspjesno skidanje slike");
                    }
                }).execute(userData.getAvatar());
            }
        });
    }

    private void onSaveSettingsClick(View v){
        String email = tbEmail.getText().toString();
        String username = tbUsername.getText().toString();

        if(email.length() == 0 || !AuthHelper.isEmailValid(email)){
            TastyToast.makeText(getActivity(), getResources().getString(R.string.settings_wrong_email), Toast.LENGTH_SHORT, TastyToast.INFO).show();
            return;
        }

        if(username.length() == 0){
            TastyToast.makeText(getActivity(), getResources().getString(R.string.settings_wrong_email), Toast.LENGTH_SHORT, TastyToast.INFO).show();
            return;
        }

        presenter.saveCurrentUserSettings(email, username);
    }

    private void onSettingsChanged(boolean isSuccessful, String username){
        tvUsername.setText(username.toUpperCase());

        if(isSuccessful)
            TastyToast.makeText(getActivity(), getResources().getString(R.string.settings_change_success), Toast.LENGTH_SHORT, TastyToast.SUCCESS).show();
        else
            TastyToast.makeText(getActivity(), getResources().getString(R.string.settings_change_failed), Toast.LENGTH_SHORT, TastyToast.ERROR).show();
    }

    private void onAvatarClick(View v){
        checkFilePerms();
        loadAvatarImage();
    }

    private void loadAvatarImage(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        startActivityForResult(intent, 200);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {
            Uri currFileURI = data.getData();

            if(currFileURI != null){
                String path = currFileURI.getPath();
                presenter.uploadAvatarToFirestore(currFileURI);

                ivAvatar.setImageURI(currFileURI);
            }
        }
    }

    @Override
    public void onAvatarUpload(boolean isSuccessful){
        if(isSuccessful)
            TastyToast.makeText(getActivity(), getResources().getString(R.string.settings_avatar_change_success), Toast.LENGTH_SHORT, TastyToast.SUCCESS).show();
        else
            TastyToast.makeText(getActivity(), getResources().getString(R.string.settings_avatar_change_failed), Toast.LENGTH_SHORT, TastyToast.ERROR).show();
    }

    @Override
    public void checkFilePerms() {
        PermsHelper.checkPerms(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void onLogoutClick(){
        AuthHelper.removeSavedLoginCreds(getActivity());
        FirebaseAuth.getInstance().signOut(); //oh wow!
        startActivity(new Intent(getContext(), LoginActivity.class));
    }
}
