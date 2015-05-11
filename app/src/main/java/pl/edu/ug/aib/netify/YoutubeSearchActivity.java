package pl.edu.ug.aib.netify;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.Random;

import pl.edu.ug.aib.netify.data.SongData;
import pl.edu.ug.aib.netify.data.YoutubeVideoItem;

@EActivity(R.layout.activity_youtube_search)
public class YoutubeSearchActivity extends ActionBarActivity {

    @Extra
    SongData parentSongData;
    @ViewById
    EditText searchInput;
    @ViewById
    ListView videosFound;

    Handler handler;
    List<YoutubeVideoItem> searchResults;

    @AfterViews
    void init(){
        handler = new Handler();

    }
    @Click
    void searchButtonClicked(){
        searchOnYoutube(searchInput.getText().toString().trim());
    }
    @Background
    void searchOnYoutube(String keywords){
        YoutubeSearchHandler youtubeSearchHandler = new YoutubeSearchHandler(this);
        List<YoutubeVideoItem> results = youtubeSearchHandler.search(keywords);
        updateVideosFound(results);
    }
    @UiThread
    void updateVideosFound(List<YoutubeVideoItem> results){
        searchResults = results;
        ArrayAdapter<YoutubeVideoItem> adapter = new ArrayAdapter<YoutubeVideoItem>(getApplicationContext(), R.layout.youtube_search_item, searchResults){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.youtube_search_item, parent, false);
                }
                ImageView thumbnail = (ImageView)convertView.findViewById(R.id.video_thumbnail);
                TextView title = (TextView)convertView.findViewById(R.id.video_title);
                TextView description = (TextView)convertView.findViewById(R.id.video_description);

                YoutubeVideoItem searchResult = searchResults.get(position);

                Picasso.with(getApplicationContext()).load(searchResult.getThumbnailURL()).into(thumbnail);
                title.setText(searchResult.getTitle());
                description.setText(searchResult.getDescription());
                return convertView;
            }
        };

        videosFound.setAdapter(adapter);
    }
    @ItemClick
    void videosFoundItemClicked(YoutubeVideoItem item){
        openOptionSelectionDialog(item);
    }
    private void openOptionSelectionDialog(final YoutubeVideoItem videoItem) {
        final CharSequence[] items = { getString(R.string.song_dialog_play), getString(R.string.song_dialog_add),
                getString(R.string.song_dialog_cancel) };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(videoItem.getTitle());
    builder.setItems(items, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int item) {
            if (item == 0) {
                startYoutubePlayerIntent(videoItem);
            } else if (item == 1) {
                addToGraphIntent(videoItem);
            } else {
                dialog.dismiss();
            }
        }
    });
    builder.show();
}
    void startYoutubePlayerIntent(YoutubeVideoItem item){
        YoutubePlayerActivity_.intent(this).youtubeVideo(item).start();
    }
    void addToGraphIntent(YoutubeVideoItem item){
        SongData newSongData = new SongData();
        newSongData.parentId = parentSongData.id;
        newSongData.videoId = item.getId();
        newSongData.groupId = parentSongData.groupId;
        newSongData.thumbnail = item.getThumbnailURL();
        newSongData.title = item.getTitle();
        Intent intent = new Intent();
        intent.putExtra("songData", newSongData);
        setResult(RESULT_OK, intent);
        finish();
    }


}
