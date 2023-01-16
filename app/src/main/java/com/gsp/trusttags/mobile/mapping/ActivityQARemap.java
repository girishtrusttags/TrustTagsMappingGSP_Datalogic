package com.gsp.trusttags.mobile.mapping;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gsp.trusttags.mobile.mapping.databinding.ActivityQaRemapBinding;
import com.gsp.trusttags.mobile.mapping.viewModel.QARemapViewModel;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseFreeMapping;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseScanForRemap;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityQARemap extends BaseActivity {

    ActivityQaRemapBinding mActivityQaRemapBinding;
    QARemapViewModel mQaRemapViewModel;

    ProductListAdapter mProductListAdapter;

    //Data
    VoResponseScanForRemap voResponseScanForRemap;
    String mStringShipperCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        mActivityQaRemapBinding = (ActivityQaRemapBinding) putContentView(R.layout.activity_qa_remap);
        mQaRemapViewModel = new QARemapViewModel(this, mMyService);
        mActivityQaRemapBinding.setQaRemapViewModel(mQaRemapViewModel);

        mImageViewDrawer.setImageResource(R.drawable.ic_menu);
        mTextViewTitle.setText(getResources().getString(R.string.text_remap_title));
        mImageViewSync.setVisibility(View.GONE);

        mImageViewDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });

        if (getIntent().hasExtra("remap_data")) {
            voResponseScanForRemap = (VoResponseScanForRemap) getIntent().getSerializableExtra("remap_data");
            mStringShipperCode = getIntent().getStringExtra("shipper_code");

            if (voResponseScanForRemap != null) {
                if (voResponseScanForRemap.getData() != null && voResponseScanForRemap.getData().getSubCodes() != null
                        && voResponseScanForRemap.getData().getSubCodes().size() > 0) {
                    mProductListAdapter = new ProductListAdapter(voResponseScanForRemap.getData().getSubCodes());
                    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(ActivityQARemap.this, RecyclerView.VERTICAL, false);
                    mActivityQaRemapBinding.activityQaRemapRecyclerviewProductListing.setLayoutManager(mLinearLayoutManager);
                    mActivityQaRemapBinding.activityQaRemapRecyclerviewProductListing.setAdapter(mProductListAdapter);
                }
            }
        }

        mActivityQaRemapBinding.activityQaRemapTextviewScanDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityQARemap.this, R.style.AlertDialogStyleAppCompat);
                builder.setTitle(getString(R.string.app_name));
                builder.setCancelable(false);
                builder.setMessage(getString(R.string.sure_to_remap_shipper));

                builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        if (mUtility.haveInternet()) {
                            mQaRemapViewModel.freeMapping(mStringShipperCode);
                        } else {
                            mUtility.ToastMsg(getString(R.string.internet_error));
                        }
                    }
                });

                builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

    }

    @Override
    public void showWait() {

    }

    @Override
    public void removeWait() {

    }

    public void setResponseData(VoResponseFreeMapping voResponseFreeMapping) {
        if (voResponseFreeMapping.getMessage() != null && !voResponseFreeMapping.getMessage().equalsIgnoreCase("")) {
            Toast.makeText(this, voResponseFreeMapping.getMessage(), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

        ArrayList<String> mArrayListSubCodes;

        ProductListAdapter(ArrayList<String> mArrayListSubCodes) {
            this.mArrayListSubCodes = mArrayListSubCodes;
        }

        @NonNull
        @Override
        public ProductListAdapter.ViewHolder onCreateViewHolder(@NonNull @io.reactivex.annotations.NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_remap_product_history, parent, false);
            return new ProductListAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductListAdapter.ViewHolder holder, int position) {
            holder.mTextViewProductSubCodes.setText(mArrayListSubCodes.get(position));
        }

        @Override
        public int getItemCount() {
            return mArrayListSubCodes.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.raw_history_product_txt_batch_no)
            TextView mTextViewProductSubCodes;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

    }

}