package training.busboard.web;

import training.busboard.Arrival;
import training.busboard.BusStop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BusInfo {
    private final String postcode;
    HashMap<BusStop, Stream<Arrival>> nearbyBuses;

    public BusInfo(String postcode, HashMap<BusStop, Stream<Arrival>> buses) {
        this.postcode = postcode;
        this.nearbyBuses = buses;
    }

    public String getPostcode() {
        return postcode;
    }

    public ArrayList<String> getStops() {
        ArrayList<String> stopNames = new ArrayList<String>();
        for (BusStop busStop : nearbyBuses.keySet()) {
            stopNames.add(busStop.commonName);
        }
        return stopNames;
    }

    public BusStop getStopWithName(String name) {
        for (BusStop busStop : nearbyBuses.keySet()) {
            if(busStop.commonName == name) {
                return busStop;
            }
        }
        return null;
    }

    public ArrayList<Arrival> getArrivals(BusStop stop) {
        List<Arrival> aList = nearbyBuses.get(stop).collect(Collectors.toList());
        Collections.sort(aList);
        return new ArrayList<Arrival>(aList);
    }
}
