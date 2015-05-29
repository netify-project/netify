package pl.edu.ug.aib.netify.rest;

import android.text.TextUtils;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import java.util.HashMap;
import java.util.HashSet;

import pl.edu.ug.aib.netify.HomeActivity;
import pl.edu.ug.aib.netify.LoginActivity;
import pl.edu.ug.aib.netify.data.EmailAndPassword;
import pl.edu.ug.aib.netify.data.FriendData;
import pl.edu.ug.aib.netify.data.FriendDataList;
import pl.edu.ug.aib.netify.data.GroupData;
import pl.edu.ug.aib.netify.data.GroupDataList;
import pl.edu.ug.aib.netify.data.IdData;
import pl.edu.ug.aib.netify.data.InviteData;
import pl.edu.ug.aib.netify.data.InviteDataList;
import pl.edu.ug.aib.netify.data.MemberGroupData;
import pl.edu.ug.aib.netify.data.MemberGroupDataList;
import pl.edu.ug.aib.netify.data.SongData;
import pl.edu.ug.aib.netify.data.User;
import pl.edu.ug.aib.netify.data.UserList;
import pl.edu.ug.aib.netify.data.UserLogout;
import pl.edu.ug.aib.netify.data.UserRegistrationData;
import pl.edu.ug.aib.netify.itemView.UserListItemView;

@EBean
public class RestHomeBackgroundTask {

    @RootContext
    HomeActivity activity;
    UserListItemView list;
    @RestService
    NetifyRestClient restClient;


    @Background
    public void searchGroups(String query) {
        try {
            restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
            //Sets search conditions: looks for query in name, description and genre in public groups
            GroupDataList groupDataList = restClient.getGroupsByQuery("(name like '%" + query + "%'||description like '%" + query + "%'||genre like '%" + query + "%')and isPrivate=false");
            publishSearchGroupsResult(groupDataList);
        } catch (Exception e) {
            publishError(e);
        }
    }

    @Background
    public void searchUsers(String query, String sessionId) {
        try {
            restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
            restClient.setHeader("X-Dreamfactory-Session-Token", sessionId);
            //Sets search conditions: looks for query in name, description and genre in public groups
            UserList userList = restClient.getUsersByQuery("first_name like '%" + query + "%'||last_name like '%" + query + "%'||email like '%" + query + "%'");
            publishSearchUsersResult(userList);
        } catch (Exception e) {
            publishError(e);
        }
    }

    @Background
    public void getUserFriends(String userId, String sessionId) {
        try {
            restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
            restClient.setHeader("X-Dreamfactory-Session-Token", sessionId);
            //search for userId on both positions of relation
            FriendDataList friendDataList = restClient.getFriendDataByUserId("UserId=" + userId + "||User2Id=" + userId);
            UserList userFriendsList;
            //Check if user has any friends, if true, return empty object without sending request
            if (friendDataList.records.isEmpty()) userFriendsList = new UserList();
            else {
                //used to provide unique ids
                HashSet<String> friendIds = new HashSet<String>();
                for (FriendData item : friendDataList.records) {
                    if (item.userId.equals(userId)) friendIds.add(item.user2Id);
                    else friendIds.add(item.userId);
                }
                userFriendsList = restClient.getUsersById(TextUtils.join(",", friendIds));
            }
            publishUserFriendsResult(userFriendsList);
        } catch (Exception e) {
            publishError(e);
        }
    }


    @Background
    public void getUserGroups(String userId) {
        try {
            restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
            MemberGroupDataList memberGroupDataList = restClient.getMemberGroupsByUserId("UserId=" + userId);
            GroupDataList groupDataList;
            //Check if user has any groups, if true, return empty object without sending request
            if (memberGroupDataList.records.isEmpty()) groupDataList = new GroupDataList();
            else {
                //used to provide unique ids
                HashSet<String> groupIds = new HashSet<String>();
                for (MemberGroupData item : memberGroupDataList.records) groupIds.add(item.groupId);
                groupDataList = restClient.getGroupsById(TextUtils.join(",", groupIds));
            }
            publishUserGroupsResult(groupDataList);
        } catch (Exception e) {
            publishError(e);
        }
    }

