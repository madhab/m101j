package com.mongodb.education;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;


public class App
{
    public static void main( String[] args ) throws UnknownHostException {

        MongoClient mongoClient = new MongoClient();
        DB school = mongoClient.getDB("school");
        DBCollection studentsCollection = school.getCollection("students");

        DBCursor students = studentsCollection.find();

        DBObject student = null;
        while(students.hasNext()) {
            student = students.next();

            List<DBObject> scores = (List<DBObject>)student.get("scores");

            DBObject lowestScore = null;

            for(DBObject scoreObject : scores) {

                double score = (Double)scoreObject.get("score");
                String scoreType = (String) scoreObject.get("type");

                if(lowestScore == null && "homework".equals(scoreType)) {
                    lowestScore = scoreObject;
                    continue;
                }

                if(lowestScore != null && (score < (Double)lowestScore.get("score") && ("homework").equals(scoreType))) {
                    lowestScore = scoreObject;
                }
            }

            scores.remove(lowestScore);

            System.out.println("Scores: " + scores + ", lowestScore homework: " + lowestScore);

            studentsCollection.update(new BasicDBObject("_id", student.get("_id")), new BasicDBObject("$set", new BasicDBObject("scores", scores)));
        }


    }
}
