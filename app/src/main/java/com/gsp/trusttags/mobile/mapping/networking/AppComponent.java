package com.gsp.trusttags.mobile.mapping.networking;

import android.app.Application;

import com.gsp.trusttags.mobile.mapping.MyApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(MyApplication mvvmApplication);
}
