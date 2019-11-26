package training.busboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BusStop implements Comparable<BusStop>{
    public String naptanId;
    public String indicator;
    public String commonName;
    public Double distance;

    @Override
    public int compareTo(BusStop b) {
        return Double.compare(distance, b.distance);
    }

    public String getCommonName() {
        return commonName;
    }
}
