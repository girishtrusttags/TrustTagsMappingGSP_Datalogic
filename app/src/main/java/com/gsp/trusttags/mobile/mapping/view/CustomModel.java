package com.gsp.trusttags.mobile.mapping.view;

import com.gsp.trusttags.mobile.mapping.vo.VoResponseIncompleteShipperList;

import java.util.ArrayList;

public class CustomModel  {

    public interface OnCustomStateListener {
        void onScanAndDelete(ArrayList<VoResponseIncompleteShipperList> mArrayListQRCode);
    }

    public interface OnShipperScan {
        void onScanAndDelete(String strShipperID);
    }

    public interface OnFirstLevelScan {
        void onScanAndDelete(String qrCode);
    }

    public interface OnSecondLevelScan {
        void onScanAndDelete(String qrCode);
    }

    private static CustomModel mInstance;
    private OnCustomStateListener mListener;
    private OnShipperScan mOnShipperScan;
    private OnFirstLevelScan mOnFirstLevelScan;
    private OnSecondLevelScan mOnSecondLevelScan;
    private boolean mState;

    private CustomModel() {}

    public static CustomModel getInstance() {
        if(mInstance == null) {
            mInstance = new CustomModel();
        }
        return mInstance;
    }

    public void setListener(OnCustomStateListener listener) {
        mListener = listener;
    }

    public void setShipperListener(OnShipperScan listener) {
        mOnShipperScan = listener;
    }

    public void setFirstLvelListener(OnFirstLevelScan listener) {
        mOnFirstLevelScan = listener;
    }

    public void setSecondLvelListener(OnSecondLevelScan listener) {
        mOnSecondLevelScan = listener;
    }
    public void changeState(boolean state, ArrayList<VoResponseIncompleteShipperList> mArrayListQRCod) {
        if(mListener != null) {
            mState = state;
            notifyStateChange(mArrayListQRCod);
        }
    }

    public void changeShipperState(boolean state, String strShipperID) {
        if(mOnShipperScan != null) {
            mState = state;
            notifyShipperStateChange(strShipperID);
        }
    }

    public void changeFirstLevelState(boolean state, String qrCode) {
        if(mOnFirstLevelScan != null) {
            mState = state;
            notifyFirstLevelStateChange(qrCode);
        }
    }
    public void changeSecondLevelState(boolean state, String qrCode) {
        if(mOnSecondLevelScan != null) {
            mState = state;
            notifySecondLevelStateChange(qrCode);
        }
    }


    public boolean getState() {
        return mState;
    }

    private void notifyStateChange(ArrayList<VoResponseIncompleteShipperList> mArrayListQRCode) {
        mListener.onScanAndDelete(mArrayListQRCode);
    }
    private void notifyShipperStateChange(String strShipper) {
        mOnShipperScan.onScanAndDelete(strShipper);
    }

    private void notifyFirstLevelStateChange(String qrCode) {
        mOnFirstLevelScan.onScanAndDelete(qrCode);
    }

    private void notifySecondLevelStateChange(String qrCode) {
        mOnSecondLevelScan.onScanAndDelete(qrCode);
    }
}
