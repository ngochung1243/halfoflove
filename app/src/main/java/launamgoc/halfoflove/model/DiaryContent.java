package launamgoc.halfoflove.model;

/**
 * Created by KhaTran on 11/11/2016.
 */

public class DiaryContent {

    private String title;
    private String content;
    private String imageUrl;
    private String videoUrl;
    private int id;

    public DiaryContent(){

    }

    public DiaryContent(String title, String content, String image, String video, int id) {
        this.title = title;
        this.content = content;
        this.imageUrl = image;
        this.videoUrl = video;
        this.id = id;
    }

    public String getImage() {
        return imageUrl;
    }

    public String getVideo() {
        return videoUrl;
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
