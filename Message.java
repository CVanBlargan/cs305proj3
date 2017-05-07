import java.io.*;
/**
 * Write a description of class Message here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Message implements Serializable
{
    // instance variables - replace the example below with your own
    //
    private int type;

    private DistanceVector distV;
    private int newWeight;
    private String message;
    private String destIp;
    private int destPort;
    private String senderIP;
    private int senderPort;
    private int weight;
    /**
     * Overloaded Constructor for 3 different types of Messages as per project description
     */
    public Message(String ip, int port, String newMessage, String sendIP, int sendPort)
    {
      type = 3;
        // initialise instance variables
        message = newMessage;
        destIp = ip;
        destPort = port;
        senderIP = sendIP;
        senderPort = sendPort;
    }

    public Message(String ip, int port, DistanceVector newDistV, String sendIP, int sendPort)
    {
      type = 1;
        distV = newDistV;
        destIp = ip;
        destPort = port;
        senderIP = sendIP;
        senderPort = sendPort;
    }

    public Message(String ip, int port, int weight, String sendIP, int sendPort)
    {
      type = 2;
        weight = newWeight;
        destIp = ip;
        destPort = port;
        senderIP = sendIP;
        senderPort = sendPort;
    }

    public DistanceVector getDistanceVector() {
      return distV;
    }

    public String getSenderRouter() {
      return senderIP + ":" + senderPort;
    }

    public int getType() {
      return type;
    }
}
