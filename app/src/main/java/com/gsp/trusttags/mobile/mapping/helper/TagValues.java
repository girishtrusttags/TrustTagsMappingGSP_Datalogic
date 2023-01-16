package com.gsp.trusttags.mobile.mapping.helper;

import com.gsp.trusttags.mobile.mapping.BuildConfig;

public class TagValues {

//        public static String MainURL = "http://gspapi.trusttags.in/"; //Live
//    public static String MainURL = "http://demo-api.trusttags.in/"; //Live
//    public static String MainURL = "http://13.233.237.232:3005/"; //Development VLCC
    //    public static String MainURL = "http://13.233.237.232:3002/"; //Development KC

//    public static String MainURL = "http://gspdev-api.trusttags.in/"; //DEV
//    public static String MainURL = BuildConfig.FLAVOR=="prod"?"https://gsp-api.trusttags.in/":"http://gspdev-api.trusttags.in/"; //PROD:DEV
//    public static String MainURL = BuildConfig.FLAVOR=="mildev"?" http://mol-api.trusttags.in/":"http://ms-api.trusttags.in/"; //PROD:DEV
   // public static String MainURL = "https://mil-api.trusttags.in/"/*"http://mildev-api.trusttags.in/"*/;
//    public static String MainURL = "http://ms-api.trusttags.in/";
//    public static String MainURL = "https://ww-dev-api.trusttags.in/";
    public static String MainURL = "";


//    public static String MainURL = "https://ww-api.trusttags.in/";//"https://msdev-api.trusttags.in/";


    public static final String id = "id";
    public static final String version = "version";
    public static String IS_FROM = "is_from";

    public static final String isFromSideBar = "isFromSideBar";

    public static final String PREF_CREDENTIAL_PREFERENCE = "pref_credential_preference";
    public static final String PREF_USER_EMAIL = "pref_user_email";
    public static final String PREF_USER_PASSWORD = "pref_user_password";
    public static final String PREF_IS_REMEMBER_ME = "pref_is_remember_me";

    public static final String CONST_NUMBER_0 = "0";
    public static final String CONST_NUMBER_1 = "1";

    public static final int INT_UNAUTHORIZED = 401;

    //API URL Endpoints
    public static final String URL_LOGIN = "scaninguserlogin";
    public static final String URL_FORGOT_PASSWORD = "scaninguserforgotpassword";
    public static final String URL_CHECK_INCOMPLETE_SHIPPER = "scanning/checkincompleteshipper/{user_id}";
    public static final String URL_FETCH_BATCHES = "scanning/getbatchbysku/{sku_id}";
    public static final String URL_FETCH_SKU = "scanning/getsku";
    public static final String URL_CREATE_PACKAGE_SCAN_LOG = "scanning/addpackagesscanlog";
    public static final String URL_FETCH_BATCHES_OF_SAME_SKU = "scanning/getbatcheswithsamesku/{unit_id}/{product_id}";
    public static final String URL_SCAN_QR_CODE = "scanning/packagingqrcodescan/{scanQrCode}";
    public static final String URL_UNSCAN_QR_CODE = "scanning/packagingqrcodeunscan/{scanQrCode}/{log_id}";
    public static final String URL_UPDATE_PACKAGE_SCAN_LOG = "scanning/updatepackagesscanlog/{log_id}";
    public static final String URL_SCAN_SHIPPER_CODE = "scanning/shipperqrcodescan/{qr_code}";
//    public static final String URL_FETCH_BATCH_HISTORY = "scanning/batchhistory/{productId}/{batchId}/{startDate}/{endDate}";
public static final String URL_FETCH_BATCH_HISTORY = "scanning/batchhistory";
//    public static final String URL_DISCARD_SHIPPER = "scanning/discardShipper/{log_id}";
public static final String URL_DISCARD_SHIPPER = "scanning/discardShipper";
    public static final String URL_SCAN_FOR_REPRINT = "scanning/scanForReprint";
    public static final String URL_SCAN_FOR_REMAP = "scanning/scanForRemap/{shipper_code}";
    public static final String URL_REPRINT_SEND_MAIL = "scanning/resendCode";
    public static final String URL_FREE_MAPPING = "scanning/freeMapping";

    //First Level Scan
    public static final String URL_FIRST_LEVEL_SCAN = "scanning/fst-lvl-inner/map-qrcode";
    public static final String URL_SECOND_LEVEL_SCAN = "scanning/snd-lvl-inner/map-qrcode";


    //Inline
    public static final String URL_FETCH_BATCHES_INLINE = "scanning/getbatchbysku-inline/{sku_id}";
    public static final String URL_FIRST_LEVEL_SCAN_INLINE = "scanning/fst-lvl-inner/inline/map-qrcode";
    public static final String URL_SECOND_LEVEL_SCAN_INLINE = "scanning/snd-lvl-inner/inline/map-qrcode";
    public static final String URL_SCAN_SHIPPER_CODE_INLINE = "scanning/shipperqrcodescan/inline/{qr_code}";
    public static final String URL_CHECK_INCOMPLETE_SHIPPER_IN_LINE = "scanning/checkincompleteshipper/inline/{user_id}";

    //API Call Types
    public static final String CALL_GET = "GET";
    public static final String CALL_POST = "POST";
    public static final String CALL_PUT = "PUT";

    public static final String isFromQARemapDashboard = "isFromQARemapDashboard";
    public static final String isFromQARemap = "isFromQARemap";
    public static final String isFromQAReprintShipperScan = "isFromQAReprintShipperScan";
    public static final String isFromQAReprintSKUCodeScan = "isFromQAReprintSKUCodeScan";

    public static final String SKUCODE_KEY = "skuCode";
    public static final String SHIPPER_KEY = "shipper";
    public static final String FIRST_INNER_KEY = "fstInner";
    public static final String SECOND_INNER_KEY = "sndInner";

}
