package com.gsp.trusttags.mobile.mapping;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.android.gms.vision.barcode.Barcode;
import com.gsp.trusttags.mobile.mapping.databinding.ActivityScanDeleteBinding;
import com.gsp.trusttags.mobile.mapping.viewModel.ScanDeleteVIewModel;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseIncompleteShipperList;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class ActivityScanAndDelete extends BaseActivity implements BarcodeReader.BarcodeReaderListener {

    ActivityScanDeleteBinding mActivityScanDeleteBinding;

    ScanDeleteVIewModel mScanDeleteVIewModel;

    ProductListAdapter mProductListAdapter;

    Dialog mDialogScanRemove;
    Dialog mDialogError;

    BarcodeReader mBarcodeReader;

    String strProductID = "";
    String strLogID = "";
    String strBatchID = "";
    String strBatchNo = "";
    String strShipperID = "";
    String strShipperSize = "";

    BarcodeManager mBarcodeManager;

    ReadListener mReadListener;

    ArrayList<VoResponseIncompleteShipperList> mArrayListQRCode = new ArrayList<>();

    String strQrCode = "";

    public static final int ACTIVITY_REQUEST_FOR_REMOVING_PRODUCT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        mActivityScanDeleteBinding = (ActivityScanDeleteBinding) putContentView(R.layout.activity_scan_delete);

        strProductID = getIntent().getStringExtra("strProductID");
        strLogID = getIntent().getStringExtra("strLogID");
        strBatchID = getIntent().getStringExtra("strBatchID");
        strBatchNo = getIntent().getStringExtra("strBatchNo");
        strShipperSize = getIntent().getStringExtra("strShipperSize");

        mArrayListQRCode = (ArrayList<VoResponseIncompleteShipperList>) getIntent().getSerializableExtra("mArrayListQRCode");

        mScanDeleteVIewModel = ViewModelProviders.of(this).get(ScanDeleteVIewModel.class);
        mScanDeleteVIewModel.setActivityAndService(this, mMyService, mActivityScanDeleteBinding);

        mProductListAdapter = new ProductListAdapter();
        mActivityScanDeleteBinding.activityScanDeleteRecyclerProductListing.setLayoutManager(new LinearLayoutManager(this));
        mActivityScanDeleteBinding.activityScanDeleteRecyclerProductListing.setAdapter(mProductListAdapter);

        mImageViewDrawer.setImageResource(R.drawable.ic_back_white);
        mTextViewTitle.setText("Scan and Delete");

        mImageViewDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mImageViewSync.setVisibility(View.GONE);

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
//                    mScanDeleteVIewModel.scanAndDelete(mPreferenceHelper.getUserId(), mPreferenceHelper.getAccessToken(), mStringValue, strLogID);
//                } else {
//                    showErrorMessage(getResources().getString(R.string.internet_error));
//                }
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

    public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

        @NonNull
        @Override
        public ProductListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductListAdapter.ViewHolder holder, int position) {
            TextView mTextViewProductName = (TextView) holder.binding.getRoot().findViewById(R.id.raw_scanned_product_txt_product);
            mTextViewProductName.setText(mArrayListQRCode.get(position).getQr_code());
        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.raw_scanned_product;
        }

        @Override
        public int getItemCount() {
            return mArrayListQRCode.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            final ViewDataBinding binding;

            ViewHolder(ViewDataBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

    public void showDeleteDialog() {

        if (mDialogScanRemove != null && mDialogScanRemove.isShowing()) {
            mDialogScanRemove.dismiss();
        }

        mDialogScanRemove = new Dialog(ActivityScanAndDelete.this);
        mDialogScanRemove.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        mDialogScanRemove.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogScanRemove.setCancelable(true);
        mDialogScanRemove.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialogScanRemove.setContentView(R.layout.dialog_scan_remove);
        mDialogScanRemove.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView mTextViewScanRemove = (TextView) mDialogScanRemove.findViewById(R.id.dialog_scan_remove_txt_remove);

        mTextViewScanRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogScanRemove.dismiss();
            }
        });

        mDialogScanRemove.show();
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
                            strQrCode = strQrCode.substring(strQrCode.lastIndexOf("/") + 1);
                        } else if (strQrCode.contains("\\")) {
                            strQrCode = strQrCode.substring(strQrCode.lastIndexOf("\\") + 1);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                boolean isScanned = false;
                                for (int i = 0; i < mArrayListQRCode.size(); i++) {
                                    if (mArrayListQRCode.get(i).getQr_code().equalsIgnoreCase(strQrCode)) {
                                        isScanned = true;
                                    }
                                }

                                if (isScanned) {
                                    if (mUtility.haveInternet()) {
                                        mScanDeleteVIewModel.scanAndDelete(mPreferenceHelper.getUserId(), mPreferenceHelper.getAccessToken(), strQrCode, strLogID);
                                    } else {
                                        showErrorMessage(getResources().getString(R.string.internet_error));
                                    }
                                } else {
                                    showError();
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

    public void updateArrayList(String mStringQRCode) {

        int intPosition = 0;

        for (int i = 0; i < mArrayListQRCode.size(); i++) {
            if (mArrayListQRCode.get(i).getQr_code().equalsIgnoreCase(mStringQRCode)) {
                intPosition = i;
            }
        }

        if (mArrayListQRCode.size() >= intPosition) {
            mArrayListQRCode.remove(intPosition);
        }

        if (mProductListAdapter != null) {
            mProductListAdapter.notifyDataSetChanged();
        }

        Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_LONG).show();

    }

    public void showError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyleAppCompat);
        builder.setTitle("Error");
        builder.setCancelable(false);
        builder.setMessage("Still this box not scanned with this shipper");
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

//        (new Handler()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mDialogError.dismiss();
//            }
//        }, 2000);
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
        mIntent.putExtra("mArrayListQRCode", mArrayListQRCode);
        setResult(ACTIVITY_REQUEST_FOR_REMOVING_PRODUCT, mIntent);

        super.onBackPressed();
    }

}
