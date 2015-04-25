package rs.pedjaapps.tvshowtracker.model;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import android.os.Parcel;
import android.os.Parcelable;
// KEEP INCLUDES END
/**
 * Entity mapped to table IMAGE.
 */
public class Image implements Parcelable {

    private String poster;
    private String fanart;
    private String banner;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Image() {
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getFanart() {
        return fanart;
    }

    public void setFanart(String fanart) {
        this.fanart = fanart;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    // KEEP METHODS - put your custom methods here
    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.poster);
        dest.writeString(this.fanart);
        dest.writeString(this.banner);
    }

    private Image(Parcel in)
    {
        this.poster = in.readString();
        this.fanart = in.readString();
        this.banner = in.readString();
    }

    public static Creator<Image> CREATOR = new Creator<Image>()
    {
        public Image createFromParcel(Parcel source)
        {
            return new Image(source);
        }

        public Image[] newArray(int size)
        {
            return new Image[size];
        }
    };
    // KEEP METHODS END

}