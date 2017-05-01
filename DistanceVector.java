import java.util.HashMap;
/**
 * A hashmap of the different distances to the other nodes within the network of Routers.
 *
 * @author Matthew Becka and Chris VanBlargan
 */
public class DistanceVector
{
    // instance variables - replace the example below with your own
    private HashMap dV;

    /**
     * Constructor for objects of class DistanceVector
     */
    public DistanceVector()
    {
        // initialise instance variables

    }

    /**
     * Add item to the router's distance vector
     *
     * @param  ipAddress  the ipAddress of a router
     * @param  port  the port of a router
     * @param  weighet the weight to get to that router from the current one
     * @return     true if added to the distance vector
     */
    public boolean add(String ipAddress, int port, int weight)
    {
        //convert ipAddress, port into one identifying item
        //key is in the form of ipAddress:port#
        String key = ipAddress + ":" + Integer.toString(port);

        //place item into hash map
        //if already exists, only place if weight is smaller
        if (dV.get(key) != null) {
          if (dV.get(key) > weight) {
            dV.put(key, weight);
            return true;
          }
        } else {
          dV.put(key, weight);
          return true;
        }
        return false;
    }
}
