package com.gsp.trusttags.mobile.mapping.viewModel;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.gsp.trusttags.mobile.mapping.R;
import com.gsp.trusttags.mobile.mapping.databinding.ActivityCreateShipperBinding;
import com.gsp.trusttags.mobile.mapping.ActivityCreateShipper;
import com.gsp.trusttags.mobile.mapping.helper.TagValues;
import com.gsp.trusttags.mobile.mapping.helper.Utility;
import com.gsp.trusttags.mobile.mapping.services.MyService;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceCreateLog;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceGetBatches;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceGetBatchesScanSequence;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseFetchSKU;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Created by Vajsi on 8/1/17.
 */
public class CreateShipperViewModel extends ViewModel {

    private ActivityCreateShipper context;
    public Utility mUtility;
    public MyService mMyService;

    public ActivityCreateShipperBinding mActivityCreateShipperBinding;

    SharedPreferences mSharedPreferencesCredential;
    SharedPreferences.Editor mEditor;

    public void setActivityAndService(@NonNull ActivityCreateShipper context, MyService myService, ActivityCreateShipperBinding activityCreateShipperBinding) {
        this.context = context;
        mUtility = new Utility(context);
        mMyService = myService;
        mActivityCreateShipperBinding = activityCreateShipperBinding;
        mSharedPreferencesCredential = context.getSharedPreferences(TagValues.PREF_CREDENTIAL_PREFERENCE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferencesCredential.edit();
    }

    public void getSkus(String strCompanyID, String strAccessToken) {
        mUtility.showAnimatedProgress(context);

        mMyService.getSkus(strCompanyID, strAccessToken, new MyService.ServiceCallback<VoResponseFetchSKU>() {
            @Override
            public void onSuccess(VoResponseFetchSKU data) {
                mUtility.HideAnimatedProgress();

                if (data != null && data.getSuccess() != null && data.getSuccess().equalsIgnoreCase("1") && data.getData() != null) {
                    if (data.getData() != null && data.getData().size() > 0) {
                        context.setSkuSpinnerData(data.getData());
                    }
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

    public void getBatches(String strCompanyID, String strAccessToken, String strSkuId,boolean isInLine) {
        mUtility.showAnimatedProgress(context);

        mMyService.getBatches(strCompanyID, strAccessToken, strSkuId, new MyService.ServiceCallback<VoResponceGetBatches>() {
            @Override
            public void onSuccess(VoResponceGetBatches data) {
                mUtility.HideAnimatedProgress();



                if (data != null && data.getSuccess() != null && data.getSuccess().equalsIgnoreCase("1") && data.getData() != null) {
                    if (data.getData() != null && data.getData().size() > 0) {
                        context.setSpinnerData(data.getData(),data.getScanSequence());
                    } else {
                        context.setSpinnerDataAsNull();
                    }
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
        },isInLine);
    }

    public void createLog(String strCompanyID, String strAccessToken, String strBatchID, String strBatchNo, Map<String, String> hashMap, VoResponceGetBatchesScanSequence scanSequence,boolean isInLine,
                          String strLineNo) {
        mUtility.showAnimatedProgress(context);

        mMyService.createPackagesLog(strCompanyID, strAccessToken, hashMap, new MyService.ServiceCallback<VoResponceCreateLog>() {

            @Override
            public void onSuccess(VoResponceCreateLog data) {
                mUtility.HideAnimatedProgress();
                if (data != null && data.getSuccess().equalsIgnoreCase("1")) {

                    if (data.getData() != null && data.getData().getId() != null) {
                        context.gotoScanningWithScanSequence(hashMap.get("product_id"), data.getData().getId(), strBatchID, data.getShipper_size(), strBatchNo,scanSequence,isInLine,strLineNo);
                    }

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