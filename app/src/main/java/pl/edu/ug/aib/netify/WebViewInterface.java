package pl.edu.ug.aib.netify;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.Gson;

import pl.edu.ug.aib.netify.data.SongData;

public class WebViewInterface {
    //instead of Context context; to get access to activity methods
    GroupActivity activity;
    Gson gson;
    //Primitive hack for preventing calling songClicked() twice
    Boolean isSongClicked;

    WebViewInterface(GroupActivity activity){
        this.activity = activity;
        gson = new Gson();
        isSongClicked = false;
    }

    @JavascriptInterface
    public void songClicked(String songjson){
        if(isSongClicked) isSongClicked = false;
        else {
            isSongClicked = true;
            SongData songData = gson.fromJson(songjson, SongData.class);
            activity.songClicked(songData);
        }
    }
}
