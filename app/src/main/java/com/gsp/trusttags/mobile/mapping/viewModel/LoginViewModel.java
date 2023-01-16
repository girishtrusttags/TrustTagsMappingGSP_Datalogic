package com.gsp.trusttags.mobile.mapping.viewModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.gsp.trusttags.mobile.mapping.ActivityLogin;
import com.gsp.trusttags.mobile.mapping.R;
import com.gsp.trusttags.mobile.mapping.databinding.ActivityLoginBinding;
import com.gsp.trusttags.mobile.mapping.helper.TagValues;
import com.gsp.trusttags.mobile.mapping.helper.Utility;
import com.gsp.trusttags.mobile.mapping.services.MyService;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceLogin;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseForgotPassword;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;

import retrofit2.HttpException;

public class LoginViewModel extends ViewModel {
    private ActivityLogin context;
    public Utility mUtility;
    public MyService mMyService;

    private ActivityLoginBinding mActivityLoginBinding;

    private SharedPreferences mSharedPreferencesCredential;
    private SharedPreferences.Editor mEditor;

    public void setActivityAndService(@NonNull ActivityLogin context, MyService myService, ActivityLoginBinding activityLoginBinding) {
        this.context = context;
        mUtility = new Utility(context);
        mMyService = myService;
        mActivityLoginBinding = activityLoginBinding;
        mSharedPreferencesCredential = context.getSharedPreferences(TagValues.PREF_CREDENTIAL_PREFERENCE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferencesCredential.edit();
    }

    public boolean isValid() {

        if (mActivityLoginBinding.activityLoginEditEmail.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(context, context.getResources().getString(R.string.text_login_empty_email), Toast.LENGTH_LONG).show();
            return false;
        }

        if (!context.mUtility.isValidEmail(mActivityLoginBinding.activityLoginEditEmail.getText().toString().trim())) {
            Toast.makeText(context, context.getResources().getString(R.string.text_login_invalid_email), Toast.LENGTH_LONG).show();
            return false;
        }

        if (mActivityLoginBinding.activityLoginEditPassword.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(context, context.getResources().getString(R.string.text_login_empty_password), Toast.LENGTH_LONG).show();
            return false;
        }

        if (mActivityLoginBinding.activityLoginCheckboxRememberMe.isChecked()) {
            mEditor.putString(TagValues.PREF_USER_EMAIL, mActivityLoginBinding.activityLoginEditEmail.getText().toString().trim());
            mEditor.putString(TagValues.PREF_USER_PASSWORD, mActivityLoginBinding.activityLoginEditPassword.getText().toString().trim());
            mEditor.commit();
        } else {
            mEditor.clear();
            mEditor.commit();
        }

        return true;
    }

    public void checkRememberMe() {
        mActivityLoginBinding.activityLoginCheckboxRememberMe.setChecked(mSharedPreferencesCredential.getBoolean(TagValues.PREF_IS_REMEMBER_ME, false));

        if (mActivityLoginBinding.activityLoginCheckboxRememberMe.isChecked()) {
            mActivityLoginBinding.activityLoginEditEmail.setText(mSharedPreferencesCredential.getString(TagValues.PREF_USER_EMAIL, ""));
            mActivityLoginBinding.activityLoginEditPassword.setText(mSharedPreferencesCredential.getString(TagValues.PREF_USER_PASSWORD, ""));
        }
    }

    public void setRememberMeData(boolean isChecked) {
        mEditor.putBoolean(TagValues.PREF_IS_REMEMBER_ME, isChecked);
        mEditor.apply();
    }

    public void doNormalUserLogin(Map<String, String> hashMap) {
        mUtility.showAnimatedProgress(context);

        mMyService.login(hashMap, new MyService.ServiceCallback<VoResponceLogin>() {
            @Override
            public void onSuccess(VoResponceLogin data) {
                mUtility.HideAnimatedProgress();

                if (data != null && data.getSuccess() != null && data.getSuccess().equalsIgnoreCase(TagValues.CONST_NUMBER_1) && data.getData() != null) {

                    context.mPreferenceHelper.setUserId(data.getData().getId());
                    context.mPreferenceHelper.setFirstName(data.getData().getFirst_name());
                    context.mPreferenceHelper.setLastName(data.getData().getLast_name());
                    context.mPreferenceHelper.setEmail(data.getData().getEmail());
                    context.mPreferenceHelper.setCompanyID(data.getData().getCompany_id());
                    context.mPreferenceHelper.setContactNo(data.getData().getContact_no());
                    context.mPreferenceHelper.setCompanyName(data.getData().getCompany_name());
                    context.mPreferenceHelper.setCompanyLogo(data.getData().getCompany_logo());
                    context.mPreferenceHelper.setCompanyRandomID(data.getData().getCompany_random_id());
                    context.mPreferenceHelper.setAccessToken(data.getAccess_token());
                    context.mPreferenceHelper.setUnitID(data.getData().getUnit_id());
                    context.gotoDashborad();

                } else {
                    if (data != null && data.getMessage() != null && !data.getMessage().equalsIgnoreCase("")) {
                        context.showErrorMessage(data.getMessage());
                    } else {
                        context.showErrorMessage(context.getString(R.string.something_went_wrong_fix_soon));
                    }
                }
            }

            @Override
            public void onError(Throwable networkError) {
                mUtility.HideAnimatedProgress();

                if (networkError instanceof HttpException) {
                    try {
                        if (((HttpException) networkError).code() == TagValues.INT_UNAUTHORIZED) {
                            context.alreadyLoginWithOtherDevice();
                        } else {
                            JSONObject mJsonObjectMsg = new JSONObject(((HttpException) networkError).response().errorBody().string());
                            context.mUtility.ToastMsg(mJsonObjectMsg.optString("message"));
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                } else if (networkError instanceof SocketTimeoutException) {
                    context.mUtility.ToastMsg(context.getResources().getString(R.string.time_out_msg));
                } else {
                    context.mUtility.ToastMsg(context.getResources().getString(R.string.server_error_msg));
                }

            }
        });
    }

    public void forgotPassword(Map<String, String> hashMap) {
        mUtility.showAnimatedProgress(context);

        mMyService.forgotPassword(hashMap, new MyService.ServiceCallback<VoResponseForgotPassword>() {
            @Override
            public void onSuccess(VoResponseForgotPassword data) {
                mUtility.HideAnimatedProgress();

                if (data != null && data.getMessage() != null && !data.getMessage().equalsIgnoreCase("")) {
                    context.showErrorMessage(data.getMessage());
                } else {
                    context.showErrorMessage(context.getString(R.string.something_went_wrong_fix_soon));
                }

            }

            @Override
            public void onError(Throwable networkError) {
                mUtility.HideAnimatedProgress();

                if (networkError instanceof HttpException) {
                    try {
                        if (((HttpException) networkError).code() == TagValues.INT_UNAUTHORIZED) {
                            context.alreadyLoginWithOtherDevice();
                        } else {
                            JSONObject mJsonObjectMsg = new JSONObject(((HttpException) networkError).response().errorBody().string());
                            context.mUtility.ToastMsg(mJsonObjectMsg.optString("message"));
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                } else if (networkError instanceof SocketTimeoutException) {
                    context.mUtility.ToastMsg(context.getResources().getString(R.string.time_out_msg));
                } else {
                    context.mUtility.ToastMsg(context.getResources().getString(R.string.server_error_msg));
                }

            }
        });
    }

}