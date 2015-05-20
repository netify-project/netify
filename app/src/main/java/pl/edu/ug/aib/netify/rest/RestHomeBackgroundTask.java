package pl.edu.ug.aib.netify.rest;

import android.text.TextUtils;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import java.util.HashSet;

import pl.edu.ug.aib.netify.HomeActivity;
import pl.edu.ug.aib.netify.LoginActivity;
import pl.edu.ug.aib.netify.data.EmailAndPassword;
import pl.edu.ug.aib.netify.data.GroupData;
import pl.edu.ug.aib.netify.data.GroupDataList;
import pl.edu.ug.aib.netify.data.IdData;
import pl.edu.ug.aib.netify.data.MemberGroupData;
import pl.edu.ug.aib.netify.data.MemberGroupDataList;
import pl.edu.ug.aib.netify.data.SongData;
import pl.edu.ug.aib.netify.data.User;
import pl.edu.ug.aib.netify.data.UserLogout;
import pl.edu.ug.aib.netify.data.UserRegistrationData;

@EBean
public class RestHomeBackgroundTask {

    @RootContext
    HomeActivity activity;
    @RestService
    NetifyRestClient restClient;


    @Background
    public void getUserGroups(String userId){
        try {
            restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
            MemberGroupDataList memberGroupDataList = restClient.getMemberGroupsByUserId("UserId=" + userId);
            HashSet<String> groupIds = new HashSet<String>();
            for(MemberGroupData item : memberGroupDataList.records) groupIds.add(item.groupId);
            GroupDataList groupDataList = restClient.getGroupsById(TextUtils.join(",", groupIds));
            publishUserGroupsResult(groupDataList);
        }catch(Exception e){
            publishError(e);
        }
    }

    @Background
    public void addNewGroup(String userId, String sessionId, GroupData groupData, SongData firstSong){
        try {
            restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
            restClient.setHeader("X-Dreamfactory-Session-Token", sessionId);
            //send group data and receive group id
            IdData result = restClient.addGroup(groupData);
            groupData.id = result.id;
            firstSong.groupId = Integer.parseInt(result.id);
            //send data about user membership in the group and receive confirmation
            MemberGroupData memberGroupData = new MemberGroupData();
            memberGroupData.groupId = groupData.id; memberGroupData.userId = userId;
            result = restClient.addMemberGroupData(memberGroupData);
            //TODO send first song data
            result = restClient.addSongToGraph(firstSong);
            publishAddNewGroupResult(groupData);
        }catch(Exception e){
            publishError(e);
        }
    }

    //TODO

    @Background
    public void logout(String sessionId){
        try{
            restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
            restClient.setHeader("X-Dreamfactory-Session-Token", sessionId);
            UserLogout userLogout = restClient.logout();
            publishLogoutResult(userLogout.success);
        }
        catch(Exception e){
            publishError(e);
        }
    }

    @UiThread
    void publishUserGroupsResult(GroupDataList groupDataList){
        activity.onUserGroupListDownloaded(groupDataList);
    }
    @UiThread
    void publishAddNewGroupResult(GroupData groupData){
        activity.onNewGroupAdded(groupData);
    }
    @UiThread
    void publishLogoutResult(Boolean success){
        activity.onLogout(success);
    }
    @UiThread
    void publishError(Exception e) {
        activity.showError(e);
    }
}
