package com.gsp.trusttags.mobile.mapping.viewModel;

import androidx.lifecycle.ViewModel;

import com.gsp.trusttags.mobile.mapping.ActivityQARemap;
import com.gsp.trusttags.mobile.mapping.helper.TagValues;
import com.gsp.trusttags.mobile.mapping.helper.Utility;
import com.gsp.trusttags.mobile.mapping.services.MyService;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseFreeMapping;

import org.json.JSONObject;

public class QARemapViewModel extends ViewModel {

    private ActivityQARemap mActivityQARemap;
    private Utility mUtility;
    private MyService mMyService;

    public QARemapViewModel(ActivityQARemap mActivityQARemap, MyService mMyService) {
        this.mActivityQARemap = mActivityQARemap;
        this.mMyService = mMyService;
        this.mUtility = new Utility(this.mActivityQARemap);
    }

    public void freeMapping(String mStringShipperCode) {
        mUtility.showAnimatedProgress(mActivityQARemap);

        JSONObject mJsonObjectScanForReprint = new JSONObject();
        try {
            mJsonObjectScanForReprint.put("shipperCode", mStringShipperCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMyService.freeMapping(mActivityQARemap.mPreferenceHelper.getAccessToken(),
                mActivityQARemap.mPreferenceHelper.getUserId(),
                mJsonObjectScanForReprint,
                new MyService.ServiceCallback<VoResponseFreeMapping>() {
                    @Override
                    public void onSuccess(VoResponseFreeMapping data) {
                        mUtility.HideAnimatedProgress();

                        if (data != null) {
                            if (data.getSuccess() != null && data.getSuccess().equalsIgnoreCase(TagValues.CONST_NUMBER_1)) {
                                mActivityQARemap.setResponseData(data);
                            } else {
                                if (data.getStatus_code() != null && data.getStatus_code().equalsIgnoreCase("TTE003")) {
                                    mActivityQARemap.alreadyLoginWithOtherDevice();
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
