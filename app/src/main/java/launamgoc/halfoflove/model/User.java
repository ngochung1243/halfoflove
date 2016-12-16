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
    }

//    public User(String id, Map<String, String> value) {
//        this.fid = id;
//        this.fullname = value.get("fullname");
//        this.mood = value.get("mood");
//        this.mobile = value.get("mobile");
//        this.location = value.get("location");
//        this.bio = value.get("bio");
//        this.email = value.get("email");
//        this.birthday = value.get("birthday");
//        this.gender = value.get("gender");
//        this.hobby = value.get("hobby");
//    }
}
