package training.busboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Arrival {
    String id;
    String vehicleId;
    String naptanId;
    String stationName;
    String lineId;
    String destinationName;
    String timestamp;
    int timeToStation;
    String towards;
    String expectedArrival;
    // time until bus leaves stop
    String timeToLive;
    String modeName;
}
