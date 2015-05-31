package pl.edu.ug.aib.netify.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
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
    Button unfriendButton;
    @ViewById
    TextView alreadyInvitedInfo;
    @ViewById
    TextView hasInvitedYouInfo;
    @ViewById
    LinearLayout userSongsLayout;
    @ViewById
    LinearLayout buttonsLayout;
    @ViewById
    ListView userSongsList;
    @ViewById
    ProgressBar progressBar;
    @ViewById
    TextView noSongInfo;
    User user;
    @Bean
    PlayerPropositionAdapter adapter;
    SongDataList userSong;
    //Fields indicating what buttons should be displayed
    Boolean isAlreadyInvited;
    Boolean hasInvitedYou;
    Boolean isAlreadyFriend;
    String friendDataId;
    OnProfileFragmentCommunicationListener listener;

    @AfterViews
    void init(){
        userSongsList.setAdapter(adapter);
        if(userSong!= null) adapter.update(userSong.records);
        user = listener.getUser();
        firstNameField.setText(String.format("%s: %s", getString(R.string.first_name), user.firstName));
        lastNameField.setText(String.format("%s: %s", getString(R.string.last_name), user.lastName));
        emailField.setText(String.format("%s: %s", getString(R.string.email), user.email));
        updateLayoutVisibility();
    }

    public void setUserSongsList(SongDataList userSong) {
        this.userSong = userSong;
        adapter.update(userSong.records);
        //update if layout is already loaded
        if(userSongsLayout!= null) updateLayoutVisibility();
    }
    public void setRelationshipFields(Boolean isAlreadyInvited,  Boolean hasInvitedYou, Boolean isAlreadyFriend, String friendDataId){
        this.isAlreadyInvited = isAlreadyInvited;
        this.hasInvitedYou = hasInvitedYou;
        this.isAlreadyFriend = isAlreadyFriend;
        this.friendDataId = friendDataId;
        //update if layout is already loaded
        if(userSongsLayout!= null) updateLayoutVisibility();
    }
    public void friendDataRemoved(){
        isAlreadyFriend = false; friendDataId = null;
        updateLayoutVisibility();
    }
    public void newInviteSent(){
        isAlreadyInvited = true;
        updateLayoutVisibility();
    }

    private void updateLayoutVisibility(){
        //return if data is not yet present
        if(userSong == null || isAlreadyInvited == null) return;
        progressBar.setVisibility(View.GONE);
        userSongsLayout.setVisibility(View.VISIBLE);
        if(adapter.getCount() == 0) noSongInfo.setVisibility(View.VISIBLE);
        else updateListViewHeight();
        //if user displays own profile, no buttons are visible
        if(user.id != listener.getCurrentUserId()) buttonsLayout.setVisibility(View.VISIBLE);
        //show buttons according to users' relationship
        if(isAlreadyFriend) unfriendButton.setVisibility(View.VISIBLE);
        else if(isAlreadyInvited) alreadyInvitedInfo.setVisibility(View.VISIBLE);
        else if(hasInvitedYou) hasInvitedYouInfo.setVisibility(View.VISIBLE);
        else sendNewInviteButton.setVisibility(View.VISIBLE);
    }
    private void updateListViewHeight(){
        //adjust listview height since in won't adjust automatically with scrollview as parent
        ViewGroup.LayoutParams memberListParams = userSongsList.getLayoutParams();
        int itemHeight = (int)getResources().getDimension(R.dimen.player_proposition_item_height);
        memberListParams.height = (itemHeight+2) * adapter.getCount();
        userSongsList.setLayoutParams(memberListParams);
    }
    private void showProgressBar(){
        sendNewInviteButton.setVisibility(View.GONE);
        unfriendButton.setVisibility(View.GONE);
        alreadyInvitedInfo.setVisibility(View.GONE);
        hasInvitedYouInfo.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
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
        showProgressBar();
    }
    @Click
    void unfriendButtonClicked(){
        if(friendDataId == null) return;
        listener.destroyFriendship(friendDataId);
        showProgressBar();
    }


    public interface OnProfileFragmentCommunicationListener{
        public int getCurrentUserId();
        public User getUser();
        public void sendNewInvite(InviteData inviteData);
        public void getUserSongs();
        public void destroyFriendship(String friendDataId);
    }

}
