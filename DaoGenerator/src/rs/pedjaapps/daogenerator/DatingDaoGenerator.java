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

public class DatingDaoGenerator
{

    public static void main(String[] args) throws Exception
    {
        Schema schema = new Schema(1, "rs.pedjaapps.tvshowtracker.model");

        addMessage(schema);
        addQuestions(schema);

        new DaoGenerator().generateAll(schema, "./src-gen");
    }

    private static void addMessage(Schema schema)
    {
        Entity message = schema.addEntity("Message");
        message.implementsInterface("Parcelable");
        message.setHasKeepSections(true);
        message.addIdProperty();
        message.addStringProperty("text");
        message.addStringProperty("username").notNull();
        message.addStringProperty("user_photo_url");
        message.addIntProperty("user_id");
        message.addBooleanProperty("sent").notNull();
        message.addIntProperty("type").notNull();
        message.addBooleanProperty("unlocked").notNull();
        message.addBooleanProperty("seen").notNull();

        /*Entity photo = schema.addEntity("Photo");
        photo.implementsInterface("Parcelable");
        photo.setHasKeepSections(true);
        photo.addIntProperty("image_id").notNull();
        photo.addIntProperty("gallery_id").notNull();
        photo.addStringProperty("comment");
        photo.addIntProperty("version").notNull();
        photo.addIntProperty("adult");
        photo.addIntProperty("status");
        photo.addIntProperty("likes").notNull();
        photo.addIntProperty("liked").notNull();
        photo.addIntProperty("user_id").notNull();
        photo.addStringProperty("photo");
        photo.addStringProperty("thumb");

        Property messageId = photo.addLongProperty("message_id").notNull().getProperty();
        ToMany messageToPhoto = message.addToMany(photo, messageId);
        messageToPhoto.setName("photos");*/

    }


    private static void addQuestions(Schema schema)
    {
        Entity group = schema.addEntity("QuestionGroup");
        group.setHasKeepSections(true);
        group.addStringProperty("group_id").primaryKey();
        group.addIntProperty("side_id");
        group.addIntProperty("group_order");
        group.addBooleanProperty("group_searchable");
        group.addIntProperty("group_points");
        group.addStringProperty("group_name");
        group.addStringProperty("group_name_profile");
        group.addStringProperty("group_code_name");
        group.addStringProperty("group_prompt");
        group.addStringProperty("group_description");
        group.addStringProperty("group_text");

        Entity question = schema.addEntity("Question");
        question.addStringProperty("question_id").primaryKey();
        question.addIntProperty("question_order");
        question.addBooleanProperty("searchable");
        question.addStringProperty("type");
        question.addIntProperty("value_min");
        question.addIntProperty("value_max");
        question.addBooleanProperty("required");
        question.addBooleanProperty("sortable");
        question.addBooleanProperty("multiselect");
        question.addStringProperty("question_name");
        question.addStringProperty("question_name_profile");
        question.addStringProperty("question_name_code");
        question.addStringProperty("hide_from");
        question.addStringProperty("description");

        Property groupId = question.addStringProperty("group_id").notNull().getProperty();
        ToMany groupToQuestion = group.addToMany(question, groupId);
        groupToQuestion.setName("questions");

        Entity answer = schema.addEntity("Answer");
        answer.addIdProperty();
        answer.addStringProperty("text");

        Property questionId = answer.addStringProperty("question_id").notNull().getProperty();
        ToMany questionToAnswer = question.addToMany(answer, questionId);
        questionToAnswer.setName("answers");
    }

}
