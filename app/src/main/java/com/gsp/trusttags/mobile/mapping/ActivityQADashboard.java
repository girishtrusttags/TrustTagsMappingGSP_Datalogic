package com.gsp.trusttags.mobile.mapping;

import android.os.Bundle;
import android.view.View;

import com.gsp.trusttags.mobile.mapping.databinding.ActivityQaDashboardBinding;
import com.gsp.trusttags.mobile.mapping.viewModel.QADashboardViewModel;

public class ActivityQADashboard extends BaseActivity {

    ActivityQaDashboardBinding mActivityQaDashboardBinding;
    QADashboardViewModel mQaDashboardViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        mToolbarMain.setVisibility(View.VISIBLE);

        mActivityQaDashboardBinding = (ActivityQaDashboardBinding) putContentView(R.layout.activity_qa_dashboard);
        mQaDashboardViewModel = new QADashboardViewModel(this, mMyService);
        mActivityQaDashboardBinding.setQaDashboardViewModel(mQaDashboardViewModel);

        mImageViewDrawer.setImageResource(R.drawable.ic_menu);
        mTextViewTitle.setText(getResources().getString(R.string.qa_dashboard));
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

}
