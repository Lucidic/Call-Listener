package trevathan.com.calllistener.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.OnClick;
import trevathan.com.calllistener.R;
import trevathan.com.calllistener.service.CallListenerService;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @OnClick(R.id.btn_start)
    void onStartClick() {
        getActivity().startService(new Intent(getActivity(), CallListenerService.class));
    }

    @OnClick(R.id.btn_stop)
    void onStopClick() {
        getActivity().stopService(new Intent(getActivity(), CallListenerService.class));
    }

}
