package pl.edu.ug.aib.netify.fragment;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.netify.R;

@EFragment(R.layout.fragment_intro)
public class IntroFragment extends Fragment {

    @ViewById
    ImageView bgImage;

    OnIntroFragmentCommunicationListener listener;

    @AfterViews
    void init(){
        changeBgImageOrientation();
    }
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        changeBgImageOrientation();
    }

    private void changeBgImageOrientation(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            bgImage.setImageResource(R.drawable.netifygraph_land);
        }
        else bgImage.setImageResource(R.drawable.netifygraph);
    }

    @Click
    void loginButtonClicked(){
        listener.onLoginClicked();
    }
    @Click
    void registerButtonClicked(){
        listener.onRegisterClicked();
    }

    public interface OnIntroFragmentCommunicationListener {
        public void onLoginClicked();
        public void onRegisterClicked();
    }
}
