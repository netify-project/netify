package pl.edu.ug.aib.netify.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

import org.androidannotations.annotations.EFragment;

import pl.edu.ug.aib.netify.R;

@EFragment(R.layout.fragment_progressbar)
public class LogoutFragment extends Fragment {

    OnLogoutFragmentCommunicationListener listener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            listener = (OnLogoutFragmentCommunicationListener) activity;
            listener.logout();
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnLogoutFragmentCommunicationListener");
        }
    }

    public interface OnLogoutFragmentCommunicationListener{
        public void logout();
    }
}
