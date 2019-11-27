package training.busboard;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Caller {
    private final String TFL_URI = "https://api.tfl.gov.uk";
    private final String POSTCODE_URI = "https://api.postcodes.io";
    private WebTarget tflTarget;
    private WebTarget postcodeTarget;

    public void setUp() throws Exception {
        final Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        tflTarget = client.target(TFL_URI);
        postcodeTarget = client.target(POSTCODE_URI);
    }

    public Stream<Arrival> getArrivals(final String stopID) {
        final List<Arrival> responseArrivals = tflTarget.path("/StopPoint/" + stopID + "/Arrivals")
                .request(MediaType.APPLICATION_JSON).get(new GenericType<ArrayList<Arrival>>() {
                });
        final Stream<Arrival> arrivalStream = responseArrivals.stream().limit(5);

        return arrivalStream;
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
