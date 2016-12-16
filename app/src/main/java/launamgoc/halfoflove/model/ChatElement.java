package launamgoc.halfoflove.model;

/**
 * Created by KhaTran on 12/14/2016.
 */

public class ChatElement {

    private int avatarId;
    private String name;
    private int id;

    public ChatElement(int avatarId, String name, int id) {
        this.avatarId = avatarId;
        this.name = name;
        this.id = id;
    }

    public int getImage() {
        return avatarId;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
