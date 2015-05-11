package pl.edu.ug.aib.netify.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import pl.edu.ug.aib.netify.R;

@EFragment(R.layout.fragment_intro)
public class IntroFragment extends Fragment {

    OnIntroFragmentCommunicationListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            listener = (OnIntroFragmentCommunicationListener) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnIntroFragmentCommunicationListener");
        }
    }

    @Click
    void loginButtonClicked(){
        //Toast.makeText(getActivity(), "Login", Toast.LENGTH_SHORT).show();
        listener.onLoginClicked();
    }
    @Click
    void registerButtonClicked(){
        //Toast.makeText(getActivity(), "Register", Toast.LENGTH_SHORT).show();
        listener.onRegisterClicked();
    }

    public interface OnIntroFragmentCommunicationListener {
        public void onLoginClicked();
        public void onRegisterClicked();
    }
}
