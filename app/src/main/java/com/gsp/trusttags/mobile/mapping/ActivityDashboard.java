package com.gsp.trusttags.mobile.mapping;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.gsp.trusttags.mobile.mapping.R;
import com.gsp.trusttags.mobile.mapping.databinding.ActivityDashboardBinding;
import com.gsp.trusttags.mobile.mapping.viewModel.DashboardViewModel;

public class ActivityDashboard extends BaseActivity {

    ActivityDashboardBinding mActivityDashboardBinding;

    DashboardViewModel mDashboardViewModel;

    private final String[] ALL_PERMS = {Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        mActivityDashboardBinding = (ActivityDashboardBinding) putContentView(R.layout.activity_dashboard);
        mToolbarMain.setVisibility(View.VISIBLE);

        mDashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        mDashboardViewModel.setActivityAndService(this, mMyService, mActivityDashboardBinding);

        mImageViewDrawer.setImageResource(R.drawable.ic_menu);
        mTextViewTitle.setText(getResources().getString(R.string.text_title_dashboard));
        mImageViewSync.setVisibility(View.GONE);

        mImageViewDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });

        mActivityDashboardBinding.llCreateShipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUtility.haveInternet()) {
                    mDashboardViewModel.checkInCompleteShipper(mPreferenceHelper.getUserId(), mPreferenceHelper.getAccessToken(),false);
                } else {
                    Toast.makeText(ActivityDashboard.this, getResources().getString(R.string.internet_error), Toast.LENGTH_LONG).show();
                }
            }
        });
        mActivityDashboardBinding.llInLineMapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUtility.haveInternet()) {
//                    gotoCreateShipper(true);
                    mDashboardViewModel.checkInCompleteShipperInLine(mPreferenceHelper.getUserId(), mPreferenceHelper.getAccessToken(),true);
                } else {
                    Toast.makeText(ActivityDashboard.this, getResources().getString(R.string.internet_error), Toast.LENGTH_LONG).show();
                }
            }
        });
        mUtility.verifyPermissions(ActivityDashboard.this, ALL_PERMS);

    }

    @Override
    public void showWait() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


    }

    @Override
    public void removeWait() {

    }

    @Override
    public void onBackPressed() {
        mUtility.ExitDialog(getResources().getString(R.string.sure_to_exit));
    }

}
