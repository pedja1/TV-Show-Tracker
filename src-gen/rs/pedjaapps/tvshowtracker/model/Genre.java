package rs.pedjaapps.tvshowtracker.model;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table GENRE.
 */
public class Genre {

    private Long id;
    private String name;
    private long show_id;

    public Genre() {
    }

    public Genre(Long id) {
        this.id = id;
    }

    public Genre(Long id, String name, long show_id) {
        this.id = id;
        this.name = name;
        this.show_id = show_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getShow_id() {
        return show_id;
    }

    public void setShow_id(long show_id) {
        this.show_id = show_id;
    }

}