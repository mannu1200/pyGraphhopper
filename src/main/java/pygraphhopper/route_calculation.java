/**
 *Created by dariusaudryc on 28/2/17.
 */

package pygraphhopper;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.graphhopper.GraphHopper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.PathWrapper;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import static java.lang.Math.sqrt;

public class route_calculation {

  private static GraphHopper graphHopper;

  public route_calculation(String workDir, String vehicle, String osmFile) {
    // create one GraphHopper instance
    graphHopper = new GraphHopperOSM().forServer();
    graphHopper.setGraphHopperLocation(workDir); // "when graphhopper is calculating the route, it creates nodes. workDir is a temporary directory to store the nodes"
    graphHopper.setEncodingManager(new EncodingManager(vehicle));
    graphHopper.setDataReaderFile(osmFile); // "osmFile is the open street map data that allows us to create nodes for routing"
    graphHopper.importOrLoad();//The nodes will be called if the nodes had been created in this syntax, else the nodes will be created
  }

  public static double getDistance(double orig_lat, double orig_lon, 
                            double dest_lat, double dest_lon, String vehicle) {

    //System.out.println("Calculating route distance for following lat/long pair:" 
    //  + orig_lat + ", " + orig_lon + " & " + dest_lat + ", " + dest_lon + "  " + vehicle);


    GHRequest request = new GHRequest(orig_lat, orig_lon, dest_lat, dest_lon);// simple configuration of the request object
    request.setWeighting("fastest");
    request.setVehicle(vehicle);

    try {
      GHResponse route = graphHopper.route(request);
      PathWrapper path = route.getBest();// use the best path, see the GHResponse class for more possibilities.
      return path.getDistance() / 1000;
    } catch(Exception e){
      //TODO: find a better approach
      System.out.println("Exception caught: " + e.getMessage());
      System.out.println("Calculating RMS value for following lat/long pair:" 
      + orig_lat + ", " + orig_lon + " & " + dest_lat + ", " + dest_lon);

      double 
        dlat = dest_lat - orig_lat,
        dlon = dest_lon - orig_lon,
        dist2 = dlat * dlat + dlon * dlon,
        dist = sqrt(dist2);
      return(dist);
    }
  }

  public static void mainHelper(String FileInput, String FileOutput, String originInputLat, String originInputLong,
        String destInputLat, String destInputLong, String vehicle) {


    CSVReader reader = null;
    CSVWriter writer = null;

    System.out.println("Going to process file: " + FileInput + "...........\n");

    try {
      reader = new CSVReader(new FileReader(FileInput));
      writer = new CSVWriter(new FileWriter(FileOutput), ',');
    } catch (Exception e) {
      
      System.out.println("Arguments are not defined correctly" 
                      + e.getMessage());

    }

    String[] entries = null;
    Boolean firstRow = true;
    String[] header = null;

    int originLat = -1;
    int originLon = -1;
    int destLat = -1;
    int destLon = -1;

    try {
      while ((entries = reader.readNext()) != null) {

        List<String> val = new ArrayList<String>();

        if (firstRow) {
          firstRow = false;
          for (int i = 0; i < entries.length; i++) {
            if (entries[i].equals(originInputLat)) {
              originLat = i;
            }
            if (entries[i].equals(originInputLong)) {
              originLon = i;
            }
            if (entries[i].equals(destInputLat)) {
              destLat = i;
            }
            if (entries[i].equals(destInputLong)) {
              destLon = i;
            }
            val.add(entries[i]);
          }

          val.add("customer_route_dist");
          String[] new_val = new String[val.size()];
          val.toArray(new_val);

          writer.writeNext(new_val);
          continue;
        }

        for (int i = 0; i < entries.length; i++)
          val.add(entries[i]);

        Double mul = new Double(-1.0);

        if (originLat != -1 && originLon != -1 && destLat != -1 && destLon != -1) {
          mul = getDistance(Double.parseDouble(entries[originLat]), Double.parseDouble(entries[originLon]),
              Double.parseDouble(entries[destLat]), Double.parseDouble(entries[destLon]), vehicle);
        }

        val.add(mul.toString());

        String[] new_val = new String[val.size()];
        val.toArray(new_val);
        writer.writeNext(new_val);
      }

    } catch (Exception e) {
      System.out.println("Error in running the routing function" 
            + e.getMessage());
    }
    try {
      writer.close();
    } catch (Exception e) {
      System.out.println("Error in Writing to csv"
            + e.getMessage());
    }
  }

  //To be used form command line.
  public static void main(String[] args) {

    String 
      FileInput = args[0], 
      FileOutput = args[1], 
      originInputLat = args[2], 
      originInputLong = args[3],
      destInputLat = args[4], 
      destInputLong = args[5], 
      workDir = args[6], 
      osmFile = args[7], 
      vehicle = args[8];

    mainHelper(FileInput, FileOutput, originInputLat, originInputLong, 
            destInputLat, destInputLong, vehicle);

  }
}
