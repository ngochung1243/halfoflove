package launamgoc.halfoflove.model;

import java.util.UUID;

/**
 * Created by Admin on 12/18/2016.
 */

public class AppEvent {
    public String id;
    public String fid;
    public String name;
    public String start_time;
    public String end_time;
    public String post_time;
    public String description;
    public String photo_url;
    public String video_url;

    public AppEvent(){
        id = UUID.randomUUID().toString();
        fid = "";
        name = "";
        start_time = "";
        end_time = "";
        post_time = "";
        description = "";
        photo_url = "";
        video_url = "";
    }
}
