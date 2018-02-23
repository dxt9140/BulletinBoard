/**
 * PostProxy.java
 * Author: Dominick Taylor (dxt9140@g.rit.edu)
 * Created: 12/2/2017
 * Proxy object for network communication. Receives messages from the Post.
*/

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.DataInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class PostProxy {

    /*
     * The tag size as indicated by the SHA-256 algorithm.
    */
    private static final int TAG_SIZE = 32;

    /*
     * The Socket that acts as a BulletinBoard to receive Posts.
    */
    private DatagramSocket bulletinBoard;

    /*
     * The listener object.Once a message is received, the listener is the
     *  object that completes authentication.
    */
    private BulletinBoardModel listener;

    /**
     * Construct a PostProxy object with the given Socket.
     * @param socket - The network socket to receive messages.
    */
    public PostProxy( DatagramSocket socket ) {
        this.bulletinBoard = socket;
    }

    /**
     * setListener
     * Set the listener object for this object. Also begins the ReaderThread
     *  which directly receives messages across the network. This is so that
     *  a message cannot be received unless an object exists to authenticate it.
     * @param listener - The listener to use.
    */
    public void setListener( BulletinBoardModel listener ) {
        this.listener = listener;
        new ReaderThread().start();
    }

    /*
     * A private class that is directly used to receive messages across the
     *  network.
    */
    private class ReaderThread extends Thread {

        /**
         * run
         * Run the thread. Indefinitely receives messages.
        */
        public void run() {

            byte[] payload = new byte[1024];
            byte[] byteMessage;
            byte[] tag = new byte[TAG_SIZE];
            try {
                for (;;) {
                    DatagramPacket packet = 
                        new DatagramPacket( payload, payload.length );

                    bulletinBoard.receive( packet );

                    DataInputStream in = new DataInputStream(
                        new ByteArrayInputStream(
                            payload, 0, packet.getLength() ) );

                    int length = packet.getLength();
                    if ( length < TAG_SIZE ) {
                        listener.error();
                        return;
                    }

                    listener.receive( payload, length );
                } 
            } catch ( IOException exc ) {
                listener.error();
            }

        }

    }

}
