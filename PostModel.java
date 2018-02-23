/**
 * PostModel.java
 * Author: Dominick Taylor (dxt9140@g.rit.edu)
 * Created: 12/4/2017
 * The model object used by a Post object to compute the encryption for
 *  the Post tag.
*/

import java.io.IOException;
import java.nio.charset.Charset;

/*
 * Class declaration for a PostModel.
*/
public class PostModel {

    /*
     * The listener object for this class.
    */
    private PostListener listener;

    /**
     * Construct an empty instance of this class.
    */
    public PostModel() {
    }

    /**
     * setListener
     * Set the listener for this class.
     * @param listener - The listener object to set.
    */
    public void setListener( PostListener listener ) {
        this.listener = listener;
    }

    /**
     * prepareAndSendMessage
     * Method used to take a message and a key, do some kind of Computation
     *  on it, and then telling the listener proxy object to send the message.
     * @param message - The message to use for encryption.
     * @param key - The key used for encryption.
    */
    public void prepareAndSendMessage( String message, byte[] key ) {
        HMACComputer computer = new HMACComputer();
        computer.setMessage( message );
        computer.setKey( key );
        byte[] byteMessage = message.getBytes( Charset.forName("UTF-8") );
        byte[] tag = computer.compute();
        try {
            listener.post( byteMessage, tag );
        } catch ( Exception exc ) {
            // Ignore
        }
    }

}
