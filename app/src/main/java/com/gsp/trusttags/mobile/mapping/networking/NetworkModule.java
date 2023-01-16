package com.gsp.trusttags.mobile.mapping.networking;

import android.content.Context;

import com.gsp.trusttags.mobile.mapping.BuildConfig;
import com.gsp.trusttags.mobile.mapping.R;
import com.gsp.trusttags.mobile.mapping.helper.TagValues;
import com.gsp.trusttags.mobile.mapping.services.API;
import com.gsp.trusttags.mobile.mapping.services.MyService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by oneclickpc001 on 11/4/18.
 */
@Module
public class NetworkModule {
    Context context;
    File cacheFile;

    public NetworkModule(File cacheFile, Context context) {
        this.cacheFile = cacheFile;
        this.context = context;
    }

    @Provides
    @Singleton
    Retrofit provideCall() {

        Cache cache = null;
        try {
            cache = new Cache(cacheFile, 10 * 1024 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        SSLContext sslContext = null;
        try {
//            sslContext = createCertificate(context.getResources().openRawResource(R.raw.gsp_ssl_live));
            /*if(BuildConfig.FLAVOR=="ww")
                sslContext = createCertificate(context.getResources().openRawResource(R.raw.ww_dev));
            else if(BuildConfig.FLAVOR=="dev" || BuildConfig.FLAVOR=="prod")
                sslContext = createCertificate(context.getResources().openRawResource(R.raw.gsp_ssl_live1));
            else if(BuildConfig.FLAVOR=="mildev")
                sslContext = createCertificate(context.getResources().openRawResource(R.raw.mil_prod));
            else
                sslContext = createCertificate(context.getResources().openRawResource(R.raw.msdev));*/

            if(BuildConfig.FLAVOR=="dev")
                sslContext = createCertificate(context.getResources().openRawResource(R.raw.gsp_ssl_live));
            else if(BuildConfig.FLAVOR=="prod")
                sslContext = createCertificate(context.getResources().openRawResource(R.raw.gsp_ssl_live));
            else if(BuildConfig.FLAVOR=="msdev")
                sslContext = createCertificate(context.getResources().openRawResource(R.raw.msdev));
            else if(BuildConfig.FLAVOR=="msprod")
                sslContext = createCertificate(context.getResources().openRawResource(R.raw.ms_prod));
            else if(BuildConfig.FLAVOR=="mildev")
                sslContext = createCertificate(context.getResources().openRawResource(R.raw.mil_prod));
            else if(BuildConfig.FLAVOR=="milprod")
                sslContext = createCertificate(context.getResources().openRawResource(R.raw.mil_prod));
            else if(BuildConfig.FLAVOR=="moldev")
                sslContext = createCertificate(context.getResources().openRawResource(R.raw.mil_prod));
            else if(BuildConfig.FLAVOR=="molprod")
                sslContext = createCertificate(context.getResources().openRawResource(R.raw.mil_prod));
            else if(BuildConfig.FLAVOR=="wwdev")
                sslContext = createCertificate(context.getResources().openRawResource(R.raw.ww_dev));
            else if(BuildConfig.FLAVOR=="wwprod")
                sslContext = createCertificate(context.getResources().openRawResource(R.raw.ww_prod));

        } catch (CertificateException | IOException | KeyStoreException | KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        OkHttpClient okHttpClient;

        if (sslContext != null) {
            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();

                            // Customize the request
                            Request.Builder request = original.newBuilder()
                                    .header("Content-Type", "application/json")
                                    .removeHeader("Pragma")
                                    .header("Cache-Control", String.format("max-age=%d", 10000));

                            okhttp3.Response response = chain.proceed(request.build());
                            return response;
                        }
                    })
                    .addInterceptor(interceptor)
                    .sslSocketFactory(sslContext.getSocketFactory(), systemDefaultTrustManager())
                    .cache(cache)
                    .build();
        } else {
            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();

                            // Customize the request
                            Request.Builder request = original.newBuilder()
                                    .header("Content-Type", "application/json")
                                    .removeHeader("Pragma")
                                    .header("Cache-Control", String.format("max-age=%d", 10000));

                            okhttp3.Response response = chain.proceed(request.build());
                            return response;
                        }
                    })
                    .addInterceptor(interceptor)
                    .cache(cache)
                    .build();
        }

        return new Retrofit.Builder()
                .baseUrl(TagValues.MainURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private static SSLContext createCertificate(InputStream trustedCertificateIS) throws CertificateException, IOException, KeyStoreException, KeyManagementException, NoSuchAlgorithmException {

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate ca;
        try {
            ca = cf.generateCertificate(trustedCertificateIS);
        } finally {
            trustedCertificateIS.close();
        }

        // creating a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // creating a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // creating an SSLSocketFactory that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        return sslContext;

    }


    private static X509TrustManager systemDefaultTrustManager() {

        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            return (X509TrustManager) trustManagers[0];
        } catch (GeneralSecurityException e) {
            throw new AssertionError(); // The system has no TLS. Just give up.
        }

    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public API providesNetworkService(Retrofit retrofit) {
        return retrofit.create(API.class);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public MyService providesService(API networkService) {
        return new MyService(networkService);
    }


}

