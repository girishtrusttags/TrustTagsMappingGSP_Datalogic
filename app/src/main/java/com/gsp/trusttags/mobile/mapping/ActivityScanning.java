package com.gsp.trusttags.mobile.mapping;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.datalogic.decode.BarcodeManager;
import com.datalogic.decode.DecodeException;
import com.datalogic.decode.DecodeResult;
import com.datalogic.decode.ReadListener;
import com.datalogic.decode.StartListener;
import com.datalogic.decode.StopListener;
import com.gsp.trusttags.mobile.mapping.databinding.ActivityScanningBinding;
import com.gsp.trusttags.mobile.mapping.helper.TagValues;
import com.gsp.trusttags.mobile.mapping.view.CustomModel;
import com.gsp.trusttags.mobile.mapping.viewModel.ScanningViewModel;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceGetBatchesData;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceGetBatchesScanSequence;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseCheckInCompleteShipper;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseDiscardShipper;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseIncompleteShipperList;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ActivityScanning extends BaseActivity {

    ActivityScanningBinding mActivityScanningBinding;

    ProductListAdapter mProductListAdapter;

    Dialog mDialogChangeBatch;
    Dialog mDialogError;
    Dialog mDialogCompleteShipper;

    ArrayList<VoResponseIncompleteShipperList> mArrayListQRCode = new ArrayList<>();
    ArrayList<VoResponceGetBatchesData> mArrayListBatches = new ArrayList<>();

    ArrayList<String> mArrayListFirstLevelQRCode = new ArrayList<>();
    ArrayList<String> mArrayListSecondLevelQRCode = new ArrayList<>();
    ScanningViewModel mScanningViewModel;


    String strProductID = "";
    String strLogID = "";
    String strFirstInnerLogID = "";
    String strSecondInnerLogID = "";
    String strBatchID = "";
    String strBatchNo = "";
    String strShipperID = "";
    String strShipperSize = "";
    String strQrCode = "";
    String strLineNo = "";


    boolean isFromDashBoard = false;
    boolean isShipperAllocated = false;


    VoResponceGetBatchesScanSequence scanSequence = new VoResponceGetBatchesScanSequence();
    private final String[] ALL_PERMS = {Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static final int ACTIVITY_REQUEST_FOR_SCANNING_PRODUCT = 1;
    public static final int ACTIVITY_REQUEST_FOR_REMOVING_PRODUCT = 2;

    BarcodeManager mBarcodeManager;

    ReadListener mReadListener;


    private LOG_TYPE log_type;
    public enum LOG_TYPE
    {
        SKU,FIRST_INNER,SECOND_INNER;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();

        log_type = LOG_TYPE.SKU;
        mActivityScanningBinding = (ActivityScanningBinding) putContentView(R.layout.activity_scanning);

        mScanningViewModel = ViewModelProviders.of(this).get(ScanningViewModel.class);
        mScanningViewModel.setActivityAndService(this, mMyService, mActivityScanningBinding);

        mImageViewDrawer.setImageResource(R.drawable.ic_back_white);
        mTextViewTitle.setText("Scan Product");
        mActivityScanningBinding.activityMainTxtCount.setAlpha(0f);

        mImageViewDrawer.setOnClickListener(v -> onBackPressed());

        mImageViewSync.setVisibility(View.GONE);

        strProductID = getIntent().getStringExtra("strProductID");
        strBatchID = getIntent().getStringExtra("strBatchID");
        strBatchNo = getIntent().getStringExtra("strBatchNo");
        strShipperSize = getIntent().getStringExtra("strShipperSize");
        isFromDashBoard = getIntent().getBooleanExtra("isFromDashBoard", false);
        strLineNo= getIntent().getStringExtra("strLineNo");
        if(TextUtils.isEmpty(strLineNo) || strLineNo == null){
            strLineNo = "0";
        }
//        isShipperAllocated = getIntent().getBooleanExtra("isShipperAllocated", false);
        scanSequence = (VoResponceGetBatchesScanSequence) getIntent().getSerializableExtra("scanSequence");

        if (isFromDashBoard) {
            VoResponseCheckInCompleteShipper.VoResponceLogs logs
                    = (VoResponseCheckInCompleteShipper.VoResponceLogs) getIntent().getSerializableExtra("logs");

            for(int i=0;i<logs.getSkuLogs().size();i++){
                mArrayListQRCode = new ArrayList<>();
                for(int j=0;j<logs.getSkuLogs().get(i).getQrCodes().size();j++){
                    VoResponseCheckInCompleteShipper.VoResponceQrCodes qrModel = logs.getSkuLogs().get(i).getQrCodes().get(j);
                    VoResponseIncompleteShipperList model = new VoResponseIncompleteShipperList();
                    model.setUnique_code(qrModel.getQrCode());
                    model.setQr_code(qrModel.getQrCode());
                    model.setBatch_id(strBatchID);
                    model.setLog_id(qrModel.getQrId());
                    mArrayListQRCode.add(model);

                }
                /*if(scanSequence.getSecond() != null && Integer.parseInt(scanSequence.getSecond().getSize()) == mArrayListQRCode.size()){
                    mArrayListQRCode.clear();
                }*/
                if(!logs.getSkuLogs().get(i).isCompleted()){
                    strLogID =logs.getSkuLogs().get(i).getId();
                    continue;
                }else if(scanSequence.getSecond() != null && Integer.parseInt(scanSequence.getSecond().getSize()) == mArrayListQRCode.size()){
                    mArrayListQRCode.clear();
                }
            }
            productSort();

            for(int i=0;i<logs.getFstInnerLogs().size();i++){
                mArrayListFirstLevelQRCode = new ArrayList<>();
                for(int j=0;j<logs.getFstInnerLogs().get(i).getQrCodes().size();j++){
                    VoResponseCheckInCompleteShipper.VoResponceQrCodes qrModel = logs.getFstInnerLogs().get(i).getQrCodes().get(j);
                    mArrayListFirstLevelQRCode.add(qrModel.getQrCode());
                }
                if(!logs.getFstInnerLogs().get(i).isCompleted()){
                    strFirstInnerLogID =logs.getFstInnerLogs().get(i).getId();
                    continue;
                }else if(scanSequence.getThird() != null && Integer.parseInt(scanSequence.getThird().getSize()) == mArrayListFirstLevelQRCode.size()){
                    mArrayListFirstLevelQRCode.clear();
                }
            }
            mArrayListSecondLevelQRCode = new ArrayList<>();

            for(int i=0;i<logs.getSndInnerLogs().size();i++){

                for(int j=0;j<logs.getSndInnerLogs().get(i).getQrCodes().size();j++){
                    VoResponseCheckInCompleteShipper.VoResponceQrCodes qrModel = logs.getSndInnerLogs().get(i).getQrCodes().get(j);
                    mArrayListSecondLevelQRCode.add(qrModel.getQrCode());
                }
                if(!logs.getSndInnerLogs().get(i).isCompleted()){
                    strSecondInnerLogID =logs.getSndInnerLogs().get(i).getId();
                    continue;
                }else if(scanSequence.getFourth() != null && Integer.parseInt(scanSequence.getFourth().getSize()) == mArrayListSecondLevelQRCode.size()){
//                    mArrayListSecondLevelQRCode.clear();
                }
            }
            VoResponseCheckInCompleteShipper.VoResponceLastScan modelLastScan = logs.getLastScanned();

          /*  if(modelLastScan != null){
                if(modelLastScan.getTitle().equalsIgnoreCase("sku")){
                    strLogID =modelLastScan.getLastLogId();
                }else if(modelLastScan.getTitle().equalsIgnoreCase("fst-inner")){
                    strFirstInnerLogID =modelLastScan.getLastLogId();
                }else if(modelLastScan.getTitle().equalsIgnoreCase("snd-inner")){
                    strSecondInnerLogID =modelLastScan.getLastLogId();
                }
            }*/


//            setLevelText();
            scanSequence();
            /*
             strShipperID = getIntent().getStringExtra("strShipperID");
            mArrayListQRCode = (ArrayList<VoResponseIncompleteShipperList>) getIntent().getSerializableExtra("mArrayListQRCode");
            if ((Integer.parseInt(strShipperSize) == mArrayListQRCode.size()) && isShipperAllocated) {
                updateLog(TagValues.SHIPPER_KEY,strLogID);
            }else *//*if((Integer.parseInt(strShipperSize) == mArrayListQRCode.size()))*//*{
                scanSequence();
            }*/
        }else{
            strLogID = getIntent().getStringExtra("strLogID");

        }

        mProductListAdapter = new ProductListAdapter();
        mActivityScanningBinding.activityScanningRecyclerProductListing.setLayoutManager(new LinearLayoutManager(this));
        mActivityScanningBinding.activityScanningRecyclerProductListing.setAdapter(mProductListAdapter);

        mActivityScanningBinding.activityScanningBtnScanProduct.setOnClickListener(v -> {
            if (mUtility.verifyPermissions(ActivityScanning.this, ALL_PERMS)) {
                if (Integer.parseInt(strShipperSize) == mArrayListQRCode.size()) {
                    Toast.makeText(ActivityScanning.this, "Product scan completed. Please scan shipper code.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent mIntentScanProduct = new Intent(ActivityScanning.this, ActivityScanProductQR.class);
                    mIntentScanProduct.putExtra("strProductID", strProductID);
                    mIntentScanProduct.putExtra("strLogID", strLogID);
                    mIntentScanProduct.putExtra("strBatchID", strBatchID);
                    mIntentScanProduct.putExtra("strBatchNo", strBatchNo);
                    mIntentScanProduct.putExtra("strShipperSize", strShipperSize);
                    mIntentScanProduct.putExtra("strShipperID", strShipperID);
                    mIntentScanProduct.putExtra("mArrayListQRCode", mArrayListQRCode);
                    mIntentScanProduct.putExtra("strLineNo", strLineNo);
                    startActivityForResult(mIntentScanProduct, ACTIVITY_REQUEST_FOR_SCANNING_PRODUCT);
                }
            }
        });

        mActivityScanningBinding.activityScanningBtnScanShipper.setOnClickListener(v -> {
            if (strShipperID.equalsIgnoreCase("")) {
                Intent mIntent = new Intent(ActivityScanning.this, ActivityScanShipper.class);
                mIntent.putExtra("strProductID", strProductID);
                mIntent.putExtra("strLogID", strLogID);
                mIntent.putExtra("batch_id", strBatchID);
                mIntent.putExtra("strLineNo", strLineNo);
                startActivity(mIntent);
            } else {
                Toast.makeText(ActivityScanning.this, getResources().getString(R.string.text_scanning_shipper_already_allocated), Toast.LENGTH_LONG).show();
            }
        });

        mActivityScanningBinding.activityScanningBtnChangeBatch.setOnClickListener(v -> {
            if (mUtility.haveInternet()) {
                mScanningViewModel.getBatches(mPreferenceHelper.getUserId(), mPreferenceHelper.getAccessToken(), strProductID);
            }
        });

        mActivityScanningBinding.activityScanningActionButtonRemoveCases.setOnClickListener(v -> {
            mActivityScanningBinding.activityScanningActionButtonUpdate.close(true);

            if (mArrayListQRCode != null && !mArrayListQRCode.isEmpty()) {
                Intent mIntentScanProduct = new Intent(ActivityScanning.this, ActivityScanAndDelete.class);
                mIntentScanProduct.putExtra("strProductID", strProductID);
                mIntentScanProduct.putExtra("strLogID", strLogID);
                mIntentScanProduct.putExtra("strBatchID", strBatchID);
                mIntentScanProduct.putExtra("strBatchNo", strBatchNo);
                mIntentScanProduct.putExtra("strShipperSize", strShipperSize);
                mIntentScanProduct.putExtra("mArrayListQRCode", mArrayListQRCode);
                startActivityForResult(mIntentScanProduct, ACTIVITY_REQUEST_FOR_REMOVING_PRODUCT);
            } else {
                showErrorMessage(getResources().getString(R.string.text_scanning_scan_product_first));
            }
        });

        mActivityScanningBinding.activityScanningActionButtonDiscardShipper.setOnClickListener(v -> {
            mActivityScanningBinding.activityScanningActionButtonUpdate.close(true);

            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ActivityScanning.this, R.style.AlertDialogStyleAppCompat);
            builder.setTitle(getString(R.string.discard_shipper));
            builder.setCancelable(false);
            builder.setMessage(getString(R.string.sure_to_discard_shipper));

            builder.setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                dialog.dismiss();
                discardScan();

            });

            builder.setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss());

            builder.show();
        });

        CustomModel.getInstance().setListener(mArrayListQRCodes -> {

            mArrayListQRCode = mArrayListQRCodes;
            productSort();
            mProductListAdapter.notifyDataSetChanged();
            mActivityScanningBinding.activityScanningTxtScannedBox.setText(getResources().getString(R.string.text_scanning_scanned_product,
                    String.valueOf(mArrayListQRCode.size())));

            System.out.println("Sanjay mArrayListQRCode  CustomModelssssss..." + mArrayListQRCode.size());
        });

        CustomModel.getInstance().setShipperListener(strShipperId -> {
            strShipperID = strShipperId;

            if (!strShipperID.equalsIgnoreCase("")) {
//                if (Integer.parseInt(strShipperSize) == mArrayListQRCode.size()) {
                if(scanSequence.getFourth() != null)
                    updateLog(TagValues.SECOND_INNER_KEY,strSecondInnerLogID);
                else  if(scanSequence.getThird() != null)
                    updateLog(TagValues.FIRST_INNER_KEY,strFirstInnerLogID);
                else
                    updateLog(TagValues.SKUCODE_KEY,strLogID);
//                }
            }else{
                cancelScanConfirmation();
            }
        });

        CustomModel.getInstance().setFirstLvelListener(qrCode -> {
//            strShipperID = strShipperId;
           /* if(mArrayListQRCode != null)
                mArrayListQRCode.clear();

            mArrayListQRCode = new ArrayList<>();*/
            if(TextUtils.isEmpty(qrCode)){
                cancelScanConfirmation();
            }else{
                if(mArrayListQRCode != null)
                    mArrayListQRCode.clear();
                mArrayListQRCode = new ArrayList<>();
                mProductListAdapter.notifyDataSetChanged();
                if (mArrayListFirstLevelQRCode == null) {
                    mArrayListFirstLevelQRCode = new ArrayList<>();
                }
                mArrayListFirstLevelQRCode.add(qrCode);

                updateLog(TagValues.SKUCODE_KEY,strLogID);

//                Toast.makeText(ActivityScanning.this,"First inner scan completed",Toast.LENGTH_SHORT).show();
                customToast("First inner scan completed");
//            }
            }


        });

        CustomModel.getInstance().setSecondLvelListener(qrCode -> {
            if(TextUtils.isEmpty(qrCode)){
                cancelScanConfirmation();
            }else{

//            strShipperID = strShipperId;
                if(mArrayListQRCode != null)
                    mArrayListQRCode.clear();

                mArrayListQRCode = new ArrayList<>();

                if(mArrayListFirstLevelQRCode != null)
                    mArrayListFirstLevelQRCode.clear();

                mArrayListFirstLevelQRCode = new ArrayList<>();

                if (mArrayListSecondLevelQRCode == null) {
                    mArrayListSecondLevelQRCode = new ArrayList<>();
                }
                mArrayListSecondLevelQRCode.add(qrCode);

//            strShipperID = "";
//            strLogID = "";
                updateLog(TagValues.FIRST_INNER_KEY,strFirstInnerLogID);
//                Toast.makeText(ActivityScanning.this,"Second inner scan completed",Toast.LENGTH_SHORT).show();
                customToast("Second inner scan completed");
                strFirstInnerLogID="";

             /*  createLogCall();
//            if(scanSequence  != null && scanSequence.getFourth() != null && mArrayListSecondLevelQRCode.size() == Integer.parseInt(scanSequence.getFourth().getSize())){

            scanSequence();*/
//            }

            }
        });

        mActivityScanningBinding.activityScanningTxtBatchNo.setText(""+strBatchNo);

        mActivityScanningBinding.activityScanningTxtTotalBox.setText(strShipperSize);
        mActivityScanningBinding.activityScanningTxtScannedBox.setText(getResources().getString(R.string.text_scanning_scanned_product, String.valueOf(mArrayListQRCode.size())));

        setLevelText();

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
                            strQrCode = strQrCode.substring(strQrCode.lastIndexOf("/") + 1);
                        } else if (strQrCode.contains("\\")) {
                            strQrCode = strQrCode.substring(strQrCode.lastIndexOf("\\") + 1);
                        }


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    boolean isScanned = false;

                                    if (mArrayListQRCode != null && !mArrayListQRCode.isEmpty()) {
                                        for (int i = 0; i < mArrayListQRCode.size(); i++) {
                                            if (mArrayListQRCode.get(i).getQr_code().equalsIgnoreCase(strQrCode)) {
                                                isScanned = true;
                                            }
                                        }
                                    }

                                    if (!isScanned) {
                                        Map<String, String> hashMap = new HashMap<>();
                                        hashMap.put("batch_id", strBatchID);
                                        hashMap.put("log_id", strLogID);
                                        hashMap.put("product_id", strProductID);
                                        hashMap.put("shipper_id", strShipperID);
                                        hashMap.put("lineNo", strLineNo);

                                        mScanningViewModel.scanQrCode(mPreferenceHelper.getUserId(), mPreferenceHelper.getAccessToken(), strQrCode, hashMap);

                                    } else {
                                        showError();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    sendMail(e.getMessage());
                                }
                            }
                        });
                    }
                };

                mBarcodeManager.addReadListener(mReadListener);

                mBarcodeManager.addStartListener(new StartListener() {
                    @Override
                    public void onScanStarted() {
                        //No Code Required
                    }
                });

                mBarcodeManager.addStopListener(new StopListener() {
                    @Override
                    public void onScanStopped() {
                        //No Code Required
                    }
                });
            }

        } catch(NoClassDefFoundError e) {
            e.printStackTrace();
        }catch (DecodeException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showWait() {
        //No Code Required
    }

    protected void onPause() {
        super.onPause();
       /* runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    if (mBarcodeManager != null) {
                        try {
                            mBarcodeManager.removeReadListener(mReadListener);
                            mBarcodeManager = null;
                        } catch(NoClassDefFoundError e) {
                            e.printStackTrace();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }catch (WindowManager.BadTokenException ex) {
                    ex.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });*/

        try{
            if (mBarcodeManager != null) {
                try {
                    mBarcodeManager.removeReadListener(mReadListener);
                    mBarcodeManager = null;
                } catch(NoClassDefFoundError e) {
                    e.printStackTrace();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (WindowManager.BadTokenException ex) {
            ex.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void removeWait() {
        //No Code Required
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ACTIVITY_REQUEST_FOR_SCANNING_PRODUCT:
                if (data != null && data.getBooleanExtra("is_success", false)) {
                    if (data.hasExtra("mArrayListQRCode")) {
                        mArrayListQRCode = new ArrayList<>();
                        mArrayListQRCode = (ArrayList<VoResponseIncompleteShipperList>) data.getSerializableExtra("mArrayListQRCode");

                        if (mArrayListQRCode != null && mArrayListQRCode.size() > 0) {
                            mActivityScanningBinding.activityScanningTxtScannedBox.setText(getResources().getString(R.string.text_scanning_scanned_product,
                                    String.valueOf(mArrayListQRCode.size())));

                            if (mProductListAdapter != null) {
                                mProductListAdapter.notifyDataSetChanged();
                            }
                        }

                        if (Integer.parseInt(strShipperSize) == mArrayListQRCode.size()) {
                            if (strShipperID.equalsIgnoreCase("")) {
                                Intent mIntent = new Intent(ActivityScanning.this, ActivityScanShipper.class);
                                mIntent.putExtra("strProductID", strProductID);
                                mIntent.putExtra("strLogID", strLogID);
                                mIntent.putExtra("batch_id", strBatchID);
                                mIntent.putExtra("strLineNo", strLineNo);
                                startActivity(mIntent);
                            } else {
                                updateLog(TagValues.SHIPPER_KEY,strLogID);
                            }
                        }
                    }
                }
                break;
            case ACTIVITY_REQUEST_FOR_REMOVING_PRODUCT:
                if (data != null && data.getBooleanExtra("is_success", false)) {
                    try {
                        if (data.hasExtra("mArrayListQRCode")) {
                            mArrayListQRCode = new ArrayList<>();
                            mArrayListQRCode = (ArrayList<VoResponseIncompleteShipperList>) data.getSerializableExtra("mArrayListQRCode");

                            if (mArrayListQRCode != null) {

                                mActivityScanningBinding.activityScanningTxtScannedBox.setText(getResources().getString(R.string.text_scanning_scanned_product,
                                        String.valueOf(mArrayListQRCode.size())));

                                if (mProductListAdapter != null) {
                                    mProductListAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        sendMail(e.getMessage());
                    }
                }
                break;
        }

    }

    public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

        @NonNull
        @Override
        public ProductListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ActivityScanning.this);
            ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductListAdapter.ViewHolder holder, int position) {
            TextView mTextViewProductName = (TextView) holder.binding.getRoot().findViewById(R.id.raw_scanned_product_txt_product);
            TextView mTextViewProductCount = (TextView) holder.binding.getRoot().findViewById(R.id.raw_scanned_product_txt_product_count);

            mTextViewProductName.setText(mArrayListQRCode.get(position).getQr_code());
            mTextViewProductCount.setText(String.valueOf(position));
        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.raw_scanned_product;
        }

        @Override
        public int getItemCount() {
            return mArrayListQRCode.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {

            final ViewDataBinding binding;

            ViewHolder(ViewDataBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

    public void setChangeBatchDialog(ArrayList<VoResponceGetBatchesData> mArrayListBatches) {
        showChangeBatchDialog(mArrayListBatches);
    }

    public void showChangeBatchDialog(ArrayList<VoResponceGetBatchesData> mArrayListBatches) {

        if (mDialogChangeBatch == null) {
            mDialogChangeBatch = new Dialog(ActivityScanning.this);
            mDialogChangeBatch.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            mDialogChangeBatch.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialogChangeBatch.setCancelable(true);
            mDialogChangeBatch.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mDialogChangeBatch.setContentView(R.layout.dialog_change_batch);
            mDialogChangeBatch.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        TextView mTextViewChanage = (TextView) mDialogChangeBatch.findViewById(R.id.dialog_change_batch_txt_change);
        Spinner mSpinnerBatches = (Spinner) mDialogChangeBatch.findViewById(R.id.dialog_change_batch_spinner);

        mTextViewChanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogChangeBatch.dismiss();
            }
        });

        ArrayAdapter<VoResponceGetBatchesData> adapter = new ArrayAdapter<VoResponceGetBatchesData>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, mArrayListBatches);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerBatches.setAdapter(adapter);

        mSpinnerBatches.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strProductID = mArrayListBatches.get(position).getProduct_id();
                strBatchNo = mArrayListBatches.get(position).getBatch_no();
                strBatchID = mArrayListBatches.get(position).getId();

                mActivityScanningBinding.activityScanningTxtBatchNo.setText("" + strBatchNo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (!mDialogChangeBatch.isShowing())
            mDialogChangeBatch.show();
    }

    public void setSpinnerData(ArrayList<VoResponceGetBatchesData> mArrayListBatches) {
        this.mArrayListBatches = mArrayListBatches;
    }

    public void scannedProductQRCode(String strQRCode) {
        VoResponseIncompleteShipperList mVoResponseIncompleteShipperList = new VoResponseIncompleteShipperList();
        mVoResponseIncompleteShipperList.setUnique_code(strQRCode);
        mVoResponseIncompleteShipperList.setQr_code(strQRCode);
        mVoResponseIncompleteShipperList.setBatch_id(strBatchID);
        mVoResponseIncompleteShipperList.setLog_id(strLogID);

        if (mArrayListQRCode == null) {
            mArrayListQRCode = new ArrayList<>();
        }

        mArrayListQRCode.add(mVoResponseIncompleteShipperList);
        productSort();
        int mIntAnimationTime = 500;
        int mIntSecondAnimationTime = 700;
        mActivityScanningBinding.activityMainTxtCount.setText(String.valueOf(mArrayListQRCode.size()));

        mActivityScanningBinding.activityMainTxtCount
                .animate()
                .scaleX(0)
                .scaleY(0)
                .setDuration(0)
                .alpha(1)
                .setStartDelay(0)
                .start();

        mActivityScanningBinding.activityMainTxtCount
                .animate()
                .scaleX(7.0f)
                .scaleY(7.0f)
                .setStartDelay(0)
                .setDuration(mIntAnimationTime)
                .start();

        mActivityScanningBinding.activityMainTxtCount
                .animate()
                .alpha(0)
                .setStartDelay(mIntAnimationTime)
                .setDuration(mIntSecondAnimationTime)
                .start();

        mActivityScanningBinding.activityScanningTxtScannedBox.setText("Scanned Product : " + mArrayListQRCode.size());
        mProductListAdapter.notifyDataSetChanged();

//        Toast.makeText(ActivityScanning.this, "Product scanned successfully", Toast.LENGTH_LONG).show();
        customToast("Product scanned successfully");
        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    Toast.makeText(ActivityScanning.this, "Product scanned successfully", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });*/

       /* if(scanSequence  != null && scanSequence.getSecond() == null && scanSequence.getThird() == null) {
            if (Integer.parseInt(strShipperSize) == mArrayListQRCode.size()) {
                if (strShipperID.equalsIgnoreCase("")) {

                    Intent mIntent = new Intent(ActivityScanning.this, ActivityScanShipper.class);
                    mIntent.putExtra("strProductID", strProductID);
                    mIntent.putExtra("strLogID", strLogID);
                    mIntent.putExtra("batch_id", strBatchID);
                    startActivity(mIntent);

                } else {

                    updateLog(TagValues.SHIPPER_KEY,strLogID);

                }
            }
        }else{
            scanSequence();
        }*/
//        setLevelText();
        scanSequence();
//        Toast.makeText(ActivityScanning.this, "Product scanned successfully", Toast.LENGTH_LONG).show();
    }
    private void scanSequence(){
        if(!isFinishing()) {
            try {
                if(!TextUtils.isEmpty(strShipperID)){
                    completeShipper();
                    return;
                }
                if (scanSequence != null && scanSequence.getSecond() != null && Integer.parseInt(scanSequence.getSecond().getSize()) == mArrayListQRCode.size()
                        && scanSequence.getSecond().getTitle().equalsIgnoreCase("fstInner")) {
                    // Toast.makeText(ActivityScanning.this,"Start scan first level",Toast.LENGTH_SHORT).show();
                    if(TextUtils.isEmpty(strFirstInnerLogID)){
                        createLogCall(LOG_TYPE.FIRST_INNER);
                    }else{
                        Intent mIntent = new Intent(ActivityScanning.this, ActivityScanFirstLevel.class);
                        mIntent.putExtra("strProductID", strProductID);
                        mIntent.putExtra("strLogID", strLogID);
                        mIntent.putExtra("strFirstInnerLogID", strFirstInnerLogID);
                        mIntent.putExtra("batch_id", strBatchID);
                        mIntent.putExtra("strLineNo", strLineNo);
                        startActivity(mIntent);
                    }

                } else if (scanSequence != null && ((scanSequence.getSecond() != null && Integer.parseInt(scanSequence.getSecond().getSize()) == mArrayListQRCode.size()
                        && scanSequence.getSecond().getTitle().equalsIgnoreCase("sndInner"))
                        || scanSequence.getThird() != null && Integer.parseInt(scanSequence.getThird().getSize()) == mArrayListFirstLevelQRCode.size()
                        && scanSequence.getThird().getTitle().equalsIgnoreCase("sndInner"))) {
                    //Toast.makeText(ActivityScanning.this,"Start scan second level",Toast.LENGTH_SHORT).show();
                    if(TextUtils.isEmpty(strSecondInnerLogID)){
                        createLogCall(LOG_TYPE.SECOND_INNER);
                    }else{
                        Intent mIntent = new Intent(ActivityScanning.this, ActivityScanSecondLevel.class);
                        mIntent.putExtra("strProductID", strProductID);
                        mIntent.putExtra("strSecondInnerLogID", strSecondInnerLogID);
                        mIntent.putExtra("strLogID", strFirstInnerLogID);
                        mIntent.putExtra("batch_id", strBatchID);
                        mIntent.putExtra("strLineNo", strLineNo);
                        startActivity(mIntent);
                    }

                } else if (scanSequence != null && (
                        (scanSequence.getSecond() != null && Integer.parseInt(scanSequence.getSecond().getSize()) == mArrayListQRCode.size()
                                && scanSequence.getSecond().getTitle().equalsIgnoreCase("shipper"))
                                || (scanSequence.getThird() != null && Integer.parseInt(scanSequence.getThird().getSize()) == mArrayListFirstLevelQRCode.size()
                                && scanSequence.getThird().getTitle().equalsIgnoreCase("shipper"))
                                || (scanSequence.getFourth() != null && Integer.parseInt(scanSequence.getFourth().getSize()) == mArrayListSecondLevelQRCode.size()
                                && scanSequence.getFourth().getTitle().equalsIgnoreCase("shipper")))) {
                    Intent mIntent = new Intent(ActivityScanning.this, ActivityScanShipper.class);
                    mIntent.putExtra("strProductID", strProductID);

                    if(scanSequence.getFourth() != null)
                        mIntent.putExtra("strLogID", strSecondInnerLogID);
                    else if(scanSequence.getThird() != null)
                        mIntent.putExtra("strLogID", strFirstInnerLogID);
                    else if(scanSequence.getSecond() != null)
                        mIntent.putExtra("strLogID", strLogID);

                    mIntent.putExtra("batch_id", strBatchID);

                    mIntent.putExtra("strLineNo", strLineNo);
                    startActivity(mIntent);
                }else{
                    if(TextUtils.isEmpty(strLogID))
                        createLogCall(LOG_TYPE.SKU);
                }
            } catch (WindowManager.BadTokenException e) {
                Log.e("WindowManagerBad ", e.toString());
            } catch (Exception e) {
                Log.e("Exception ", e.toString());
            }
        }
        setLevelText();


    }


    public void completeShipper() {
        strShipperID = "";
        strLogID = "";
        strFirstInnerLogID ="";
        strSecondInnerLogID = "";
        mArrayListQRCode = new ArrayList<>();
        mArrayListFirstLevelQRCode = new ArrayList<>();
        mArrayListSecondLevelQRCode  = new ArrayList<>();
//        mActivityScanningBinding.activityScanningTxtScannedBox.setText("Scanned Product : " + mArrayListQRCode.size());
        setLevelText();
        Toast.makeText(ActivityScanning.this,"Shipper Mapping Successfully completed",Toast.LENGTH_SHORT).show();
        createLogCall(LOG_TYPE.SKU);
        /*AlertDialog.Builder builders = new AlertDialog.Builder(this, R.style.AlertDialogStyleAppCompat);
        builders.setTitle(getString(R.string.mapping_completed));
        builders.setCancelable(false);
        builders.setMessage("Create new shipper with same batch?");

        builders.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                createLogCall(LOG_TYPE.SKU);
            }
        });

        builders.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gotoDashborad();

            }
        });

        if (mDialogCompleteShipper == null) {
            mDialogCompleteShipper = builders.create();
        }

        if (!mDialogCompleteShipper.isShowing())
            mDialogCompleteShipper.show();*/

    }

    public void completeSKU() {
//        strShipperID = "";
        //  setLevelText();
        strLogID = "";
        if(mArrayListQRCode != null)
            mArrayListQRCode.clear();
        mArrayListQRCode = new ArrayList<>();
        mProductListAdapter.notifyDataSetChanged();
//        Toast.makeText(ActivityScanning.this,"First inner scan completed",Toast.LENGTH_SHORT).show();
//        updateLog(TagValues.FIRST_INNER_KEY);

//        if(scanSequence.getThird() != null && Integer.valueOf(scanSequence.getThird().getSize()) != mArrayListFirstLevelQRCode.size())
//        createLogCall(LOG_TYPE.SKU);
        scanSequence();
//        scanSequence();
    }
    public void completeFirstInner() {

//        strShipperID = "";
        strLogID = "";
//        strFirstInnerLogID="";
        if(mArrayListQRCode != null)
            mArrayListQRCode.clear();
        mArrayListQRCode = new ArrayList<>();
        mProductListAdapter.notifyDataSetChanged();
//        Toast.makeText(ActivityScanning.this,"First inner scan completed",Toast.LENGTH_SHORT).show();
//        updateLog(TagValues.FIRST_INNER_KEY,strFirstInnerLogID);

//        if(scanSequence.getThird() != null && Integer.valueOf(scanSequence.getThird().getSize()) != mArrayListFirstLevelQRCode.size())
//            createLogCall(LOG_TYPE.SKU);
        scanSequence();
//        setLevelText();
    }
    public void completeSecondInner() {
//        setLevelText();
//        strShipperID = "";
        strLogID = "";
        strFirstInnerLogID="";
//        strSecondInnerLogID="";

        if(mArrayListQRCode != null)
            mArrayListQRCode.clear();
        mArrayListQRCode = new ArrayList<>();

        if(mArrayListFirstLevelQRCode != null)
            mArrayListFirstLevelQRCode.clear();
        mArrayListFirstLevelQRCode = new ArrayList<>();
//        createLogCall();
        scanSequence();
//        Toast.makeText(ActivityScanning.this,"Second inner scan completed",Toast.LENGTH_SHORT).show();
    }
    public void completeDiscardShipper(VoResponseDiscardShipper voResponseDiscardShipper) {
        if (voResponseDiscardShipper != null && voResponseDiscardShipper.getMessage() != null
                && !voResponseDiscardShipper.getMessage().equalsIgnoreCase("")) {
            Toast.makeText(this, voResponseDiscardShipper.getMessage(), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    private void updateLog(String type,String logid){
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("employee_id", mPreferenceHelper.getUserId());
        hashMap.put("product_id", strProductID);
        hashMap.put("type", type);
        mScanningViewModel.updateLog(mPreferenceHelper.getUserId(), mPreferenceHelper.getAccessToken(), logid, hashMap);

    }
    public void updateLogData(String strLogID, String strShipperSize) {
        if(log_type == LOG_TYPE.SKU){
            this.strLogID = strLogID;
            this.strShipperSize = strShipperSize;
            this.strShipperID = "";

            mArrayListQRCode = new ArrayList<>();
            mProductListAdapter.notifyDataSetChanged();
        }else if(log_type == LOG_TYPE.FIRST_INNER){
            this.strFirstInnerLogID = strLogID;

            Intent mIntent = new Intent(ActivityScanning.this, ActivityScanFirstLevel.class);
            mIntent.putExtra("strProductID", strProductID);
            mIntent.putExtra("strLogID", this.strLogID);
            mIntent.putExtra("strFirstInnerLogID", this.strFirstInnerLogID);
            mIntent.putExtra("batch_id", strBatchID);
            mIntent.putExtra("strLineNo", strLineNo);
            startActivity(mIntent);

        }else{
            this.strSecondInnerLogID = strLogID;
            Intent mIntent = new Intent(ActivityScanning.this, ActivityScanSecondLevel.class);
            mIntent.putExtra("strProductID", strProductID);
            mIntent.putExtra("strSecondInnerLogID", this.strSecondInnerLogID);
            mIntent.putExtra("strLogID", this.strFirstInnerLogID);
            mIntent.putExtra("batch_id", strBatchID);
            mIntent.putExtra("strLineNo", strLineNo);
            startActivity(mIntent);
        }


    }

    public void showError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyleAppCompat);
        builder.setTitle("Error");
        builder.setCancelable(false);
        builder.setMessage("Duplicate data. Please check and try again");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        if (mDialogError == null) {
            mDialogError = builder.create();
        }

        if (!mDialogError.isShowing())
            mDialogError.show();

    }

    private void createLogCall(LOG_TYPE type){
        log_type = type;
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("employee_id", mPreferenceHelper.getUserId());
        hashMap.put("product_id", strProductID);
        mScanningViewModel.createLog(mPreferenceHelper.getUserId(), mPreferenceHelper.getAccessToken(), strBatchNo, strBatchID, hashMap);
    }

    private void sendMail(String mStringData) {

        class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

            @Override
            protected Void doInBackground(String... strings) {
                final String username = "darshan@trusttags.in";
                final String password = "Trusttags1@";

                String mStringMailBody = "Dear Developer,\n\nDebug Steps for  " + getString(R.string.app_name) + " Project";

                if (mStringData != null && !mStringData.equalsIgnoreCase("")) {
                    mStringMailBody = mStringMailBody
                            + "\n\nData : " + mStringData;
                }

                if (MyApplication.getVersionNumber() != null && !MyApplication.getVersionNumber().equalsIgnoreCase("")) {
                    mStringMailBody = mStringMailBody + "\n\nApp Version : " + MyApplication.getVersionNumber();
                }

                if (Build.VERSION.RELEASE != null && Build.VERSION.RELEASE.equalsIgnoreCase("")) {
                    mStringMailBody = mStringMailBody + "\n\nOS Version : " + Build.VERSION.RELEASE;
                }

                if (Build.MANUFACTURER != null && !Build.MANUFACTURER.equalsIgnoreCase("")
                        && Build.MODEL != null && !Build.MODEL.equalsIgnoreCase("")) {
                    mStringMailBody = mStringMailBody + "\n\nDevice Model : " + Build.MANUFACTURER + " " + Build.MODEL;
                }

                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.trusttags.in");
                props.put("mail.smtp.port", "587");

                Session session = Session.getInstance(props,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });

                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("darshan@trusttags.in"));

                    String mStringAddress = "darshan.oneclick@gmail.com";
                    InternetAddress[] mInternetAddresses = InternetAddress.parse(mStringAddress);
                    message.setRecipients(Message.RecipientType.CC, mInternetAddresses);

                    message.setSubject(getString(R.string.app_name) + " Debug Steps");

                    message.setText(mStringMailBody);

                    Transport.send(message);

                    System.out.println("Darshan Error Mail Done : " + mStringMailBody);
                    System.out.println("Darshan Error Mail Subject : " + message.getSubject());

                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        new RetrieveFeedTask().execute();

    }


    private void setLevelText(){

        if(scanSequence != null){
            /*if(scanSequence.getFourth() != null){
                mActivityScanningBinding.activityScanningTxtTotalBox.setText(getResources().getString(R.string.text_scanning_shipper_size,mArrayListSecondLevelQRCode.size()+" / "+" "+scanSequence.getFourth().getSize()));
                mActivityScanningBinding.activityScanningTxtSecondInner.setText(getResources().getString(R.string.text_scanning_second_inner_size)+ " "+mArrayListFirstLevelQRCode.size() +" / "+scanSequence.getThird().getSize());
                mActivityScanningBinding.activityScanningTxtFirstInner.setText(getResources().getString(R.string.text_scanning_first_inner_size)+" "+mArrayListQRCode.size()+" / "+scanSequence.getSecond().getSize());
            }else if(scanSequence.getThird() != null){
                mActivityScanningBinding.activityScanningTxtSecondInner.setVisibility(View.GONE);
                // mActivityScanningBinding.activityScanningTxtTotalBox.setText(getResources().getString(R.string.text_scanning_shipper_size)+scanSequence.getFourth().getSize());
                mActivityScanningBinding.activityScanningTxtTotalBox.setText(getResources().getString(R.string.text_scanning_shipper_size," "+mArrayListFirstLevelQRCode.size() +" / "+scanSequence.getThird().getSize()));
                mActivityScanningBinding.activityScanningTxtFirstInner.setText(getResources().getString(R.string.text_scanning_first_inner_size)+" "+mArrayListQRCode.size()+" / "+scanSequence.getSecond().getSize());

            }else{
                mActivityScanningBinding.activityScanningTxtFirstInner.setVisibility(View.GONE);
                mActivityScanningBinding.activityScanningTxtSecondInner.setVisibility(View.GONE);
            }*/

            if(scanSequence.getFourth() != null){
                mActivityScanningBinding.activityScanningTxtTotalBox.setText("0 / 1");
                mActivityScanningBinding.activityScanningTxtSecondInner.setText(" "+mArrayListSecondLevelQRCode.size() +" / "+scanSequence.getFourth().getSize());
                if(mArrayListSecondLevelQRCode.size()>0){
                    mActivityScanningBinding.activityScanningTxtFirstInner.setText(" "+((mArrayListSecondLevelQRCode.size()*Integer.valueOf(scanSequence.getThird().getSize()))+mArrayListFirstLevelQRCode.size())+" / "+(Integer.valueOf(scanSequence.getThird().getSize())*
                            Integer.valueOf(scanSequence.getFourth().getSize())));
                }else{
                    mActivityScanningBinding.activityScanningTxtFirstInner.setText(" "+mArrayListFirstLevelQRCode.size()+" / "+(Integer.valueOf(scanSequence.getThird().getSize())*
                            Integer.valueOf(scanSequence.getFourth().getSize())));
                }
//                scanSequence.getSecond().getSize())
            }else if(scanSequence.getThird() != null){
//                mActivityScanningBinding.activityScanningTxtSecondInner.setVisibility(View.GONE);
                mActivityScanningBinding.activityScanningLlSecondInner.setVisibility(View.GONE);
                // mActivityScanningBinding.activityScanningTxtTotalBox.setText(getResources().getString(R.string.text_scanning_shipper_size)+scanSequence.getFourth().getSize());
                mActivityScanningBinding.activityScanningTxtTotalBox.setText(" "+mArrayListFirstLevelQRCode.size() +" / "+scanSequence.getThird().getSize());
                mActivityScanningBinding.activityScanningTxtFirstInner.setText(" "+mArrayListQRCode.size()+" / "+scanSequence.getSecond().getSize());

            }else{
//                mActivityScanningBinding.activityScanningTxtFirstInner.setVisibility(View.GONE);
//                mActivityScanningBinding.activityScanningTxtSecondInner.setVisibility(View.GONE);

                mActivityScanningBinding.activityScanningLlFirstInner.setVisibility(View.GONE);
                mActivityScanningBinding.activityScanningLlSecondInner.setVisibility(View.GONE);
            }

            mActivityScanningBinding.activityScanningTxtScannedBox.setText(""+getScannedTotalProds());

        }
    }


    private String getScannedTotalProds(){
        int totalProds = 0;
        int scannedProd =0;
        if(scanSequence.getFourth() != null){
            totalProds = Integer.parseInt(scanSequence.getFourth().getSize())*Integer.parseInt(scanSequence.getThird().getSize())
                    *Integer.parseInt(scanSequence.getSecond().getSize());

        }else if(scanSequence.getThird() != null){
            totalProds = Integer.parseInt(scanSequence.getThird().getSize())*Integer.parseInt(scanSequence.getSecond().getSize());

        }else{
            totalProds = Integer.parseInt(scanSequence.getSecond().getSize());

        }

        if(mArrayListSecondLevelQRCode.size()>0){
//            if(mArrayListQRCode.size()>0)
//            scannedProd = (mArrayListSecondLevelQRCode.size()*Integer.parseInt(scanSequence.getThird().getSize())
//                    *Integer.parseInt(scanSequence.getSecond().getSize()))+mArrayListQRCode.size();
//            else
//                scannedProd = (mArrayListSecondLevelQRCode.size()*Integer.parseInt(scanSequence.getThird().getSize())
//                        *Integer.parseInt(scanSequence.getSecond().getSize()))+Integer.parseInt(scanSequence.getSecond().getSize());

//                       +" / "+(Integer.valueOf(scanSequence.getThird().getSize())*
//                    Integer.valueOf(scanSequence.getFourth().getSize())));


            scannedProd = ((mArrayListSecondLevelQRCode.size()*Integer.valueOf(scanSequence.getThird().getSize()))+mArrayListFirstLevelQRCode.size())*Integer.parseInt(scanSequence.getSecond().getSize())+mArrayListQRCode.size();

        }else if(mArrayListFirstLevelQRCode.size()>0){
//            if(mArrayListQRCode.size()>0)
            scannedProd = (mArrayListFirstLevelQRCode.size()*Integer.parseInt(scanSequence.getSecond().getSize()))+mArrayListQRCode.size();
//            else
//                scannedProd = (mArrayListFirstLevelQRCode.size()*Integer.parseInt(scanSequence.getSecond().getSize()))+Integer.parseInt(scanSequence.getSecond().getSize());
        }else{
            scannedProd = mArrayListQRCode.size();
        }

        return scannedProd+" / "+totalProds;
    }
    private void cancelScanConfirmation(){

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ActivityScanning.this, R.style.AlertDialogStyleAppCompat);
        builder.setTitle(getString(R.string.discard_shipper));
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.discard_shipper_message));

        builder.setPositiveButton("Continue", (dialog, which) -> {
            dialog.dismiss();
            scanSequence();

        });
        builder.setNegativeButton("Discard", (dialog, which) ->
        {
            dialog.dismiss();
            discardScan();
        });
        builder.show();
    }

    private void discardScan(){
        if (mUtility.haveInternet()) {
            JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put("productId", strProductID);
                jsonRequest.put("batchId", strBatchID);

            } catch (Exception e) {
                e.printStackTrace();
            }
            mScanningViewModel.discardShipper(jsonRequest);
        } else {
            Toast.makeText(ActivityScanning.this, getResources().getString(R.string.internet_error), Toast.LENGTH_LONG).show();
        }
    }

    private void customToast(String message){
        final Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 500);
    }

    private void productSort(){
        Collections.sort(mArrayListQRCode, new Comparator<VoResponseIncompleteShipperList>() {
            @Override
            public int compare(VoResponseIncompleteShipperList a1, VoResponseIncompleteShipperList a2) {
                return a1.getQr_code().compareToIgnoreCase(a2.getQr_code());
            }
        });
    }

}
