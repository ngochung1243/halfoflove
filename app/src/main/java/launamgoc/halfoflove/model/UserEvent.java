package launamgoc.halfoflove.model;

/**
 * Created by Admin on 12/18/2016.
 */

public class UserEvent {
    public User user;
    public AppEvent event;

    public UserEvent(){
        user = new User();
        event = new AppEvent();
    }

    public UserEvent(User user, AppEvent event){
        this.user = user;
        this.event = event;
    }
}
