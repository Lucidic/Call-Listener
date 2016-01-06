package trevathan.com.calllistener.fragment;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Base fragment responsible for interfacing with Dagger and {@link ButterKnife}. All fragments
 * within the application should extend from this one. Doing so will ensure that they are setup for dependency injection from Dagger if needed,
 * and also sets them up to manage the Butterknife life cycle.
 */
public abstract class BaseFragment extends DialogFragment { //By extending DialogFragment instead of Fragment we get the option to display normally or as a dialog. No drawbacks that I can think of?

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, v);

        initUi(v, savedInstanceState);

        return v;
    }

    /**
     * Fragments extending this one who wish to modify the UI after all views have been bound may do so here.
     * This can sometimes be useful to ensure certain operations occur AFTER all {@link ButterKnife} elements have been bound
     * through {@link ButterKnife#bind(Object, View)}.
     */
    protected void initUi(View _rootView, Bundle _savedInstanceState) {

    }

    /**
     * Gets the layout id that should be inflated for this fragment.
     * @return The layout id used for this fragment.
     */
    protected abstract int getLayoutId();

    /**
     * Sets the action bar title to the specified string provided the actionbar exists.
     * @param _string the string to display as the aciton bar's title.
     */
    protected void setActionBarTitle(String _string) {
        if(getActivity().getActionBar() != null) {
            getActivity().getActionBar().setTitle(_string);
        }
    }

    /**
     * Sets the action bar icon to the specified resource
     * @param _resourceId the resource id of the icon
     */
    protected void setActionBarIcon(int _resourceId) {
        if(getActivity().getActionBar() != null) {
            getActivity().getActionBar().setIcon(_resourceId);
        }
    }

    /**
     * Checks if this fragment has a bundle.
     * @return True if it does, False otherwise.
     */
    protected boolean checkBundle() {
        Bundle b = getArguments();
        return b != null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
