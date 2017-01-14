package launamgoc.halfoflove.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 1/9/2017.
 */

public class Message {
    @SerializedName("to")
    private String to = "";

    @SerializedName("data")
    private Map<String, String> data = new HashMap<>();

    public void setTo(String to){
        this.to = to;
    }

    public String getTo(){
        return this.to;
    }

    public void setData(Map<String, String> data){
        this.data = data;
    }

    public Map<String, String> getData(){
        return data;
    }
}
