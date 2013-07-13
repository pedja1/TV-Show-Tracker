package rs.pedjaapps.tvshowtracker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SeriesData {

    @SerializedName("Data") Data data;

    public Data getData() {
        return data;
    }

    public class Data{
        @SerializedName("Series") Series series;
        @SerializedName("Episode") List<EpisodeItem> episodes;

        public Series getSeries() {
            return series;
        }

        public List<EpisodeItem> getEpisodes() {
            return episodes;
        }
    }




}
