package training.busboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Postcode {
    public String postcode;
    public Double latitude;
    public Double longitude;
}
