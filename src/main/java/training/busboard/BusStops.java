package training.busboard;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BusStops {
    public Double[] centrePoint;
    public ArrayList<BusStop> stopPoints;
}
