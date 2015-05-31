package pl.edu.ug.aib.netify.dataManager;

import android.app.Activity;

import pl.edu.ug.aib.netify.data.GroupDataList;
import pl.edu.ug.aib.netify.data.InviteDataList;
import pl.edu.ug.aib.netify.data.SongDataList;
import pl.edu.ug.aib.netify.data.User;
import pl.edu.ug.aib.netify.data.UserList;

//class for managing asynchronous data requests, manipulation based on the data
//and passing them to the fragments
public class ProfileDataManager {

    private User currentUser;
    private SongDataList userSongs;
    private UserList userFriends;
    private GroupDataList userGroups;
    private InviteDataList userInvites;

    OnProfileDataManagerCommunicationListener listener;

    public ProfileDataManager(Activity activity){
        try{
            listener = (OnProfileDataManagerCommunicationListener)activity;
            currentUser = listener.getUser();
            listener.getUserSongsForManager();
            listener.getUserFriendsForManager();
            listener.getUserGroupsForManager();
            listener.getUserInvitesForManager();
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnProfileDataManagerCommunicationListener");
        }
    }

    public void setUserSongs(SongDataList userSongs) {
        this.userSongs = userSongs;
    }

    public void setUserFriends(UserList userFriends) {
        this.userFriends = userFriends;
    }

    public void setUserGroups(GroupDataList userGroups) {
        this.userGroups = userGroups;
    }

    public void setUserInvites(InviteDataList userInvites) {
        this.userInvites = userInvites;
    }




    public interface OnProfileDataManagerCommunicationListener{
        public User getUser();
        public void getUserSongsForManager();
        public void getUserFriendsForManager();
        public void getUserGroupsForManager();
        public void getUserInvitesForManager();
    }
}
