/**
 * Computer.java
 * Author: Dominick Taylor (dxt9140@g.rit.edu)
 * Created: 12/4/2017
 * Interface used by any program that requires encryption of a message.
 *  A class using a particular encryption algorithm should extend this
 *  interface in order to be compatible with the BulletinBoard application.
*/

public interface Computer {

    /**
     * compute
     * Main method used by a Computer. This actual computation depends on 
     *  the particular class that implements this interface.
    */ 
    public byte[] compute();

}
