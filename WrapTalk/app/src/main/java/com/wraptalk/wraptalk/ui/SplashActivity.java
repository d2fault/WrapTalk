package com.wraptalk.wraptalk.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.wraptalk.wraptalk.R;
import com.wraptalk.wraptalk.models.QuickstartPreferences;
import com.wraptalk.wraptalk.models.UserInfo;
import com.wraptalk.wraptalk.services.RegistrationIntentService;
import com.wraptalk.wraptalk.utils.DBManager;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    // SharedPreferences에 저장할 때 key 값으로 사용됨.
    public static final String PROPERTY_REG_ID = "registration_id";

    // SharedPreferences에 저장할 때 key 값으로 사용됨.
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String TAG = "ICELANCER";

    DBManager dbManager;

    String SENDER_ID = "100866488970";

    GoogleCloudMessaging gcm;
    Context context;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    TelephonyManager tm;

//    String regid;

    UserInfo data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        registBroadcastReceiver();
        Init();
        getInstanceIdToken();
//        if (regid.isEmpty()) {
//            registerInBackground();
//        }
//        else {
//            Log.e("register key", regid);
//            Log.i(TAG, "No valid Google Play Services APK found.");
//        }

        UserInfo.getInstance().deviceId = tm.getDeviceId();
//        UserInfo.getInstance().gcmKey = regid;

        Handler hd = new Handler();

        getInstalledApplication();

        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }




    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Instance ID를 이용하여 디바이스 토큰을 가져오는 RegistrationIntentService를 실행한다.
     */
    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    /**
     * LocalBroadcast 리시버를 정의한다. 토큰을 획득하기 위한 READY, GENERATING, COMPLETE 액션에 따라 UI에 변화를 준다.
     */
    public void registBroadcastReceiver(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();



                if(action.equals(QuickstartPreferences.REGISTRATION_READY)){
                    // 액션이 READY일 경우
                } else if(action.equals(QuickstartPreferences.REGISTRATION_GENERATING)){
                    // 액션이 GENERATING일 경우
                } else if(action.equals(QuickstartPreferences.REGISTRATION_COMPLETE)){
                    // 액션이 COMPLETE일 경우
                    String token = intent.getStringExtra("token");
                    UserInfo.getInstance().gcmKey = token;
                    Log.v("token", token);
                }
            }
        };
    }


    private void Init() {
        context = getApplicationContext();
        dbManager = new DBManager(context, "wraptalkdb.sqlite", null, 1);

        gcm = GoogleCloudMessaging.getInstance(this);
//        regid = getRegistrationId(context);

        tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
    }

//    private String getRegistrationId(Context context) {
//        final SharedPreferences prefs = getGCMPreferences(context);
//        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
//        if (registrationId.isEmpty()) {
//            Log.i(TAG, "Registration not found.");
//            return "";
//        }
//
//        // 앱이 업데이트 되었는지 확인하고, 업데이트 되었다면 기존 등록 아이디를 제거한다.
//        // 새로운 버전에서도 기존 등록 아이디가 정상적으로 동작하는지를 보장할 수 없기 때문이다.
//        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
//        int currentVersion = getAppVersion(context);
//        if (registeredVersion != currentVersion) {
//            Log.i(TAG, "App version changed.");
//            return "";
//        }
//        return registrationId;
//    }
//
//    private SharedPreferences getGCMPreferences(Context context) {
//        return getSharedPreferences(MainActivity.class.getSimpleName(),
//                Context.MODE_PRIVATE);
//    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

//    private void registerInBackground() {
//        new AsyncTask<Void, Void, String>() {
//            @Override
//            protected String doInBackground(Void... params) {
//                String msg = "";
//                try {
//                    if (gcm == null) {
//                        gcm = GoogleCloudMessaging.getInstance(context);
//                    }
//                    regid = gcm.register(SENDER_ID);
//                    UserInfo.getInstance().gcmKey = regid;
//
//                    msg = "Device registered, registration ID=" + regid;
//
//                    // 서버에 발급받은 등록 아이디를 전송한다.
//                    // 등록 아이디는 서버에서 앱에 푸쉬 메시지를 전송할 때 사용된다.
//                    sendRegistrationIdToBackend();
//
//                    // 등록 아이디를 저장해 등록 아이디를 매번 받지 않도록 한다.
//                    storeRegistrationId(context, regid);
//                } catch (IOException ex) {
//                    msg = "Error :" + ex.getMessage();
//                    // If there is an error, don't just keep trying to register.
//                    // Require the user to click a button again, or perform
//                    // exponential back-off.
//                }
//                return msg;
//            }
//
//            @Override
//            protected void onPostExecute(String msg) {
////                mDisplay.append(msg + "\n");
//            }
//
//        }.execute(null, null, null);
//    }

//    private void storeRegistrationId(Context context, String regid) {
//        final SharedPreferences prefs = getGCMPreferences(context);
//        int appVersion = getAppVersion(context);
//        Log.i(TAG, "Saving regId on app version " + appVersion);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString(PROPERTY_REG_ID, regid);
//        editor.putInt(PROPERTY_APP_VERSION, appVersion);
//        editor.commit();
//    }
//
//    private void sendRegistrationIdToBackend() {
//    }


    /**
     * 앱이 실행되어 화면에 나타날때 LocalBoardcastManager에 액션을 정의하여 등록한다.
     */
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

    }

    /**
     * 앱이 화면에서 사라지면 등록된 LocalBoardcast를 모두 삭제한다.
     */
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }




    private void getInstalledApplication() {
        PackageManager packageManager;

        packageManager = getPackageManager();
        List<PackageInfo> tempPackageList = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        String query;

        /*To filter out System apps*/
        for(PackageInfo pi : tempPackageList) {
            boolean flag = isSystemPackage(pi);
            if(!flag) {
                query = String.format("INSERT INTO app_info (app_id, app_name, user_nick, check_registration) VALUES ('%s', '%s', '%s', %d)",
                        pi.packageName, packageManager.getApplicationLabel(pi.applicationInfo).toString(), "", 0);
                try {
                    DBManager.getInstance().write(query);
                } catch (RuntimeException e) {
                }
            }
        }
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                : false;
    }
}
