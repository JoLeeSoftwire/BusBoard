package training.busboard;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.glassfish.jersey.jackson.JacksonFeature;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static javax.swing.UIManager.get;

public class Main {

    public static void main(final String args[]) throws Exception {
        final HashMap<BusStop, List<Arrival>> output = new HashMap<>();

        final Caller caller = new Caller();
        caller.setUp();
        final Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a postcode: ");
        final String code = scanner.nextLine();

        // whatever postcode the user gives
        final Postcode queryPostcode = caller.getPostcode(code);
        final BusStops nearbyStops = caller.getNearbyStops(queryPostcode);

        // sort bus stops by proximity
        Collections.sort(nearbyStops.stopPoints);
        final Stream<BusStop> stopStream = nearbyStops.stopPoints.stream();

        // take closest two stops
        stopStream.limit(2).forEach((s) -> output.put(s, caller.getArrivals(s.naptanId)));

        for (final BusStop i : output.keySet()) {
            final List<Arrival> arrivals = output.get(i);
            System.out.println(
                    "Bus stop: " + i.commonName + "\nDistance: " + Math.round(i.distance) + "m\nArrivals: " + arrivals);
        }

        scanner.close();
    }
}

class Caller {
    private final String TFL_URI = "https://api.tfl.gov.uk";
    private final String POSTCODE_URI = "https://api.postcodes.io";
    private WebTarget tflTarget;
    private WebTarget postcodeTarget;

    public void setUp() throws Exception {
        final Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        tflTarget = client.target(TFL_URI);
        postcodeTarget = client.target(POSTCODE_URI);
    }

    public List<Arrival> getArrivals(final String stopID) {
        final List<Arrival> responseArrivals = tflTarget.path("/StopPoint/" + stopID + "/Arrivals")
                .request(MediaType.APPLICATION_JSON).get(new GenericType<ArrayList<Arrival>>() {
                });
        return responseArrivals;
    }

    public BusStops getNearbyStops(final Postcode p) {
        final BusStops responseStops = tflTarget.path("/StopPoint").queryParam("lat", p.latitude)
                .queryParam("lon", p.longitude).queryParam("stopTypes", "NaptanPublicBusCoachTram")
                .request(MediaType.APPLICATION_JSON).get(new GenericType<BusStops>() {
                });
        return responseStops;
    }

    public Postcode getPostcode(final String postcode) throws IOException {
        final String responsePostcode = postcodeTarget
                .path("/postcodes/" + postcode)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        JsonNode postcodeNode = new ObjectMapper().readTree(responsePostcode);
        Postcode toReturn = new Postcode(
            postcodeNode.get("result").get("postcode").textValue(),
            postcodeNode.get("result").get("latitude").doubleValue(),
            postcodeNode.get("result").get("longitude").doubleValue());
        return toReturn;

    }
}
