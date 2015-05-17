package pl.edu.ug.aib.netify;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;

import pl.edu.ug.aib.netify.data.Edge;
import pl.edu.ug.aib.netify.data.EdgeData;
import pl.edu.ug.aib.netify.data.Node;
import pl.edu.ug.aib.netify.data.SongData;
import pl.edu.ug.aib.netify.data.SongDataList;
import pl.edu.ug.aib.netify.rest.RestSongGraphBackgroundTask;


@EActivity(R.layout.activity_group)
@OptionsMenu(R.menu.menu_group)
public class GroupActivity extends ActionBarActivity {

    public static final int INTENT_SONG_ADDED = 1;
    @ViewById
    WebView webView;
    Gson gson;
    @Extra
    String groupId;
    @Extra
    SongDataList songDataList;
    ArrayList<Node> nodes;
    ArrayList<Edge> edges;
    @Bean
    @NonConfigurationInstance
    RestSongGraphBackgroundTask restSongGraphBackgroundTask;

    @Pref
    UserPreferences_ preferences;

    @AfterViews
    void init(){
        //get SongGraph data from Rest Client and initialize webView
        gson = new Gson();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){});
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new WebViewInterface(this), "Android");
        webView.loadUrl("file:///android_asset/cytoscape1.html");
        if(songDataList == null) restSongGraphBackgroundTask.getSongs(preferences.sessionId().get(), groupId);
        else updateSongGraph(songDataList);
    }

    public void setSongDataList(SongDataList songDataList) {
        this.songDataList = songDataList;
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
        webView.loadUrl("javascript:setNodesAndEdges(" + gson.toJson(nodes) + "," + gson.toJson(edges) + ")");
        //FOR TESTING ON EMULATOR
        //YoutubePlayerActivity_.intent(this).initVideo(songDataList.records.get(0)).songDataList(songDataList).start();
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
        YoutubeSearchActivity_.intent(this).parentSongData(songData).startForResult(INTENT_SONG_ADDED);
    }

    @OnActivityResult(INTENT_SONG_ADDED)
    void onSongAdded(int result, @OnActivityResult.Extra SongData songData){
        if(result == RESULT_OK) restSongGraphBackgroundTask.addSongToGraph(songData, preferences.sessionId().get()); //addSongToGraph(songData);
    }

    @OptionsItem(R.id.action_logout)
    void actionLogoutSelected(){
        restSongGraphBackgroundTask.logout(preferences.sessionId().get());
    }

    public void onLogout(Boolean success){
        if(success) {
            preferences.id().put(0);
            preferences.sessionId().put("");
            preferences.email().put("");
            preferences.password().put("");
            preferences.firstName().put("");
            preferences.lastName().put("");
            preferences.displayName().put("");
            LoginActivity_.intent(this).start();
            finish();
        }
    }
}
