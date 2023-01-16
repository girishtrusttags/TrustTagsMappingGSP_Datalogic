package com.gsp.trusttags.mobile.mapping;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.vision.barcode.Barcode;
import com.gsp.trusttags.mobile.mapping.databinding.ActivityScanProductQrBinding;
import com.gsp.trusttags.mobile.mapping.viewModel.ScanProductViewModel;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseIncompleteShipperList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.barcode.BarcodeReader;

public class ActivityScanProductQR extends BaseActivity implements BarcodeReader.BarcodeReaderListener {

    private String TAG = ActivityScanProductQR.class.getSimpleName();

    ActivityScanProductQrBinding mActivityScanProductQrBinding;

    ScanProductViewModel mScanProductViewModel;

    BarcodeReader mBarcodeReader;

    ArrayList<VoResponseIncompleteShipperList> mArrayListQRCode = new ArrayList<>();

    String strProductID = "";
    String strLogID = "";
    String strBatchID = "";
    String strBatchNo = "";
    String strShipperID = "";
    String strShipperSize = "";
    String strLineNo ="";

    boolean isShipperComplete = false;

    public static final int ACTIVITY_REQUEST_FOR_SCANNING_PRODUCT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        strProductID = getIntent().getStringExtra("strProductID");
        strLogID = getIntent().getStringExtra("strLogID");
        strBatchID = getIntent().getStringExtra("strBatchID");
        strBatchNo = getIntent().getStringExtra("strBatchNo");
        strShipperSize = getIntent().getStringExtra("strShipperSize");
        strShipperID = getIntent().getStringExtra("strShipperID");
        strLineNo = getIntent().getStringExtra("strLineNo");
        if (getIntent().hasExtra("mArrayListQRCode")) {
            mArrayListQRCode = (ArrayList<VoResponseIncompleteShipperList>) getIntent().getSerializableExtra("mArrayListQRCode");
        }

        mActivityScanProductQrBinding = (ActivityScanProductQrBinding) putContentView(R.layout.activity_scan_product_qr);
        mActivityScanProductQrBinding.activityMainTxtCount.setAlpha(0f);

        mBarcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.activity_scan_product_qr_scanner);

        mScanProductViewModel = ViewModelProviders.of(this).get(ScanProductViewModel.class);
        mScanProductViewModel.setActivityAndService(this, mMyService);

        setOnlyMyActionBar();
        mToolbarMain.setVisibility(View.GONE);

        mActivityScanProductQrBinding.activityScanningTxtScannedBox.setText("Scanned Product : " + mArrayListQRCode.size());
        mActivityScanProductQrBinding.activityScanningTxtBatchNo.setText(getResources().getString(R.string.text_scanning_batch_no, strBatchNo));
        mActivityScanProductQrBinding.activityScanningTxtTotalBox.setText(getResources().getString(R.string.text_scanning_shipper_size, strShipperSize));

        mActivityScanProductQrBinding.activityScanProductQrImageviewFlashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBarcodeReader.getFlase()) {
                    mActivityScanProductQrBinding.activityScanProductQrImageviewFlashlight.setImageResource(R.drawable.ic_touch_off);
                    mBarcodeReader.setFalsh(false);
                } else {
                    mActivityScanProductQrBinding.activityScanProductQrImageviewFlashlight.setImageResource(R.drawable.torch);
                    mBarcodeReader.setFalsh(true);
                }
            }
        });

        mActivityScanProductQrBinding.activityScanningBtnShowPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mActivityScanProductQrBinding.activityScanProductQrImageviewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent mIntent = new Intent();
        mIntent.putExtra("is_success", true);
        mIntent.putExtra("isShipperComplete", isShipperComplete);
        mIntent.putExtra("mArrayListQRCode", mArrayListQRCode);
        setResult(ACTIVITY_REQUEST_FOR_SCANNING_PRODUCT, mIntent);

        super.onBackPressed();

    }

    @Override
    protected void onResume() {
        super.onResume();
        startScanning();
    }

    @Override
    public void onScanned(Barcode barcode) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mUtility.haveInternet()) {

                    String mStringValue = barcode.displayValue;

                    if (mStringValue.contains("/")) {
                        mStringValue = mStringValue.substring(mStringValue.lastIndexOf("/") + 1, mStringValue.length());
                    } else if (mStringValue.contains("\\")) {
                        mStringValue = mStringValue.substring(mStringValue.lastIndexOf("\\") + 1, mStringValue.length());
                    }

                    Map<String, String> hashMap = new HashMap<>();
                    hashMap.put("batch_id", strBatchID);
                    hashMap.put("log_id", strLogID);
                    hashMap.put("product_id", strProductID);
                    hashMap.put("shipper_id", strShipperID);
                    hashMap.put("lineNo", strLineNo);
                    mScanProductViewModel.scanQrCode(mPreferenceHelper.getUserId(), mPreferenceHelper.getAccessToken(), mStringValue, hashMap);

                } else {
                    showErrorMessage(getResources().getString(R.string.internet_error));
                }
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_touch_off));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            if (mBarcodeReader.getFlase()) {
                mBarcodeReader.setFalsh(false);
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_touch_off));
            } else {
                mBarcodeReader.setFalsh(true);
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.torch));
            }
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showWait() {
        mUtility.showAnimatedProgress(ActivityScanProductQR.this);
    }

    public void removeWait() {
        mUtility.HideAnimatedProgress();
    }

    public void startScanning() {
        mBarcodeReader.setFalsh(mBarcodeReader.getFlase());
    }

    public void setOnlyMyActionBar() {
        if (mToolbarMain != null) {
            mTextViewTitle.setText(getResources().getString(R.string.qr_scan));
            mToolbarMain.setTitle("");
            setSupportActionBar(mToolbarMain);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void scannedProductQRCode(String strQRCode) {
        VoResponseIncompleteShipperList mVoResponseIncompleteShipperList = new VoResponseIncompleteShipperList();
        mVoResponseIncompleteShipperList.setUnique_code(strQRCode);
        mVoResponseIncompleteShipperList.setQr_code(strQRCode);
        mVoResponseIncompleteShipperList.setBatch_id(strBatchID);
        mVoResponseIncompleteShipperList.setLog_id(strLogID);
        mArrayListQRCode.add(mVoResponseIncompleteShipperList);

        int mIntAnimationTime = 500;
        int mIntSecondAnimationTime = 700;
        mActivityScanProductQrBinding.activityMainTxtCount.setText(String.valueOf(mArrayListQRCode.size()));

        mActivityScanProductQrBinding.activityMainTxtCount
                .animate()
                .scaleX(0)
                .scaleY(0)
                .setDuration(0)
                .alpha(1)
                .setStartDelay(0)
                .start();

        mActivityScanProductQrBinding.activityMainTxtCount
                .animate()
                .scaleX(7.0f)
                .scaleY(7.0f)
                .setStartDelay(0)
                .setDuration(mIntAnimationTime)
                .start();

        mActivityScanProductQrBinding.activityMainTxtCount
                .animate()
                .alpha(0)
                .setStartDelay(mIntAnimationTime)
                .setDuration(mIntSecondAnimationTime)
                .start();

        if (Integer.parseInt(strShipperSize) == mArrayListQRCode.size()) {
            if (strShipperID.equalsIgnoreCase("")) {

                isShipperComplete = true;
                onBackPressed();

            } else {

                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("employee_id", mPreferenceHelper.getUserId());
                hashMap.put("product_id", strProductID);
                mScanProductViewModel.updateLog(mPreferenceHelper.getUserId(), mPreferenceHelper.getAccessToken(), strLogID, hashMap);

            }
        }

        mActivityScanProductQrBinding.activityScanningTxtScannedBox.setText("Scanned Product : " + mArrayListQRCode.size());
//        mProductListAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Product scanned successfully", Toast.LENGTH_LONG).show();
    }

    public void completeShipper() {
        strShipperID = "";
        strLogID = "";
        mArrayListQRCode = new ArrayList<>();
        mActivityScanProductQrBinding.activityScanningTxtScannedBox.setText("Scanned Product : " + mArrayListQRCode.size());

        gotoDashborad();

    }

}
