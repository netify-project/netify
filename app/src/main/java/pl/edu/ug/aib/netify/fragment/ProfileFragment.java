package pl.edu.ug.aib.netify.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.adapter.PlayerPropositionAdapter;
import pl.edu.ug.aib.netify.adapter.SongListAdapter;
import pl.edu.ug.aib.netify.data.GroupData;
import pl.edu.ug.aib.netify.data.InviteData;
import pl.edu.ug.aib.netify.data.SongData;
import pl.edu.ug.aib.netify.data.SongDataList;
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
    @ViewById
    LinearLayout userSongsLayout;
    @ViewById
    LinearLayout buttonsLayout;
    @ViewById
    ListView userSongsList;
    @ViewById
    ProgressBar progressBar;
    User user;
    @Bean
    PlayerPropositionAdapter adapter;
    SongDataList userSong;
    OnProfileFragmentCommunicationListener listener;

    @AfterViews
    void init(){
        userSongsList.setAdapter(adapter);
        user = listener.getUser();
        firstNameField.setText(String.format("%s: %s", getString(R.string.first_name), user.firstName));
        lastNameField.setText(String.format("%s: %s", getString(R.string.last_name), user.lastName));
        emailField.setText(String.format("%s: %s", getString(R.string.email), user.email));
        updateLayoutVisibility();
    }

    public void setUserSongsList(SongDataList userSong) {
        this.userSong = userSong;
        adapter.update(userSong.records);
        updateLayoutVisibility();
    }

    private void updateLayoutVisibility(){
        if(userSong == null) return;
        progressBar.setVisibility(View.GONE);
        userSongsLayout.setVisibility(View.VISIBLE);
        buttonsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            listener = (OnProfileFragmentCommunicationListener)activity;
            listener.getUserSongs();
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
        void getUserSongs();

    }

}
