import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.HashMap;
import java.io.*;
import java.net.*;
/**
* Write a description of class Router here.
*
* @author (your name)
* @version (a version number or a date)
*/
public class Router
{
  // instance variables
  private static String ipAddress;
  private static String filename;
  private static int portNumber;
  private static DistanceVector distV = new DistanceVector();
  private Socket clientSocket;
  private InetAddress ipAdd;
  private static boolean poisonedReverse;
  private ServerSocket serverSocket;
  private InputStream inputStream;
  private DataOutputStream socketOut;
  /**
  * Constructor for objects of class Router
  */
  public Router(boolean reverse, String filename)
  {
    // initialise instance variables
    poisonedReverse = reverse;
    //read file and convert to DistanceVector Object
    BufferedReader br = null;
    FileReader fr = null;

  }

  public static void main(String[] args) throws Exception
  {
    poisonedReverse = Boolean.parseBoolean(args[0]);
    filename = args[1];

    BufferedReader br = null;
    FileReader fr = null;

    try {

      fr = new FileReader(filename);
      br = new BufferedReader(fr);

      String sCurrentLine;

      if ((sCurrentLine = br.readLine()) != null) {    //parses first line for this router's info
      System.out.println(sCurrentLine);
      String[] parts = sCurrentLine.split(" ");
      ipAddress = parts[0];
      portNumber = Integer.parseInt(parts[1]);
    }

    while ((sCurrentLine = br.readLine()) != null) {  //parses rest of file for distance vector info
      System.out.println(sCurrentLine);
      String[] parts = sCurrentLine.split(" ");
      int tempPortNumber = Integer.parseInt(parts[1]);
      int tempWeight = Integer.parseInt(parts[2]);
      distV.updateNeighbor(parts[0], tempPortNumber, tempWeight);
      distV.addNeighbor(parts[0], tempPortNumber, tempWeight);
    }

  } catch (IOException e) {

    e.printStackTrace();

  } finally {

    try {

      if (br != null)
      br.close();

      if (fr != null)
      fr.close();

    } catch (IOException ex) {

      ex.printStackTrace();

    }

  }
  startServer();
  //startSender();

  Timer timer = new Timer();

  timer.scheduleAtFixedRate(new TimerTask() {
    @Override
    public void run() {
      sendUpdates();
    }
  }, 100, 10000);

  run();
}

public static void startSender(String neighborIP, int neighborPort, int sendType, int weight, String messageText) {
  (new Thread() {
    @Override
    public void run() {
      try {
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName(neighborIP);
        byte[] sendData = new byte[1024];

        Message message;

        switch(sendType) {
          default:
          //Distance Vector
          case 1:
          message = new Message(neighborIP, neighborPort, distV, ipAddress, portNumber);
          break;
          //Weight update
          case 2:
          message = new Message(neighborIP, neighborPort, weight, ipAddress, portNumber);
          break;
          //Message
          case 3:
          String updatedMessageText =  messageText + " " + ipAddress + ":" + portNumber;
          message = new Message(neighborIP, neighborPort, updatedMessageText, ipAddress, portNumber);
          break;
        }

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(5000);
        ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
        os.flush();
        os.writeObject(message);
        os.flush();
        //retrieves byte array
        byte[] sendBuf = byteStream.toByteArray();

        DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, IPAddress, neighborPort);
        clientSocket.send(sendPacket);
        clientSocket.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }).start();
}

public static void startServer() {
  (new Thread() {
    @Override
    public void run() {
      try {
        DatagramSocket serverSocket = new DatagramSocket(portNumber);
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        while(true) {
          receiveData = new byte[1024];
          sendData = new byte[1024];
          DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
          serverSocket.receive(receivePacket);
          byte[] sentence = receivePacket.getData();

          int byteCount = receivePacket.getLength();
          ByteArrayInputStream byteStream = new
          ByteArrayInputStream(sentence);
          ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream));
          Message message = (Message) is.readObject();
          is.close();

          System.out.println("new dv received from " + message.getSenderRouter() + " with the following distances:");
          Message.getDistanceVector().printSentDistanceVector();
        }
      } catch(Exception e) {
        e.printStackTrace();
      } finally {

      }
    }
  }).start();
}

public static void run() {
  (new Thread() {
    @Override
    public void run() {
      try {
      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
      String input = inFromUser.readLine();
      String[] parts = input.split(" ", 4);

      switch(parts[0]) {
        case "PRINT":

        break;
        case "MSG":

        break;
        case "CHANGE":

        break;
        default:
          System.out.println("Invalid command");
          break;
      }
    } catch (Exception e) {

    }
    }
  }).start();
}

/**
* method that sends updated weights to all neighbors
*
*/
public static boolean sendUpdates()
{
  HashMap<String, Integer> neighbors = distV.getNeighbors();
  for (String key : neighbors.keySet()) {
    String IPAddress = key.split(":")[0];
    int port = Integer.parseInt(key.split(":")[1]);

    startSender(IPAddress, port, 1, 0, "");
  }
  System.out.println("Update sent to all neighbors at time t(in seconds)");
  distV.printSentDistanceVector();
  return true;
}
/**
* method that recieves all updated weights from neighbors
*
*/
public boolean recieveUpdates()
{
  return true;
}
/**
* method that sends a message to a specific neighbor
*
*/
public boolean sendMessage(String message, String ip, int port)
{
  return true;
}
/**
* method that sends a message to a specific neighbor
*
*/
public boolean updateWeight(int weight, String ip, int port)
{
  return true;
}
/**
* method that prints the current Distance Vector and the Distance Vectors received from the neighbors
*
*/
public boolean Print()
{
  return true;
}
}
