package com.gsp.trusttags.mobile.mapping;

import android.os.Bundle;
import android.view.View;

import com.gsp.trusttags.mobile.mapping.databinding.ActivitySettingBinding;

public class ActivitySetting extends BaseActivity {

    ActivitySettingBinding mActivitySettingBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();

        mActivitySettingBinding = (ActivitySettingBinding) putContentView(R.layout.activity_setting);

        mTextViewTitle.setText("Settings");
        mImageViewSync.setVisibility(View.GONE);

        mImageViewDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });
    }

    @Override
    public void showWait() {

    }

    @Override
    public void removeWait() {

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        gotoDashborad();
    }
}
