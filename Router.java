import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileReader;

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
  private int portNumber;
  private DistanceVector distV;

  private boolean poisonedReverse;

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
      distV.add(parts[0], tempPortNumber, tempWeight);
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


public static void main(String[] args) throws Exception
{
  if(args.length != 1){
    System.out.println("Please specify a command: PRINT, MSG<dst-ip> <dst-port> <msg>, or CHANGE <dst-ip> <dst-port> <new-weight>");
    System.exit(1);
  }
  if(args.length == 1 && args[0].equals("PRINT")){
    System.out.println("Print");
  }
  if(args.length == 4 && args[0].equals("MSG")) {
    System.out.println("Message");
  }
  if(args.length == 4 && args[0].equals("CHANGE")){
    System.out.println("Change");
  }
}

/**
* An example of a method - replace this comment with your own
*
* @param  y   a sample parameter for a method
* @return     the sum of x and y
*/
public int print(int y)
{
  // put your code here
  return  y;
}
}
