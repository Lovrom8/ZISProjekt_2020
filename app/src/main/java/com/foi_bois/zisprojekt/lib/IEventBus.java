package com.foi_bois.zisprojekt.lib;

public interface IEventBus {
    void registerEvent(Object subscriber);
    void unregisterEvent(Object subscriber);
    void postEvent(Object subscriber);
}
