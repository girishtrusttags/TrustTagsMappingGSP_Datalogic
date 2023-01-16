package com.gsp.trusttags.mobile.mapping.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.gsp.trusttags.mobile.mapping.R;
import com.gsp.trusttags.mobile.mapping.databinding.ActivityScanDeleteBinding;
import com.gsp.trusttags.mobile.mapping.ActivityScanAndDelete;
import com.gsp.trusttags.mobile.mapping.helper.TagValues;
import com.gsp.trusttags.mobile.mapping.helper.Utility;
import com.gsp.trusttags.mobile.mapping.services.MyService;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceScanPackageQrCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

public class ScanDeleteVIewModel extends ViewModel {

    ActivityScanAndDelete context;
    MyService mMyService;
    Utility mUtility;
    ActivityScanDeleteBinding mActivityScanDeleteBinding;

    public void setActivityAndService(@NonNull ActivityScanAndDelete context, MyService myService, ActivityScanDeleteBinding activityScanDeleteBinding) {
        this.context = context;
        mUtility = new Utility(context);
        mMyService = myService;
        mActivityScanDeleteBinding = activityScanDeleteBinding;
    }

    public void scanAndDelete(String strUserID, String strAccessToken, String strQrCode, String strLogId) {
        mUtility.showAnimatedProgress(context);

        mMyService.scanAndDeletePackageQrCode(strUserID, strAccessToken, strQrCode, strLogId, new MyService.ServiceCallback<VoResponceScanPackageQrCode>() {
            @Override
            public void onSuccess(VoResponceScanPackageQrCode data) {
                mUtility.HideAnimatedProgress();
                if (data != null && data.getSuccess() != null && data.getSuccess().equalsIgnoreCase("1")
                        && data.getQrcode() != null) {
                    context.updateArrayList(data.getQrcode());
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

}
