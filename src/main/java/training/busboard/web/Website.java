package training.busboard.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import training.busboard.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Controller
@EnableAutoConfiguration
public class Website {

    @RequestMapping("/")
    ModelAndView home() {
        return new ModelAndView("index");
    }

    @RequestMapping("/busInfo")
    ModelAndView busInfo(@RequestParam("postcode") String postcode) throws Exception {
        boolean valid = validatePostcode(postcode);

        if(valid) {
            HashMap<BusStop, List<Arrival>> departures = getDeparturesNearby(postcode);
            return new ModelAndView("info", "busInfo", new BusInfo(postcode, departures));
        } else {
            return new ModelAndView("index", "error", true);
        }
    }

    private boolean validatePostcode(String postcode) {
        Pattern pattern = Pattern.compile("[a-z,A-Z]+[0-9]+( )?[0-9][a-z,A-Z]{2}");
        return pattern.matcher(postcode).matches();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Website.class, args);
    }

    public HashMap<BusStop, List<Arrival>> getDeparturesNearby(String postcode) throws Exception {
        final HashMap<BusStop, List<Arrival>> output = new HashMap<>();

        final Caller caller = new Caller();
        caller.setUp();

        final Postcode queryPostcode = caller.getPostcode(postcode);
        final BusStops nearbyStops = caller.getNearbyStops(queryPostcode);

        // sort bus stops by proximity
        Collections.sort(nearbyStops.stopPoints);
        final Stream<BusStop> stopStream = nearbyStops.stopPoints.stream();

        // take closest two stops
        stopStream.limit(2).forEach((s) -> output.put(s, caller.getArrivals(s.naptanId)));

        return output;

//        for (final BusStop i : output.keySet()) {
//            final List<Arrival> arrivals = output.get(i);
//            StringBuffer prettyArrivals = new StringBuffer();
//            // System.out.println(arrivals);
//            arrivals.forEach((a) -> {
//                prettyArrivals.append("\n");
//                prettyArrivals.append(a);
//            });
//            System.out.println(
//                    "Bus stop: " + i.commonName + "\nDistance: " + Math.round(i.distance) + "m\nArrivals: " + prettyArrivals);
//        }
    }

}