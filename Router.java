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
  private String ipAddress;
  private static int portNumber;
  private static DistanceVector distV;
  private Socket clientSocket;
  private InetAddress ipAdd;
  private boolean poisonedReverse;
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
    portNumber = Integer.parseInt(args[0]);

    startServer();

    Timer timer = new Timer();

    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        sendUpdates();
      }
    }, 100, 10000);
    // if(args.length != 2)
    // {
    //   System.out.println("Please specify if this router uses poisoned reverse and give a valid filename, as such; 'java router true neighbors.txt'");
    //   System.exit(1);
    // } else {
    //   Router test = new Router(Boolean.parseBoolean(args[0]), args[1]);
    // }
  }

  public static void startSender(String neighborIP, int neighborPort, int sendType) {
    (new Thread() {
      @Override
      public void run() {
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost");
            byte[] sendData = new byte[1024];

            switch(sendType) {
              default:
              //Distance Vector
              case 1:
                sendData = "Hi".getBytes();
              break;
              //Weight update
              case 2:
                sendData = "sentence".getBytes();
              break;
              //Message
              case 3:
                sendData = "no".getBytes();
              break;
            }


            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, neighborPort);
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
            String sentence = new String(receivePacket.getData());
            System.out.println(sentence);
            // InetAddress IPAddress = receivePacket.getAddress();
            // int port = receivePacket.getPort();
            // String capitalizedSentence = sentence.toUpperCase();
            // sendData = capitalizedSentence.getBytes();
            // DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            // serverSocket.send(sendPacket);
          }
        } catch(Exception e) {
          e.printStackTrace();
        } finally {

        }
      }
    }).start();
  }

  public static void run(String[] args) throws Exception
  {
    if(args.length != 1 || args.length != 4 || args.length != 2){
      System.out.println("Please specify a command: PRINT, MSG<dst-ip> <dst-port> <msg>, or CHANGE <dst-ip> <dst-port> <new-weight>");
      //System.exit(1);
    }
    if(args.length == 1 && args[0].equals("PRINT")){
      System.out.println("Print");
      //this.print();
    }
    if(args.length == 4 && args[0].equals("MSG")) {
      System.out.println("Message");
      //this.sendMessage(args[3], args[1], Integer.parseInt(args[2]));
    }
    if(args.length == 4 && args[0].equals("CHANGE")){
      System.out.println("Change");
      //this.updateWeight(Integer.parseInt(args[3]), args[1], Integer.parseInt(args[2]));
    }
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

       startSender(IPAddress, port, 1);
     }
    //push DV to serverSocket output?
    System.out.println("sent");
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
