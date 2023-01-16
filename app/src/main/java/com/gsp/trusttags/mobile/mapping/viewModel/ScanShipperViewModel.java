package com.gsp.trusttags.mobile.mapping.viewModel;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.gsp.trusttags.mobile.mapping.ActivityScanShipper;
import com.gsp.trusttags.mobile.mapping.R;
import com.gsp.trusttags.mobile.mapping.helper.TagValues;
import com.gsp.trusttags.mobile.mapping.helper.Utility;
import com.gsp.trusttags.mobile.mapping.services.MyService;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceScanShipperCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;

import retrofit2.HttpException;

public class ScanShipperViewModel extends ViewModel {

    private ActivityScanShipper context;
    public Utility mUtility;
    public MyService mMyService;

    public void setActivityAndService(@NonNull ActivityScanShipper context, MyService myService) {
        this.context = context;
        mUtility = new Utility(context);
        mMyService = myService;
    }

    public void scanShipperQrCode(String strUserID, String strAccessToken, String strQrCode, Map<String, String> hashMap) {
        mUtility.showAnimatedProgress(context);

        //Toast.makeText(context, "Shipper URL : " + TagValues.URL_SCAN_SHIPPER_CODE, Toast.LENGTH_SHORT).show();

        mMyService.scanShipperQrCode(strUserID, strAccessToken, strQrCode, hashMap, new MyService.ServiceCallback<VoResponceScanShipperCode>() {
            @Override
            public void onSuccess(VoResponceScanShipperCode data) {
                mUtility.HideAnimatedProgress();
                if (data != null && data.getSuccess() != null && data.getSuccess().equalsIgnoreCase("1") && data.getData() != null) {
                    context.getShipperData(data.getData().getId());
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

    public void scanShipperQrCodeInLine(String strUserID, String strAccessToken, String strQrCode, Map<String, String> hashMap) {
        mUtility.showAnimatedProgress(context);

        //Toast.makeText(context, "Shipper URL : " + TagValues.URL_SCAN_SHIPPER_CODE, Toast.LENGTH_SHORT).show();

        mMyService.scanShipperQrCodeInLine(strUserID, strAccessToken, strQrCode, hashMap, new MyService.ServiceCallback<VoResponceScanShipperCode>() {
            @Override
            public void onSuccess(VoResponceScanShipperCode data) {
                mUtility.HideAnimatedProgress();
                if (data != null && data.getSuccess() != null && data.getSuccess().equalsIgnoreCase("1") && data.getData() != null) {
                    context.getShipperData(data.getData().getId());
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
