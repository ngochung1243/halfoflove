package launamgoc.halfoflove.model;

import java.util.UUID;

/**
 * Created by Admin on 12/17/2016.
 */

public class Relationship {
    public String id;
    public String id_request;
    public String id_accept;
    public String start_time;
    public String love_statement;

    public Relationship(){
        id = UUID.randomUUID().toString();
        id_accept = "";
        id_request = "";
        start_time = "";
        love_statement = "";
    }
}
