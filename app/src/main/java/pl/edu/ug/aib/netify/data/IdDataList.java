package pl.edu.ug.aib.netify.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
//class for storing result of posting multiple records
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdDataList implements Serializable{

    @JsonProperty("record")
    public List<IdData> records = new ArrayList<IdData>();

}
