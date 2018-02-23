/**
 * BulletinBoardModel.java
 * Author: Dominick Taylor (dxt9140@g.rit.edu)
 * Created: 12/4/2017
 * Model class for the BulletinBoard application. Handles message decryption
 *  and authentication.
*/

import java.nio.charset.Charset;

public class BulletinBoardModel {

    /*
     * The expected length of the tag that the model should authenticate.
    */
    public static final int TAG_LENGTH = 32;

    /*
     * The key used for authentication.
    */
    private byte[] key;

    /**
     * Construct an instance of a BulletinBoardModel. It MUST have a key.
     * @param key - The key used to authenticate the message sent over the
     *               network.
    */
    public BulletinBoardModel( byte[] key ) {
        this.key = key;
    }

    /**
     * receive
     * Receive a packet from the network. This method begins by separating the
     *  payload into its two parts, the message and the tag. It is known where
     *  these values begin and end because the length of the tag is specified
     *  by the hash method and is sent at the end. Thus, everything before the
     *  tag is the message. This method then verifies the sent tag and displays
     *  the result.
     * @param byteMessage - The byte string sent over the network that is equal
     *                       to the original message sent.
     * @param tag - The tag that was computed by the Post object and sent across
     *               the network. Used for comparison with a local tag.
    */
    public void receive( byte[] payload, int length ) {
        if ( length < TAG_LENGTH ) {
            error();
            return;
        }
        
        byte[] tag = new byte[TAG_LENGTH];
        int tIndex = 0;
        for ( int i = length - TAG_LENGTH; i < length; i++ ) {
            tag[tIndex] = payload[i];
            tIndex++;
        }

        byte[] byteMessage = new byte[ length - TAG_LENGTH ];
        for ( int i = 0; i < length - TAG_LENGTH; i++ ) {
            byteMessage[i] = payload[i];
        }

        String message = new String( byteMessage, Charset.forName("UTF-8") );

        HMACComputer computer = new HMACComputer();
        computer.setMessage( message );
        computer.setKey( key );

        boolean result = computer.verify( tag );
   
        if ( result ) {
            success( message, key, tag );
        } else {
            forgery();
        }
    }

    /**
     * forgery
     * If the locally hashed tag did not equal the network tag, this method
     *  is called to indicate a forgery.
    */
    public void forgery() {
        System.out.println("FORGERY");
    }

    /**
     * error
     * If any part of the payload sent across the network was erroneous,
     *  this method is called to display an error.
    */
    public void error() {
        System.out.println("ERROR");
    }

    /**
     * success
     * If the locally hashed tag equaled the one sent over the network, this
     *  method indicates a success and prints the message, key, and encrypted
     *  tag.
     * @param message - The message sent across the network.
     * @param key - The key used for authentication.
     * @param tag - The tag computed by the Computer object.
    */
    public void success( String message, byte[] key, byte[] tag ) {
        System.out.println("HMAC/SHA-256");
        System.out.print("  Message = \"" + message + "\"");
        if ( message.length() > 0 ) {
            System.out.print(" = ");
            printByteArray( message.getBytes( Charset.forName("UTF-8") ) );
        } else {
            System.out.println();
        }
        System.out.print("  Key = ");
        printByteArray( key );
        System.out.print("  Tag = ");
        printByteArray( tag );
    }

    //
    // HELPER FUNCTIONS
    //

    /**
     * printByteArray
     * Prints an array of bytes followed by a newline.
     * @param arr - The array of bytes to print.
    */
    private void printByteArray( byte[] arr ) {
        for ( int i = 0; i < arr.length; i++ ) {
            System.out.printf( "%02x", arr[i] );
        } 
        System.out.println();
    }
}
