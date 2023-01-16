package com.gsp.trusttags.mobile.mapping.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.gsp.trusttags.mobile.mapping.ActivityScanning;
import com.gsp.trusttags.mobile.mapping.R;
import com.gsp.trusttags.mobile.mapping.databinding.ActivityScanningBinding;
import com.gsp.trusttags.mobile.mapping.helper.TagValues;
import com.gsp.trusttags.mobile.mapping.helper.Utility;
import com.gsp.trusttags.mobile.mapping.services.MyService;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceCreateLog;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceGetBatches;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceScanPackageQrCode;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseDiscardShipper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.HttpException;

public class ScanningViewModel extends ViewModel {

    private ActivityScanning context;
    public Utility mUtility;
    public MyService mMyService;
    public ActivityScanningBinding mActivityScanningBinding;

    public void setActivityAndService(@NonNull ActivityScanning context, MyService myService, ActivityScanningBinding activityScanningBinding) {
        this.context = context;
        mUtility = new Utility(context);
        mMyService = myService;
        mActivityScanningBinding = activityScanningBinding;
    }



    public void getBatchesForSameSKU(String strUserID, String strAccessToken, String strUnitID, String strProductID) {
        mMyService.getBatchesWithSameSKU(strUserID, strAccessToken, strUnitID, strProductID, new MyService.ServiceCallback<VoResponceGetBatches>() {
            @Override
            public void onSuccess(VoResponceGetBatches data) {

                if (data != null && data.getSuccess() != null && data.getSuccess().equalsIgnoreCase("1") && data.getData() != null) {
                    if (data.getData() != null && data.getData().size() > 0) {
                        context.setSpinnerData(data.getData());
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
                networkError.printStackTrace();
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

    public void scanQrCode(String strUserID, String strAccessToken, String strQRCode, Map<String, String> hashMap) {
        mUtility.showAnimatedProgress(context);
        mMyService.scanPackageQrcode(strUserID, strAccessToken, strQRCode, hashMap, new MyService.ServiceCallback<VoResponceScanPackageQrCode>() {
            @Override
            public void onSuccess(VoResponceScanPackageQrCode data) {
                mUtility.HideAnimatedProgress();
                if (data != null) {
                    if (data.getSuccess() != null && data.getSuccess().equalsIgnoreCase("1")) {
                        context.scannedProductQRCode(strQRCode);
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

    public void discardShipper(JSONObject mJsonObjectData) {
        mUtility.showAnimatedProgress(context);

        RequestBody mRequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mJsonObjectData.toString());

        mMyService.discardShipper(context.mPreferenceHelper.getUserId(), context.mPreferenceHelper.getAccessToken(),
                mRequestBody, new MyService.ServiceCallback<VoResponseDiscardShipper>() {
                    @Override
                    public void onSuccess(VoResponseDiscardShipper data) {
                        mUtility.HideAnimatedProgress();

                        if (data != null) {
                            if (data.getSuccess() != null && data.getSuccess().equalsIgnoreCase("1")) {
                                context.completeDiscardShipper(data);
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

    public void updateLog(String strUserID, String strAccessToken, String strLogID, Map<String, String> hashMap) {
        mUtility.showAnimatedProgress(context);

        mMyService.updateLogData(strUserID, strAccessToken, strLogID, hashMap, new MyService.ServiceCallback<VoResponceCreateLog>() {
            @Override
            public void onSuccess(VoResponceCreateLog data) {
                mUtility.HideAnimatedProgress();

                if (data != null) {
                    if (data.getSuccess() != null && data.getSuccess().equalsIgnoreCase("1")) {
                        if(hashMap.get("type").equals(TagValues.SHIPPER_KEY)){
                            context.completeShipper();
                        }else if(hashMap.get("type").equals(TagValues.SECOND_INNER_KEY)){
                            context.completeSecondInner();
                        }else if(hashMap.get("type").equals(TagValues.FIRST_INNER_KEY)){
                            context.completeFirstInner();
                        }else if(hashMap.get("type").equals(TagValues.SKUCODE_KEY)){
                            context.completeSKU();
                        }

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

    public void createLog(String strCompanyID, String strAccessToken, String strBatchID, String strBatchNo, Map<String, String> hashMap) {
        mUtility.showAnimatedProgress(context);

        mMyService.createPackagesLog(strCompanyID, strAccessToken, hashMap, new MyService.ServiceCallback<VoResponceCreateLog>() {

            @Override
            public void onSuccess(VoResponceCreateLog data) {
                mUtility.HideAnimatedProgress();
                if (data != null && data.getSuccess().equalsIgnoreCase("1")) {

                    if (data.getData() != null && data.getData().getId() != null) {
                        context.updateLogData(data.getData().getId(), data.getShipper_size());
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
                        context.setChangeBatchDialog(data.getData());
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
