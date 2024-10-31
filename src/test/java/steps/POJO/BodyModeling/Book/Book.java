package steps.POJO.BodyModeling.Book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    private int id;
    private String name;
    private String author;
    private String type;
    private boolean available;

    // Getters and setters for all fields
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

}