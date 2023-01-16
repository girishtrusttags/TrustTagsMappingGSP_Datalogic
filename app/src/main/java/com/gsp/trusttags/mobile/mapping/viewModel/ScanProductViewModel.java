package com.gsp.trusttags.mobile.mapping.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.gsp.trusttags.mobile.mapping.ActivityScanProductQR;
import com.gsp.trusttags.mobile.mapping.R;
import com.gsp.trusttags.mobile.mapping.helper.TagValues;
import com.gsp.trusttags.mobile.mapping.helper.Utility;
import com.gsp.trusttags.mobile.mapping.services.MyService;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceCreateLog;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceScanPackageQrCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;

import retrofit2.HttpException;

public class ScanProductViewModel extends ViewModel {

    private ActivityScanProductQR context;
    public Utility mUtility;
    public MyService mMyService;

    public void setActivityAndService(@NonNull ActivityScanProductQR context, MyService myService) {
        this.context = context;
        mUtility = new Utility(context);
        mMyService = myService;
    }

    public void scanQrCode(String strUserID, String strAccessToken, String strQRCode, Map<String, String> hashMap) {
        mUtility.showAnimatedProgress(context);

        mMyService.scanPackageQrcode(strUserID, strAccessToken, strQRCode, hashMap, new MyService.ServiceCallback<VoResponceScanPackageQrCode>() {
            @Override
            public void onSuccess(VoResponceScanPackageQrCode data) {
                mUtility.HideAnimatedProgress();
                if (data != null) {
                    if (data.getSuccess() != null && data.getSuccess().equalsIgnoreCase("1")) {
                        context.scannedProductQRCode(data.getSub_code());
                    } else {
                        if (data.getMessage() != null && !data.getMessage().equalsIgnoreCase("")) {
                            context.showErrorMessage(data.getMessage());
                        } else {
                            context.showErrorMessage(context.getString(R.string.something_went_wrong_fix_soon));
                        }
                    }
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

    public void updateLog(String strUserID, String strAccessToken, String strLogID, Map<String, String> hashMap) {
        mUtility.showAnimatedProgress(context);

        mMyService.updateLogData(strUserID, strAccessToken, strLogID, hashMap, new MyService.ServiceCallback<VoResponceCreateLog>() {
            @Override
            public void onSuccess(VoResponceCreateLog data) {
                mUtility.HideAnimatedProgress();

                if (data != null) {
                    if (data.getSuccess() != null && data.getSuccess().equalsIgnoreCase("1")) {
                        context.completeShipper();
                    } else {
                        if (data != null && data.getMessage() != null && !data.getMessage().equalsIgnoreCase("")) {
                            context.showErrorMessage(data.getMessage());
                        } else {
                            context.showErrorMessage(context.getString(R.string.something_went_wrong_fix_soon));
                        }
                    }
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
