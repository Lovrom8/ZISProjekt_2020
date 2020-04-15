package com.foi_bois.zisprojekt.lib;

public class EventBus implements IEventBus {
    private org.greenrobot.eventbus.EventBus mEventBus;

    private static class SingletonHolder {
        private static final EventBus INSTANCE = new EventBus();
    }

    public EventBus(){
        mEventBus = mEventBus.getDefault();
    }

    public static EventBus getInstance(){
        return SingletonHolder.INSTANCE;
    }

    @Override public void registerEvent(Object subscriber) {
        mEventBus.register(subscriber);
    }

    @Override public void unregisterEvent(Object subscriber) {
        mEventBus.unregister(subscriber);
    }

    @Override public void postEvent(Object subscriber) {
        mEventBus.post(subscriber);
    }
}
