package cz.cvut.fit.urbanp11.main.data.model;

/**
 * Created by Petr Urban on 18.03.15.
 */
public class Article {
    int id;
    public String title;
    public String description;
    public String link;

    public Article(int id, String title, String description, String link) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
    }
}
