package com.foi_bois.zisprojekt.main.settings;

import android.net.Uri;

import com.foi_bois.zisprojekt.base.BasePresenter;
import com.foi_bois.zisprojekt.main.settings.ui.SettingsView;

public interface SettingsPresenter<V extends SettingsView> extends BasePresenter<V> {
    void uploadAvatarToFirestore(final Uri path);
    void saveCurrentUserSettings(String email, String username);
}
