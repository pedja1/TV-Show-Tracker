package rs.pedjaapps.tvshowtracker.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResults {

    @SerializedName("Data") Data data;

    public Data getData() {
        return data;
    }

    public class Data{
        @SerializedName("Series") List<SearchResult> series;

        public List<SearchResult> getSeries() {
            return series;
        }

    }

    public class SearchResult{
        @SerializedName("seriesid") int seriesId;
        String language;
        @SerializedName("SeriesName") String seriesName;
        @SerializedName("Overview") String overview;
        @SerializedName("FirstAired") String firstAired;
        @SerializedName("Network") String network;

        SearchResult(int seriesId, String language, String seriesName, String overview, String firstAired, String network) {
            this.seriesId = seriesId;
            this.language = language;
            this.seriesName = seriesName;
            this.overview = overview;
            this.firstAired = firstAired;
            this.network = network;
        }

        public int getSeriesId() {
            return seriesId;
        }

        public void setSeriesId(int seriesId) {
            this.seriesId = seriesId;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getSeriesName() {
            return seriesName;
        }

        public void setSeriesName(String seriesName) {
            this.seriesName = seriesName;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getFirstAired() {
            return firstAired;
        }

        public void setFirstAired(String firstAired) {
            this.firstAired = firstAired;
        }

        public String getNetwork() {
            return network;
        }

        public void setNetwork(String network) {
            this.network = network;
        }
    }


}
