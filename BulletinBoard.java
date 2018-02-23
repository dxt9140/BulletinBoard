/**
 * BulletinBoard.java
 * Author: Dominick Taylor (dxt9140@g.rit.edu)
 * Created: 11/30/2017
 * Main program for running a Bulletin Board where a user can send an encrypted message.
*/

import java.net.InetSocketAddress;
import java.net.DatagramSocket;
import edu.rit.util.Hex;
import java.nio.charset.Charset;

public class BulletinBoard {

    /* 
     * Main method of application run. Handles input and creates the bulletin
     * board repository.
    */
    public static void main( String[] args ) throws Exception {

        if ( args.length < 3 ) {
            usage();
        }

        String hostname = args[0];
        int port = Integer.parseInt( args[1] );
        byte[] key = Hex.toByteArray( args[2] );

        if ( key.length != 8 ) {
            System.err.println("Error: Key must correspond to 8 hexadecimal bytes.");
            System.exit(-1);
        }

        DatagramSocket bulletinboard = new DatagramSocket (
            new InetSocketAddress( hostname, port ) );
 
        BulletinBoardModel model = new BulletinBoardModel( key );
        PostProxy proxy = new PostProxy( bulletinboard );
        proxy.setListener( model );
    }

    /*
     * Prints the usage message and then exits. Replace <bhost> with the hostname, 
     * <bport> with the port number, and <key> with the 16-hexadecimal-bit key.
    */
    public static void usage() {
        System.out.println("Usage: java BulletinBoard <bhost> <bport> <key>");
        System.exit(-1);
    }

}
