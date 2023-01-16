package com.gsp.trusttags.mobile.mapping;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gsp.trusttags.mobile.mapping.R;
import com.gsp.trusttags.mobile.mapping.databinding.ActivityHistoryBinding;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceHistoryShipperListData;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ActivityShipperListing extends BaseActivity {

    ActivityHistoryBinding mActivityHistoryBinding;

    ShipperListAdapter mProductListAdapter;

    ArrayList<VoResponceHistoryShipperListData> mArrayListShipperList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();
        mActivityHistoryBinding = (ActivityHistoryBinding) putContentView(R.layout.activity_history);

        mImageViewDrawer.setImageResource(R.drawable.ic_back_white);
        mTextViewTitle.setText("Scanned Shipper");

        mArrayListShipperList = (ArrayList<VoResponceHistoryShipperListData>) getIntent().getSerializableExtra("mArrayListShipperList");

        mImageViewDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mImageViewSync.setVisibility(View.GONE);

        mProductListAdapter = new ShipperListAdapter();
        mActivityHistoryBinding.activityHistoryRecyclerProductListing.setLayoutManager(new LinearLayoutManager(ActivityShipperListing.this));
        mActivityHistoryBinding.activityHistoryRecyclerProductListing.setAdapter(mProductListAdapter);

        mActivityHistoryBinding.activityHistoryTxtProductTitle.setVisibility(View.GONE);
        mActivityHistoryBinding.activityHistoryEditSearching.setVisibility(View.GONE);

    }

    @Override
    public void showWait() {

    }

    @Override
    public void removeWait() {

    }

    public class ShipperListAdapter extends RecyclerView.Adapter<ShipperListAdapter.ViewHolder> {

        @NonNull
        @Override
        public ShipperListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);
            return new ShipperListAdapter.ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ShipperListAdapter.ViewHolder holder, int position) {
            TextView mTextViewBatchNo = (TextView) holder.binding.getRoot().findViewById(R.id.raw_history_shipper_list_txt_batch_no);
            TextView mTextViewStatus = (TextView) holder.binding.getRoot().findViewById(R.id.raw_history_shipper_list_txt_status);

            mTextViewBatchNo.setText(getResources().getString(R.string.text_history_shipper_id, mArrayListShipperList.get(position).getUnique_code()));

            if (mArrayListShipperList.get(position).isIs_completed()) {
                mTextViewStatus.setText(getResources().getString(R.string.text_history_shipper_completed));
            } else {
                mTextViewStatus.setText(getResources().getString(R.string.text_history_shipper_pending));
            }

        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.raw_history_shipper_list;
        }

        @Override
        public int getItemCount() {
            return mArrayListShipperList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            final ViewDataBinding binding;

            ViewHolder(ViewDataBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
