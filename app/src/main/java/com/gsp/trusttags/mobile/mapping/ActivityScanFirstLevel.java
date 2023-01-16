package com.gsp.trusttags.mobile.mapping;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;

import com.datalogic.decode.BarcodeManager;
import com.datalogic.decode.DecodeException;
import com.datalogic.decode.DecodeResult;
import com.datalogic.decode.ReadListener;
import com.datalogic.decode.StartListener;
import com.datalogic.decode.StopListener;
import com.google.android.gms.vision.barcode.Barcode;
import com.gsp.trusttags.mobile.mapping.databinding.ActivityScanShipperBinding;
import com.gsp.trusttags.mobile.mapping.view.CustomModel;
import com.gsp.trusttags.mobile.mapping.viewModel.ScanFirstLevelViewModel;
import com.gsp.trusttags.mobile.mapping.viewModel.ScanShipperViewModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.barcode.BarcodeReader;

public class ActivityScanFirstLevel extends BaseActivity implements BarcodeReader.BarcodeReaderListener {

    ActivityScanShipperBinding mActivityScanShipperBinding;

    ScanFirstLevelViewModel mScanFirstLevelViewModel;

    BarcodeManager mBarcodeManager;

    ReadListener mReadListener;

    String strFirstLevelID = "";
    String strProductID = "";
    String strLogID = "";
    String strBatchID = "";
    String strQrCode = "";
    String strFirstInnerLogID = "";
    String strLineNo="";
    BarcodeReader mBarcodeReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        mActivityScanShipperBinding = (ActivityScanShipperBinding) putContentView(R.layout.activity_scan_shipper);

        mScanFirstLevelViewModel = ViewModelProviders.of(this).get(ScanFirstLevelViewModel.class);
        mScanFirstLevelViewModel.setActivityAndService(this, mMyService);

        mImageViewDrawer.setImageResource(R.drawable.ic_back_white);
        mTextViewTitle.setText("Scan First Level Code");

        strProductID = getIntent().getStringExtra("strProductID");
        strLogID = getIntent().getStringExtra("strLogID");
        strBatchID = getIntent().getStringExtra("batch_id");
        strFirstInnerLogID = getIntent().getStringExtra("strFirstInnerLogID");
        strLineNo= getIntent().getStringExtra("strLineNo");

        mImageViewDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mImageViewSync.setVisibility(View.GONE);
    }

    public void getFirstLevelData(String strFirstLevelID) {
        this.strFirstLevelID = strFirstLevelID;
        onBackPressed();
    }

    @Override
    public void showWait() {

    }

    @Override
    public void removeWait() {

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

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mUtility.haveInternet()) {
                                    JSONObject jsonRequest = new JSONObject();
                                    try {
                                        jsonRequest.put("log_id", strLogID);
                                        jsonRequest.put("product_id", strProductID);
                                        jsonRequest.put("qrCode", strQrCode);
                                        jsonRequest.put("innerLogId", strFirstInnerLogID);
                                        jsonRequest.put("batchId", strBatchID);
                                        jsonRequest.put("lineNo", strLineNo);


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    mScanFirstLevelViewModel.scanFirstLevelQrCode(mPreferenceHelper.getUserId(), mPreferenceHelper.getAccessToken(), jsonRequest,strQrCode);
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

        } catch (DecodeException e) {
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
        CustomModel.getInstance().changeFirstLevelState(true, strFirstLevelID);
        super.onBackPressed();
    }

    @Override
    public void onScanned(Barcode barcode) {

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                String mStringValue = barcode.displayValue;
//
//                if (mStringValue.contains("/")) {
//                    mStringValue = mStringValue.substring(mStringValue.lastIndexOf("/") + 1, mStringValue.length());
//                } else if (mStringValue.contains("\\")) {
//                    mStringValue = mStringValue.substring(mStringValue.lastIndexOf("\\") + 1, mStringValue.length());
//                }
//
//                if (mUtility.haveInternet()) {
//                    Map<String, String> hashMap = new HashMap<>();
//                    hashMap.put("log_id", strLogID);
//                    hashMap.put("employee_id", mPreferenceHelper.getUserId());
//                    hashMap.put("product_id", strProductID);
//                    hashMap.put("batch_id", strBatchID);
//                    mScanShipperViewModel.scanShipperQrCode(mPreferenceHelper.getUserId(), mPreferenceHelper.getAccessToken(), mStringValue, hashMap);
//                } else {
//                    showErrorMessage(getResources().getString(R.string.internet_error));
//                }
//
//            }
//        });

    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {

    }

}
