package com.gsp.trusttags.mobile.mapping;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModel;

import com.google.android.material.navigation.NavigationView;
import com.gsp.trusttags.mobile.mapping.databinding.BaseActivityBinding;
import com.gsp.trusttags.mobile.mapping.helper.PreferenceHelper;
import com.gsp.trusttags.mobile.mapping.helper.TagValues;
import com.gsp.trusttags.mobile.mapping.helper.Utility;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceGetBatchesScanSequence;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseCheckInCompleteShipper;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseIncompleteShipperList;

import java.util.ArrayList;

public abstract class BaseActivity<T extends ViewDataBinding, V extends ViewModel> extends MyDaggerActivity {

    BaseActivityBinding mBaseActivityBinding;

    public Utility mUtility;

    public PreferenceHelper mPreferenceHelper;

    public Toolbar mToolbarMain;

    public DrawerLayout mDrawerLayoutMain;

    public ActionBarDrawerToggle mActionBarDrawerToggle;

    public NavigationView mNavigationViewLeft;

    public TextView mTextViewUsername;
    public TextView mTextViewTitle;
    public TextView mTextViewDashboard;
    public TextView mTextViewHistory;
    public TextView mTextViewQADashboard;
    public TextView mTextViewSetting;
    public TextView mTextViewLogout;

    public ImageView mImageViewDrawer;
    public ImageView mImageViewSync;

    public Dialog mDialogCrashLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBaseActivityBinding = DataBindingUtil.setContentView(this, R.layout.base_activity);

        mUtility = new Utility(BaseActivity.this);
        mPreferenceHelper = new PreferenceHelper(BaseActivity.this);

        initToolbar();
        mTextViewUsername.setText(mPreferenceHelper.getFirstName() + " " + mPreferenceHelper.getLastName());

        mTextViewLogout.setOnClickListener(view -> {
            toggleDrawer();
            logoutDialog();
        });

