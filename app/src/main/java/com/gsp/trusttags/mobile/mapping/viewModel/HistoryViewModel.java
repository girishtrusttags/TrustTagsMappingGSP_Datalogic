package com.gsp.trusttags.mobile.mapping.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.gsp.trusttags.mobile.mapping.R;
import com.gsp.trusttags.mobile.mapping.databinding.ActivityHistoryBinding;
import com.gsp.trusttags.mobile.mapping.ActivityHistory;
import com.gsp.trusttags.mobile.mapping.helper.TagValues;
import com.gsp.trusttags.mobile.mapping.helper.Utility;
import com.gsp.trusttags.mobile.mapping.services.MyService;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceGetBatches;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceHistory;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseFetchSKU;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class HistoryViewModel extends ViewModel {

    private ActivityHistory context;
    public Utility mUtility;
    public MyService mMyService;

    public ActivityHistoryBinding mActivityHistoryBinding;

    public void setActivityAndService(@NonNull ActivityHistory context, MyService myService, ActivityHistoryBinding activityHistoryBinding) {
        this.context = context;
        mUtility = new Utility(context);
        mMyService = myService;
        mActivityHistoryBinding = activityHistoryBinding;
    }

    public void getScannedBatches(String strUserID, String strAccessToken,String productId,String  batchId, String startDate, String endDate) {
        mUtility.showAnimatedProgress(context);

        mMyService.getFilterScannnedHistoryData(strUserID, strAccessToken, productId,batchId,startDate,endDate,
                new MyService.ServiceCallback<VoResponceHistory>() {
            @Override
            public void onSuccess(VoResponceHistory data) {
                mUtility.HideAnimatedProgress();

                if (data != null) {
                    if (data.getSuccess() != null && data.getSuccess().equalsIgnoreCase("1")) {
                        if (data.getHistory() != null && data.getHistory().size() > 0) {
                            context.setBachListData(data.getHistory());
                        }
                    } else {
                        if (data.getMessage() != null && !data.getMessage().equalsIgnoreCase("")) {
                            context.showErrorMessage(data.getMessage());
                        } else {
                            context.showErrorMessage(context.getString(R.string.something_went_wrong_fix_soon));
                        }
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

    public void getBatches(String strCompanyID, String strAccessToken, String strSkuId) {
        mUtility.showAnimatedProgress(context);

        mMyService.getBatches(strCompanyID, strAccessToken, strSkuId, new MyService.ServiceCallback<VoResponceGetBatches>() {
            @Override
            public void onSuccess(VoResponceGetBatches data) {
                mUtility.HideAnimatedProgress();



                if (data != null && data.getSuccess() != null && data.getSuccess().equalsIgnoreCase("1") && data.getData() != null) {
                    if (data.getData() != null && data.getData().size() > 0) {
                        context.setBatchSpinnerData(data.getData());
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
        },false);
    }

}
