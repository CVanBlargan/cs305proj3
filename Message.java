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
    private DistanceVector distV;
    private int newWeight;
    private String message;
    private String destIp;
    private int destPort;
    private int weight;
    /**
     * Overloaded Constructor for 3 different types of Messages as per project description
     */
    public Message(String ip, int port, String newMessage)
    {
        // initialise instance variables
        message = newMessage;
        destIp = ip;
        destPort = port;
    }

    public Message(String ip, int port, DistanceVector newDistV)
    {
        distV = newDistV;
        destIp = ip;
        destPort = port;
    }

    public Message(String ip, int port, int weight)
    {
        weight = newWeight;
        destIp = ip;
        destPort = port;
    }
}
