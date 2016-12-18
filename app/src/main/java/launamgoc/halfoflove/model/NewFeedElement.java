package launamgoc.halfoflove.model;

/**
 * Created by KhaTran on 12/14/2016.
 */

public class NewFeedElement {

    private String ava_url;
    private String name;
    private String day;
    private String content;
    private String photo_url;
    private int id;

    public NewFeedElement(String ava_url, String name, String day, String content, String photo_url, int id) {
        this.ava_url = ava_url;
        this.name = name;
        this.day = day;
        this.content = content;
        this.photo_url = photo_url;
        this.id = id;
    }

    public String getAva() {
        return ava_url;
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

    public String getImage() {
        return photo_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
