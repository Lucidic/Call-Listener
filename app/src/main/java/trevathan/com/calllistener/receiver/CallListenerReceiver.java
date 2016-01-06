package trevathan.com.calllistener.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;


public class CallListenerReceiver extends BroadcastReceiver {

    public final String TAG = getClass().getSimpleName();

    private ICallListener mListener;

    public interface ICallListener {
        void onCallReceived();
        void onCallEnded();
    }

    private TelephonyManager mTelephonyManager;
    private PhoneStateListener mPhoneStateListener;

    public CallListenerReceiver(ICallListener _callListener) {
        mListener = _callListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if(mPhoneStateListener == null) {
            mTelephonyManager.listen(getPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
        }

    }

    public PhoneStateListener getPhoneStateListener() {
        if(mPhoneStateListener == null) {
            mPhoneStateListener = new PhoneStateListener() {
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                    switch (state) {

                        //Incoming call
                        case TelephonyManager.CALL_STATE_OFFHOOK:
                            Log.d(TAG, "Call answered from " + incomingNumber);
                            mListener.onCallReceived();
                            break;

                        //Call ended
                        case TelephonyManager.CALL_STATE_IDLE:
                            Log.d(TAG, " Call with " + incomingNumber + " ended");
                            mListener.onCallEnded();
                            break;
                    }

                }
            };
        }
        return mPhoneStateListener;
    }

    public void stopListening() {
        mTelephonyManager.listen(getPhoneStateListener(), PhoneStateListener.LISTEN_NONE);
    }

    public void setCallListener(ICallListener _callListener) {
        mListener = _callListener;
    }

}
