package training.busboard.commandLine;

import java.util.*;
import java.util.stream.Stream;
import training.busboard.*;


public class Main {

    public static void main(final String args[]) throws Exception {
        final HashMap<BusStop, Stream<Arrival>> output = new HashMap<>();

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
            final Stream<Arrival> arrivals = output.get(i);
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
