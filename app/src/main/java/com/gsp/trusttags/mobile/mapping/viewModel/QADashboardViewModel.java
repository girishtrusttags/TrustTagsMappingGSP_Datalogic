package com.gsp.trusttags.mobile.mapping.viewModel;

import androidx.lifecycle.ViewModel;

import com.gsp.trusttags.mobile.mapping.ActivityQADashboard;
import com.gsp.trusttags.mobile.mapping.helper.TagValues;
import com.gsp.trusttags.mobile.mapping.helper.Utility;
import com.gsp.trusttags.mobile.mapping.services.MyService;

public class QADashboardViewModel extends ViewModel {

    private ActivityQADashboard mActivityQADashboard;
    private Utility mUtility;
    private MyService mMyService;

    public QADashboardViewModel(ActivityQADashboard mActivityQADashboard, MyService mMyService) {
        this.mActivityQADashboard = mActivityQADashboard;
        this.mMyService = mMyService;
        this.mUtility = new Utility(this.mActivityQADashboard);
    }

    public void onClickRemap() {
        mActivityQADashboard.gotoQAScanScreen(TagValues.isFromQARemapDashboard);
    }

    public void onClickReprint() {
        mActivityQADashboard.gotoQAReprint();
    }

    public void onClickStockReconciliation() {
//        mActivityQADashboard.gotoQAStockReconciliation();
    }

}
