package training.busboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Postcode {
    public String postcode;
    public Double latitude;
    public Double longitude;

    Postcode(String _postcode, Double _latitude, Double _longitde) {
        postcode = _postcode;
        latitude = _latitude;
        longitude = _longitde;
    }
}
