package rs.pedjaapps.tvshowtracker.model;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import android.util.Log;

import java.util.Date;
// KEEP INCLUDES END


import rs.pedjaapps.tvshowtracker.BuildConfig;

/**
 * Entity mapped to table SYNC_LOG.
 */
public class SyncLog {

    private Long id;
    private String status;
    private String message;
    private Integer type;
    private String show_title;
    private java.util.Date time;

    // KEEP FIELDS - put your custom fields here

    public static final int TYPE_GENERAL = 1;
    public static final int TYPE_UP = 2;
    public static final int TYPE_DOWN = 3;

    public SyncLog(String status, String message, Integer type, String show_title)
    {
        this.status = status;
        this.message = message;
        this.type = type;
        this.show_title = show_title;
        this.time = new java.util.Date();

        if(BuildConfig.DEBUG) Log.d(getClass().getSimpleName(), toString());
    }
    // KEEP FIELDS END

    public SyncLog() {
    }

    public SyncLog(Long id) {
        this.id = id;
    }

    public SyncLog(Long id, String status, String message, Integer type, String show_title, java.util.Date time) {
        this.id = id;
        this.status = status;
        this.message = message;
        this.type = type;
        this.show_title = show_title;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getShow_title() {
        return show_title;
    }

    public void setShow_title(String show_title) {
        this.show_title = show_title;
    }

    public java.util.Date getTime() {
        return time;
    }

    public void setTime(java.util.Date time) {
        this.time = time;
    }

    // KEEP METHODS - put your custom methods here

    @Override
    public String toString()
    {
        return "SyncLog{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", type=" + type +
                ", show_title='" + show_title + '\'' +
                ", time=" + time +
                '}';
    }
    // KEEP METHODS END

}
