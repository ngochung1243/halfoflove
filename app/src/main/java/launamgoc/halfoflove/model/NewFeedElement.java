package launamgoc.halfoflove.model;

/**
 * Created by KhaTran on 12/14/2016.
 */

public class NewFeedElement {

    private int avatarId;
    private String name;
    private String day;
    private String content;
    private int image;
    private int id;

    public NewFeedElement(int avatarId, String name, String day, String content, int image, int id) {
        this.avatarId = avatarId;
        this.name = name;
        this.day = day;
        this.content = content;
        this.image = image;
        this.id = id;
    }

    public int getAva() {
        return avatarId;
    }

    public String getName() {
        return name;
    }

    public String getDay() {
        return day;
    }

    public String getContent() {
        return content;
    }

    public int getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
