package pl.edu.ug.aib.netify.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
    public Integer id;
    @JsonProperty("session_id")
    public String sessionId;
    @JsonProperty("display_name")
    public String displayName;
    @JsonProperty("first_name")
    public String firstName;
    @JsonProperty("last_name")
    public String lastName;
    public String email;

    //for selection purposes in InviteFriendsFragment
    @JsonIgnore
    public Boolean isChecked = false;
}
