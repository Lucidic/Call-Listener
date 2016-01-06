package trevathan.com.calllistener.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.OnClick;
import trevathan.com.calllistener.R;
import trevathan.com.calllistener.utility.Constants;

/**
 * Created by dnorvell on 1/3/16.
 */
public class SettingsFragment extends BaseFragment {

    @Bind(R.id.etBaseUrl)
    EditText mEtBaseUrl;

    @Bind(R.id.etCallStartEndpoint)
    EditText mEtCallStartEndpoint;

    @Bind(R.id.etCallEndEndpoint)
    EditText mEtCallEndEndpoint;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initUi(View _rootView, Bundle _savedInstanceState) {
        super.initUi(_rootView, _savedInstanceState);
        loadSettings();
    }

    @OnClick(R.id.btn_save_settings)
    void onSaveClick() {

        String baseUrl = mEtBaseUrl.getText().toString();
        String callStartEndpoint = mEtCallStartEndpoint.getText().toString();
        String callEndEndpoint = mEtCallEndEndpoint.getText().toString();

        boolean cancel = false;

        if(TextUtils.isEmpty(baseUrl)) {
            mEtBaseUrl.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        if(TextUtils.isEmpty(callStartEndpoint)) {
            mEtCallStartEndpoint.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        if(TextUtils.isEmpty(callEndEndpoint)) {
            mEtCallEndEndpoint.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        if (!cancel) {
            saveSettings(baseUrl, callStartEndpoint, callEndEndpoint);
        }

    }

    private void loadSettings() {
        SharedPreferences preferences = getActivity()
                .getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        String saved_base_url = preferences.getString(Constants.PREFERENCES_BASE_URL, "");
        String saved_call_start_endpoint = preferences.getString(Constants.PREFERENCES_CALL_START_ENDPOINT, "");
        String saved_call_end_endpoint= preferences.getString(Constants.PREFERENCES_CALL_END_ENDPOINT, "");

        if(!TextUtils.isEmpty(saved_base_url)) {
            mEtBaseUrl.setText(saved_base_url);
        }

        if(!TextUtils.isEmpty(saved_call_start_endpoint)) {
            mEtCallStartEndpoint.setText(saved_call_start_endpoint);
        }

        if(!TextUtils.isEmpty(saved_call_end_endpoint)) {
            mEtCallEndEndpoint.setText(saved_call_end_endpoint);
        }

    }

    private void saveSettings(String _baseUrl, String _callStartEndpoint, String _callEndEndpoint) {
        SharedPreferences preferences = getActivity()
                .getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.PREFERENCES_BASE_URL, _baseUrl).apply();
        editor.putString(Constants.PREFERENCES_CALL_START_ENDPOINT, _callStartEndpoint).apply();
        editor.putString(Constants.PREFERENCES_CALL_END_ENDPOINT, _callEndEndpoint).apply();

        Toast.makeText(getActivity(), "Settings Saved!", Toast.LENGTH_SHORT).show();

        getActivity().finish();
    }

}
