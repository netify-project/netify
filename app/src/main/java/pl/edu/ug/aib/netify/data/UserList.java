package pl.edu.ug.aib.netify.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserList implements Serializable{

    @JsonProperty("record")
    public List<User> records = new ArrayList<User>();

    public void deleteUser(int userId){
        for(User user : records){
            if(user.id == userId){
                records.remove(user);
                return;
            }
        }
    }

}
