package trevathan.com.calllistener.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import trevathan.com.calllistener.entity.BaseResponse;
import trevathan.com.calllistener.receiver.CallListenerReceiver;
import trevathan.com.calllistener.utility.Constants;
import trevathan.com.calllistener.web.WebApi;

public class CallListenerService extends Service implements CallListenerReceiver.ICallListener{

    public final String TAG = getClass().getSimpleName();

    CallListenerReceiver mCallListenerReceiver;

    SharedPreferences mSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        Toast.makeText(getApplicationContext(), "Call listener service started.", Toast.LENGTH_LONG).show();

        mSharedPreferences = getApplicationContext()
                .getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        //set up the intent filter for the broadcast receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);

        mCallListenerReceiver = new CallListenerReceiver(this);
        registerReceiver(mCallListenerReceiver, intentFilter);
    }

    @Override
    public void onCallReceived() {
        if(checkWebConfigs()) {
            try {
                final String baseUrl = mSharedPreferences.getString(Constants.PREFERENCES_BASE_URL, "");
                final String endPoint = mSharedPreferences.getString(Constants.PREFERENCES_CALL_START_ENDPOINT, "");
                getRetrofitAdapter(baseUrl, endPoint)
                        .create(WebApi.class)
                        .callStarted()
                        .enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Response<BaseResponse> response, Retrofit retrofit) {
                                Toast.makeText(getApplicationContext(), "Successfully communicated start of call with " + baseUrl + "/" + endPoint, Toast.LENGTH_LONG).show();
                                Log.v(TAG, "Successfully communicated start of call");
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(getApplicationContext(), "Failed to communicate start of call with " + baseUrl + "/" + endPoint, Toast.LENGTH_LONG).show();
                                Log.v(TAG, "Failed to communicate start of call with " + baseUrl + "/" + endPoint);
                            }
                        });
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error making web call: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "No Url was specified, please configure your web settings!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCallEnded() {
        if(checkWebConfigs()) {
            try {
                final String baseUrl = mSharedPreferences.getString(Constants.PREFERENCES_BASE_URL, "");
                final String endPoint = mSharedPreferences.getString(Constants.PREFERENCES_CALL_END_ENDPOINT, "");
                getRetrofitAdapter(baseUrl, endPoint)
                        .create(WebApi.class)
                        .callEnded()
                        .enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Response<BaseResponse> response, Retrofit retrofit) {
                                Toast.makeText(getApplicationContext(), "Successfully communicated end of call with " + baseUrl + "/" + endPoint, Toast.LENGTH_LONG).show();
                                Log.v(TAG, "Successfully communicated end of call with " + baseUrl + "/" + endPoint);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(getApplicationContext(), "Failed to communicate end of call with " + baseUrl + "/" + endPoint, Toast.LENGTH_LONG).show();
                                Log.v(TAG, "Failed to communicate end of call with " + baseUrl + "/" + endPoint);
                            }
                        });
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error making web call: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "No URL was specified, please configure your web settings!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "Call listener service stopped.", Toast.LENGTH_LONG).show();
        mCallListenerReceiver.stopListening();
        unregisterReceiver(mCallListenerReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Retrofit getRetrofitAdapter(String _baseUrl, String _endPoint) {
        return new Retrofit.Builder()
                .baseUrl(_baseUrl + "/" + _endPoint)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private boolean checkWebConfigs() {
        return !mSharedPreferences.getString(Constants.PREFERENCES_BASE_URL, "").isEmpty() &&
            !mSharedPreferences.getString(Constants.PREFERENCES_CALL_START_ENDPOINT, "").isEmpty() &&
            !mSharedPreferences.getString(Constants.PREFERENCES_CALL_END_ENDPOINT, "").isEmpty();
    }

}
