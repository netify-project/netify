package pl.edu.ug.aib.netify.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.data.GroupData;
import pl.edu.ug.aib.netify.data.InviteData;
import pl.edu.ug.aib.netify.data.User;

@EFragment(R.layout.fragment_profile)
public class ProfileFragment extends Fragment {

    @ViewById
    TextView firstNameField;
    @ViewById
    TextView lastNameField;
    @ViewById
    TextView emailField;
    @ViewById
    Button sendNewInviteButton;

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

    @Click
    void sendNewInviteButtonClicked(){
        //Create new InviteData object and pass it to activity
        InviteData inviteData = new InviteData();
        inviteData.groupId = null;
        inviteData.toUser = user.id.toString();
        listener.sendNewInvite(inviteData);
        sendNewInviteButton.setVisibility(View.GONE);
    }


    public interface OnProfileFragmentCommunicationListener{
        User getUser();
        void sendNewInvite(InviteData inviteData);
    }

}
