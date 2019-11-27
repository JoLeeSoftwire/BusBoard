package training.busboard.web;

import training.busboard.Arrival;
import training.busboard.BusStop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BusInfo {
    private String postcode;
    HashMap<BusStop, Stream<Arrival>> nearbyBuses;

    public BusInfo(String postcode, HashMap<BusStop, Stream<Arrival>> buses) {
        Pattern pattern = Pattern.compile("([a-z,A-Z]+[0-9]+[a-z,A-Z]?)( )?([0-9][a-z,A-Z]{2})");
        Matcher matcher = pattern.matcher(postcode);
        matcher.matches();
        this.postcode = matcher.group(1) + " " + matcher.group(3);
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
