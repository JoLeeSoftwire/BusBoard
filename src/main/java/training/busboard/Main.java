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
            StringBuffer prettyArrivals = new StringBuffer();
            // System.out.println(arrivals);
            arrivals.forEach((a) -> {
                prettyArrivals.append("\n");
                prettyArrivals.append(a);
            });
            System.out.println(
                "Bus stop: " + i.commonName + "\nDistance: " + Math.round(i.distance) + "m\nArrivals: " + prettyArrivals);
        }

        scanner.close();
    }
}
