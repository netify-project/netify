package pl.edu.ug.aib.netify.rest;

import android.text.TextUtils;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import java.util.HashSet;

import pl.edu.ug.aib.netify.HomeActivity;
import pl.edu.ug.aib.netify.ProfileActivity;
import pl.edu.ug.aib.netify.R;
import pl.edu.ug.aib.netify.data.FriendData;
import pl.edu.ug.aib.netify.data.FriendDataList;
import pl.edu.ug.aib.netify.data.GroupData;
import pl.edu.ug.aib.netify.data.GroupDataList;
import pl.edu.ug.aib.netify.data.IdData;
import pl.edu.ug.aib.netify.data.InviteData;
import pl.edu.ug.aib.netify.data.MemberGroupData;
import pl.edu.ug.aib.netify.data.MemberGroupDataList;
import pl.edu.ug.aib.netify.data.SongData;
import pl.edu.ug.aib.netify.data.UserList;
import pl.edu.ug.aib.netify.data.UserLogout;

@EBean
public class RestProfileBackgroundTask {

    @RootContext
    ProfileActivity activity;
    @RestService
    NetifyRestClient restClient;


    @Background
    public void getUserGroups(String userId){
        try {
            restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
            MemberGroupDataList memberGroupDataList = restClient.getMemberGroupsByUserId("UserId=" + userId);
            GroupDataList groupDataList;
            //Check if user has any groups, if true, return empty object without sending request
            if(memberGroupDataList.records.isEmpty()) groupDataList = new GroupDataList();
            else {
                //used to provide unique ids
                HashSet<String> groupIds = new HashSet<String>();
                for (MemberGroupData item : memberGroupDataList.records) groupIds.add(item.groupId);
                groupDataList = restClient.getGroupsById(TextUtils.join(",", groupIds));
            }
            publishUserGroupsResult(groupDataList);
        }catch(Exception e){
            publishError(e);
        }
    }


    @Background
    public void getUserFriends(String userId, String sessionId){
        try {
            restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
            restClient.setHeader("X-Dreamfactory-Session-Token", sessionId);
            //search for userId on both positions of relation
            FriendDataList friendDataList = restClient.getFriendDataByUserId("UserId=" + userId + "||User2Id=" + userId);
            UserList userFriendsList;
            //Check if user has any friends, if true, return empty object without sending request
            if(friendDataList.records.isEmpty()) userFriendsList = new UserList();
            else {
                //used to provide unique ids
                HashSet<String> friendIds = new HashSet<String>();
                for (FriendData item : friendDataList.records){
                    if(item.userId.equals(userId)) friendIds.add(item.user2Id);
                    else friendIds.add(item.userId);
                }
                userFriendsList = restClient.getUsersById(TextUtils.join(",", friendIds));
            }
            publishUserFriendsResult(userFriendsList);
        }catch(Exception e){
            publishError(e);
        }
    }
    @Background
    public void sendNewInvite (String userId, String sessionId, String firstName, String lastName, InviteData inviteData){
        try {
            restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
            restClient.setHeader("X-Dreamfactory-Session-Token", sessionId);
            //send group data and receive group id
            inviteData.fromUser=userId;
           // inviteData.text=(firstName + " " + lastName + " " + getString(R.string.new_invite_friend));
            IdData result = restClient.sendInvite(inviteData);
            inviteData.id = result.id;
            publishSendNewInviteResult(inviteData);
        } catch (Exception e) {
            publishError(e);
        }
    }
    //TODO


    //Calls to activity and pushing objects to UiThread
    @UiThread
    void publishUserGroupsResult(GroupDataList groupDataList){
        activity.onUserGroupListDownloaded(groupDataList);
    }
    @UiThread
    void publishUserFriendsResult(UserList userList) {
        activity.onUserFriendsListDownloaded(userList);
    }
    @UiThread
    void publishSendNewInviteResult(InviteData inviteData) {
        activity.onSendNewInviteSuccess(inviteData);
    }
    @UiThread
    void publishError(Exception e) {
        activity.showError(e);
    }
}
