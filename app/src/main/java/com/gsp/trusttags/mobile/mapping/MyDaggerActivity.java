package com.gsp.trusttags.mobile.mapping;

import android.os.Bundle;

import com.gsp.trusttags.mobile.mapping.services.MyService;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MyDaggerActivity extends AppCompatActivity {
    @Inject
    public MyService mMyService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getApplication()).getDeps().injectMyDaggerActivity(this);
    }
}
