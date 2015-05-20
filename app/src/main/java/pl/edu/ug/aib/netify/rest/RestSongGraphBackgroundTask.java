package pl.edu.ug.aib.netify.rest;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import pl.edu.ug.aib.netify.GroupActivity;
import pl.edu.ug.aib.netify.data.EmailAndPassword;
import pl.edu.ug.aib.netify.data.IdData;
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
    void publishResult(SongDataList songDataList){
        activity.setSongDataList(songDataList);
        activity.updateSongGraph(songDataList);
    }

    @UiThread
     void publishPostResult(SongData songData){
        activity.addSongToGraph(songData);
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
