package com.gsp.trusttags.mobile.mapping.services;

import com.gsp.trusttags.mobile.mapping.helper.TagValues;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseCheckInCompleteShipper;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceCreateLog;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceGetBatches;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceHistory;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceLogin;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceScanPackageQrCode;
import com.gsp.trusttags.mobile.mapping.vo.VoResponceScanShipperCode;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseDiscardShipper;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseFetchSKU;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseForgotPassword;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseFreeMapping;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseReprintSendMail;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseScanFirstLevel;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseScanForRemap;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseScanForReprint;
import com.gsp.trusttags.mobile.mapping.vo.VoResponseScanSecondLevel;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {

    @FormUrlEncoded
    @POST(TagValues.URL_LOGIN)
    Observable<VoResponceLogin> login(@FieldMap Map<String, String> mHashMap);

    @FormUrlEncoded
    @POST(TagValues.URL_FORGOT_PASSWORD)
    Observable<VoResponseForgotPassword> forgotPassword(@FieldMap Map<String, String> mHashMap);

    @GET(TagValues.URL_CHECK_INCOMPLETE_SHIPPER)
    Observable<VoResponseCheckInCompleteShipper> checkInCompleteShipper(@Header("x-key") String strCompanyID, @Header("x-access-token") String strAccessToken, @Path("user_id") String strUserID);

    @GET(TagValues.URL_FETCH_BATCHES)
    Observable<VoResponceGetBatches> getBatches(@Header("x-key") String strCompanyID, @Header("x-access-token") String strAccessToken, @Path("sku_id") String skuID);

    @FormUrlEncoded
    @POST(TagValues.URL_CREATE_PACKAGE_SCAN_LOG)
    Observable<VoResponceCreateLog> createPackagesLog(@Header("x-key") String strCompanyID, @Header("x-access-token") String strAccessToken, @FieldMap Map<String, String> mHashMap);

    @GET(TagValues.URL_FETCH_BATCHES_OF_SAME_SKU)
    Observable<VoResponceGetBatches> getBatchesWithSameSKU(@Header("x-key") String strCompanyID, @Header("x-access-token") String strAccessToken, @Path("unit_id") String strUnitID, @Path("product_id") String strProductID);

    @FormUrlEncoded
    @PUT(TagValues.URL_SCAN_QR_CODE)
    Observable<VoResponceScanPackageQrCode> scanPackageQrCode(@Header("x-key") String strCompanyID, @Header("x-access-token") String strAccessToken, @Path("scanQrCode") String strScanQrCode, @FieldMap Map<String, String> mHashMap);

    @PUT(TagValues.URL_UNSCAN_QR_CODE)
    Observable<VoResponceScanPackageQrCode> scanAndRemovePackageQrCode(@Header("x-key") String strCompanyID,
                                                                       @Header("x-access-token") String strAccessToken,
                                                                       @Path("scanQrCode") String strScanQrCode,
                                                                       @Path("log_id") String strLogId);

    @FormUrlEncoded
    @PUT(TagValues.URL_UPDATE_PACKAGE_SCAN_LOG)
    Observable<VoResponceCreateLog> updatePackagesLog(@Header("x-key") String strCompanyID, @Header("x-access-token") String strAccessToken, @Path("log_id") String strLogID, @FieldMap Map<String, String> mHashMap);

    @FormUrlEncoded
    @PUT(TagValues.URL_SCAN_SHIPPER_CODE)
    Observable<VoResponceScanShipperCode> scanShipperQrCode(@Header("x-key") String strCompanyID, @Header("x-access-token") String strAccessToken, @Path("qr_code") String strQrCode, @FieldMap Map<String, String> mHashMap);

   /* @GET(TagValues.URL_FETCH_BATCH_HISTORY)
    Observable<VoResponceHistory> getScannedHistory(@Header("x-key") String strCompanyID, @Header("x-access-token") String strAccessToken,
                                                    @Path("user_id") String strUserID);*/
   @GET(TagValues.URL_FETCH_BATCH_HISTORY)
   Observable<VoResponceHistory> getScannedHistory(@Header("x-key") String strCompanyID, @Header("x-access-token") String strAccessToken,
                                                   @Query("productId") String productId, @Query("batchId") String batchId, @Query("startDate") String startDate,
                                                   @Query("endDate") String endDate);

//   /{productId}/{batchId}/{startDate}/{endDate}


    @GET(TagValues.URL_FETCH_SKU)
    Observable<VoResponseFetchSKU> fetchSkuList(@Header("x-key") String strCompanyID, @Header("x-access-token") String strAccessToken);

    @GET(TagValues.URL_DISCARD_SHIPPER)
    Observable<VoResponseDiscardShipper> discardShipper(@Header("x-key") String strKey,
                                                        @Header("x-access-token") String strAccessToken,
                                                        @Path("log_id") String strLogId);

    //First Level Scan
    @Headers("Content-Type: application/json")
    @POST(TagValues.URL_DISCARD_SHIPPER)
    Observable<VoResponseDiscardShipper> discardShipper(@Header("X-key") String mStringXKey,@Header("x-access-token") String mStringXAccessToken,
                                                                @Body RequestBody mRequestBody);

    //QA
    @Headers("Content-Type: application/json")
    @POST(TagValues.URL_SCAN_FOR_REPRINT)
    Observable<VoResponseScanForReprint> scanForReprint(@Header("x-access-token") String mStringXAccessToken,
                                                        @Header("X-key") String mStringXKey,
                                                        @Body RequestBody mRequestBody);

    @GET(TagValues.URL_SCAN_FOR_REMAP)
    Observable<VoResponseScanForRemap> scanForRemap(@Header("x-access-token") String mStringXAccessToken,
                                                    @Header("X-key") String mStringXKey,
                                                    @Path("shipper_code") String mStringShipperCode);

    @Headers("Content-Type: application/json")
    @PUT(TagValues.URL_FREE_MAPPING)
    Observable<VoResponseFreeMapping> freeMapping(@Header("x-access-token") String mStringXAccessToken,
                                                  @Header("X-key") String mStringXKey,
                                                  @Body RequestBody mRequestBody);

    @Headers("Content-Type: application/json")
    @POST(TagValues.URL_REPRINT_SEND_MAIL)
    Observable<VoResponseReprintSendMail> reprintSendMail(@Header("x-access-token") String mStringXAccessToken,
                                                          @Header("X-key") String mStringXKey,
                                                          @Body RequestBody mRequestBody);


    //First Level Scan
    @Headers("Content-Type: application/json")
    @POST(TagValues.URL_FIRST_LEVEL_SCAN)
    Observable<VoResponseScanFirstLevel> scanFirstLevelQrCode(@Header("X-key") String mStringXKey,@Header("x-access-token") String mStringXAccessToken,

                                                              @Body RequestBody mRequestBody);

    //First Level Scan
    @Headers("Content-Type: application/json")
    @POST(TagValues.URL_SECOND_LEVEL_SCAN)
    Observable<VoResponseScanSecondLevel> scanSecondLevelQrCode(@Header("X-key") String mStringXKey,@Header("x-access-token") String mStringXAccessToken,
                                                                          @Body RequestBody mRequestBody);

    //inline
    @GET(TagValues.URL_FETCH_BATCHES_INLINE)
    Observable<VoResponceGetBatches> getBatchesInLine(@Header("x-key") String strCompanyID, @Header("x-access-token") String strAccessToken, @Path("sku_id") String skuID);

    //First Level Scan InLine
    @Headers("Content-Type: application/json")
    @POST(TagValues.URL_FIRST_LEVEL_SCAN_INLINE)
    Observable<VoResponseScanFirstLevel> scanFirstLevelQrCodeInLine(@Header("X-key") String mStringXKey,@Header("x-access-token") String mStringXAccessToken,
                                                              @Body RequestBody mRequestBody);

    //Second Level Scan InLine
    @Headers("Content-Type: application/json")
    @POST(TagValues.URL_SECOND_LEVEL_SCAN_INLINE)
    Observable<VoResponseScanSecondLevel> scanSecondLevelQrCodeInLine(@Header("X-key") String mStringXKey,@Header("x-access-token") String mStringXAccessToken,
                                                                @Body RequestBody mRequestBody);

    //Scan shipper inline
    @FormUrlEncoded
    @PUT(TagValues.URL_SCAN_SHIPPER_CODE_INLINE)
    Observable<VoResponceScanShipperCode> scanShipperQrCodeInLine(@Header("x-key") String strCompanyID, @Header("x-access-token") String strAccessToken, @Path("qr_code") String strQrCode, @FieldMap Map<String, String> mHashMap);


    @GET(TagValues.URL_CHECK_INCOMPLETE_SHIPPER_IN_LINE)
    Observable<VoResponseCheckInCompleteShipper> checkInCompleteShipperInLine(@Header("x-key") String strCompanyID, @Header("x-access-token") String strAccessToken, @Path("user_id") String strUserID);

}