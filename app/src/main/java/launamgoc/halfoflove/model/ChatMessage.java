package launamgoc.halfoflove.model;

import java.util.UUID;

/**
 * Created by Admin on 1/10/2017.
 */

public class ChatMessage {
    public String id = UUID.randomUUID().toString();;
    public String id_sender = "";
    public String id_receiver = "";
    public String name_sender = "";
    public String name_receiver = "";
    public String datetime = "";
    public String message = "";
}
