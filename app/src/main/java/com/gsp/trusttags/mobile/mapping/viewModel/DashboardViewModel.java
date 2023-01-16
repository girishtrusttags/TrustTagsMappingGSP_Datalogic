package com.gsp.trusttags.mobile.mapping.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.gsp.trusttags.mobile.mapping.R;
import com.gsp.trusttags.mobile.mapping.databinding.ActivityDashboardBinding;
import com.gsp.trusttags.mobile.mapping.ActivityDashboard;
import com.gsp.trusttags.mobile.mapping.helper.TagValues;
import com.gsp.trusttags.mobile.mapping.helper.Utility;
import com.gsp.trusttags.mobile.mapping.services.MyService;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseCheckInCompleteShipper;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceCreateLog;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseIncompleteShipperList;
import com.sun.mail.iap.ResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class DashboardViewModel extends ViewModel {

    private ActivityDashboard context;
    public Utility mUtility;
    public MyService mMyService;

    public ActivityDashboardBinding mActivityDashboardBinding;

    public void setActivityAndService(@NonNull ActivityDashboard context, MyService myService, ActivityDashboardBinding activityDashboardBinding) {
        this.context = context;
        mUtility = new Utility(context);
        mMyService = myService;
        mActivityDashboardBinding = activityDashboardBinding;
    }

    public void checkInCompleteShipper(String strUserID, String strAccessToken,boolean isInLine) {
        mUtility.showAnimatedProgress(context);

        mMyService.checkIncompleteShipper(strUserID, strAccessToken, new MyService.ServiceCallback<VoResponseCheckInCompleteShipper>() {
            @Override
            public void onSuccess(VoResponseCheckInCompleteShipper data) {
                mUtility.HideAnimatedProgress();

                String strLogId = "";
                String strProductID = "";
                String strShipperID = "";
                ArrayList<VoResponseIncompleteShipperList> mArrayListQRCode = new ArrayList<>();
//                context.gotoCreateShipper();

                if (data != null) {
                    if (data.getSuccess() != null && data.getSuccess().equalsIgnoreCase("1")) {

                        if(data.isCompleted()){
                            context.gotoCreateShipper(isInLine);
                        }else{
                            VoResponseCheckInCompleteShipper.VoResponceLogs logs = data.getLogs();
                            if (data.getLog() != null) {
                                strLogId = data.getLog().getId();
                                strProductID = data.getLog().getProduct_id();
                            }

                            if (data.isShipperAllocated()) {
                                strShipperID = data.getShipperId();
                            }

                            if (data.getIncomplete_shipperlist() != null) {
                                mArrayListQRCode = data.getIncomplete_shipperlist();
                            }


                            context.gotoScanning(logs.getProductId(), false, strLogId, logs.getBatchId(), logs.getBatchNo(), strShipperID,
                                    logs.getShipperSize(),
                                    mArrayListQRCode,
                                    data.getScanSequence(),logs,isInLine);
                        }
                    } else {
                        context.gotoCreateShipper(isInLine);
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

    public void checkInCompleteShipperInLine(String strUserID, String strAccessToken,boolean isInLine) {
        mUtility.showAnimatedProgress(context);

        mMyService.checkIncompleteShipperInLine(strUserID, strAccessToken, new MyService.ServiceCallback<VoResponseCheckInCompleteShipper>() {
            @Override
            public void onSuccess(VoResponseCheckInCompleteShipper data) {
                mUtility.HideAnimatedProgress();

                String strLogId = "";
                String strProductID = "";
                String strShipperID = "";
                ArrayList<VoResponseIncompleteShipperList> mArrayListQRCode = new ArrayList<>();
//                context.gotoCreateShipper();

                if (data != null) {
                    if (data.getSuccess() != null && data.getSuccess().equalsIgnoreCase("1")) {

                        if(data.isCompleted()){
                            context.gotoCreateShipper(isInLine);
                        }else{
                            VoResponseCheckInCompleteShipper.VoResponceLogs logs = data.getLogs();
                            if (data.getLog() != null) {
                                strLogId = data.getLog().getId();
                                strProductID = data.getLog().getProduct_id();
                            }

                            if (data.isShipperAllocated()) {
                                strShipperID = data.getShipperId();
                            }

                            if (data.getIncomplete_shipperlist() != null) {
                                mArrayListQRCode = data.getIncomplete_shipperlist();
                            }


                            context.gotoScanning(logs.getProductId(), false, strLogId, logs.getBatchId(), logs.getBatchNo(), strShipperID,
                                    logs.getShipperSize(),
                                    mArrayListQRCode,
                                    data.getScanSequence(),logs,isInLine);
                        }
                    } else {
                        context.gotoCreateShipper(isInLine);
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


   /* public void checkInCompleteShipper(String strUserID, String strAccessToken) {
        mUtility.showAnimatedProgress(context);

        mMyService.checkIncompleteShipper(strUserID, strAccessToken, new MyService.ServiceCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody data) {
                mUtility.HideAnimatedProgress();

                String strLogId = "";
                String strProductID = "";
                String strShipperID = "";
                ArrayList<VoResponseIncompleteShipperList> mArrayListQRCode = new ArrayList<>();
                context.gotoCreateShipper();

               *//* if (data != null) {
                    if (data.getSuccess() != null && data.getSuccess().equalsIgnoreCase("1")) {

                        if(data.isCompleted()){
                            context.gotoCreateShipper();
                        }else{
                            if (data.getLog() != null) {
                                strLogId = data.getLog().getId();
                                strProductID = data.getLog().getProduct_id();
                            }

                            if (data.isShipperAllocated()) {
                                strShipperID = data.getShipperId();
                            }

                            if (data.getIncomplete_shipperlist() != null) {
                                mArrayListQRCode = data.getIncomplete_shipperlist();
                            }


                            context.gotoScanning(strProductID, data.isShipperAllocated(), strLogId, data.getBatchId(), data.getBatchNo(), strShipperID, data.getShipper_size(), mArrayListQRCode,
                                    data.getScanSequence());
                        }


                    } else {

//                        Map<String, String> hashMap = new HashMap<>();
//                        hashMap.put("employee_id", context.mPreferenceHelper.getUserId());
//                        hashMap.put("product_id", mStringProductID);
//
//                        createLog(context.mPreferenceHelper.getUserId(), context.mPreferenceHelper.getAccessToken(), mStringBatchID, hashMap);

//                        context.gotoScanning();
                        context.gotoCreateShipper();
                    }
                }*//*
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
    }*/

    private void createLog(String strCompanyID, String strAccessToken, String strBatchID, Map<String, String> hashMap) {
        mUtility.showAnimatedProgress(context);

        mMyService.createPackagesLog(strCompanyID, strAccessToken, hashMap, new MyService.ServiceCallback<VoResponceCreateLog>() {

            @Override
            public void onSuccess(VoResponceCreateLog data) {
                mUtility.HideAnimatedProgress();
                if (data != null && data.getSuccess().equalsIgnoreCase("1")) {

                    if (data.getData() != null && data.getData().getId() != null) {
                        context.gotoScanning(hashMap.get("product_id"), data.getData().getId(), strBatchID, data.getShipper_size(), "");
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
