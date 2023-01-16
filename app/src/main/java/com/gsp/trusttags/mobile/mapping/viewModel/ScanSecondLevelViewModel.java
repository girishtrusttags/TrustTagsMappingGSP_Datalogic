package com.gsp.trusttags.mobile.mapping.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.gsp.trusttags.mobile.mapping.ActivityScanSecondLevel;
import com.gsp.trusttags.mobile.mapping.R;
import com.gsp.trusttags.mobile.mapping.helper.TagValues;
import com.gsp.trusttags.mobile.mapping.helper.Utility;
import com.gsp.trusttags.mobile.mapping.services.MyService;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseScanSecondLevel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

public class ScanSecondLevelViewModel extends ViewModel {

    private ActivityScanSecondLevel context;
    public Utility mUtility;
    public MyService mMyService;

    public void setActivityAndService(@NonNull ActivityScanSecondLevel context, MyService myService) {
        this.context = context;
        mUtility = new Utility(context);
        mMyService = myService;
    }

    public void scanSecondLevelQrCode(String strUserID, String strAccessToken,JSONObject mJsonObjectData,String qrCode) {
        mUtility.showAnimatedProgress(context);

        //Toast.makeText(context, "Shipper URL : " + TagValues.URL_SCAN_SHIPPER_CODE, Toast.LENGTH_SHORT).show();

        mMyService.scanSecondLevelQrCode(strUserID, strAccessToken, mJsonObjectData, new MyService.ServiceCallback<VoResponseScanSecondLevel>() {
            @Override
            public void onSuccess(VoResponseScanSecondLevel data) {
                mUtility.HideAnimatedProgress();
                if (data != null && data.getSuccess() != null && data.getSuccess().equalsIgnoreCase("1")) {
                    context.getSecondLevelData(qrCode);
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
    public void scanSecondLevelQrCodeInLine(String strUserID, String strAccessToken,JSONObject mJsonObjectData,String qrCode) {
        mUtility.showAnimatedProgress(context);

        //Toast.makeText(context, "Shipper URL : " + TagValues.URL_SCAN_SHIPPER_CODE, Toast.LENGTH_SHORT).show();

        mMyService.scanSecondLevelQrCodeInLine(strUserID, strAccessToken, mJsonObjectData, new MyService.ServiceCallback<VoResponseScanSecondLevel>() {
            @Override
            public void onSuccess(VoResponseScanSecondLevel data) {
                mUtility.HideAnimatedProgress();
                if (data != null && data.getSuccess() != null && data.getSuccess().equalsIgnoreCase("1")) {
                    context.getSecondLevelData(qrCode);
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
