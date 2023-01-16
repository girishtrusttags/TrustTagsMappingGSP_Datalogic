package com.gsp.trusttags.mobile.mapping;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.gsp.trusttags.mobile.mapping.R;
import com.gsp.trusttags.mobile.mapping.databinding.ActivitySplashBinding;

public class ActivitySplash extends BaseActivity {

    private int intAnimationTime = 2000;
    private int intAnimationDelayTime = 0;

    private ActivitySplashBinding mActivitySplashBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        mActivitySplashBinding = (ActivitySplashBinding) putContentView(R.layout.activity_splash);
        mToolbarMain.setVisibility(View.GONE);

        if(BuildConfig.FLAVOR=="dev" || BuildConfig.FLAVOR=="prod")
            mActivitySplashBinding.activitySplashScreenImgSbLogo.setImageResource(R.drawable.ic_gsp_logo);
        else if(BuildConfig.FLAVOR=="mildev")
            mActivitySplashBinding.activitySplashScreenImgSbLogo.setImageResource(R.drawable.mil_icon);
        else if(BuildConfig.FLAVOR=="moldev")
            mActivitySplashBinding.activitySplashScreenImgSbLogo.setImageResource(R.drawable.mol_icon);
        else if(BuildConfig.FLAVOR=="wwdev" || BuildConfig.FLAVOR=="wwprod")
            mActivitySplashBinding.activitySplashScreenImgSbLogo.setImageResource(R.drawable.ww_icon);
        else if(BuildConfig.FLAVOR=="msdev" || BuildConfig.FLAVOR=="msprod")
            mActivitySplashBinding.activitySplashScreenImgSbLogo.setImageResource(R.drawable.ic_mahindra_logo);
        else
            mActivitySplashBinding.activitySplashScreenImgSbLogo.setImageResource(R.drawable.ic_mahindra_logo);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        mActivitySplashBinding.activitySplashScreenImgBottomImage.animate()
                .setStartDelay(intAnimationDelayTime)
                .setDuration(intAnimationTime)
                .translationX(-width / 2);

        mActivitySplashBinding.activitySplashScreenImgLeftImage.animate()
                .setStartDelay(intAnimationDelayTime)
                .setDuration(intAnimationTime)
                .translationY(-height / 2);

        mActivitySplashBinding.activitySplashScreenImgRightImage.animate()
                .setStartDelay(intAnimationDelayTime)
                .setDuration(intAnimationTime)
                .translationY(height / 2);

        mActivitySplashBinding.activitySplashScreenImgLogo.animate()
                .alpha(1)
                .setDuration(intAnimationTime)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mActivitySplashBinding.activitySplashScreenClContent.animate()
                                .setStartDelay(0)
                                .setDuration(600)
                                .scaleX(20)
                                .scaleY(20)
                                .alpha(0)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        if (!mPreferenceHelper.getUserId().equalsIgnoreCase("")) {
                                            Intent i = new Intent(ActivitySplash.this, ActivityDashboard.class);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            Intent i = new Intent(ActivitySplash.this, ActivityLogin.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                });
                    }
                }).start();

        mActivitySplashBinding.activitySplashScreenImgHandShake.animate()
                .alpha(1)
                .setDuration(intAnimationTime)
                .start();

      /*  mActivitySplashBinding.activitySplashScreenTxtLogo.animate()
                .alpha(1)
                .setDuration(intAnimationTime)
                .start();*/

//        mActivitySplashBinding.activitySplashScreenTxtTrust.setTranslationX(-mUtility.getDisplayMetrics().widthPixels / 2);
//        mActivitySplashBinding.activitySplashScreenTxtTrust.animate().setDuration(intAnimationTime)
//                .translationX(0).setDuration(intAnimationTime).start();
//
//        mActivitySplashBinding.activitySplashScreenTxtTags.setTranslationX(mUtility.getDisplayMetrics().widthPixels / 2);
//        mActivitySplashBinding.activitySplashScreenTxtTags.animate().setDuration(intAnimationTime)
//                .translationX(0).setDuration(intAnimationTime).start();

        mActivitySplashBinding.activitySplashScreenImgBottomImage.animate()
                .setStartDelay(intAnimationDelayTime)
                .setDuration(intAnimationTime)
                .translationX(-width / 2);

    }

    public void showWait() {
        mUtility.hideKeyboard();
        mUtility.showAnimatedProgress(ActivitySplash.this);
    }

    public void removeWait() {
        mUtility.HideAnimatedProgress();
    }

}

