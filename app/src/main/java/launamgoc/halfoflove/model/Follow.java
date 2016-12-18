package launamgoc.halfoflove.model;

import java.util.UUID;

/**
 * Created by Admin on 12/17/2016.
 */

public class Follow {
    public String id;
    public String id_follower;
    public String id_following;

    public Follow(){
        id = UUID.randomUUID().toString();
        id_follower = "";
        id_following = "";
    }
}
