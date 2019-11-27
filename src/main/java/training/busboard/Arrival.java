package training.busboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Arrival implements Comparable<Arrival> {
    public String id;
    public String vehicleId;
    public String naptanId;
    public String stationName;
    public String lineId;
    public String destinationName;
    public String timestamp;
    public int timeToStation;
    public String towards;
    public String expectedArrival;
    // time until bus leaves stop
    public String timeToLive;
    public String modeName;
    
    @Override
    public String toString() {
        int minutesToArrival = (int) Math.ceil(timeToStation/60);
        return "Bus Number: "+lineId+"\nArriving at: "+timeToStation+"\nTime to arrival: "+minutesToArrival+"\n";
    };

    @Override
    public int compareTo(Arrival a) {
        return Integer.compare(timeToStation, a.timeToStation);
    }
}
