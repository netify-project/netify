package pl.edu.ug.aib.netify.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FriendData implements Serializable {

    public String id;
    @JsonProperty("UserId")
    public String userId;
    @JsonProperty("User2Id")
    public String user2Id;

}
