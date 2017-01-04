package launamgoc.halfoflove.model;

/**
 * Created by KhaTran on 11/11/2016.
 */

public class DiaryContent {

    private String title;
    private String content;
    private int imageId;
    private int videoId;
    private int id;

    public DiaryContent(){

    }

    public DiaryContent(int image, int video, String title, String content, int id) {
        this.imageId = image;
        this.videoId = video;
        this.title = title;
        this.content = content;
        this.id = id;
    }

    public int getImage() {
        return imageId;
    }

    public int getVideo() {
        return videoId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
