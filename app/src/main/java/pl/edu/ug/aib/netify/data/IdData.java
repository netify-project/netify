package pl.edu.ug.aib.netify.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
//Class encapsulating server responses after posting data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdData implements Serializable{

    public String id;
}
