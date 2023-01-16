package com.gsp.trusttags.mobile.mapping.networking;

import com.gsp.trusttags.mobile.mapping.MyDaggerActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class,})
public interface DependancyInjector {
    void injectMyDaggerActivity(MyDaggerActivity myoDaggerActivity);
}
