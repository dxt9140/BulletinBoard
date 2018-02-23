/**
 * HMACComputer.java
 * Author: Dominick Taylor (dxt9140@g.rit.edu)
 * Created: 12/2/2017
 * This computer is used for encoding a message using HMAC and SHA-256. 
 *  Prepares the message for sending. Computes the SHA-256 encryption and 
 *  then sends the results to the proxy to send the message.
*/

import edu.rit.crypto.SHA256;
import edu.rit.util.Hex;
import java.nio.charset.Charset;
import java.io.IOException;

/*
 * Model class that computes the encryption.
*/
public class HMACComputer implements Computer {

    /*
     * The block size used by the HMAC computation.
    */
    public static final int BLOCK_SIZE = 64;

    /*
     * The message used for encryption.
    */
    private String message;

    /*
     * The key used for encryption.
    */
    private byte[] key;

    /**
     * Construct an instance of a HMACComputer
    */
    public HMACComputer() {
    }

    /**
     * setMessage
     * Set the message for this computer object.
     * @param message - The message used during encryption.
    */
    public void setMessage( String message ) {
        this.message = message;
    }

    /**
     * setKey
     * Set the key for this computer.
     * @param key - The key to use.
    */
    public void setKey( byte[] key ) {
        this.key = key;
        this.key = this.extendKey();
    }

    /**
     * compute
     * After creation, this message is called by a Post object to complete the
     *  HMAC computation.
    */
    public byte[] compute() {
 
        SHA256 hash = new SHA256(); 

        byte[] byteMessage = message.getBytes( Charset.forName("UTF-8") );
        byte[] ipad = prepareIpad();
        byte[] opad = prepareOpad();

        byte[] ipad_xor_arr = new byte[BLOCK_SIZE];
        byte[] opad_xor_arr = new byte[BLOCK_SIZE];

        for ( int i = 0; i < BLOCK_SIZE; i++ ) {
            ipad_xor_arr[i] = (byte) (key[i] ^ ipad[i]);
            opad_xor_arr[i] = (byte) (key[i] ^ opad[i]);
        }

        hash.hash( ipad_xor_arr );
        hash.hash( byteMessage );

        byte[] ipad_buf = new byte[ hash.digestSize() ];
        hash.digest( ipad_buf );

        hash.hash( opad_xor_arr );
        hash.hash( ipad_buf );

        byte[] opad_buf = new byte[ hash.digestSize() ];
        hash.digest( opad_buf );

        return opad_buf;
    }

    /**
     * verify
     * Given a valid tag, verify that it matches the computed tag given the
     *  same message and key.
     * @param vtag - The tag for authentication.
     * @return True if the computed tag matches vtag, false otherwise.
    */
    public boolean verify( byte[] vtag ) {
        byte[] local_tag = this.compute();
        for ( int i = 0; i < vtag.length; i++ ) {
            if ( vtag[i] - local_tag[i] != 0 ) {
                return false;
            }
        }
        return true;
    }

    /**
     * main method for unit testing.
     * @param args - Command line arguments
    */
    public static void main( String[] args ) {
        if ( args.length < 2 ) {
            System.err.println("Usage error.");
            System.exit(-1);
        }

        String message = args[0];
        byte[] key = Hex.toByteArray( args[1] );

        HMACComputer comp = new HMACComputer();
        comp.setMessage( message );
        comp.setKey( key );

        byte[] tag = comp.compute();
        for ( int i = 0; i < tag.length; i++ ) {
            System.out.printf( "%02x", tag[i] );
        }
        System.out.println();
    }

    //
    // HELPER FUNCTIONS
    //

    /**
     * prepareIpad
     * Creates the byte[] ipad according to the HMAC specification. Block size
     *  for this item is 64.
    */
    private byte[] prepareIpad() {
        byte[] ipad = new byte[BLOCK_SIZE];
        for ( int i = 0; i < BLOCK_SIZE; i++ ) {
            ipad[i] = 0x36;
        }
        return ipad;
    }

    /**
     * prepareOpad
     * Creates the byte[] opad according to the HMAC specification. Block size
     *  for this item is 64.
    */
    private byte[] prepareOpad() {
        byte[] opad = new byte[BLOCK_SIZE];
        for ( int i = 0; i < BLOCK_SIZE; i++ ) {
            opad[i] = 0x5c;
        }
        return opad;
    }

    /**
     * extendKey
     * Enforces that the key is equal to the block size as required by the
     *  algorithm. If the key is less than block size, it pads the rest with 
     *  0x00 bytes. If it is more, it only takes the first BLOCK_SIZE bytes.
    */ 
    private byte[] extendKey() {
        byte[] nkey = new byte[BLOCK_SIZE];
        for ( int i = 0; i < key.length; i++ ) {
            nkey[i] = key[i];
        }
        for ( int i = key.length; i < BLOCK_SIZE; i++ ) {
            nkey[i] = 0x00;
        }
        return nkey;
    }

}

