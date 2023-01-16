package com.gsp.trusttags.mobile.mapping.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceHelper {

    private final SharedPreferences mSharedPreferences;

    public PreferenceHelper(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private String PREF_UserId = "UserId";
    private String PREF_Email = "Email";
    private String PREF_FirstName = "FirstName";
    private String PREF_CompanyID = "Company_ID";
    private String PREF_CompanyName = "Company_Name";
    private String PREF_CompanyLogo = "Company_Logo";
    private String PREF_CompanyRandomID = "Company_Random_ID";
    private String PREF_ContactNo = "Contact_No";
    private String PREF_AccessToken = "Access_Token";
    private String PREF_UnitID = "Unit_ID";

    public String getFirstName() {
        String str = mSharedPreferences.getString(PREF_FirstName, "");
        return str;
    }

    public void setFirstName(String pREF_Key) {
        Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(PREF_FirstName, pREF_Key);
        mEditor.commit();
    }

    public String getLastName() {
        String str = mSharedPreferences.getString(PREF_FirstName, "");
        return str;
    }

    public void setLastName(String pREF_Key) {
        Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(PREF_FirstName, pREF_Key);
        mEditor.commit();
    }

    public String getEmail() {
        String str = mSharedPreferences.getString(PREF_Email, "");
        return str;
    }

    public void setEmail(String pREF_Key) {
        Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(PREF_Email, pREF_Key);
        mEditor.commit();
    }

    public String getUserId() {
        String str = mSharedPreferences.getString(PREF_UserId, "");
        return str;
    }

    public void setUserId(String pREF_Key) {
        Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(PREF_UserId, pREF_Key);
        mEditor.commit();
    }

    public String getCompanyID() {
        String str = mSharedPreferences.getString(PREF_CompanyID, "");
        return str;
    }

    public void setCompanyID(String pREF_Key) {
        Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(PREF_CompanyID, pREF_Key);
        mEditor.commit();
    }

    public String getCompanyName() {
        String str = mSharedPreferences.getString(PREF_CompanyName, "");
        return str;
    }

    public void setCompanyName(String pREF_Key) {
        Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(PREF_CompanyName, pREF_Key);
        mEditor.commit();
    }

    public String getCompanyLogo() {
        String str = mSharedPreferences.getString(PREF_CompanyLogo, "");
        return str;
    }

    public void setCompanyLogo(String pREF_Key) {
        Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(PREF_CompanyLogo, pREF_Key);
        mEditor.commit();
    }

    public String getCompanyRandomID() {
        String str = mSharedPreferences.getString(PREF_CompanyRandomID, "");
        return str;
    }

    public void setCompanyRandomID(String pREF_Key) {
        Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(PREF_CompanyRandomID, pREF_Key);
        mEditor.commit();
    }

    public String getContactNo() {
        String str = mSharedPreferences.getString(PREF_ContactNo, "");
        return str;
    }

    public void setContactNo(String pREF_Key) {
        Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(PREF_ContactNo, pREF_Key);
        mEditor.commit();
    }

    public String getAccessToken() {
        String str = mSharedPreferences.getString(PREF_AccessToken, "");
        return str;
    }

    public void setAccessToken(String pREF_Key) {
        Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(PREF_AccessToken, pREF_Key);
        mEditor.commit();
    }

    public String getUnitID() {
        String str = mSharedPreferences.getString(PREF_UnitID, "");
        return str;
    }

    public void setUnitID(String pREF_Key) {
        Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(PREF_UnitID, pREF_Key);
        mEditor.commit();
    }


    public void clearUserData() {
        Editor mEditor = mSharedPreferences.edit();

        mEditor.putString(PREF_UserId, "");
        mEditor.putString(PREF_Email, "");
        mEditor.putString(PREF_FirstName, "");
        mEditor.putString(PREF_CompanyID, "");
        mEditor.putString(PREF_ContactNo, "");
        mEditor.putString(PREF_AccessToken, "");
        mEditor.putString(PREF_UnitID, "");

        mEditor.commit();
    }

}
