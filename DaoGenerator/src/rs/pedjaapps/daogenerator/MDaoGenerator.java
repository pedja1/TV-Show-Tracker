/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rs.pedjaapps.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MDaoGenerator
{

    public static void main(String[] args) throws Exception
    {
        Schema schema = new Schema(3, "rs.pedjaapps.tvshowtracker.model");

        addShow(schema);
        addSyncLog(schema);

        new DaoGenerator().generateAll(schema, "./src");
    }

    private static void addSyncLog(Schema schema)
    {
        Entity syncLog = schema.addEntity("SyncLog");
        syncLog.setHasKeepSections(true);
        syncLog.addIdProperty();
        syncLog.addStringProperty("status");
        syncLog.addStringProperty("message");
        syncLog.addIntProperty("type");
        syncLog.addStringProperty("show_title");
        syncLog.addDateProperty("time");

    }

    private static void addShow(Schema schema)
    {
        Entity user = schema.addEntity("User");
        user.setHasKeepSections(true);
        user.addStringProperty("username").primaryKey().notNull();
        user.addStringProperty("password").notNull();
        user.addStringProperty("full_name");
        user.addStringProperty("gender");
        user.addIntProperty("age");
        user.addStringProperty("location");
        user.addStringProperty("about");
        user.addLongProperty("joined");
        user.addLongProperty("last_login");
        user.addStringProperty("avatar");
        user.addStringProperty("url");
        //user.addBooleanProperty("true");
        user.addStringProperty("share_text_watched");
        user.addStringProperty("share_text_watching");

        Entity show = schema.addEntity("Show");
        show.setHasKeepSections(true);
        show.addLongProperty("tvdb_id").primaryKey().notNull();
        show.addStringProperty("title");
        show.addIntProperty("year");
        show.addStringProperty("url");
        show.addLongProperty("first_aired");
        show.addStringProperty("country");
        show.addStringProperty("overview");
        show.addIntProperty("runtime");
        show.addStringProperty("status");
        show.addStringProperty("network");
        show.addStringProperty("air_day");
        show.addStringProperty("air_time");
        show.addStringProperty("certification");
        show.addStringProperty("imdb_id");
        show.addIntProperty("tvrage_id");
        show.addLongProperty("last_updated");
        show.addIntProperty("rating");
        show.addIntProperty("votes");
        show.addIntProperty("loved");
        show.addIntProperty("hated");

        Property username = show.addStringProperty("username").notNull().getProperty();
        ToMany userToShow= user.addToMany(show, username);
        userToShow.setName("shows");

        Entity image = schema.addEntity("Image");
        image.implementsInterface("Parcelable");
        image.setHasKeepSections(true);
        image.addIdProperty();
        image.addStringProperty("poster");
        image.addStringProperty("fanart");
        image.addStringProperty("banner");

        /*Property showId = image.addLongProperty("show_id").notNull().getProperty();
        ToMany showToImage = show.addToMany(image, showId);
        showToImage.setName("images");*/
        Property imageId = show.addLongProperty("image_id").getProperty();
        show.addToOne(image, imageId);

        Entity actor = schema.addEntity("Actor");
        actor.implementsInterface("Parcelable");
        actor.setHasKeepSections(true);
        actor.addIdProperty();
        actor.addStringProperty("name");
        actor.addStringProperty("character");
        actor.addStringProperty("image");

        Property showId = actor.addLongProperty("show_id").notNull().getProperty();
        ToMany showToActor= show.addToMany(actor, showId);
        showToActor.setName("actors");

        Entity genre = schema.addEntity("Genre");
        genre.implementsInterface("Parcelable");
        genre.setHasKeepSections(true);
        genre.addIdProperty();
        genre.addStringProperty("name");

        showId = genre.addLongProperty("show_id").notNull().getProperty();
        ToMany showToGenre = show.addToMany(genre, showId);
        showToGenre.setName("genres");

        Entity episode = schema.addEntity("Episode");
        episode.implementsInterface("Parcelable");
        episode.setHasKeepSections(true);
        episode.addIdProperty();
        episode.addIntProperty("season").notNull();
        episode.addIntProperty("episode").notNull();
        episode.addIntProperty("tvdb_id");
        episode.addStringProperty("title");
        episode.addStringProperty("overview");
        episode.addLongProperty("first_aired");
        episode.addStringProperty("url");
        episode.addStringProperty("screen");
        episode.addIntProperty("rating");
        episode.addIntProperty("votes");
        episode.addIntProperty("loved");
        episode.addIntProperty("hated");
        episode.addBooleanProperty("watched").notNull();

        showId = episode.addLongProperty("show_id").notNull().getProperty();
        ToMany showToSeason = show.addToMany(episode, showId);
        showToSeason.setName("episodes");

    }
}