    @Background
    public void getUserInvites(String userId, String sessionId) {
        try {
            restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
            restClient.setHeader("X-Dreamfactory-Session-Token", sessionId);
            InviteDataList inviteDataList = restClient.getInviteDataByUserId("ToUser=" + userId);
            UserList userList;
            GroupDataList groupDataList;
            //Check if user has any friends, if true, return empty object without sending request
            if (inviteDataList.records.isEmpty()) userList = new UserList();
            else {
                //used to provide unique ids
                HashMap<Integer, String> usersIds = new HashMap<>();
                HashMap<Integer, String> groupIds = new HashMap<>();
                for (InviteData item : inviteDataList.records) {
                    if(item.groupId != null) groupIds.put(inviteDataList.records.indexOf(item), item.groupId);
                    usersIds.put(inviteDataList.records.indexOf(item), item.fromUser);
                }

                //get users and groups info
                userList = restClient.getUsersById(TextUtils.join(",", usersIds.values()));
                groupDataList = restClient.getGroupsById(TextUtils.join(",", groupIds.values()));
                //create hashmap to make matching easier
                HashMap<String, String> usersNames = new HashMap<>();
                HashMap<String, String> groupNames = new HashMap<>();
                for(User user : userList.records) usersNames.put(Integer.toString(user.id), user.firstName + " " + user.lastName);
                for(GroupData group : groupDataList.records) groupNames.put(group.id, group.name);
                //add display info to invite data
                InviteData currentInvite;
                for (int i=0; i< inviteDataList.records.size(); i++) {
                    currentInvite = inviteDataList.records.get(i);
                    currentInvite.fullName = usersNames.get(currentInvite.fromUser);
                    if(currentInvite.groupId != null) currentInvite.groupName = groupNames.get(currentInvite.groupId);
                }

            }
            publishUserInviteResult(inviteDataList);
        } catch (Exception e) {
            publishError(e);
        }
    }

        @Background
        public void logout (String sessionId){
            try {
                restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
                restClient.setHeader("X-Dreamfactory-Session-Token", sessionId);
                UserLogout userLogout = restClient.logout();
                publishLogoutResult(userLogout.success);
            } catch (Exception e) {
                publishError(e);
            }
        }

    @Background
    public void deleteInvite (String sessionId, InviteData inviteData){
        try {
            restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
            restClient.setHeader("X-Dreamfactory-Session-Token", sessionId);
            restClient.deleteInviteById(inviteData.id);
            publishDeleteResult(inviteData);
        } catch (Exception e) {
            publishError(e);
        }
    }

        @Background
        public void addNewGroup (String userId, String sessionId, GroupData groupData, SongData
        firstSong){
            try {
                restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
                restClient.setHeader("X-Dreamfactory-Session-Token", sessionId);
                //send group data and receive group id
                IdData result = restClient.addGroup(groupData);
                groupData.id = result.id;
                firstSong.groupId = Integer.parseInt(result.id);
                //send data about user membership in the group and receive confirmation
                MemberGroupData memberGroupData = new MemberGroupData();
                memberGroupData.groupId = groupData.id;
                memberGroupData.userId = userId;
                result = restClient.addMemberGroupData(memberGroupData);
                //send first song
                result = restClient.addSongToGraph(firstSong);
                publishAddNewGroupResult(groupData);
            } catch (Exception e) {
                publishError(e);
            }
        }
        @Background
        public void sendInvite (String userId, String sessionId, InviteData inviteData){
            try {
                restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
                restClient.setHeader("X-Dreamfactory-Session-Token", sessionId);
                IdData send = restClient.sendInvite(inviteData);
                inviteData.id = send.id;
                inviteData.fromUser = userId;

                publishSendInviteResult(inviteData);
            } catch (Exception e) {
                publishError(e);
            }
        }

