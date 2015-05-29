package pl.edu.ug.aib.netify.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberGroupDataList implements Serializable{

    @JsonProperty("record")
    public List<MemberGroupData> records = new ArrayList<MemberGroupData>();

    public MemberGroupData getMemberGroupDataByUserId(String userId){
        for(MemberGroupData memberGroupData : records){
            if(memberGroupData.userId.equals(userId)) return memberGroupData;
        }
        return null;
    }
}
