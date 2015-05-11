package pl.edu.ug.aib.netify;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import pl.edu.ug.aib.netify.adapter.PlayerPropositionAdapter;
import pl.edu.ug.aib.netify.data.SongData;
import pl.edu.ug.aib.netify.data.SongDataList;
import pl.edu.ug.aib.netify.data.YoutubeVideoItem;

@EActivity(R.layout.activity_youtube_player)
public class YoutubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int SONG_CHOICE_TIME_MILLIS = 5000;
    private static final int VIDEO_FINISHING_CHECK_DELAY = 1000;
    @ViewById
    YouTubePlayerView playerView;
    @ViewById
    TextView videoTitle;
    @ViewById
    LinearLayout descriptionLayout;
    @ViewById
    TextView videoDescription;
    @ViewById
    ListView propositionList;
    @Bean
    PlayerPropositionAdapter adapter;

    //Extra passed from GroupActivity
    @Extra
    SongData initVideo;
    //Extra passed from YoutubeSearch
    @Extra
    YoutubeVideoItem youtubeVideo;
    //Extra passed from GroupActivity
    @Extra
    SongDataList songDataList;
    HashSet<String> watchedVideos;
    SongData currentVideo;
    String videoId;
    ArrayList<SongData> propositions;

    //LayoutParams for resizing propositionList
    ViewGroup.LayoutParams listViewParams;
    int itemHeight;

    YouTubePlayer youTubePlayer;
    YoutubePlayerStateChangeListener playerStateChangeListener;
    YoutubePlaybackEventListener playbackEventListener;

    Random random = new Random();
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isVideoFinishing();
        }
    };

    @AfterViews
    void init() {
        videoId = (initVideo != null) ? initVideo.videoId : youtubeVideo.getId();
        if(initVideo != null) currentVideo = initVideo;
        updateVideoInfo();
        if(songDataList != null) {
            propositions = getChildrenSongData(initVideo.videoId);
            propositionList.setAdapter(adapter);
            adapter.update(propositions);
            listViewParams = propositionList.getLayoutParams();
            itemHeight = (int)getResources().getDimension(R.dimen.player_proposition_item_height);
            updatePropositionListLayout();

        }
        watchedVideos = new HashSet<String>();
        playerStateChangeListener = new YoutubePlayerStateChangeListener();
        playbackEventListener = new YoutubePlaybackEventListener();

        playerView.initialize(YoutubeSearchHandler.KEY, this);
    }
    private void killActivity(){
        finish();
    }

    @ItemClick
    void propositionListItemClicked(SongData songData){
        playNextSong(songData);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult result) {
        Toast.makeText(this, getString(R.string.youtube_player_failed), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean restored) {
        youTubePlayer = player;
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        if(!restored){
            youTubePlayer.loadVideo(videoId);
        }
    }

    private void playNextSong(SongData nextSong){
        youTubePlayer.loadVideo(nextSong.videoId);
        watchedVideos.add(videoId);
        videoId = nextSong.videoId;
        currentVideo = nextSong;
        propositions = getChildrenSongData(videoId);
        adapter.update(propositions);
        updateVideoInfo();
        updatePropositionListLayout();
    }

    private void updateVideoInfo(){
        if(initVideo != null){
            videoTitle.setText(currentVideo.title);
            descriptionLayout.setVisibility(View.GONE);
        }
        else{descriptionLayout.setVisibility(View.VISIBLE);
            videoTitle.setText(youtubeVideo.getTitle());
            videoDescription.setText(youtubeVideo.getDescription());

        }
    }
    private void updatePropositionListLayout(){
        if(propositions.size()>0) {
            listViewParams.height = itemHeight * propositions.size();
            propositionList.setLayoutParams(listViewParams);
        }
    }

    private void isVideoFinishing(){
        if(youTubePlayer.getDurationMillis()-youTubePlayer.getCurrentTimeMillis() <= SONG_CHOICE_TIME_MILLIS ){
            Log.d("player", "is finishing");
            if(songDataList != null) {
                //ArrayList<SongData> children = getChildrenSongData(videoId);
                //TODO DO SOMETHING WITH IT
                //Toast.makeText(this, videoId + " is finishing", Toast.LENGTH_LONG).show();
            }
        } else {
            handler.postDelayed(runnable, VIDEO_FINISHING_CHECK_DELAY);
        }
    }

    private ArrayList<SongData> getChildrenSongData(String parentVideoId){
        SongData parentSongData = songDataList.findByVideoId(parentVideoId);
        ArrayList<SongData> children = new ArrayList<SongData>();
        if(parentSongData != null){
            String id = parentSongData.id;
            for(SongData songData : songDataList.records){
                if(songData.parentId != null && songData.parentId.equals(id)) children.add(songData);
            }
        }
        return children;
    }
    private SongData getRandomSongData(ArrayList<SongData> songDatas, Boolean watchedVideosExcluded){
        int size = songDatas.size();
        if(watchedVideosExcluded){
            ArrayList<Integer> unusedIndexes = new ArrayList<Integer>();
            for(int i = 0; i<size;i++) unusedIndexes.add(i);
            Integer index, pos;
            SongData selectedSongData;
            do{
                pos = random.nextInt(unusedIndexes.size());
                index = unusedIndexes.get(pos);
                unusedIndexes.remove(pos);
                selectedSongData = songDatas.get(index);
                if(!watchedVideos.contains(selectedSongData.videoId)) return selectedSongData;
            }while (!unusedIndexes.isEmpty());
        }
        return songDatas.get(random.nextInt(size));
    }

    private final class YoutubePlayerStateChangeListener implements YouTubePlayer.PlayerStateChangeListener {

        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {
            Log.d("VideoStarted", "started");
            //updateVideoInfo();
        }

        @Override
        public void onVideoEnded() {
            Log.d("VideoEnded", "ended");

            if(songDataList != null) {
                //ArrayList<SongData> children = getChildrenSongData(videoId);
                if(propositions == null || propositions.isEmpty()) {
                    //Toast.makeText(getBaseContext(), "This song has no children", Toast.LENGTH_LONG).show();
                    killActivity();
                    return;
                }
                playNextSong(getRandomSongData(propositions, false));
            }
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    }
    private final class YoutubePlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            runnable.run();
        }

        @Override
        public void onPaused() {
            handler.removeCallbacks(runnable);
        }

        @Override
        public void onStopped() {
            handler.removeCallbacks(runnable);
        }

        @Override
        public void onBuffering(boolean b) { }

        @Override
        public void onSeekTo(int i) {
            handler.removeCallbacks(runnable);
        }
    }
}

