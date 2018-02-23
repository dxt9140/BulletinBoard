/**
 * PostListener.java
 * Author: Dominick Taylor (dxt9140@g.rit.edu)
 * Created: 12/1/2017
 * Interface for objects that listen to a Post object.
*/

import java.io.IOException;

/*
 * PostListener Interface
*/
public interface PostListener {

    /**
     * post
     * Post a message to the bulletin board.
     * @param byteMessage - The message in byte form to post.
     * @param key - The key used for authentication.
     * @throws IOException
    */
    public abstract void post( byte[] byteMessage, byte[] key) throws IOException;

}
