package huuduc.nhd.rssreader.Entity;

public class FeedEntity {
    private String title;
    private String description;
    private String link;

    public void FeedEntity() { }

    public void FeedEntity(String title, String link, String description){
        this.description = description;
        this.title       = title;
        this.link        = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "FeedEntity{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
