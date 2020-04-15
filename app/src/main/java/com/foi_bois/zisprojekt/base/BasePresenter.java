package com.foi_bois.zisprojekt.base;

public interface BasePresenter<V extends BaseView> {
    void attach(V view);
    void detach();
}
