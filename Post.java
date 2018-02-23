/**
 * Post.java
 * Author: Dominick Taylor (dxt9140@g.rit.edu)
 * Created: 11/30/2017
 * Post program for the BulletinBoard network program. This class serves
 *  as the client program, which posts messages to the specified Bulletin Board
 *  program at <bhost> and <bport>.
*/

import java.net.InetSocketAddress;
import java.net.DatagramSocket;
import edu.rit.util.Hex;

/*
 * Post class specification. Sends messages to the Bulletin Board program.
*/ 
public class Post {
    
    /*
     * Main method.Handles command line arguments, performs some computations,
     *  and then posts the message.
     * @param args - Command line arguments.
    */
    public static void main( String[] args ) throws Exception {

        if ( args.length < 6 ) {
            usage();
        }

        String bhost = args[0];
        int bport = Integer.parseInt( args[1] );
        String phost = args[2];
        int pport = Integer.parseInt( args[3] );
        byte[] key = Hex.toByteArray( args[4] );
        String message = args[5];

        if ( key.length != 8 ) {
            System.err.println("Error: Key must correspond to 8 hexadecimal bytes.");
            System.exit(-1);
        }

        DatagramSocket poster = new DatagramSocket(
            new InetSocketAddress( phost, pport ) );

        BulletinBoardProxy messenger = new BulletinBoardProxy( poster, 
            new InetSocketAddress( bhost, bport ) );

        PostModel model = new PostModel();
        model.setListener( messenger );
        model.prepareAndSendMessage( message, key );
    }

    /*
     * usage
     * Prints an error message and exits if there are erroneous arguments.
     *  bhost and bport are the hostname and port number of the Bulletin Board,
     *  phost and pport serve the same purpose for the Post program, the
     *  key is the 16-hexadecimal-bit authentication key and message is
     *  is the encrypted message to send over the network.
    */
    public static void usage() {
        System.out.println("Usage: java Port <bhost> <bport>" + 
            "<phost> <pport> <key> <message>");
        System.exit(-1);
    }

} 
