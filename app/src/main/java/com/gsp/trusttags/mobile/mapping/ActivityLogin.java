package com.gsp.trusttags.mobile.mapping;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.gsp.trusttags.mobile.mapping.databinding.ActivityLoginBinding;
import com.gsp.trusttags.mobile.mapping.helper.Utility;
import com.gsp.trusttags.mobile.mapping.viewModel.LoginViewModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ActivityLogin extends BaseActivity {

    private ActivityLoginBinding mActivityLoginBinding;

    LoginViewModel mLoginViewModel;

    Dialog mDialogForgotPassword;

    private final String[] ALL_PERMS = {Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        mActivityLoginBinding = (ActivityLoginBinding) putContentView(R.layout.activity_login);
        mToolbarMain.setVisibility(View.GONE);

        mLoginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        mLoginViewModel.setActivityAndService(this, mMyService, mActivityLoginBinding);

        mActivityLoginBinding.setLoginViewModel(mLoginViewModel);
        mActivityLoginBinding.setLifecycleOwner(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

//        mActivityLoginBinding.activityLoginEditEmail.setText("rajiv@trusttags.in");
//        mActivityLoginBinding.activityLoginEditPassword.setText("GSPTT1@");

//        mActivityLoginBinding.activityLoginEditEmail.setText("girish@trusttags.in");
//        mActivityLoginBinding.activityLoginEditPassword.setText("GSPTT1@");

//        mActivityLoginBinding.activityLoginEditEmail.setText("jigar@trusttags.in");
//        mActivityLoginBinding.activityLoginEditPassword.setText("GSPTT1@");
//        mActivityLoginBinding.activityLoginEditEmail.setText("factory01@trusttags.in");
//        mActivityLoginBinding.activityLoginEditPassword.setText("123456");

//        mActivityLoginBinding.activityLoginEditEmail.setText("factory@shreejipesticides.com");
//        mActivityLoginBinding.activityLoginEditPassword.setText("123456");

//        mActivityLoginBinding.activityLoginEditEmail.setText("alpeshdhanjibhai189@gmail.com");
//        mActivityLoginBinding.activityLoginEditPassword.setText("Gsp@123");

//        mActivityLoginBinding.activityLoginEditEmail.setText("simranfact@gmail.com");
//        mActivityLoginBinding.activityLoginEditPassword.setText("123456");

//        mActivityLoginBinding.activityLoginEditEmail.setText("test1prod@ms.com");
//        mActivityLoginBinding.activityLoginEditPassword.setText("123456");

        /*mActivityLoginBinding.activityLoginEditEmail.setText("Snehal.pandya@shreejipesticides.com");
        mActivityLoginBinding.activityLoginEditPassword.setText("123456");*/


//        mActivityLoginBinding.activityLoginEditEmail.setText("production@ww.com");
//        mActivityLoginBinding.activityLoginEditPassword.setText("123456");
//        mActivityLoginBinding.activityLoginEditEmail.setText("cdqr2@ww.com");
//        mActivityLoginBinding.activityLoginEditPassword.setText("123456");

//        mActivityLoginBinding.activityLoginEditEmail.setText("production@ms.com");
//        mActivityLoginBinding.activityLoginEditPassword.setText("123456");

//        mActivityLoginBinding.activityLoginEditEmail.setText("simran@trusttags.in");
//        mActivityLoginBinding.activityLoginEditPassword.setText("123456");

//        mActivityLoginBinding.activityLoginEditEmail.setText("production@ms.com");
//        mActivityLoginBinding.activityLoginEditPassword.setText("123456");



        mActivityLoginBinding.activityLoginTxtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUtility.haveInternet()) {
                    if (mLoginViewModel.isValid()) {
                        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                        String mStringVersion = "";

                        try {
                            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                            mStringVersion = pInfo.versionName;
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                        Map<String, String> hashMap = new HashMap<>();
                        hashMap.put("email", mActivityLoginBinding.activityLoginEditEmail.getText().toString().trim());
                        hashMap.put("password", mActivityLoginBinding.activityLoginEditPassword.getText().toString().trim());
                        hashMap.put("device_type", "1");
                        hashMap.put("device_token", android_id);
                        hashMap.put("os", Build.VERSION.RELEASE);
                        hashMap.put("app_version", mStringVersion);
                        hashMap.put("device_model", Build.MANUFACTURER + " " + Build.MODEL);
                        hashMap.put("latitude", "0.00");
                        hashMap.put("longitude", "0.00");
                        hashMap.put("uniqueId", Utility.getDeviceID(ActivityLogin.this));

                        mLoginViewModel.doNormalUserLogin(hashMap);
                    }
                } else {
                    Toast.makeText(ActivityLogin.this, getResources().getString(R.string.internet_error), Toast.LENGTH_LONG).show();
                }
            }
        });

        mLoginViewModel.checkRememberMe();

        mActivityLoginBinding.activityLoginCheckboxRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mLoginViewModel.setRememberMeData(isChecked);
            }
        });

        mActivityLoginBinding.activityLoginTxtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtility.errorDialog(getString(R.string.contact_admin));
            }
        });

        mUtility.verifyPermissions(ActivityLogin.this, ALL_PERMS);

    }

    public void showForgotPassword() {
        if (mDialogForgotPassword != null && mDialogForgotPassword.isShowing()) {
            mDialogForgotPassword.dismiss();
        }

        mDialogForgotPassword = new Dialog(ActivityLogin.this);
        Objects.requireNonNull(mDialogForgotPassword.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        mDialogForgotPassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogForgotPassword.setCancelable(true);
        mDialogForgotPassword.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialogForgotPassword.setContentView(R.layout.dialog_forgot_password);
        mDialogForgotPassword.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView mTextViewOkay = (TextView) mDialogForgotPassword.findViewById(R.id.dialog_forgot_password_txt_ok);
        TextView mTextViewCancel = (TextView) mDialogForgotPassword.findViewById(R.id.dialog_forgot_password_txt_cancel);
        EditText mEditTextEmail = (EditText) mDialogForgotPassword.findViewById(R.id.dialog_forgot_password_edit_email);

        mTextViewOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogForgotPassword.dismiss();
                mUtility.hideKeyboard();

                if (checkIfValidEmail(mEditTextEmail.getText().toString().trim())) {
                    if (mUtility.haveInternet()) {
                        Map<String, String> mHashMap = new HashMap<>();
                        mHashMap.put("email", mEditTextEmail.getText().toString().trim());

                        mLoginViewModel.forgotPassword(mHashMap);
                    }
                }
            }
        });

        mTextViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogForgotPassword.dismiss();
                mUtility.hideKeyboard();
            }
        });

        mDialogForgotPassword.show();

    }

    public boolean checkIfValidEmail(String mStringEmail) {
        if (!mStringEmail.trim().equalsIgnoreCase("")) {
            if (mUtility.isValidEmail(mStringEmail)) {
                return true;
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.text_login_invalid_email), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.text_login_empty_email), Toast.LENGTH_LONG).show();
        }

        return false;
    }

    public void showWait() {
        mUtility.hideKeyboard();
        mUtility.showAnimatedProgress(ActivityLogin.this);
    }

    public void removeWait() {
        mUtility.HideAnimatedProgress();
    }

}