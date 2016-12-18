package launamgoc.halfoflove.model;

/**
 * Created by Admin on 11/21/2016.
 */

public class User {
    public String fid;
    public String fullname;
    public String birthday;
    public String photo_url;
    public String cover_url;
    public String gender;
    public String location;
    public String mobile;
    public String email;
    public String hobby;
    public String bio;
    public String mood;
    public String interested;
    public boolean allow_find;
    public boolean allow_see_timeline;

    public User(){
        fid = "";
        fullname = "";
        birthday = "";
        photo_url = "";
        cover_url = "";
        gender = "";
        location = "";
        mobile = "";
        email = "";
        hobby = "";
        bio = "";
        mood = "";
        interested = "";
        allow_find = true;
        allow_see_timeline = true;

        photo_url = "";
        cover_url = "";
    }
}
