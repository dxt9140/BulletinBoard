/**
 * BulletinBoardProxy.java
 * Author: Dominick Taylor (dxt9140@g.rit.edu)
 * Created: 12/1/2017
 * Proxy class on the Bulletin Board side of the application. Receives messages
 *  and differs to the BulletinBoard model for decryption.
*/

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.net.InetSocketAddress;

public class BulletinBoardProxy implements PostListener {

    /*
     * The socket that posts messages.
    */
    private DatagramSocket poster;

    /*
     * The address that receives the message.
    */
    private SocketAddress address;

    /**
     * Creates a BulletinBoardProxy object.
     * @param socket - The socket to use for network communication.
     * @param address - Th address of the message sender.
    */
    public BulletinBoardProxy( DatagramSocket socket, SocketAddress address ) {
        this.poster = socket;
        this.address = address;
    }

    /**
     * post
     * Post a message to the Bulletin Board.
     * @param byteMessage - The message to send across the network, in byte
     *                       form.
     * @param tag - The tag computation completed by the Computer.
     * @throws IOException
    */
    public void post( byte[] byteMessage, byte[] tag ) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( baos );
        for ( int i = 0; i < byteMessage.length; i++ ) {
            out.writeByte( byteMessage[i] );
        }
        for ( int i = 0; i < tag.length; i++ ) {
            out.writeByte( tag[i] );
        }
        out.close();

        byte[] payload = baos.toByteArray();
        poster.send( new DatagramPacket( payload, payload.length, address ) );
    }

}
