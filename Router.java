import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Write a description of class Router here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Router
{
    // instance variables - replace the example below with your own
    private String ipAddress;
    private int portNumber;
    

    /**
     * Constructor for objects of class Router
     */
    public Router(boolean reverse, String filename)
    {
        // initialise instance variables
        if(reverse){       //if poisoned reverse
        
        }else{              //no poisoned reverse
        
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
