import java.util.HashMap;
/**
 * A hashmap of the different distances to the other nodes within the network of Routers.
 *
 * @author Matthew Beck and Chris VanBlargan
 */
public class DistanceVector
{
    // the distance vector is stored as a hashmap. The key is the ip address and port number in one string
    // the value stored is the weight and the next node on the path to get there
    private HashMap<String, String> dV;

    /**
     * Constructor for objects of class DistanceVector
     */
    public DistanceVector()
    {
        // initialise instance variables

    }

    /**
     * Add item to the router's distance vector. Meant to be called upon initial setup of neighbors.
     *
     * @param  ipAddress  the ipAddress of a router
     * @param  port  the port of a router
     * @param  weight the weight to get to that router from the current one
     * @return     true if added to the distance vector
     */
    public boolean addNeighbor(String ipAddress, int port, int weight)
    {
        //convert ipAddress, port into one identifying item
        //key is in the form of ipAddress:port#
        //value is in the form of weight:nextNode
        //value is only used here if the distance to neighbor is shorter than an already existing path
        String key = ipAddress + ":" + Integer.toString(port);
        String value = Integer.toString(weight) + ":" + key;

        //place item into hash map
        //if already exists, only place if weight is smaller
        if (dV.get(key) != null) {
          //pulls the integer value of the weight of the old entry in the hashmap
          int oldWeight = Integer.parseInt(dV.get(key).split(":")[0]);

          //new weight is smaller, update
          if (weight < oldWeight) {
            dV.put(key, value);
            return true;
          }
        } else {
          //no entry exists for neighbor, place in hashmap
          dV.put(key, value);
          return true;
        }
        //entry was not added
        return false;
    }

   /**
    *
    * @param vector the dstance vector receieved by the router to update
    * @param sender the router who sent the distance vector
    * @return true when distance vector is finished updating
    */
    public boolean addVector(DistanceVector vector, String sender) {
      return true;
    }
}
