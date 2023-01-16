package com.gsp.trusttags.mobile.mapping.services;

import android.os.AsyncTask;

import com.gsp.trusttags.mobile.mapping.MyApplication;
import com.gsp.trusttags.mobile.mapping.R;
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

import org.json.JSONObject;

import java.net.Authenticator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by oneclickpc001 on 11/4/18.
 */

public class MyService {

    private final API mApiService;

    public MyService(API apiService) {
        this.mApiService = apiService;
    }

    public void login(Map<String, String> hasMap, final ServiceCallback<VoResponceLogin> mServiceCallback) {

        mApiService.login(hasMap)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponceLogin>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(final VoResponceLogin serializables) {
                        mServiceCallback.onSuccess(serializables);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(e);

//                        sendMail(TagValues.CALL_POST,
//                                TagValues.MainURL + TagValues.URL_LOGIN,
//                                hasMap.toString(),
//                                "",
//                                "",
//                                new Exception(e).toString());

                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    public void forgotPassword(Map<String, String> hasMap, final ServiceCallback<VoResponseForgotPassword> mServiceCallback) {

        mApiService.forgotPassword(hasMap)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponseForgotPassword>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(final VoResponseForgotPassword serializables) {
                        mServiceCallback.onSuccess(serializables);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(e);

//                        sendMail(TagValues.CALL_POST,
//                                TagValues.MainURL + TagValues.URL_FORGOT_PASSWORD,
//                                hasMap.toString(),
//                                "",
//                                "",
//                                new Exception(e).toString());

                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    public void getSkus(String strUserID, String strAccessToken, final ServiceCallback<VoResponseFetchSKU> mServiceCallback) {

        mApiService.fetchSkuList(strUserID, strAccessToken)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponseFetchSKU>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VoResponseFetchSKU voResponseFetchSKU) {
                        mServiceCallback.onSuccess(voResponseFetchSKU);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(e);

//                        sendMail(TagValues.CALL_GET,
//                                TagValues.MainURL + TagValues.URL_FETCH_SKU,
//                                "",
//                                strAccessToken,
//                                strUserID,
//                                new Exception(e).toString());

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void getBatches(String strUserID, String strAccessToken, String strSkuID, final ServiceCallback<VoResponceGetBatches> mServiceCallback,boolean isInLine) {

        Observable<VoResponceGetBatches> observer ;
        if(!isInLine)
            observer = mApiService.getBatches(strUserID, strAccessToken, strSkuID);
        else
            observer = mApiService.getBatchesInLine(strUserID, strAccessToken, strSkuID);

        observer.timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponceGetBatches>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(final VoResponceGetBatches serializables) {
                        mServiceCallback.onSuccess(serializables);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(e);

                        String mStringServiceEndPoint = TagValues.URL_FETCH_BATCHES.replace("{sku_id}", strSkuID);

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void getBatchesWithSameSKU(String strUserID, String strAccessToken, String strUnitID, String strProductID, final ServiceCallback<VoResponceGetBatches> mServiceCallback) {
        mApiService.getBatchesWithSameSKU(strUserID, strAccessToken, strUnitID, strProductID)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponceGetBatches>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(final VoResponceGetBatches serializables) {
                        mServiceCallback.onSuccess(serializables);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(e);

                        String mStringServiceEndPoint = TagValues.URL_FETCH_BATCHES_OF_SAME_SKU.replace("{unit_id}", strUnitID);
                        mStringServiceEndPoint = mStringServiceEndPoint.replace("{product_id}", strProductID);

//                        sendMail(TagValues.CALL_GET,
//                                TagValues.MainURL + mStringServiceEndPoint,
//                                "",
//                                strAccessToken,
//                                strUserID,
//                                new Exception(e).toString());

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void createPackagesLog(String strUserID, String strAccessToken, Map<String, String> hashMap, final ServiceCallback<VoResponceCreateLog> mServiceCallback) {

        mApiService.createPackagesLog(strUserID, strAccessToken, hashMap)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponceCreateLog>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(final VoResponceCreateLog serializables) {
                        mServiceCallback.onSuccess(serializables);
                    }


                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(e);

//                        sendMail(TagValues.CALL_POST,
//                                TagValues.MainURL + TagValues.URL_CREATE_PACKAGE_SCAN_LOG,
//                                hashMap.toString(),
//                                strAccessToken,
//                                strUserID,
//                                new Exception(e).toString());

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void scanPackageQrcode(String strUserID, String strAccessToken, String strQRCode, Map<String, String> hashMap, final ServiceCallback<VoResponceScanPackageQrCode> mServiceCallback) {

        mApiService.scanPackageQrCode(strUserID, strAccessToken, strQRCode, hashMap)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponceScanPackageQrCode>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(final VoResponceScanPackageQrCode serializables) {
                        mServiceCallback.onSuccess(serializables);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(e);

                        //String mStringServiceEndPoint = TagValues.URL_SCAN_QR_CODE.replace("{scanQrCode}", strQRCode);

//                        sendMail(TagValues.CALL_PUT,
//                                TagValues.MainURL + mStringServiceEndPoint,
//                                hashMap.toString(),
//                                strAccessToken,
//                                strUserID,
//                                new Exception(e).toString());

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void scanAndDeletePackageQrCode(String strUserID, String strAccessToken, String strQrCode, String strLogId, final ServiceCallback<VoResponceScanPackageQrCode> mServiceCallback) {

        mApiService.scanAndRemovePackageQrCode(strUserID, strAccessToken, strQrCode, strLogId)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponceScanPackageQrCode>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(final VoResponceScanPackageQrCode serializables) {
                        mServiceCallback.onSuccess(serializables);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(e);

                        String mStringServiceEndPoint = TagValues.URL_UNSCAN_QR_CODE.replace("{scanQrCode}", strQrCode);

//                        sendMail(TagValues.CALL_PUT,
//                                TagValues.MainURL + mStringServiceEndPoint,
//                                "",
//                                strAccessToken,
//                                strUserID,
//                                new Exception(e).toString());

                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    public void checkIncompleteShipper(String strUserID, String strAccessToken, final ServiceCallback<VoResponseCheckInCompleteShipper> mServiceCallback) {

        mApiService.checkInCompleteShipper(strUserID, strAccessToken, strUserID)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponseCheckInCompleteShipper>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(final VoResponseCheckInCompleteShipper serializables) {
                        mServiceCallback.onSuccess(serializables);
                    }


                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(e);

                        String mStringServiceEndPoint = TagValues.URL_CHECK_INCOMPLETE_SHIPPER.replace("{user_id}", strUserID);

//                        sendMail(TagValues.CALL_GET,
//                                TagValues.MainURL + mStringServiceEndPoint,
//                                "",
//                                strAccessToken,
//                                strUserID,
//                                new Exception(e).toString());

                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    public void checkIncompleteShipperInLine(String strUserID, String strAccessToken, final ServiceCallback<VoResponseCheckInCompleteShipper> mServiceCallback) {

        mApiService.checkInCompleteShipperInLine(strUserID, strAccessToken, strUserID)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponseCheckInCompleteShipper>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(final VoResponseCheckInCompleteShipper serializables) {
                        mServiceCallback.onSuccess(serializables);
                    }


                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(e);

                        String mStringServiceEndPoint = TagValues.URL_CHECK_INCOMPLETE_SHIPPER.replace("{user_id}", strUserID);

//                        sendMail(TagValues.CALL_GET,
//                                TagValues.MainURL + mStringServiceEndPoint,
//                                "",
//                                strAccessToken,
//                                strUserID,
//                                new Exception(e).toString());

                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    public void updateLogData(String strUserID, String strAccessToken, String strLogID, Map<String, String> hashMap, final ServiceCallback<VoResponceCreateLog> mServiceCallback) {
        mApiService.updatePackagesLog(strUserID, strAccessToken, strLogID, hashMap)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponceCreateLog>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(final VoResponceCreateLog serializables) {
                        mServiceCallback.onSuccess(serializables);
                    }


                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(e);

                        String mStringServiceEndPoint = TagValues.URL_UPDATE_PACKAGE_SCAN_LOG.replace("{log_id}", strLogID);

//                        sendMail(TagValues.CALL_PUT,
//                                TagValues.MainURL + mStringServiceEndPoint,
//                                hashMap.toString(),
//                                strAccessToken,
//                                strUserID,
//                                new Exception(e).toString());

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void scanShipperQrCode(String strUserID, String strAccessToken, String strQrCode, Map<String, String> hashMap, final ServiceCallback<VoResponceScanShipperCode> mServiceCallback) {

        mApiService.scanShipperQrCode(strUserID, strAccessToken, strQrCode, hashMap)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponceScanShipperCode>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(final VoResponceScanShipperCode serializables) {
                        mServiceCallback.onSuccess(serializables);
                    }


                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(e);

                        //String mStringServiceEndPoint = TagValues.URL_SCAN_SHIPPER_CODE.replace("{qr_code}", strQrCode);

//                        sendMail(TagValues.CALL_PUT,
//                                TagValues.MainURL + mStringServiceEndPoint,
//                                hashMap.toString(),
//                                strAccessToken,
//                                strUserID,
//                                new Exception(e).toString());

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void scanShipperQrCodeInLine(String strUserID, String strAccessToken, String strQrCode, Map<String, String> hashMap, final ServiceCallback<VoResponceScanShipperCode> mServiceCallback) {

        mApiService.scanShipperQrCodeInLine(strUserID, strAccessToken, strQrCode, hashMap)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponceScanShipperCode>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(final VoResponceScanShipperCode serializables) {
                        mServiceCallback.onSuccess(serializables);
                    }


                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(e);

                        //String mStringServiceEndPoint = TagValues.URL_SCAN_SHIPPER_CODE.replace("{qr_code}", strQrCode);

//                        sendMail(TagValues.CALL_PUT,
//                                TagValues.MainURL + mStringServiceEndPoint,
//                                hashMap.toString(),
//                                strAccessToken,
//                                strUserID,
//                                new Exception(e).toString());

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    public void getFilterScannnedHistoryData(String strUserID, String strAccessToken,String productId,String batchId,String startDate,String endDate,
                                     final ServiceCallback<VoResponceHistory> mServiceCallback) {

        mApiService.getScannedHistory(strUserID, strAccessToken, productId,batchId,startDate,endDate)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponceHistory>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(final VoResponceHistory serializables) {
                        mServiceCallback.onSuccess(serializables);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(e);

                        String mStringServiceEndPoint = TagValues.URL_FETCH_BATCH_HISTORY.replace("{user_id}", strUserID);

//                        sendMail(TagValues.CALL_GET,
//                                TagValues.MainURL + mStringServiceEndPoint,
//                                "",
//                                strAccessToken,
//                                strUserID,
//                                new Exception(e).toString());

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void discardShipper(String strUserID, String strAccessToken,RequestBody requestBody, final ServiceCallback<VoResponseDiscardShipper> mServiceCallback) {
        mApiService.discardShipper(strUserID, strAccessToken, requestBody)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponseDiscardShipper>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VoResponseDiscardShipper voResponseDiscardShipper) {
                        mServiceCallback.onSuccess(voResponseDiscardShipper);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(e);

//                        String mStringServiceEndPoint = TagValues.URL_DISCARD_SHIPPER.replace("{log_id}", strLogId);

//                        sendMail(TagValues.CALL_GET,
//                                TagValues.MainURL + mStringServiceEndPoint,
//                                "",
//                                strAccessToken,
//                                strUserID,
//                                new Exception(e).toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //QA
    public void scanForReprint(String mStringAccessToken, String mStringKey, JSONObject mJsonObjectData, final ServiceCallback<VoResponseScanForReprint> mServiceCallback) {
        RequestBody mRequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mJsonObjectData.toString());

        mApiService.scanForReprint(mStringAccessToken, mStringKey, mRequestBody)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponseScanForReprint>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VoResponseScanForReprint voResponseScanForReprint) {
                        mServiceCallback.onSuccess(voResponseScanForReprint);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(new Exception(e));

//                        sendMail(TagValues.CALL_POST,
//                                TagValues.MainURL + TagValues.URL_SCAN_FOR_REPRINT,
//                                mJsonObjectData.toString(),
//                                mStringAccessToken,
//                                mStringKey,
//                                new Exception(e).toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void reprintSendMail(String mStringAccessToken, String mStringKey, JSONObject mJsonObjectData, final ServiceCallback<VoResponseReprintSendMail> mServiceCallback) {
        RequestBody mRequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mJsonObjectData.toString());

        mApiService.reprintSendMail(mStringAccessToken, mStringKey, mRequestBody)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponseReprintSendMail>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VoResponseReprintSendMail voResponseReprintSendMail) {
                        mServiceCallback.onSuccess(voResponseReprintSendMail);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(new Exception(e));

//                        sendMail(TagValues.CALL_POST,
//                                TagValues.MainURL + TagValues.URL_REPRINT_SEND_MAIL,
//                                mJsonObjectData.toString(),
//                                mStringAccessToken,
//                                mStringKey,
//                                new Exception(e).toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void scanForRemap(String mStringAccessToken, String mStringKey, String mStringShipperCode, final ServiceCallback<VoResponseScanForRemap> mServiceCallback) {
        mApiService.scanForRemap(mStringAccessToken, mStringKey, mStringShipperCode)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponseScanForRemap>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VoResponseScanForRemap voResponseScanForRemap) {
                        mServiceCallback.onSuccess(voResponseScanForRemap);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(new Exception(e));

                        String mStringServiceEndPoint = TagValues.URL_SCAN_FOR_REMAP.replace("{shipper_code}", mStringShipperCode);

//                        sendMail(TagValues.CALL_GET,
//                                TagValues.MainURL + mStringServiceEndPoint,
//                                "",
//                                mStringAccessToken,
//                                mStringKey,
//                                new Exception(e).toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void freeMapping(String mStringAccessToken, String mStringKey, JSONObject mJsonObjectData, final ServiceCallback<VoResponseFreeMapping> mServiceCallback) {
        RequestBody mRequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mJsonObjectData.toString());

        mApiService.freeMapping(mStringAccessToken, mStringKey, mRequestBody)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponseFreeMapping>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VoResponseFreeMapping voResponseFreeMapping) {
                        mServiceCallback.onSuccess(voResponseFreeMapping);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(new Exception(e));

//                        sendMail(TagValues.CALL_PUT,
//                                TagValues.MainURL + TagValues.URL_FREE_MAPPING,
//                                mJsonObjectData.toString(),
//                                mStringAccessToken,
//                                mStringKey,
//                                new Exception(e).toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void sendMail(String mStringCallType,
                          String mStringURL,
                          String mStringData,
                          String mStringAccessToken,
                          String mStringKey,
                          String mStringError) {

        class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

            @Override
            protected Void doInBackground(String... strings) {
                final String username = "darshan@trusttags.in";
                final String password = "Trusttags1@";

                String mStringMailBody = "Dear Developer,\n\nAPIs Are Not Working for "
                        + MyApplication.getContext().getResources().getString(R.string.app_name) + " Project"
                        + "\n\nURL : " + mStringURL;

                if (mStringCallType != null && !mStringCallType.equalsIgnoreCase("")) {
                    mStringMailBody = mStringMailBody
                            + "\n\nCall Type : " + mStringCallType;
                }

                if (mStringAccessToken != null && !mStringAccessToken.equalsIgnoreCase("")) {
                    mStringMailBody = mStringMailBody
                            + "\n\nAccess Token : " + mStringAccessToken;
                }

                if (mStringKey != null && !mStringKey.equalsIgnoreCase("")) {
                    mStringMailBody = mStringMailBody
                            + "\n\nXKey : " + mStringKey;
                }

                if (mStringData != null && !mStringData.equalsIgnoreCase("")) {
                    mStringMailBody = mStringMailBody
                            + "\n\nData : " + mStringData;
                }

                if (MyApplication.getVersionNumber() != null && !MyApplication.getVersionNumber().equalsIgnoreCase("")) {
                    mStringMailBody = mStringMailBody
                            + "\n\nApp Version : " + MyApplication.getVersionNumber();
                }

                mStringMailBody = mStringMailBody
                        + "\n\nError : " + mStringError;

                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.trusttags.in");
                props.put("mail.smtp.port", "587");

                Session session = Session.getInstance(props,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });

                try {

                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("darshan@trusttags.in"));

                    String mStringAddress = "darshan@itoneclick.com,umang@trusttags.in,pratik@trusttags.in";
                    InternetAddress[] mInternetAddresses = InternetAddress.parse(mStringAddress);
                    message.setRecipients(Message.RecipientType.CC, mInternetAddresses);

                    message.setSubject(MyApplication.getContext().getResources().getString(R.string.app_name) + " API Not Working");

                    message.setText(mStringMailBody);

                    Transport.send(message);

                    System.out.println("Darshan Error Mail Done : " + mStringMailBody);
                    System.out.println("Darshan Error Mail Subject : " + message.getSubject());

                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        }

        new RetrieveFeedTask().execute();

    }

    //FirstLevel
    public void scanFirstLevelQrCode(String strUserID, String strAccessToken, JSONObject mJsonObjectData, final ServiceCallback<VoResponseScanFirstLevel> mServiceCallback) {


        RequestBody mRequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mJsonObjectData.toString());

        mApiService.scanFirstLevelQrCode(strUserID, strAccessToken, mRequestBody)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponseScanFirstLevel>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(final VoResponseScanFirstLevel serializables) {
                        mServiceCallback.onSuccess(serializables);
                    }
                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(e);
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }

    //FirstLevel
    public void scanFirstLevelQrCodeInLine(String strUserID, String strAccessToken, JSONObject mJsonObjectData, final ServiceCallback<VoResponseScanFirstLevel> mServiceCallback) {


        RequestBody mRequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mJsonObjectData.toString());

        mApiService.scanFirstLevelQrCodeInLine(strUserID, strAccessToken, mRequestBody)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponseScanFirstLevel>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(final VoResponseScanFirstLevel serializables) {
                        mServiceCallback.onSuccess(serializables);
                    }
                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(e);
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }

    //SecondLevel
    public void scanSecondLevelQrCode(String strUserID, String strAccessToken, JSONObject mJsonObjectData, final ServiceCallback<VoResponseScanSecondLevel> mServiceCallback) {


        RequestBody mRequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mJsonObjectData.toString());

        mApiService.scanSecondLevelQrCode(strUserID, strAccessToken, mRequestBody)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponseScanSecondLevel>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(final VoResponseScanSecondLevel serializables) {
                        mServiceCallback.onSuccess(serializables);
                    }


                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(e);

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    //SecondLevel
    public void scanSecondLevelQrCodeInLine(String strUserID, String strAccessToken, JSONObject mJsonObjectData, final ServiceCallback<VoResponseScanSecondLevel> mServiceCallback) {


        RequestBody mRequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mJsonObjectData.toString());

        mApiService.scanSecondLevelQrCodeInLine(strUserID, strAccessToken, mRequestBody)
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(data -> Observable.just(data))
                .filter(data -> data != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<VoResponseScanSecondLevel>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(final VoResponseScanSecondLevel serializables) {
                        mServiceCallback.onSuccess(serializables);
                    }


                    @Override
                    public void onError(Throwable e) {
                        mServiceCallback.onError(e);

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    public interface ServiceCallback<T> {
        void onSuccess(T data);

        void onError(Throwable networkError);
    }
}
