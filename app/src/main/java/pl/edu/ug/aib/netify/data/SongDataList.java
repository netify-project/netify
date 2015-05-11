package pl.edu.ug.aib.netify.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SongDataList implements Serializable{

    @JsonProperty("record")
    public List<SongData> records = new ArrayList<SongData>();

    //Returns SongData object with given videoId if exists, returns null otherwise
    public SongData findByVideoId(String targetVideoId){
        for(SongData songData : records){
            if(songData.videoId.contains(targetVideoId)) return songData;
        }
        return null;
    }
}
