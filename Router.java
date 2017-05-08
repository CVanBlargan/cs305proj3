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
  private String filename;
  private int portNumber;
  private DistanceVector distV;
  private Socket clientSocket;
  private InetAddress ipAdd;
  private boolean poisonedReverse;
  private ServerSocket serverSocket;
  private InputStream inputStream;
  private DataOutputStream socketOut;
  private int updateTime = 10000;

  public static void main(String[] args) throws Exception
  {
    Router router = new Router();
    router.poisonedReverse = Boolean.parseBoolean(args[0]);
    router.filename = args[1];

    router.setup(router.filename);

    router.startServer();
    //startSender();

    Timer timer = new Timer();

    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        router.sendUpdates();
      }
    }, 100, router.updateTime);

    router.run();
  }

  public void setup(String filename) {
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
      distV = new DistanceVector(parts[0] + ":" + parts[1], poisonedReverse);
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
}

public void startSender(String neighborIP, int neighborPort, int sendType, int weight, String messageText, DistanceVector dV) {
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
          message = new Message(neighborIP, neighborPort, dV, ipAddress, portNumber);
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

public void startServer() {
  (new Thread() {
    @Override
    public void run() {
      try {
        DatagramSocket serverSocket = new DatagramSocket(portNumber);
        byte[] receiveData = new byte[20480];
        while(true) {
          receiveData = new byte[20480];
          DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
          serverSocket.receive(receivePacket);
          byte[] sentence = receivePacket.getData();

          int byteCount = receivePacket.getLength();
          ByteArrayInputStream byteStream = new
          ByteArrayInputStream(sentence);
          ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream));
          Message message = (Message) is.readObject();
          is.close();

          switch(message.getType()) {
            case 1:
            System.out.println("new dv received from " + message.getSenderRouter() + " with the following distances:");
            message.getDistanceVector().printSentDistanceVector();
            recieveUpdates(message.getDistanceVector(), message.getSenderRouter());
            break;
            case 2:
            String sender = message.getSenderRouter();
            int newWeight = message.getWeight();
            System.out.println("New weight to neighbor " + sender + " of " + newWeight);
            distV.updateNeighbor(sender.split(":")[0], Integer.valueOf(sender.split(":")[0]), newWeight);
            System.out.println("new dv calculated:");
            distV.printCalculatedDistanceVector();
            break;
            case 3:
            String dest = message.getDest();
            if (dest.equals(ipAddress + ":" + portNumber)) {
              System.out.println(message.getMessage());
            } else {
              String destIP;
              int destPort;

              //pull from routing table
              if (!distV.getNextNode(dest).equals(null)) {
                String routedDest = distV.getNextNode(dest);
                destIP = routedDest.split(":")[0];
                destPort = Integer.valueOf(routedDest.split(":")[1]);

                String msg = message.getMessage();
                startSender(destIP, destPort, 3, 0, msg, distV);
                System.out.println("Message msg from ipAddr:port to ipAddr:port forwarded to " + routedDest);
                System.out.println(msg + " " + ipAddress + ":" + portNumber);
              }
            }
            default:
            break;
          }
        }
      } catch(Exception e) {
        e.printStackTrace();
      } finally {

      }
    }
  }).start();
}

public void run() {
  (new Thread() {
    @Override
    public void run() {
      while (true) {
        try {
          BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
          String input = inFromUser.readLine();
          String[] parts = input.split(" ", 4);

          switch(parts[0]) {
            case "PRINT":
            System.out.println("Current distance vector:");
            distV.printSentDistanceVector();
            distV.printNeighborVectors();
            break;
            case "MSG":
            String destIP = parts[1];
            int destPort = Integer.valueOf(parts[2]);
            String msg = parts[3];
            startSender(destIP, destPort, 3, 0, msg, distV);
            break;
            case "CHANGE":
            destIP = parts[1];
            destPort = Integer.valueOf(parts[2]);
            int destWeight = Integer.valueOf(parts[3]);
            updateWeight(destWeight, destIP, destPort);
            distV.printCalculatedDistanceVector();
            break;
            default:
            System.out.println("Invalid command");
            break;
          }
        } catch (Exception e) {

        }
      }
    }
  }).start();
}

/**
* method that sends updated weights to all neighbors
*
*/
public boolean sendUpdates()
{
  if(poisonedReverse == false){
    HashMap<String, Integer> neighbors = distV.getNeighbors();
    for (String key : neighbors.keySet()) {
      String IPAddress = key.split(":")[0];
      int port = Integer.parseInt(key.split(":")[1]);

      startSender(IPAddress, port, 1, 0, "", distV);
    }
    System.out.println("Update sent to all neighbors at time t(in seconds)");
    distV.printSentDistanceVector();
    return true;
  }else{
    distV.updateTimeouts();

    HashMap<String, Integer> neighbors = distV.getNeighbors();
    try {
      for (String key : neighbors.keySet()) {


        DistanceVector temp = new DistanceVector(distV);

        String IPAddress = key.split(":")[0];
        int port = Integer.parseInt(key.split(":")[1]);
        startSender(IPAddress, port, 1, 0, "", distV.poisonedReverse(key, temp));

      }
    } catch (Exception e) {
      System.out.println(e);
    }

    return true;
  }
}
/**
* method that recieves all updated weights from neighbors
*
*/
public boolean recieveUpdates(DistanceVector dV, String sender) {
  distV.addVector(dV.getDistanceVector(), sender);
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
public boolean updateWeight(int weight, String ip, int port) {
  distV.updateNeighbor(ip, port, weight);
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
