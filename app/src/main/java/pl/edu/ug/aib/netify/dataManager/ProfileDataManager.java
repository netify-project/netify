package pl.edu.ug.aib.netify.dataManager;

import android.app.Activity;

import java.util.HashMap;

import pl.edu.ug.aib.netify.data.FriendData;
import pl.edu.ug.aib.netify.data.FriendDataList;
import pl.edu.ug.aib.netify.data.GroupData;
import pl.edu.ug.aib.netify.data.GroupDataList;
import pl.edu.ug.aib.netify.data.InviteData;
import pl.edu.ug.aib.netify.data.InviteDataList;
import pl.edu.ug.aib.netify.data.SongData;
import pl.edu.ug.aib.netify.data.SongDataList;
import pl.edu.ug.aib.netify.data.User;
import pl.edu.ug.aib.netify.data.UserList;

//class for managing asynchronous data requests, manipulation based on the data
//and passing them to the fragments
public class ProfileDataManager {

    private int currentUserId;
    private User observedUser;
    private SongDataList userSongs;
    private UserList userFriends;
    private GroupDataList userGroups;
    private InviteDataList userInvites;
    private FriendDataList userFriendDatas;

    OnProfileDataManagerCommunicationListener listener;

    public ProfileDataManager(Activity activity){
        try{
            listener = (OnProfileDataManagerCommunicationListener)activity;
            currentUserId = listener.getCurrentUserId();
            observedUser = listener.getUser();
            listener.getUserSongsForManager();
            listener.getUserFriendsForManager();
            listener.getUserGroupsForManager();
            listener.getUserInvitesForManager();
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnProfileDataManagerCommunicationListener");
        }
    }
    //SETTERS
    //apart from that, they check if data to manipulate is already set, if it is, they invoke manipulations
    public void setUserSongs(SongDataList userSongs) {
        this.userSongs = userSongs;
        if(isSongInfoComplete()) sendSongInfo();
    }

    public void setUserFriends(UserList userFriends, FriendDataList friendDataList) {
        this.userFriends = userFriends;
        this.userFriendDatas = friendDataList;
        if(isRelationshipInfoComplete()) sendRelationshipInfo();
        listener.passUserFriendsFromManager(userFriends);
    }

    public void setUserGroups(GroupDataList userGroups) {
        this.userGroups = userGroups;
        if(isSongInfoComplete()) sendSongInfo();
        listener.passUserGroupsFromManager(userGroups);
    }

    public void setUserInvites(InviteDataList userInvites) {
        this.userInvites = userInvites;
        if(isRelationshipInfoComplete()) sendRelationshipInfo();
    }
    //Song Info require data about songs and groups
    private Boolean isSongInfoComplete(){
        return userSongs != null && userGroups != null;
    }
    //Relationship Info require data about friends and invites
    private Boolean isRelationshipInfoComplete(){
        return userFriends != null && userInvites != null && userFriendDatas != null;
    }
    //adds group name to song data
    private void completeSongInfo(){
        //create map group id -> group name
        HashMap<String, String> groupMap = new HashMap<>();
        for(GroupData group : userGroups.records) groupMap.put(group.id, group.name);
        //add group name to songData objects
        for(SongData song : userSongs.records) song.groupName = groupMap.get(song.groupId);
    }
    //returns true if user is already invited and can't be invited again
    private Boolean isAlreadyInvited(){
        for(InviteData invite : userInvites.records){
            if (invite.fromUser.equals(Integer.toString(currentUserId))) return true;
        }
        return false;
    }
    //returns true if user is already invited and can't be invited again
    private Boolean hasInvitedYou(){
        for(InviteData invite : userInvites.records){
            if (invite.toUser.equals(Integer.toString(currentUserId))) return true;
        }
        return false;
    }
    //returns true if user is already invited and can't be invited again
    private Boolean isAlreadyFriend(){
        for(User user : userFriends.records){
            if (user.id.equals(currentUserId)) return true;
        }
        return false;
    }
    //pass relationship info to activity
    private void sendRelationshipInfo(){
        boolean isFriend = isAlreadyFriend();
        String friendDataId = null;
        if(isFriend){
            String observedId = Integer.toString(observedUser.id);
            String currentId = Integer.toString(currentUserId);
            for(FriendData fd : userFriendDatas.records){
                if(isFriendDataMatching(fd, observedId, currentId)){
                    friendDataId = fd.id;
                }
            }
        }
        listener.passRelationshipInfoFromManager(isAlreadyInvited(),hasInvitedYou(), isFriend, friendDataId);
    }
    //pass song data to activity
    private void sendSongInfo(){
        completeSongInfo();
        listener.passUserSongsFromManager(userSongs);
    }
    private Boolean isFriendDataMatching(FriendData fd, String user1Id, String user2Id ){
        return (fd.userId.equals(user1Id) && fd.user2Id.equals(user2Id)) || (fd.userId.equals(user2Id) && fd.user2Id.equals(user1Id));
    }



    public interface OnProfileDataManagerCommunicationListener{
        public int getCurrentUserId();
        public User getUser();
        public void getUserSongsForManager();
        public void getUserFriendsForManager();
        public void getUserGroupsForManager();
        public void getUserInvitesForManager();
        public void passUserSongsFromManager(SongDataList songDataList);
        public void passUserGroupsFromManager(GroupDataList groupDataList);
        public void passUserFriendsFromManager(UserList userList);
        public void passRelationshipInfoFromManager(Boolean isAlreadyInvited, Boolean hasInvitedYou, Boolean isAlreadyFriend, String friendDataId);
    }
}
