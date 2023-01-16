package com.gsp.trusttags.mobile.mapping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.gsp.trusttags.mobile.mapping.databinding.ActivityQaReprintBinding;
import com.gsp.trusttags.mobile.mapping.helper.TagValues;
import com.gsp.trusttags.mobile.mapping.helper.Utility;
import com.gsp.trusttags.mobile.mapping.viewModel.QAReprintViewModel;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseScanForReprint;

import org.json.JSONObject;

public class ActivityQAReprint extends BaseActivity {

    ActivityQaReprintBinding mActivityQaReprintBinding;
    QAReprintViewModel mQaReprintViewModel;

    String mStringShipperCode = "";

    public static final int ACTIVITY_REQUEST_FOR_SCANNING = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();
        mActivityQaReprintBinding = (ActivityQaReprintBinding) putContentView(R.layout.activity_qa_reprint);
        mQaReprintViewModel = new QAReprintViewModel(this, mMyService);
        mActivityQaReprintBinding.setQaReprintViewModel(mQaReprintViewModel);

        mImageViewDrawer.setImageResource(R.drawable.ic_menu);
        mTextViewTitle.setText(getResources().getString(R.string.text_reprint_title));
        mImageViewSync.setVisibility(View.GONE);

        mImageViewDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });

        mActivityQaReprintBinding.activityQaRemapTextviewSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (mStringShipperCode != null && !mStringShipperCode.equalsIgnoreCase("")) {
                    if (mUtility.haveInternet()) {
                        mQaReprintViewModel.sendShipperMail(mStringShipperCode);
                    }
                }*/
            }
        });

    }

    @Override
    public void showWait() {

    }

    @Override
    public void removeWait() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVITY_REQUEST_FOR_SCANNING) {
            if (data != null && data.getBooleanExtra("is_success", false)) {
                String mStringUniqueCode = data.getStringExtra("unique_code");

                if (mUtility.haveInternet() && mStringUniqueCode != null && !mStringUniqueCode.equalsIgnoreCase("")) {
                    JSONObject mJsonObjectScanForReprint = new JSONObject();
                    try {
                        mJsonObjectScanForReprint.put("uniqueCode", mStringUniqueCode);
                        mJsonObjectScanForReprint.put("required", isFrom);
                        mQaReprintViewModel.scanForReprint(mJsonObjectScanForReprint);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }

    }

    public void setResponseData(VoResponseScanForReprint voResponseScanForReprint) {

        if (voResponseScanForReprint.getData() != null && !voResponseScanForReprint.getData().equalsIgnoreCase("")) {
            mStringShipperCode = voResponseScanForReprint.getData();
            mActivityQaReprintBinding.activityQaRemapTextviewSendMail.setVisibility(View.VISIBLE);
        }

        if (voResponseScanForReprint.getUrl() != null && !voResponseScanForReprint.getUrl().equalsIgnoreCase("")) {
            Glide.with(this)
                    .load(voResponseScanForReprint.getUrl())
                    .apply(Utility.getCommanRequestOptions())
                    .into(mActivityQaReprintBinding.activityQaReprintImageviewQrCode);
        }

    }
    private String isFrom = "";
    public void gotoQAReprintScanScreen(String mStringIsFrom) {
        isFrom = mStringIsFrom;
        Intent mIntentScan = new Intent(ActivityQAReprint.this, ActivityQAScan.class);
        mIntentScan.putExtra(TagValues.IS_FROM, mStringIsFrom);
        startActivityForResult(mIntentScan, ACTIVITY_REQUEST_FOR_SCANNING);

       /* JSONObject mJsonObjectScanForReprint = new JSONObject();
        try {
            mJsonObjectScanForReprint.put("uniqueCode", "WW1AA21K");
            mJsonObjectScanForReprint.put("required", isFrom);
            mQaReprintViewModel.scanForReprint(mJsonObjectScanForReprint);
        } catch (Exception e) {
            e.printStackTrace();
        }*/


    }

}