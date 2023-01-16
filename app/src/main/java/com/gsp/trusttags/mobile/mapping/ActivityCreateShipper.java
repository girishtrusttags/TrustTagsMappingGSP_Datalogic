package com.gsp.trusttags.mobile.mapping;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.gsp.trusttags.mobile.mapping.R;
import com.gsp.trusttags.mobile.mapping.databinding.ActivityCreateShipperBinding;
import com.gsp.trusttags.mobile.mapping.viewModel.CreateShipperViewModel;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceGetBatchesData;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceGetBatchesScanSequence;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseFetchSKUData;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseIncompleteShipperList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ActivityCreateShipper extends BaseActivity {

    public static String CLASS_NAME = ActivityCreateShipper.class.getSimpleName();

    ActivityCreateShipperBinding mActivityCreateShipperBinding;

    CreateShipperViewModel mCreateShipperViewModel;

    ArrayList<VoResponseFetchSKUData> mArrayListSku = new ArrayList<>();
    ArrayList<VoResponceGetBatchesData> mArrayListBatches = new ArrayList<>();

    String strSkuID = "";
    String strProductID = "";
    String strBatchID = "";
    String strBatchNo = "";
    String strLogID = "";
    private boolean isInLine = false;
    private Bundle extra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        mActivityCreateShipperBinding = (ActivityCreateShipperBinding) putContentView(R.layout.activity_create_shipper);

        mCreateShipperViewModel = ViewModelProviders.of(this).get(CreateShipperViewModel.class);
        mCreateShipperViewModel.setActivityAndService(this, mMyService, mActivityCreateShipperBinding);

        mImageViewDrawer.setImageResource(R.drawable.ic_back_white);
        mTextViewTitle.setText(getResources().getString(R.string.text_title_start_shipper_scanning));

        extra = getIntent().getExtras();
        if(extra != null){
            isInLine = extra.getBoolean("isInLine");
        }
        mImageViewDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mImageViewSync.setVisibility(View.GONE);

        if (mUtility.haveInternet()) {
            mCreateShipperViewModel.getSkus(mPreferenceHelper.getUserId(), mPreferenceHelper.getAccessToken());
        } else {
            Toast.makeText(ActivityCreateShipper.this, getResources().getString(R.string.internet_error), Toast.LENGTH_LONG).show();
        }

        mActivityCreateShipperBinding.activityCreateShipperSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strProductID = mArrayListBatches.get(position).getProduct_id();
                strBatchNo = mArrayListBatches.get(position).getBatch_no();
                strBatchID = mArrayListBatches.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mActivityCreateShipperBinding.activityCreateShipperSpinnerProductName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSkuID = mArrayListSku.get(position).getId();
                mCreateShipperViewModel.getBatches(mPreferenceHelper.getUserId(), mPreferenceHelper.getAccessToken(), strSkuID,isInLine);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mActivityCreateShipperBinding.activityCreateShipperStartScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strProductID.equalsIgnoreCase("")) {
                    Toast.makeText(ActivityCreateShipper.this, getResources().getString(R.string.text_create_shipper_select_batch), Toast.LENGTH_LONG).show();
                }else if (mActivityCreateShipperBinding.edtLineNo.getText().toString().length()==0) {
                    Toast.makeText(ActivityCreateShipper.this, getResources().getString(R.string.text_enter_line_no), Toast.LENGTH_LONG).show();
                } else {
                    Map<String, String> hashMap = new HashMap<>();
                    hashMap.put("employee_id", mPreferenceHelper.getUserId());
                    hashMap.put("product_id", strProductID);

                    mCreateShipperViewModel.createLog(mPreferenceHelper.getUserId(), mPreferenceHelper.getAccessToken(), strBatchID, strBatchNo, hashMap,scanSequence,isInLine,
                            mActivityCreateShipperBinding.edtLineNo.getText().toString());
                }
            }
        });

    }

    @Override
    public void showWait() {

    }

    @Override
    public void removeWait() {

    }

    VoResponceGetBatchesScanSequence scanSequence = new VoResponceGetBatchesScanSequence();
    public void setSpinnerData(ArrayList<VoResponceGetBatchesData> mArrayListBatches, VoResponceGetBatchesScanSequence scanSequence) {
        Collections.sort(mArrayListBatches, new Comparator<VoResponceGetBatchesData>() {
            @Override
            public int compare(VoResponceGetBatchesData a1, VoResponceGetBatchesData a2) {
                return a1.getBatch_no().compareToIgnoreCase(a2.getBatch_no());
            }
        });
        this.mArrayListBatches = mArrayListBatches;
        this.scanSequence = scanSequence;
        ArrayAdapter<VoResponceGetBatchesData> adapter =
                new ArrayAdapter<VoResponceGetBatchesData>(getApplicationContext(), R.layout.spinner_text_item_row, mArrayListBatches);
        adapter.setDropDownViewResource(R.layout.spinner_text_item_row);

        mActivityCreateShipperBinding.activityCreateShipperSpinner.setAdapter(adapter);



    }

    public void setSpinnerDataAsNull() {
        mActivityCreateShipperBinding.activityCreateShipperSpinner.setAdapter(null);
    }

    public void setSkuSpinnerData(ArrayList<VoResponseFetchSKUData> mArrayListSku) {

        Collections.sort(mArrayListSku, new Comparator<VoResponseFetchSKUData>() {
            @Override
            public int compare(VoResponseFetchSKUData a1, VoResponseFetchSKUData a2) {
                return a1.getSku().compareToIgnoreCase(a2.getSku());
            }
        });

        this.mArrayListSku = mArrayListSku;

        ArrayAdapter<VoResponseFetchSKUData> adapter =
                new ArrayAdapter<VoResponseFetchSKUData>(getApplicationContext(), R.layout.spinner_text_item_row, mArrayListSku);
        adapter.setDropDownViewResource(R.layout.spinner_text_item_row);

        mActivityCreateShipperBinding.activityCreateShipperSpinnerProductName.setAdapter(adapter);
    }

}
