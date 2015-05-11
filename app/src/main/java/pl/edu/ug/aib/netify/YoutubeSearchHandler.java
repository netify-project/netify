package pl.edu.ug.aib.netify;

import android.content.Context;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.edu.ug.aib.netify.data.YoutubeVideoItem;

public class YoutubeSearchHandler {

    private YouTube youtube;
    private YouTube.Search.List query;

    // Your developer key goes here
    public static final String KEY = "AIzaSyAHxS7m6uVz8o9M8AgLMGOqk9BmaLMWdH0";

    public YoutubeSearchHandler(Context context) {
        youtube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest hr) throws IOException {}
        }).build();

        try{
            query = youtube.search().list("id,snippet");
            query.setKey(KEY);
            query.setType("video");
            query.setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/medium/url)");
            //Filters videos that can be embedded to other websites
            //query.setVideoEmbeddable("true");
            //Filters videos that can be viewed outside of youtube website
            query.setVideoSyndicated("true");
        }catch(IOException e){
            Log.d("YoutubeSearchHandler", "Could not initialize: " + e);
        }
    }

    public List<YoutubeVideoItem> search(String keywords){
        query.setQ(keywords);
        try{
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();

            List<YoutubeVideoItem> items = new ArrayList<YoutubeVideoItem>();
            for(SearchResult result:results){
                YoutubeVideoItem item = new YoutubeVideoItem();
                item.setTitle(result.getSnippet().getTitle());
                item.setDescription(result.getSnippet().getDescription());
                item.setThumbnailURL(result.getSnippet().getThumbnails().getMedium().getUrl());
                item.setId(result.getId().getVideoId());
                items.add(item);
            }
            return items;
        }catch(IOException e){
            Log.d("YoutubeSearchHandler", "Could not search: " + e);
            return null;
        }
    }
}
