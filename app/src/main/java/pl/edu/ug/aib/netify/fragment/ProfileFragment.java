package pl.edu.ug.aib.netify.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.data.User;

@EFragment(R.layout.fragment_profile)
public class ProfileFragment extends Fragment {

    @ViewById
    TextView firstNameField;
    @ViewById
    TextView lastNameField;
    @ViewById
    TextView emailField;

    User user;

    OnProfileFragmentCommunicationListener listener;

    @AfterViews
    void init(){
        user = listener.getUser();
        firstNameField.setText(user.firstName);
        lastNameField.setText(user.lastName);
        emailField.setText(user.email);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            listener = (OnProfileFragmentCommunicationListener)activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnProfileFragmentCommunicationListener");
        }
    }

    public interface OnProfileFragmentCommunicationListener{
        User getUser();
    }

}
