package pl.edu.ug.aib.netify.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupData extends NodeData implements Serializable{

    public String id;
    public String name;
    public String thumbnail;
    public Integer founder;
    public String created;
    public String genre;
    public Boolean isPrivate;
    public String description;
}
