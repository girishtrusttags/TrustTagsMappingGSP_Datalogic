package com.gsp.trusttags.mobile.mapping;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.datalogic.decode.BarcodeManager;
import com.datalogic.decode.DecodeException;
import com.datalogic.decode.DecodeResult;
import com.datalogic.decode.ReadListener;
import com.datalogic.decode.StartListener;
import com.datalogic.decode.StopListener;
import com.gsp.trusttags.mobile.mapping.databinding.ActivityQaScanBinding;
import com.gsp.trusttags.mobile.mapping.helper.TagValues;
import com.gsp.trusttags.mobile.mapping.viewModel.QAScanViewModel;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseFreeMapping;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseScanForRemap;

import info.androidhive.barcode.BarcodeReader;

public class ActivityQAScan extends BaseActivity {

    private String TAG = ActivityQAScan.class.getSimpleName();
    ActivityQaScanBinding mActivityQaScanBinding;

    QAScanViewModel mQaScanViewModel;

    BarcodeReader mBarcodeReader;

    DisplayMetrics dm;

    String isFrom = "";
    String strQrCode = "";

    BarcodeManager mBarcodeManager;

    ReadListener mReadListener;

    public static final int ACTIVITY_REQUEST_FOR_SCANNING = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dm = getResources().getDisplayMetrics();

        if (getIntent() != null && getIntent().hasExtra(TagValues.IS_FROM)) {
            isFrom = getIntent().getStringExtra(TagValues.IS_FROM);
        }

        initToolbar();

        mActivityQaScanBinding = (ActivityQaScanBinding) putContentView(R.layout.activity_qa_scan);
        mQaScanViewModel = new QAScanViewModel(this, mMyService);

        mImageViewDrawer.setImageResource(R.drawable.ic_back_white);
        if(isFrom.equals(TagValues.isFromQARemapDashboard)){
            mTextViewTitle.setText("Scan Shipper Code");
        }else{

            mTextViewTitle.setText("Scan Code");
        }


        mImageViewDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mImageViewSync.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            if (mBarcodeManager == null) {
                mBarcodeManager = new BarcodeManager();
                mReadListener = new ReadListener() {
                    @Override
                    public void onRead(DecodeResult decodeResult) {
                        strQrCode = decodeResult.getText().toString();
                        strQrCode = strQrCode.replace("\\000026", "");
                        strQrCode = strQrCode.replace("\n", "");

                        if (strQrCode.contains("/")) {
                            strQrCode = strQrCode.substring(strQrCode.lastIndexOf("/") + 1, strQrCode.length());
                        } else if (strQrCode.contains("\\")) {
                            strQrCode = strQrCode.substring(strQrCode.lastIndexOf("\\") + 1, strQrCode.length());
                        }

                        if (isFrom.equalsIgnoreCase(TagValues.isFromQAReprintSKUCodeScan)) {
                            if (strQrCode.substring(0, 2).equalsIgnoreCase("AE")) {
                                strQrCode = strQrCode.replace("AE", "");
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mUtility.haveInternet()) {
                                    switch (isFrom) {
                                        case TagValues.isFromQARemapDashboard:
                                            if (mUtility.haveInternet()) {
                                                // before free map this below service used
                                                //mQaScanViewModel.scanForRemap(strQrCode);
                                                mQaScanViewModel.freeMapping(strQrCode);
                                            }
                                            break;
                                        case TagValues.isFromQARemap:
                                            Toast.makeText(ActivityQAScan.this, "Scan and Remove Product from Shipper", Toast.LENGTH_SHORT).show();
                                            break;
                                        case TagValues.isFromQAReprintShipperScan:
                                        case TagValues.FIRST_INNER_KEY:
                                            onBackPressed();
                                            break;
                                        case TagValues.SECOND_INNER_KEY:
                                            onBackPressed();
                                            break;
                                        case TagValues.SHIPPER_KEY:
                                            onBackPressed();
                                            break;
                                        case TagValues.isFromQAReprintSKUCodeScan:
                                            onBackPressed();
                                            break;
                                    }
                                } else {
                                    showErrorMessage(getResources().getString(R.string.internet_error));
                                }
                            }
                        });
                    }
                };

                mBarcodeManager.addReadListener(mReadListener);

                mBarcodeManager.addStartListener(new StartListener() {
                    @Override
                    public void onScanStarted() {

                    }
                });

                mBarcodeManager.addStopListener(new StopListener() {
                    @Override
                    public void onScanStopped() {
                        System.out.println("DecodeException onScanStopped....");
                    }
                });

            }

        }  catch(NoClassDefFoundError e) {
            e.printStackTrace();
        }catch (DecodeException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void onPause() {
        super.onPause();
        if (mBarcodeManager != null) {
            try {
                mBarcodeManager.removeReadListener(mReadListener);
                mBarcodeManager = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent mIntent = new Intent();
        mIntent.putExtra("is_success", true);
        mIntent.putExtra("unique_code", strQrCode);
        setResult(ACTIVITY_REQUEST_FOR_SCANNING, mIntent);

        super.onBackPressed();
    }

    public void showWait() {
        mUtility.showAnimatedProgress(ActivityQAScan.this);
    }

    public void removeWait() {
        mUtility.HideAnimatedProgress();
    }

    public void gotoRemapWithData(VoResponseScanForRemap voResponseScanForRemap, String mStringShipperCode) {
        Intent mIntentRemap = new Intent(ActivityQAScan.this, ActivityQARemap.class);
        mIntentRemap.putExtra("remap_data", voResponseScanForRemap);
        mIntentRemap.putExtra("shipper_code", mStringShipperCode);
        finish();
        startActivity(mIntentRemap);
    }

    // free map response added
    public void setResponseData(VoResponseFreeMapping voResponseFreeMapping) {
        if (voResponseFreeMapping.getMessage() != null && !voResponseFreeMapping.getMessage().equalsIgnoreCase("")) {
            Toast.makeText(this, voResponseFreeMapping.getMessage(), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

}