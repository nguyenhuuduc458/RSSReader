package huuduc.nhd.rssreader.Entity;

import java.util.Collections;
import java.util.Enumeration;

public class FeedEntity implements Comparable<FeedEntity> {
    private String title;
    private String description;
    private String link;

    public FeedEntity() { }

    public FeedEntity(String title, String link, String description){
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

    @Override
    public int compareTo(FeedEntity o) {
        return this.title.compareTo(o.getTitle());
    }
}
