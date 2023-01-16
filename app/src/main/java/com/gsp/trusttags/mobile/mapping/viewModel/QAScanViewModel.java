package com.gsp.trusttags.mobile.mapping.viewModel;

import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.gsp.trusttags.mobile.mapping.ActivityQAScan;
import com.gsp.trusttags.mobile.mapping.helper.TagValues;
import com.gsp.trusttags.mobile.mapping.helper.Utility;
import com.gsp.trusttags.mobile.mapping.services.MyService;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseFreeMapping;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseScanForRemap;

import org.json.JSONObject;

public class QAScanViewModel extends ViewModel {

    private ActivityQAScan mActivityQAScan;
    private Utility mUtility;
    private MyService mMyService;

    public QAScanViewModel(ActivityQAScan mActivityQAScan, MyService mMyService) {
        this.mActivityQAScan = mActivityQAScan;
        this.mMyService = mMyService;
        this.mUtility = new Utility(this.mActivityQAScan);
    }

    public void scanForRemap(String mStringShipperCode) {
        mUtility.showAnimatedProgress(mActivityQAScan);

        mMyService.scanForRemap(mActivityQAScan.mPreferenceHelper.getAccessToken(), mActivityQAScan.mPreferenceHelper.getUserId(),
                mStringShipperCode,
                new MyService.ServiceCallback<VoResponseScanForRemap>() {
                    @Override
                    public void onSuccess(VoResponseScanForRemap data) {
                        mUtility.HideAnimatedProgress();

                        if (data != null) {
                            if (data.getSuccess() != null && data.getSuccess().equalsIgnoreCase(TagValues.CONST_NUMBER_1)) {
                                mActivityQAScan.gotoRemapWithData(data, mStringShipperCode);
                            } else {
                                if (data.getStatus_code() != null && data.getStatus_code().equalsIgnoreCase("TTE003")) {
                                    mActivityQAScan.alreadyLoginWithOtherDevice();
                                } else if (data.getMessage() != null && !data.getMessage().equalsIgnoreCase("")) {
                                    Toast.makeText(mActivityQAScan, data.getMessage(), Toast.LENGTH_SHORT).show();
                                    mActivityQAScan.onBackPressed();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable networkError) {
                        networkError.printStackTrace();
                        mUtility.HideAnimatedProgress();
                    }
                });
    }


    public void freeMapping(String mStringShipperCode) {
        mUtility.showAnimatedProgress(mActivityQAScan);

        JSONObject mJsonObjectScanForReprint = new JSONObject();
        try {
            mJsonObjectScanForReprint.put("shipperCode", mStringShipperCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMyService.freeMapping(mActivityQAScan.mPreferenceHelper.getAccessToken(),
                mActivityQAScan.mPreferenceHelper.getUserId(),
                mJsonObjectScanForReprint,
                new MyService.ServiceCallback<VoResponseFreeMapping>() {
                    @Override
                    public void onSuccess(VoResponseFreeMapping data) {
                        mUtility.HideAnimatedProgress();

                        if (data != null) {
                            if (data.getSuccess() != null && data.getSuccess().equalsIgnoreCase(TagValues.CONST_NUMBER_1)) {
                                mActivityQAScan.setResponseData(data);
                            } else {
                                if (data.getStatus_code() != null && data.getStatus_code().equalsIgnoreCase("TTE003")) {
                                    mActivityQAScan.alreadyLoginWithOtherDevice();
                                }else if (data.getMessage() != null && !data.getMessage().equalsIgnoreCase("")) {
                                    Toast.makeText(mActivityQAScan, data.getMessage(), Toast.LENGTH_SHORT).show();
//                                    mActivityQAScan.onBackPressed();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable networkError) {
                        networkError.printStackTrace();
                        mUtility.HideAnimatedProgress();
                    }
                });
    }
}
