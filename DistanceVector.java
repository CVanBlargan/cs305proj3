import java.util.HashMap;
import java.util.Set;
import java.io.*;
/**
 * A hashmap of the different distances to the other nodes within the network of Routers.
 *
 * @author Matthew Beck and Chris VanBlargan
 */
public class DistanceVector implements Serializable
{
    // the distance vector is stored as a hashmap. The key is the ip address and port number in one string
    // the value stored is the weight and the next node on the path to get there
    private HashMap<String, String> dV;
    private HashMap<String, Integer> neighbors;
    private HashMap<String, HashMap<String, String>> neighborsDV;

    private String source;

    /**
     * Constructor for objects of class DistanceVector
     */
    public DistanceVector(String host) {
        // initialise instance variables
        neighbors = new HashMap<String, Integer>();
        dV = new HashMap<String, String>();
        neighborsDV = new HashMap<String, HashMap<String, String>>();

        source = host;
    }

   /**
    * Gets the hashmap storing the router's distance vector.
    * @return the hashmap storing the router's distance vector.
    */
    public HashMap<String, String> getDistanceVector() {
      return dV;
    }

    public HashMap<String, Integer> getNeighbors() {
      return neighbors;
    }

    /**
     * Add item to the router's distance vector. Meant to be called upon initial setup of neighbors.
     *
     * @param  ipAddress  the ipAddress of a router
     * @param  port  the port of a router
     * @param  weight the weight to get to that router from the current one
     * @return     true if added to the distance vector
     */
    public boolean updateNeighbor(String ipAddress, int port, int weight) {
        //convert ipAddress, port into one identifying item
        //key is in the form of ipAddress:port#
        //value is in the form of weight:nextNode
        //value is only used here if the distance to neighbor is shorter than an already existing path
        String key = ipAddress + ":" + Integer.toString(port);
        String value = Integer.toString(weight) + ":" + key;

        //place item into hash map
        //if already exists, only place if weight is smaller
        if (dV.containsKey(key)) {
          //pulls the integer value of the weight of the old entry in the hashmap
          int oldWeight = Integer.parseInt(dV.get(key).split(":")[0]);

          //new weight is smaller, update
          if (weight < oldWeight) {
            dV.put(key, value);
            return true;
          } else {
            String oldPath = dV.get(key).split(":", 2)[1];
            if (oldPath.equals(key)) {
              //old shortest path to neighbor was straight to neighbor. Need to update distance to this.
              dV.put(key, value);
            }
          }
        } else {
          //no entry exists for neighbor, place in hashmap
          dV.put(key, value);
          return true;
        }
        //entry was not added
        return false;
    }

    public boolean addNeighbor(String ipAddress, int port, int weight) {
      String key = ipAddress + ":" + Integer.toString(port);
      neighbors.put(key, weight);
      return true;
    }

   /**
    *
    * @param vector the distance vector receieved by the router to update
    * @param sender the router who sent the distance vector
    * @return true when distance vector is finished updating
    */
    public boolean addVector(HashMap<String, String> vector, String sender) {
      neighborsDV.put(sender, vector);

      if (!neighbors.containsKey(sender)) {
        neighbors.put(sender, Integer.parseInt(vector.get(sender).split(":")[0]));
      }

      for (String key : vector.keySet()) {
        String updatedIPAddress = key.split(":")[0];
        int updatedPort = Integer.parseInt(key.split(":")[1]);
        int updatedWeight = Integer.parseInt(vector.get(key).split(":")[0]);
        if (!key.equals(source)) {
          updateNeighbor(updatedIPAddress, updatedPort, updatedWeight);
        }
      }

      //check neighbors to see if any direct paths are now shorter
      for (String key : neighbors.keySet()) {
        if (Integer.parseInt(dV.get(key).split(":")[0]) > neighbors.get(key)) {
          updateNeighbor(key.split(":")[0], Integer.parseInt(key.split(":")[1]), neighbors.get(key));
        }
      }

      return true;
    }

    /**
     * Call after calculating a new distance vector to print results
     * @return true after printing
     */
    public boolean printCalculatedDistanceVector() {
      for (String key : dV.keySet()) {
        String node = key;
        String distance = dV.get(key).split(":", 2)[0];
        String nextHop = dV.get(key).split(":", 2)[1];
        System.out.println(key + " " + distance + " " + nextHop);
      }

      return true;
    }

    /**
     * Call when sending or recieving a distance vector to print the sent/recieved vector
     * @return true after printing
     */
    public boolean printSentDistanceVector() {
      for (String key : dV.keySet()) {
        String node = key;
        String distance = dV.get(key).split(":")[0];
        System.out.println(key + " " + distance);
      }

      return true;
    }

    public boolean printNeighborVectors() {
      for (String key : neighborsDV.keySet()) {
        HashMap<String, String> nDV = neighborsDV.get(key);
        System.out.println("Neighbor distance vector: " + key);
          for (String nDVKey : nDV.keySet()) {
            String node = nDVKey;
            String distance = nDV.get(nDVKey).split(":")[0];
            System.out.println(node + " " + distance);
          }
    }
    return false;
  }

  public String getNextNode(String dest) {
    if (!dV.containsKey(dest)) {
      return null;
    }

    return dV.get(dest).split(":", 2)[1];
  }

  public DistanceVector poisonedReverse(String dest) throws Exception {
    DistanceVector temp = (DistanceVector) this.clone();

    for (String key : dV.keySet()) {
        String nextPath = dV.get(key).split(":", 2)[1];
        if (nextPath.contains(dest)) {
          temp.updateNeighbor(nextPath.split(":")[0], Integer.valueOf(nextPath.split(":")[1]), 9999);
        }
    }

    return temp;
  }
}
