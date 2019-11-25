package training.busboard;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.jackson.JacksonFeature;

public class Main {
    public static void main(String args[]) {
        // Your code here!
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
    }
}	
