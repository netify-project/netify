package pl.edu.ug.aib.netify.fragment;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.adapter.SelectUserListAdapter;
import pl.edu.ug.aib.netify.adapter.UserListAdapter;
import pl.edu.ug.aib.netify.data.GroupData;
import pl.edu.ug.aib.netify.data.InviteData;
import pl.edu.ug.aib.netify.data.User;
import pl.edu.ug.aib.netify.data.UserList;

@EFragment(R.layout.fragment_invite_friends)
public class InviteFriendsFragment extends DialogFragment {

    @FragmentArg
    UserList groupMembers;
    @FragmentArg
    GroupData group;
    @FragmentArg
    int userId;
    @ViewById
    ListView friendsList;
    @ViewById
    Button sendInviteButton;
    @ViewById
    ProgressBar progressBar;
    @ViewById
    TextView noFriendsInfo;
    @Bean
    SelectUserListAdapter adapter;
    OnInviteFriendsFragmentCommunicationListener listener;
    public InviteFriendsFragment(){}

    @AfterViews
    void init(){
        getDialog().setTitle(R.string.select_friends);
        friendsList.setAdapter(adapter);
    }

    public void setUsersList(UserList userList){
        //remove users that are already current user's friends
        for(User user : groupMembers.records) userList.deleteUser(user.id);
        adapter.update(userList);
        //change visibility after loading data
        progressBar.setVisibility(View.GONE);
        if(adapter.getCount() > 0) {
            friendsList.setVisibility(View.VISIBLE);
            sendInviteButton.setVisibility(View.VISIBLE);
        }
        else noFriendsInfo.setVisibility(View.VISIBLE);
    }

    @Click
    void sendInviteButtonClicked(){
        ArrayList<User> selectedUsers = adapter.getSelectedUsers();
        //create list of inviteData objects
        ArrayList<InviteData> inviteDatas = new ArrayList<>();
        InviteData invite;
        for(User user : selectedUsers){
            invite = new InviteData();
            invite.fromUser = Integer.toString(userId);
            invite.toUser = Integer.toString(user.id);
            invite.groupId = group.id;
            inviteDatas.add(invite);
        }
        //pass invites to activity and dismiss dialog
        listener.sendGroupInvites(inviteDatas);
        dismiss();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            listener = (OnInviteFriendsFragmentCommunicationListener)activity;
            listener.getUserFriendsList();
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnInviteFriendsFragmentCommunicationListener");
        }
    }

    public interface OnInviteFriendsFragmentCommunicationListener{
        public void getUserFriendsList();
        public void sendGroupInvites(ArrayList<InviteData> inviteDatas);
    }
}
