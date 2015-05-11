package pl.edu.ug.aib.netify.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SongData extends NodeData implements Serializable{

    public String id;
    @JsonProperty("groupid")
    public Integer groupId;
    @JsonProperty("parentid")
    public String parentId;
    public String title;
    public String thumbnail;
    @JsonProperty("user_id")
    public Integer userId;
    public String created;
    @JsonProperty("videoid")
    public String videoId;
}
