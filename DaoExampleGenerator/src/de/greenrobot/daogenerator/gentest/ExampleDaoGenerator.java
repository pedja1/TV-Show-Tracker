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
package de.greenrobot.daogenerator.gentest;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Generates entities and DAOs for the example project DaoExample.
 * 
 * Run it as a Java application (not Android).
 * 
 * @author Markus
 */
public class ExampleDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(3, "rs.pedjaapps.trakttvandroid");

        addUserConnections(schema);

        new DaoGenerator().generateAll(schema, "../../TV-Show-Tracker/src-gen");
    }

    private static void addUserConnections(Schema schema) {
        Entity user = schema.addEntity("User");
        user.setTableName("user");
        user.addIdProperty();
        user.addStringProperty("username").notNull();
        user.addStringProperty("full_name");
        user.addStringProperty("gender");
        user.addStringProperty("age");
        user.addStringProperty("location");
        user.addStringProperty("about");
        user.addLongProperty("joined");
        user.addLongProperty("last_login");
        user.addStringProperty("avatar");
        user.addStringProperty("url");
        user.addBooleanProperty("vip");
        user.addStringProperty("timezone");
        user.addBooleanProperty("use_24");
        user.addBooleanProperty("protected");
        user.addStringProperty("ratings_mode");
        user.addBooleanProperty("show_badges");
        user.addBooleanProperty("show_spoilers");
        user.addStringProperty("watching");
        user.addStringProperty("watched");
        
        Entity connection = schema.addEntity("Connection");
        connection.setTableName("connection");
        connection.addIdProperty();
        connection.addStringProperty("name").notNull();
        connection.addBooleanProperty("connected");
        connection.addBooleanProperty("timeline_enabled");
        connection.addBooleanProperty("share_scrobblers_start");
        connection.addBooleanProperty("share_scrobblers_end");
        connection.addBooleanProperty("share_tv");
        connection.addBooleanProperty("share_movies");
        connection.addBooleanProperty("share_ratings");
        connection.addBooleanProperty("share_checkins");

        Property userId = connection.addLongProperty("userId").notNull().getProperty();
        connection.addToMany(user, userId);
    }

    

}