        mTextViewDashboard.setOnClickListener(view -> {

            toggleDrawer();

            if (this instanceof ActivityDashboard) {

            } else {
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gotoDashborad();
                    }
                }, 200);
            }
        });

        mTextViewSetting.setOnClickListener(view -> {
            toggleDrawer();

            if (this instanceof ActivitySetting) {

            } else {
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gotoSetting();
                    }
                }, 200);
            }
        });

        mTextViewHistory.setOnClickListener(view -> {
            toggleDrawer();

            if (this instanceof ActivityHistory) {

            } else {
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gotoHistory();
                    }
                }, 200);
            }
        });

        mTextViewQADashboard.setOnClickListener(view -> {
            toggleDrawer();
            if (this instanceof ActivityQADashboard) {

            } else {
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gotoActivityQADashboard(true);
                    }
                }, 200);
            }
        });

        mImageViewSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BaseActivity.this, "Batch Sync!!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void askLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this, R.style.AlertDialogStyleAppCompat);
        builder.setTitle(getString(R.string.app_name));
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.sure_to_logout));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mPreferenceHelper.clearUserData();
                Intent i = new Intent(BaseActivity.this, ActivityLogin.class);
                finishAffinity();
                startActivity(i);

            }
        });

        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    public void gotoDashborad() {
        Intent mIntent = new Intent(BaseActivity.this, ActivityDashboard.class);
        finishAffinity();
        startActivity(mIntent);
    }
    public void gotoScanningWithScanSequence(String strProductID, String strLogID, String strBatchID, String strShipperSize, String strBatchNo,
                                             VoResponceGetBatchesScanSequence scanSequence,boolean isInLine,String strLineNo ) {
        Intent mIntent;
        if(!isInLine){
            mIntent = new Intent(BaseActivity.this, ActivityScanning.class);
        }else{
            mIntent = new Intent(BaseActivity.this, ActivityScanningInLine.class);
        }
        mIntent.putExtra("strProductID", strProductID);
        mIntent.putExtra("strLogID", strLogID);
        mIntent.putExtra("strBatchID", strBatchID);
        mIntent.putExtra("strBatchNo", strBatchNo);
        mIntent.putExtra("strShipperSize", strShipperSize);
        mIntent.putExtra("isFromDashBoard", false);
        mIntent.putExtra("scanSequence", scanSequence);
        mIntent.putExtra("strLineNo", strLineNo);

        startActivity(mIntent);
        finish();
    }

    public void gotoScanning(String strProductID, String strLogID, String strBatchID, String strShipperSize, String strBatchNo) {
        Intent mIntent = new Intent(BaseActivity.this, ActivityScanning.class);
        mIntent.putExtra("strProductID", strProductID);
        mIntent.putExtra("strLogID", strLogID);
        mIntent.putExtra("strBatchID", strBatchID);
        mIntent.putExtra("strBatchNo", strBatchNo);
        mIntent.putExtra("strShipperSize", strShipperSize);
        mIntent.putExtra("isFromDashBoard", false);
        startActivity(mIntent);
        finish();
    }

    public void gotoScanning(String strProductID, boolean isShipperAllocated, String strLogID, String strBatchID, String strBatchNo, String strShipperID, String strShipperSize, ArrayList<VoResponseIncompleteShipperList> mArrayListQRCode,
                             VoResponceGetBatchesScanSequence scanSequence, VoResponseCheckInCompleteShipper.VoResponceLogs logs,boolean isInLine) {
        Intent mIntent;
        if(!isInLine)
            mIntent = new Intent(BaseActivity.this, ActivityScanning.class);
        else
            mIntent = new Intent(BaseActivity.this, ActivityScanningInLine.class);
        mIntent.putExtra("strProductID", strProductID);
//        mIntent.putExtra("strLogID", strLogID);
        mIntent.putExtra("strBatchID", strBatchID);
        mIntent.putExtra("strBatchNo", strBatchNo);
//        mIntent.putExtra("strShipperID", strShipperID);
        mIntent.putExtra("strShipperSize", strShipperSize);
        mIntent.putExtra("isFromDashBoard", true);
//        mIntent.putExtra("isShipperAllocated", isShipperAllocated);
//        mIntent.putExtra("mArrayListQRCode", mArrayListQRCode);
        mIntent.putExtra("scanSequence", scanSequence);
        mIntent.putExtra("logs", logs);
        startActivity(mIntent);

//        strProductID = getIntent().getStringExtra("strProductID");
//        strBatchID = getIntent().getStringExtra("strBatchID");
//        strShipperSize = getIntent().getStringExtra("strShipperSize");
//        isFromDashBoard = getIntent().getBooleanExtra("isFromDashBoard", false);

    }

    public void gotoScanning() {
        Intent mIntent = new Intent(BaseActivity.this, ActivityScanning.class);
        startActivity(mIntent);
    }

    public void gotoCreateShipper(boolean isInLine) {
        Intent mIntent = new Intent(BaseActivity.this, ActivityCreateShipper.class);
        mIntent.putExtra("isInLine",isInLine);
        startActivity(mIntent);
    }

    public void gotoSetting() {
        Intent mIntent = new Intent(BaseActivity.this, ActivitySetting.class);
        finishAffinity();
        startActivity(mIntent);
    }

    public void gotoHistory() {
        Intent mIntent = new Intent(BaseActivity.this, ActivityHistory.class);
        finishAffinity();
        startActivity(mIntent);
    }

    public void showErrorMessage(String strMsg) {
        Toast.makeText(BaseActivity.this, strMsg, Toast.LENGTH_LONG).show();
    }

    public void initToolbar() {
        mToolbarMain = mBaseActivityBinding.baseActivityToolbar;
        mDrawerLayoutMain = mBaseActivityBinding.baseActivityDrawerContainer;
        mNavigationViewLeft = mBaseActivityBinding.baseActivityRightDrawerNv;
        mTextViewTitle = mBaseActivityBinding.baseActivityToolbarTxtTitle;
        mImageViewDrawer = mBaseActivityBinding.baseActivityToolbarImgDrawer;
        mImageViewSync = mBaseActivityBinding.baseActivityToolbarImgSync;
        mTextViewUsername = mBaseActivityBinding.baseActivityDrawerTxtName;
        mTextViewDashboard = mBaseActivityBinding.baseActivityDrawerTxtDashboard;
        mTextViewHistory = mBaseActivityBinding.baseActivityDrawerTxtHistory;
        mTextViewQADashboard = mBaseActivityBinding.baseActivityDrawerTxtQaPanel;
        mTextViewSetting = mBaseActivityBinding.baseActivityDrawerTxtSetting;
        mTextViewLogout = mBaseActivityBinding.baseActivityDrawerTxtLogout;
    }

    public void setUpNav() {
        mActionBarDrawerToggle = new ActionBarDrawerToggle(BaseActivity.this, mDrawerLayoutMain, mToolbarMain, R.string.app_name, R.string.app_name);
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);

        if ((this instanceof ActivityDashboard
                || this instanceof ActivitySetting
                || this instanceof ActivityHistory)) {
            mDrawerLayoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            mDrawerLayoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        mDrawerLayoutMain.setDrawerListener(mActionBarDrawerToggle);

        mNavigationViewLeft.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked())
                    menuItem.setChecked(false);
                else
                    menuItem.setChecked(true);

                toggleDrawer();

                return false;
            }
        });

        mActionBarDrawerToggle.syncState();

        if (mToolbarMain != null) {
            mToolbarMain.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mActionBarDrawerToggle.isDrawerIndicatorEnabled()) {
                        onBackPressed();
                    } else {
                        toggleDrawer();
                    }
                }
            });
        }
    }

    public void alreadyLoginWithOtherDevice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyleAppCompat);
        builder.setTitle(getString(R.string.app_name));
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.already_login_with_other_device));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                logout();
            }
        });

        if (mDialogCrashLogout == null) {
            mDialogCrashLogout = builder.create();
        }

        if (!mDialogCrashLogout.isShowing()) {
            mDialogCrashLogout.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayoutMain.isDrawerOpen(Gravity.START)) {
            mDrawerLayoutMain.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("WrongConstant")
    public void toggleDrawer() {
        if (mDrawerLayoutMain.isDrawerOpen(Gravity.START)) {
            mDrawerLayoutMain.closeDrawer(Gravity.START);
        } else {
            mDrawerLayoutMain.openDrawer(Gravity.START);
            mUtility.hideKeyboard();
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setUpNav();

    }

    public void logoutDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyleAppCompat);
        builder.setTitle(getString(R.string.app_name));
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.sure_to_logout));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (mUtility.haveInternet()) {
                    //TODO remove below commented line when service available
//                    logoutService();
                    logout();
                } else {
                    mUtility.ToastMsg(getString(R.string.internet_error));
                }
            }
        });

        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    public void logout() {
        mPreferenceHelper.clearUserData();
        Intent i = new Intent(BaseActivity.this, ActivityLogin.class);
        finishAffinity();
        startActivity(i);
    }

    public abstract void showWait();

    public abstract void removeWait();

    protected <T extends ViewDataBinding> T putContentView(@LayoutRes int resId) {
        return DataBindingUtil.inflate(getLayoutInflater(), resId, mBaseActivityBinding.baseActivityFlContent, true);
    }

    //QA Panel Functions
    public void gotoActivityQADashboard(boolean isFromSideBar) {
        if (mUtility.haveInternet()) {
            Intent myIntent = new Intent(BaseActivity.this, ActivityQADashboard.class);
            myIntent.putExtra(TagValues.isFromSideBar, isFromSideBar);
            startActivity(myIntent);
        } else {
            mUtility.ToastMsg(getString(R.string.internet_error));
        }
    }

    public void gotoQAScanScreen(String mStringIsFrom) {
        Intent mIntentScan = new Intent(BaseActivity.this, ActivityQAScan.class);
        mIntentScan.putExtra(TagValues.IS_FROM, mStringIsFrom);
        startActivity(mIntentScan);
    }

    public void gotoQAReprint() {
        Intent mIntentQAReprint = new Intent(BaseActivity.this, ActivityQAReprint.class);
        startActivity(mIntentQAReprint);
    }

}
