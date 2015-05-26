package pl.edu.ug.aib.netify;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;

import pl.edu.ug.aib.netify.data.Edge;
import pl.edu.ug.aib.netify.data.EdgeData;
import pl.edu.ug.aib.netify.data.GroupData;
import pl.edu.ug.aib.netify.data.Node;
import pl.edu.ug.aib.netify.data.SongData;
import pl.edu.ug.aib.netify.data.SongDataList;
import pl.edu.ug.aib.netify.rest.RestSongGraphBackgroundTask;


@EActivity(R.layout.activity_group)
@OptionsMenu(R.menu.menu_group)
public class GroupActivity extends ActionBarActivity {

    public static final int INTENT_SONG_ADDED = 1;
    public static final int JS_HANDLER_TIMEOUT = 500;
    @OptionsMenuItem
    MenuItem action_joingroup; //not visible by default
    @ViewById
    WebView webView;
    Gson gson;
    @Extra
    GroupData groupData;
    @Extra
    SongDataList songDataList;
    ArrayList<Node> nodes;
    ArrayList<Edge> edges;
    //List of group members' Ids
    ArrayList<String> memberIds;
    //prevents spamming join button
    boolean isProcessingAddMember = false;
    @Bean
    @NonConfigurationInstance
    RestSongGraphBackgroundTask restSongGraphBackgroundTask;

    @Pref
    UserPreferences_ preferences;
    //keep information if html page already loaded
    Boolean isPageFinished = false;
    //Handler checking if page is finished after nodes and edges were downloaded
    Handler pageLoadedHandler = new Handler();
    //runnable called by handler at specified intervals
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            loadNodesAndEdges(nodes, edges);
        }
    };

    @AfterViews
    void init(){
        //get group members Ids
        restSongGraphBackgroundTask.getGroupMembers(groupData.id, preferences.sessionId().get());
        getSupportActionBar().setTitle(groupData.name);
        //get SongGraph data from Rest Client and initialize webView
        gson = new Gson();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //set flag to true to allow sending data to JS function
                isPageFinished = true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new WebViewInterface(this), "Android");
        webView.loadUrl("file:///android_asset/cytoscape1.html");
        if(songDataList == null) restSongGraphBackgroundTask.getSongs(preferences.sessionId().get(), groupData.id);
        else updateSongGraph(songDataList);
    }

    public void setSongDataList(SongDataList songDataList) {
        this.songDataList = songDataList;
    }

    public void onGroupMembersDownloaded(ArrayList<String> memberIds) {
        this.memberIds = memberIds;
        //If current user is not a member, make menuItem JoinGroup visible
        if(!isCurrentUserGroupMember()) action_joingroup.setVisible(true);
    }
    //Check if current user is group member, returns false by default
    private boolean isCurrentUserGroupMember(){
        return memberIds != null ? memberIds.contains(Integer.toString(preferences.id().get())) : false;
    }
    //After adding user to group members, update activity's members list and hide join menu item
    public void addGroupMemberConfirmed(String userId){
        memberIds.add(userId);
        action_joingroup.setVisible(false);
        isProcessingAddMember = false;
        Toast.makeText(this, getString(R.string.join_group_successful), Toast.LENGTH_LONG).show();
    }

    public void updateSongGraph(SongDataList songDataList){
        //push nodes and edges of SongGraph to webView Javascript code
        //Log.d("gson", gson.toJson(nodes));Log.d("gson", gson.toJson(edges));
        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();
        for(SongData songData : songDataList.records){
            nodes.add(new Node(songData));
            if(songData.parentId != null) edges.add(new Edge(new EdgeData(songData.parentId, songData.id)));
        }
        loadNodesAndEdges(nodes, edges);
    }
    //send nodes and edges to webView or wait for the next chance
    private void loadNodesAndEdges(ArrayList<Node> nodes, ArrayList<Edge> edges){
        if(isPageFinished) {
            webView.loadUrl("javascript:setNodesAndEdges(" + gson.toJson(nodes) + "," + gson.toJson(edges) + ")");
        }
        else{
            pageLoadedHandler.postDelayed(runnable, JS_HANDLER_TIMEOUT);
        }
    }

    public void addSongToGraph(SongData songData){
        songDataList.records.add(songData);
        GroupActivity_.intent(this).songDataList(songDataList).start();
        finish();
    }
    public void showError(Exception e){
        //display error message
        Log.d("gson", e.getMessage());
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        isProcessingAddMember = false;
    }

    public void songClicked(SongData songData){
        openOptionSelectionDialog(songData);
    }

    private void openOptionSelectionDialog(final SongData songData) {
        final CharSequence[] items = { getString(R.string.song_dialog_play), getString(R.string.song_dialog_add),
                getString(R.string.song_dialog_cancel) };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(songData.title);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    startYoutubePlayerIntent(songData);
                } else if (item == 1) {
                    startYoutubeSearchIntent(songData);
                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    void startYoutubePlayerIntent(SongData songData){
        YoutubePlayerActivity_.intent(this).initVideo(songData).songDataList(songDataList).start();
    }
    void startYoutubeSearchIntent(SongData songData){
        //allow only if user is group member
        if(isCurrentUserGroupMember()) YoutubeSearchActivity_.intent(this).parentSongData(songData).startForResult(INTENT_SONG_ADDED);
        else Toast.makeText(this, getString(R.string.join_to_add_songs_info), Toast.LENGTH_LONG).show();
    }

    @OnActivityResult(INTENT_SONG_ADDED)
    void onSongAdded(int result, @OnActivityResult.Extra SongData songData){
        if(result == RESULT_OK) restSongGraphBackgroundTask.addSongToGraph(songData, preferences.sessionId().get()); //addSongToGraph(songData);
    }
    //On clicking join group menuItem
    @OptionsItem(R.id.action_joingroup)
    void actionJoinGroupSelected(){
        //check if request is currently processed
        if(isProcessingAddMember) return;
        restSongGraphBackgroundTask.addGroupMember(groupData.id, Integer.toString(preferences.id().get()), preferences.sessionId().get());
        isProcessingAddMember = true;
    }

}
