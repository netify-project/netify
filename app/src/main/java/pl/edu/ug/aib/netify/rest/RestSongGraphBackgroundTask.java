package pl.edu.ug.aib.netify.rest;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import java.util.ArrayList;

import pl.edu.ug.aib.netify.GroupActivity;
import pl.edu.ug.aib.netify.data.EmailAndPassword;
import pl.edu.ug.aib.netify.data.IdData;
import pl.edu.ug.aib.netify.data.MemberGroupData;
import pl.edu.ug.aib.netify.data.MemberGroupDataList;
import pl.edu.ug.aib.netify.data.SongData;
import pl.edu.ug.aib.netify.data.SongDataList;
import pl.edu.ug.aib.netify.data.User;
import pl.edu.ug.aib.netify.data.UserLogout;

@EBean
public class RestSongGraphBackgroundTask {

    @RootContext
    GroupActivity activity;
    @RestService
    NetifyRestClient restClient;

    @Background
    public void getSongs(String sessionId, String groupId){
        try{
            restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
            //later will have to identify if user has access to the group, also requires header in RestClient
            //restClient.setHeader("X-Dreamfactory-Session-Token", sessionId);
            SongDataList songDataList = restClient.getSongsByGroupId("groupid=" + groupId);
            publishResult(songDataList);
       }
        catch(Exception e){
            publishError(e);
        }
    }
    @Background
    public void addSongToGraph(SongData songData, String sessionId){
        try{
            restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
            restClient.setHeader("X-Dreamfactory-Session-Token", sessionId);
            IdData result = restClient.addSongToGraph(songData);
            songData.id = result.id;
            publishPostResult(songData);
        }
        catch(Exception e){
            publishError(e);
        }
    }
    @Background
    public void getGroupMembers(String groupId, String sessionId){
        try{
            restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
            restClient.setHeader("X-Dreamfactory-Session-Token", sessionId);
            //TODO
            MemberGroupDataList memberGroupDataList = restClient.getMemberGroupsByUserId("groupId=" + groupId);
            ArrayList<String> memberIds = new ArrayList<String>();
            for(MemberGroupData memberGroupData : memberGroupDataList.records) memberIds.add(memberGroupData.userId);
            publishGroupMembersResult(memberIds);
        }
        catch(Exception e){
            publishError(e);
        }
    }
    @Background
    public void addGroupMember(String groupId, String userId, String sessionId){
        try{
            restClient.setHeader("X-Dreamfactory-Application-Name", "netify");
            restClient.setHeader("X-Dreamfactory-Session-Token", sessionId);
            //TODO
            MemberGroupData memberGroupData = new MemberGroupData();
            memberGroupData.userId = userId; memberGroupData.groupId = groupId;
            IdData result = restClient.addMemberGroupData(memberGroupData);
            publishAddGroupMemberResult(userId);
        }
        catch(Exception e){
            publishError(e);
        }
    }


    @UiThread
    void publishResult(SongDataList songDataList){
        activity.setSongDataList(songDataList);
        activity.updateSongGraph(songDataList);
    }

    @UiThread
     void publishPostResult(SongData songData){
        activity.addSongToGraph(songData);
    }

    @UiThread
    void publishGroupMembersResult(ArrayList<String> memberIds){
        activity.onGroupMembersDownloaded(memberIds);
    }
    @UiThread
    void publishAddGroupMemberResult(String userId){
        activity.addGroupMemberConfirmed(userId);
    }
    @UiThread
    void publishError(Exception e) {
        activity.showError(e);
    }
}
