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
import pl.edu.ug.aib.netify.data.GroupDataList;
import pl.edu.ug.aib.netify.data.MemberGroupData;
import pl.edu.ug.aib.netify.data.MemberGroupDataList;
import pl.edu.ug.aib.netify.data.User;
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

    //TODO

    @UiThread
    void publishUserGroupsResult(GroupDataList groupDataList){
        activity.onUserGroupListDownloaded(groupDataList);
    }
    @UiThread
    void publishError(Exception e) {
        //activity.showError(e);
    }
}
