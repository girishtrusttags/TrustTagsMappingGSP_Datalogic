package com.gsp.trusttags.mobile.mapping.viewModel;

import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.gsp.trusttags.mobile.mapping.ActivityQAReprint;
import com.gsp.trusttags.mobile.mapping.helper.TagValues;
import com.gsp.trusttags.mobile.mapping.helper.Utility;
import com.gsp.trusttags.mobile.mapping.services.MyService;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseReprintSendMail;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseScanForReprint;

import org.json.JSONObject;

public class QAReprintViewModel extends ViewModel {

    private ActivityQAReprint mActivityQAReprint;
    private Utility mUtility;
    private MyService mMyService;

    public QAReprintViewModel(ActivityQAReprint mActivityQAReprint, MyService mMyService) {
        this.mActivityQAReprint = mActivityQAReprint;
        this.mMyService = mMyService;
        this.mUtility = new Utility(this.mActivityQAReprint);
    }

    public void scanShipperClick() {
//        mActivityQAReprint.gotoQAReprintScanScreen(TagValues.isFromQAReprintShipperScan);
        mActivityQAReprint.gotoQAReprintScanScreen(TagValues.SHIPPER_KEY);
    }

    public void scanSKUCodeClick() {
        mActivityQAReprint.gotoQAReprintScanScreen(TagValues.isFromQAReprintSKUCodeScan);
    }

    public void scanFirstLevelCodeClick() {
        mActivityQAReprint.gotoQAReprintScanScreen(TagValues.FIRST_INNER_KEY);
    }

    public void scanSecondLevelCodeClick() {
        mActivityQAReprint.gotoQAReprintScanScreen(TagValues.SECOND_INNER_KEY);
    }

    public void scanForReprint(JSONObject mJsonObjectScanForReprint) {
        mUtility.showAnimatedProgress(mActivityQAReprint);



        mMyService.scanForReprint(mActivityQAReprint.mPreferenceHelper.getAccessToken(), mActivityQAReprint.mPreferenceHelper.getUserId(),
                mJsonObjectScanForReprint,
                new MyService.ServiceCallback<VoResponseScanForReprint>() {
                    @Override
                    public void onSuccess(VoResponseScanForReprint data) {
                        mUtility.HideAnimatedProgress();

                        if (data != null) {
                            if (data.getSuccess() != null && data.getSuccess().equalsIgnoreCase("1")) {
                                mActivityQAReprint.setResponseData(data);
                            } else {
                                if (data.getStatus_code() != null && data.getStatus_code().equalsIgnoreCase("TTE003")) {
                                    mActivityQAReprint.alreadyLoginWithOtherDevice();
                                } else {
                                    if (data.getMessage() != null && !data.getMessage().equalsIgnoreCase("")) {
                                        mActivityQAReprint.showErrorMessage(data.getMessage());
                                    }
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

    public void sendShipperMail(String mStringShipperCode) {
        mUtility.showAnimatedProgress(mActivityQAReprint);

        JSONObject mJsonObjectScanForReprint = new JSONObject();
        try {
            mJsonObjectScanForReprint.put("shipperCode", mStringShipperCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMyService.reprintSendMail(mActivityQAReprint.mPreferenceHelper.getAccessToken(), mActivityQAReprint.mPreferenceHelper.getUserId(),
                mJsonObjectScanForReprint,
                new MyService.ServiceCallback<VoResponseReprintSendMail>() {
                    @Override
                    public void onSuccess(VoResponseReprintSendMail data) {
                        mUtility.HideAnimatedProgress();

                        if (data != null) {
                            if (data.getSuccess() != null && data.getSuccess().equalsIgnoreCase("1")) {

                                if (data.getMessage() != null && !data.getMessage().equalsIgnoreCase("")) {
                                    Toast.makeText(mActivityQAReprint, data.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                mActivityQAReprint.onBackPressed();

                            } else {
                                if (data.getStatus_code() != null && data.getStatus_code().equalsIgnoreCase("TTE003")) {
                                    mActivityQAReprint.alreadyLoginWithOtherDevice();
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
