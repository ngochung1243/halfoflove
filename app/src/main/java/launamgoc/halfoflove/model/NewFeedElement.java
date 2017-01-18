package launamgoc.halfoflove.model;

/**
 * Created by KhaTran on 12/14/2016.
 */

public class NewFeedElement {

    private String ava_url;
    private String name;
    private String starttime;
    private String endtime;
    private String content;
    private String photo_url;
    private String video_url;
    private int id;

    public NewFeedElement(String ava_url, String name, String starttime, String endtime, String content, String photo_url, String video_url, int id) {
        this.ava_url = ava_url;
        this.name = name;
        this.starttime = starttime;
        this.endtime = endtime;
        this.content = content;
        this.photo_url = photo_url;
        this.video_url = video_url;
        this.id = id;
    }

    public String getAva() {
        return ava_url;
    }

    public String getName() {
        return name;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return photo_url;
    }

    public String getVideo() {
        return video_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
