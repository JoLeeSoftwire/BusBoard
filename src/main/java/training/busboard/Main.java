package training.busboard;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.jackson.JacksonFeature;

import java.util.*;
import java.util.stream.Stream;

import static javax.swing.UIManager.get;

public class Main {

    public static void main(String args[]) throws Exception {
        HashMap<BusStop, List<Arrival>> output = new HashMap<>();

        Caller caller = new Caller();
        caller.setUp();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a postcode: ");
        String code = scanner.nextLine();

        // whatever postcode the user gives
        Postcode queryPostcode = caller.getPostcode(code);
        BusStops nearbyStops = caller.getNearbyStops(queryPostcode);

        // sort bus stops by proximity
        Collections.sort(nearbyStops.stopPoints);
        Stream<BusStop> stopStream = nearbyStops.stopPoints.stream();

        // take closest two stops
        stopStream.limit(2).forEach((s) -> output.put(s, caller.getArrivals(s.naptanId)));

        for(BusStop i:output.keySet()) {
            List<Arrival> arrivals = output.get(i);
            System.out.println("Bus stop: " + i.commonName + "\nDistance: " + Math.round(i.distance) +
                    "m\nArrivals: " + arrivals);
        }

        scanner.close();
    }
}

class Caller {
    private String TFL_URI = "https://api.tfl.gov.uk";
    private String POSTCODE_URI = "https://api.postcodes.io";
    private WebTarget tflTarget;
    private WebTarget postcodeTarget;

    public void setUp() throws Exception {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        tflTarget = client.target(TFL_URI);
        postcodeTarget = client.target(POSTCODE_URI);
    }

    public List<Arrival> getArrivals(String stopID) {
        List<Arrival> responseArrivals = tflTarget
                .path("/StopPoint/" + stopID + "/Arrivals")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<ArrayList<Arrival>>(){});
        return responseArrivals;
    }

    public BusStops getNearbyStops(Postcode p) {
        BusStops responseStops = tflTarget
                .path("/StopPoint")
                .queryParam("lat", p.latitude)
                .queryParam("lon", p.longitude)
                .queryParam("stopTypes", "NaptanPublicBusCoachTram")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<BusStops>(){});
        return responseStops;
    }

    public Postcode getPostcode(String postcode) {
        Postcode responsePostcode = postcodeTarget
                .path("/postcodes/" + postcode)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<Postcode>(){});

        return responsePostcode;
    }
}