        @Background
        public void addMemberToGroup(String sessionId, InviteData inviteData){
            try {
                restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
                restClient.setHeader("X-Dreamfactory-Session-Token", sessionId);
                MemberGroupData memberGroupData = new MemberGroupData();
                memberGroupData.userId = inviteData.toUser;
                memberGroupData.groupId = inviteData.groupId;
                IdData result = restClient.addMemberGroupData(memberGroupData);
                publishAddMemberToGroupResult(inviteData);
                //delete invite after success
                deleteInvite(sessionId, inviteData);
            } catch (Exception e) {
                publishError(e);
            }
        }

        @Background
        public void addFriendData(String sessionId, InviteData inviteData){
            try {
                restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
                restClient.setHeader("X-Dreamfactory-Session-Token", sessionId);
                FriendData friendData = new FriendData();
                friendData.userId = inviteData.fromUser; friendData.user2Id = inviteData.toUser;
                IdData result = restClient.addFriendData(friendData);
                publishAddFriendDataResult(inviteData);
                //delete invite after success
                deleteInvite(sessionId, inviteData);
            } catch (Exception e) {
                publishError(e);
            }
        }
    @Background
    public void getGroupMembers(String sessionId, GroupData groupData){
        try {
            restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
            restClient.setHeader("X-Dreamfactory-Session-Token", sessionId);
            //find all group members by groupId filter
            MemberGroupDataList memberGroupDataList = restClient.getMemberGroupsByUserId("groupId=" + groupData.id);
            //Check if user has any groups, if true, return empty object without sending request
            UserList userList;
            if (memberGroupDataList.records.isEmpty()) userList = new UserList();
            else {
                //used to provide unique ids
                HashSet<String> userIds = new HashSet<String>();
                for (MemberGroupData item : memberGroupDataList.records) userIds.add(item.userId);
                userList = restClient.getUsersById(TextUtils.join(",", userIds));
            }
            publishGetGroupMembersResult(userList);

        } catch (Exception e) {
            publishError(e);
        }
    }


        //TODO
        //Calls to activity and pushing objects to UiThread
        @UiThread
        void publishUserGroupsResult (GroupDataList groupDataList){
            activity.onUserGroupListDownloaded(groupDataList);
        }
        @UiThread
        void publishAddNewGroupResult (GroupData groupData){
            activity.onNewGroupAdded(groupData);
        }
        @UiThread
        void publishDeleteResult (InviteData inviteData){
        activity.onDeleteInviteSuccess(inviteData);
         }
        @UiThread
        void publishSearchGroupsResult (GroupDataList groupDataList){
            activity.onSearchGroupCompleted(groupDataList);
        }
        @UiThread
        void publishUserInviteResult (InviteDataList inviteDataList){
            activity.onUserInvitesListDownloaded(inviteDataList);
        }

        @UiThread
        void publishSearchUsersResult (UserList userList){
            activity.onSearchUsersCompleted(userList);
        }
        @UiThread
        void publishUserFriendsResult (UserList userList){
            activity.onUserFriendsListDownloaded(userList);
        }
        @UiThread
        void publishLogoutResult (Boolean success){
            activity.onLogout(success);
        }
        @UiThread
        void publishSendInviteResult (InviteData inviteData){
            list.sendInviteConfirmed(inviteData);
        }
        @UiThread
        void publishAddMemberToGroupResult(InviteData inviteData){
            activity.onAcceptGroupInviteSuccess(inviteData);
        }
        @UiThread
        void publishAddFriendDataResult(InviteData inviteData){
            activity.onAcceptFriendInviteSuccess(inviteData);
        }
        @UiThread
        void publishGetGroupMembersResult(UserList userList){
            activity.onGetGroupMembersSuccess(userList);
        }
        @UiThread
        void publishError (Exception e){
            activity.showError(e);
        }
    }
