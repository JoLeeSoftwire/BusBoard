package training.busboard;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.jackson.JacksonFeature;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String args[]) throws Exception {
        Caller caller = new Caller();
        caller.setUp();
        caller.getArrivals("490008660N");
    }
}

class Caller {
    private String TFL_URI = "https://api.tfl.gov.uk";
    private String POSTCODE_URI = "https://api.postcodes.io";
    private WebTarget target;

    public void setUp() throws Exception {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        target = client.target(TFL_URI);
    }

    public void getArrivals(String stopID) {
        List<Arrival> responseArrivals = target
                .path("/StopPoint/" + stopID + "/Arrivals")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<ArrayList<Arrival>>(){});
        System.out.println(responseArrivals);
    }
}
