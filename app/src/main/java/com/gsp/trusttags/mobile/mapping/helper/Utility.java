package com.gsp.trusttags.mobile.mapping.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.request.RequestOptions;
import com.gsp.trusttags.mobile.mapping.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class Utility {

    private Activity mActivity;
    private SpotsDialog mSpotsDialog;
    private AlertDialog mAlertDialog;
    private Toast mToast;

    private static final int MULTIPLE_PERMISSIONS_RESPONSE_CODE = 1;

    public Utility(Activity mActivity) {
        this.mActivity = mActivity;
    }

    //region TOAST MSG
    public void ToastMsg(String strMsg) {
        if (mToast == null)
            mToast = Toast.makeText(mActivity, strMsg, Toast.LENGTH_SHORT);
        else
            mToast.setText(strMsg);

        View view = mToast.getView();
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setTextColor(mActivity.getResources().getColor(R.color.white));
        text.setPadding(mActivity.getResources().getDimensionPixelSize(R.dimen._10sdp), 0, mActivity.getResources().getDimensionPixelSize(R.dimen._10sdp), 0);
        view.setBackgroundResource(R.drawable.shape_rounded_background_black);

        mToast.show();
    }
    //endregion

    //region error dialog with click listener and title
    public void errorDialogWithTitleWithClick(String title, String message, DialogInterface.OnClickListener mOnClickListener) {
        AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(mActivity);
        mAlertDialogBuilder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", mOnClickListener);

        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
        mAlertDialog = mAlertDialogBuilder.create();
        mAlertDialog.show();

    }
    //endregion

    //region error dialog with message
    public void errorDialog(String message) {
        if (!mActivity.isFinishing()) {
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(mActivity);
            mAlertDialogBuilder.setTitle(mActivity.getResources().getString(R.string.text_login_forget_password_new));
            mAlertDialogBuilder.setMessage(message).setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });

            if (mAlertDialog != null) {
                mAlertDialog.dismiss();
                mAlertDialog = null;
            }
            mAlertDialog = mAlertDialogBuilder.create();
            mAlertDialog.show();
        }
    }
    //endregion

    //region is internet on
    public boolean haveInternet() {
        NetworkInfo mNetworkInfo = (NetworkInfo) ((ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (mNetworkInfo == null || !mNetworkInfo.isConnected()) {
            return false;
        }
        if (mNetworkInfo.isRoaming()) {
            return true;
        }
        return true;
    }
    //endregion

    //region hide keyboard
    public void hideKeyboard() {
        View view = mActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    //endregion

    //region FUNCTION TO SHOW LOG
    public void ShowLog(String mStringActivityName, String mStringTitle, String mStringMessage) {
        System.out.println("Darshan... " + mStringActivityName + " " + mStringTitle + " : " + mStringMessage);
    }
    //endregion

    //region validating email
    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    //endregion

    //region show animated progress
    public void showAnimatedProgress(Activity activity) {
        if (mSpotsDialog == null)
            mSpotsDialog = new SpotsDialog(activity);

        if (mSpotsDialog.isShowing()) {
            mSpotsDialog.dismiss();
        }

        mSpotsDialog.show();
    }
    //endregion

    //region hide animated progress
    public void HideAnimatedProgress() {
        if (mSpotsDialog != null && mSpotsDialog.isShowing()) {
            mSpotsDialog.dismiss();
        }
    }
    //endregion

    public boolean verifyPermissions(Activity activity, String[] ALL_PERMS) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : ALL_PERMS) {
            result = ActivityCompat.checkSelfPermission(activity, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity,ALL_PERMS,
                    MULTIPLE_PERMISSIONS_RESPONSE_CODE);
            return false;
        }else{
            return true;
        }

    }

    public void ExitDialog(String dialogMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.AlertDialogStyleAppCompat);
        builder.setTitle(mActivity.getString(R.string.exit));
        builder.setCancelable(false);
        builder.setMessage(dialogMsg);
        builder.setPositiveButton(mActivity.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mActivity.finish();
            }
        });
        builder.setNegativeButton(mActivity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    public static RequestOptions getCommanRequestOptions() {
        return new RequestOptions()
                .dontAnimate()
                .placeholder(R.drawable.ic_center_logo)
                .error(R.drawable.ic_center_logo);
    }

    public static boolean compareDate(String strStartDate,String strEndDate) {
        try{
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date dateStart = format.parse(strStartDate);
            Date dateEnd = format.parse(strEndDate);
            if(dateStart.compareTo(dateEnd)>0){
                return false;
            }
        }catch(ParseException e){
            e.printStackTrace();
        }


        return true;
    }

    public static boolean compareFutureDate(String strStartDate) {
        try{
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date dateStart = format.parse(strStartDate);
            if(new Date().before(dateStart)){
                return false;
            }
        }catch(ParseException e){
            e.printStackTrace();
        }


        return true;
    }
    public static String getDeviceID(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String device_id = tm.getDeviceId();
        if (device_id == null) {
            device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {

        }
        return device_id;
    }

}