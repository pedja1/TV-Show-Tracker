package rs.pedjaapps.tvshowtracker.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActorsList {

    @SerializedName("Actors") ActorsObject actorsObject;

    public ActorsObject getActorsObject() {
        return actorsObject;
    }

    public class ActorsObject {
        @SerializedName("Actor") List<Actor> actors;

        public List<Actor> getActorsList() {
            return actors;
        }
    }

}
