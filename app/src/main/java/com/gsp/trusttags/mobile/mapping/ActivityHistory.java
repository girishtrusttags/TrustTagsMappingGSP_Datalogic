package com.gsp.trusttags.mobile.mapping;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gsp.trusttags.mobile.mapping.R;
import com.gsp.trusttags.mobile.mapping.databinding.ActivityHistoryBinding;
import com.gsp.trusttags.mobile.mapping.helper.Utility;
import com.gsp.trusttags.mobile.mapping.viewModel.HistoryViewModel;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceGetBatchesData;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceGetBatchesScanSequence;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceHistoryListData;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseFetchSKUData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ActivityHistory extends BaseActivity {

    private static final String CLASS_NAME = ActivityHistory.class.getSimpleName();

    ActivityHistoryBinding mActivityHistoryBinding;

    BatchListAdapter mProductListAdapter;

    HistoryViewModel mHistoryViewModel;

    ArrayList<VoResponceHistoryListData> mArrayListBatchList = new ArrayList<>();
    ArrayList<VoResponceHistoryListData> mArrayListBatchListTemp = new ArrayList<>();

    SimpleDateFormat mSimpleDateFormatFetch = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
    SimpleDateFormat mSimpleDateFormatPrint = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();


        mActivityHistoryBinding = (ActivityHistoryBinding) putContentView(R.layout.activity_history);

        mHistoryViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        mHistoryViewModel.setActivityAndService(this, mMyService, mActivityHistoryBinding);

        mTextViewTitle.setText(getResources().getString(R.string.text_title_scanned_batch));

        mImageViewDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });

        mProductListAdapter = new BatchListAdapter();
        mActivityHistoryBinding.activityHistoryRecyclerProductListing.setLayoutManager(new LinearLayoutManager(ActivityHistory.this));
        mActivityHistoryBinding.activityHistoryRecyclerProductListing.setAdapter(mProductListAdapter);
        mImageViewSync.setVisibility(View.GONE);

        if (mUtility.haveInternet()) {
            mHistoryViewModel.getSkus(mPreferenceHelper.getUserId(), mPreferenceHelper.getAccessToken());
//            mHistoryViewModel.getScannedBatches(mPreferenceHelper.getUserId(), mPreferenceHelper.getAccessToken());
        } else {
            Toast.makeText(ActivityHistory.this, getResources().getString(R.string.internet_error), Toast.LENGTH_LONG).show();
        }

        mActivityHistoryBinding.activityHistoryEditSearching.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mArrayListBatchList != null && mArrayListBatchList.size() > 0) {
                    if (s.toString().length() > 0) {
                        applySearch(s.toString());
                    } else {
                        mActivityHistoryBinding.activityHistoryRecyclerProductListing.setVisibility(View.VISIBLE);

                        if (mProductListAdapter != null) {
                            mProductListAdapter.setHistoryData(mArrayListBatchList);
                            mProductListAdapter.notifyDataSetChanged();
                        }
                    }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter) {
            showChangeBatchDialog();
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setBachListData(ArrayList<VoResponceHistoryListData> mArrayListBatchList) {
        this.mArrayListBatchList = mArrayListBatchList;

        if (mProductListAdapter != null) {
            mProductListAdapter.setHistoryData(this.mArrayListBatchList);
            mProductListAdapter.notifyDataSetChanged();
        }
    }

    public class BatchListAdapter extends RecyclerView.Adapter<BatchListAdapter.ViewHolder> {

        ArrayList<VoResponceHistoryListData> mArrayListHistoryData = new ArrayList<>();

        void setHistoryData(ArrayList<VoResponceHistoryListData> mArrayListHistoryData) {
            this.mArrayListHistoryData = mArrayListHistoryData;
        }

        @NonNull
        @Override
        public BatchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);
            return new BatchListAdapter.ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull BatchListAdapter.ViewHolder holder, int position) {
            TextView mTextViewBatchNo = (TextView) holder.binding.getRoot().findViewById(R.id.raw_history_product_txt_batch_no);
            TextView mTextViewScannedShipper = (TextView) holder.binding.getRoot().findViewById(R.id.raw_history_product_txt_scanned_shipper);
            TextView mTextViewTotalShipper = (TextView) holder.binding.getRoot().findViewById(R.id.raw_history_product_txt_total_shipper);
            TextView mTextViewDate = (TextView) holder.binding.getRoot().findViewById(R.id.raw_history_product_txt_date);
//            TextView txtUniqeCode = (TextView) holder.binding.getRoot().findViewById(R.id.raw_history_product_txt_unique_code);

            VoResponceHistoryListData model = mArrayListHistoryData.get(position);
           /* mTextViewBatchNo.setText(getResources().getString(R.string.text_history_batch_no, mArrayListHistoryData.get(position).getBatch_no()));
            mTextViewTotalShipper.setText(getResources().getString(R.string.text_history_total_shipper, mArrayListHistoryData.get(position).getTotal_shipper_count()));

            if (mArrayListHistoryData.get(position).getLogDetails() != null
                    && mArrayListHistoryData.get(position).getLogDetails().getScanning_start_time() != null
                    && !mArrayListHistoryData.get(position).getLogDetails().getScanning_start_time().equalsIgnoreCase("")) {
                try {
                    Date mDate = mSimpleDateFormatFetch.parse(mArrayListHistoryData.get(position).getLogDetails().getScanning_start_time());
                    mTextViewDate.setText(getResources().getString(R.string.text_history_date, mSimpleDateFormatPrint.format(mDate)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            if (mArrayListHistoryData.get(position).getShipperlist() != null
                    && mArrayListHistoryData.get(position).getShipperlist().size() > 0) {
                mTextViewScannedShipper.setText(getResources().getString(R.string.text_history_scanned_shipper,
                        String.valueOf(mArrayListHistoryData.get(position).getShipperlist().size())));
            }*/


            if (!TextUtils.isEmpty(model.getScanning_datetime())) {
                try {
                    mSimpleDateFormatFetch.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date mDate = mSimpleDateFormatFetch.parse(model.getScanning_datetime());
                    mSimpleDateFormatPrint.setTimeZone(TimeZone.getDefault());
                    mTextViewDate.setText(getResources().getString(R.string.text_history_date, mSimpleDateFormatPrint.format(mDate)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            if (!TextUtils.isEmpty(model.getUnique_code())) {
                mTextViewScannedShipper.setText(model.getUnique_code());
            }

          /*  holder.itemView.setOnClickListener(view -> {
                Intent mIntent = new Intent(ActivityHistory.this, ActivityShipperListing.class);
                mIntent.putExtra("mArrayListShipperList", mArrayListHistoryData.get(position).getShipperlist());
                startActivity(mIntent);
            });*/

        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.raw_history_product;
        }

        @Override
        public int getItemCount() {
            return mArrayListHistoryData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            final ViewDataBinding binding;

            ViewHolder(ViewDataBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

    @Override
    public void onBackPressed() {
        gotoDashborad();
    }

    public void applySearch(String mStringSearchText) {
        mArrayListBatchListTemp.clear();

        for (int i = 0; i < mArrayListBatchList.size(); i++) {
            if (mArrayListBatchList.get(i).getBatch_no().toLowerCase().contains(mStringSearchText.toLowerCase())) {
                mArrayListBatchListTemp.add(mArrayListBatchList.get(i));
            }
        }

        if (mArrayListBatchListTemp.size() > 0) {
            mActivityHistoryBinding.activityHistoryRecyclerProductListing.setVisibility(View.VISIBLE);

            if (mProductListAdapter != null) {
                mProductListAdapter.setHistoryData(mArrayListBatchListTemp);
                mProductListAdapter.notifyDataSetChanged();
            } else {
                mProductListAdapter = new BatchListAdapter();
                mActivityHistoryBinding.activityHistoryRecyclerProductListing.setLayoutManager(new LinearLayoutManager(ActivityHistory.this));
                mActivityHistoryBinding.activityHistoryRecyclerProductListing.setAdapter(mProductListAdapter);
            }
        } else {
            mActivityHistoryBinding.activityHistoryRecyclerProductListing.setVisibility(View.GONE);
        }
    }

    Dialog mDialogChangeBatch;
    private Spinner mSpinnerProd;
    private Spinner mSpinnerBatches;
    private TextView txtStartDate;
    private TextView txtEndDate;
    private String strSkuID="";
    private int mYear, mMonth, mDay, mHour, mMinute;

    String strProductID = "";
    String strBatchID = "";
    String strBatchNo = "";

    public void showChangeBatchDialog() {

        if (mDialogChangeBatch == null) {
            mDialogChangeBatch = new Dialog(ActivityHistory.this);
            mDialogChangeBatch.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            mDialogChangeBatch.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialogChangeBatch.setCancelable(true);
            mDialogChangeBatch.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mDialogChangeBatch.setContentView(R.layout.dialog_filter_search_history);
            mDialogChangeBatch.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        TextView mTextViewChanage = (TextView) mDialogChangeBatch.findViewById(R.id.dialog_change_batch_txt_filter);
        mSpinnerProd = (Spinner) mDialogChangeBatch.findViewById(R.id.activity_create_shipper_spinner_product_name);
         mSpinnerBatches = (Spinner) mDialogChangeBatch.findViewById(R.id.activity_create_shipper_spinner);
         txtStartDate = (TextView) mDialogChangeBatch.findViewById(R.id.txtStartDate);
         txtEndDate = (TextView) mDialogChangeBatch.findViewById(R.id.txtEndDate);


        Collections.sort(mArrayListSku, new Comparator<VoResponseFetchSKUData>() {
            @Override
            public int compare(VoResponseFetchSKUData a1, VoResponseFetchSKUData a2) {
                return a1.getSku().compareToIgnoreCase(a2.getSku());
            }
        });
        ArrayAdapter<VoResponseFetchSKUData> adapter = new ArrayAdapter<VoResponseFetchSKUData>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, mArrayListSku);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerProd.setAdapter(adapter);

        mSpinnerProd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSkuID = mArrayListSku.get(position).getId();
                mHistoryViewModel.getBatches(mPreferenceHelper.getUserId(), mPreferenceHelper.getAccessToken(), strSkuID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinnerBatches.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicketrDialog(DATE_TYPE.START_DATE);
            }
        });

        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicketrDialog(DATE_TYPE.END_DATE);
            }
        });

        mTextViewChanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(strSkuID)){
                    Toast.makeText(ActivityHistory.this,"Please select Product",Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(strBatchID)){
                    Toast.makeText(ActivityHistory.this,"Please select Batch No.",Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(txtStartDate.getText().toString())){
                    Toast.makeText(ActivityHistory.this,"Please Select From Date",Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(txtEndDate.getText().toString())){
                    Toast.makeText(ActivityHistory.this,"Please Select To Date",Toast.LENGTH_SHORT).show();
                    return;
                }else if(!Utility.compareDate(txtStartDate.getText().toString(),txtEndDate.getText().toString())){
                    Toast.makeText(ActivityHistory.this,"From date should be less than or equal to To date",Toast.LENGTH_SHORT).show();
                    return;
                }else if(!Utility.compareFutureDate(txtStartDate.getText().toString())){
                    Toast.makeText(ActivityHistory.this,"Future Date not allow",Toast.LENGTH_SHORT).show();
                    return;
                }
                mHistoryViewModel.getScannedBatches(mPreferenceHelper.getUserId(), mPreferenceHelper.getAccessToken(),
                        strSkuID,strBatchID,txtStartDate.getText().toString(),txtEndDate.getText().toString());
                mDialogChangeBatch.dismiss();
            }
        });


        if (!mDialogChangeBatch.isShowing())
            mDialogChangeBatch.show();
    }
    ArrayList<VoResponseFetchSKUData> mArrayListSku = new ArrayList<>();
    public void setSkuSpinnerData(ArrayList<VoResponseFetchSKUData> mArrayListSku) {
        this.mArrayListSku = mArrayListSku;

    /*    ArrayAdapter<VoResponseFetchSKUData> adapter =
                new ArrayAdapter<VoResponseFetchSKUData>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, mArrayListSku);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mActivityCreateShipperBinding.activityCreateShipperSpinnerProductName.setAdapter(adapter);*/
        showChangeBatchDialog();
    }

    ArrayList<VoResponceGetBatchesData> mArrayListBatches = new ArrayList<>();
    public void setBatchSpinnerData(ArrayList<VoResponceGetBatchesData> mArrayListBatches) {

        Collections.sort(mArrayListBatches, new Comparator<VoResponceGetBatchesData>() {
            @Override
            public int compare(VoResponceGetBatchesData a1, VoResponceGetBatchesData a2) {
                return a1.getBatch_no().compareToIgnoreCase(a2.getBatch_no());
            }
        });

        this.mArrayListBatches = mArrayListBatches;
        ArrayAdapter<VoResponceGetBatchesData> adapter =
                new ArrayAdapter<VoResponceGetBatchesData>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, mArrayListBatches);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinnerBatches.setAdapter(adapter);
    }

    public void setSpinnerDataAsNull() {
        mSpinnerBatches.setAdapter(null);
    }

    private void datePicketrDialog(DATE_TYPE type){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if(type == DATE_TYPE.START_DATE)
                            txtStartDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        else
                            txtEndDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    public enum DATE_TYPE
    {
        START_DATE,END_DATE;
    }
}
