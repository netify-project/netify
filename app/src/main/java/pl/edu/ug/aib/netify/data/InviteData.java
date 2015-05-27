package pl.edu.ug.aib.netify.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Sandra on 2015-05-27.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InviteData extends NodeData implements Serializable {
    public String created;
    @JsonProperty("fromUser")
    public String fromUser;
    @JsonProperty("groupId")
    public String groupId;
    public String id;
    @JsonProperty("text")
    public String text;
    @JsonProperty("toUser")
    public Integer toUser;
}
