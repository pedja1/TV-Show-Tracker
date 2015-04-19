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
import de.greenrobot.daogenerator.Schema;

public class MDaoGenerator
{

    public static void main(String[] args) throws Exception
    {
        Schema schema = new Schema(3, "rs.pedjaapps.tvshowtracker.model");

        addUser(schema);;

        new DaoGenerator().generateAll(schema, "./src");
    }

    private static void addUser(Schema schema)
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

    }
}
