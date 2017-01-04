package launamgoc.halfoflove.model;

/**
 * Created by KhaTran on 1/1/2017.
 */

public class Information {

    private String title;
    private String content;
    private int id;

    public Information(){

    }

    public Information(String title, String content, int id) {
        this.title = title;
        this.content = content;
        this.id = id;
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
