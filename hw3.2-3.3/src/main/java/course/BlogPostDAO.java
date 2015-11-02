package course;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BlogPostDAO {
    MongoCollection<Document> postsCollection;

    public BlogPostDAO(final MongoDatabase blogDatabase) {
        postsCollection = blogDatabase.getCollection("posts");
    }

    // Return a single post corresponding to a permalink
    public Document findByPermalink(String permalink) {

        // XXX HW 3.2,  Work Here
		Document post = null;

		FindIterable<Document> cursor = postsCollection.find(new BasicDBObject("permalink", permalink)).limit(1);
		for (Document document : cursor) {
			post = document;
		}

        return post;
    }

    // Return a list of posts in descending order. Limit determines
    // how many posts are returned.
    public List<Document> findByDateDescending(int limit) {

        // XXX HW 3.2,  Work Here
        // Return a list of DBObjects, each one a post from the posts collection
        List<Document> posts = new ArrayList<Document>();
        
		FindIterable<Document> cursor = postsCollection.find ().sort(new BasicDBObject("date", -1)).limit(limit);
		for (Document document : cursor) {
			posts.add(document);
		}

        return posts;
    }


    public String addPost(String title, String body, List tags, String username) {

        System.out.println("inserting blog entry " + title + " " + body);

        String permalink = title.replaceAll("\\s", "_"); // whitespace becomes _
        permalink = permalink.replaceAll("\\W", ""); // get rid of non alphanumeric
        permalink = permalink.toLowerCase();


        // XXX HW 3.2, Work Here
        // Remember that a valid post has the following keys:
        // author, body, permalink, tags, comments, date
        //
        // A few hints:
        // - Don't forget to create an empty list of comments
        // - for the value of the date key, today's datetime is fine.
        // - tags are already in list form that implements suitable interface.
        // - we created the permalink for you above.

        // Build the post object and insert it
        Document post = new Document();
        
        post.append("title", title).append("author", username).append("permalink", permalink)
        .append("body", body).append("tags", tags).append("comments", new ArrayList<DBObject>())
        .append("date", Calendar.getInstance().getTime());

        postsCollection.insertOne(post);


        return permalink;
    }

    // Append a comment to a blog post
    public void addPostComment(final String name, final String email, final String body,
                               final String permalink) {

        // XXX HW 3.3, Work Here
        // Hints:
        // - email is optional and may come in NULL. Check for that.
        // - best solution uses an update command to the database and a suitable
        //   operator to append the comment on to any existing list of comments
		
		BasicDBObject postComment = new BasicDBObject("author", name);

        postComment.append("body", body);

        if(email != null && !email.isEmpty()) {
           postComment.append("email", email);
        }

        //DBObject post = this.findByPermalink(permalink);
        Document post = this.findByPermalink(permalink);

        //postsCollection.update(new BasicDBObject("_id", post.get("_id")), new BasicDBObject("$push", new BasicDBObject("comments", postComment)));
        Document query = new Document("_id", post.get("_id"));
        Document update = new Document("$push", new Document("comments", postComment));
        postsCollection.updateOne(query, update);
    }
}
