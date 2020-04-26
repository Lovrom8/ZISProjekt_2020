package com.foi_bois.zisprojekt.main.settings.ui;

import com.foi_bois.zisprojekt.base.BaseView;

public interface SettingsView extends BaseView {
    void checkFilePerms();
    void onAvatarUpload(boolean isSuccessful);
}

