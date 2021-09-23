package mobi.largemind.payoneer.data;

import com.squareup.moshi.Json;

import java.util.List;

public class PaymentsResponse {

    public Networks networks;

    public static class Networks {
        @Json(name = "applicable")
        public List<Network> networks;
    }

    public static class Network {
        public String code;
        public String label;
        public Links links;
    }

    public static class Links {
        public String logo;
    }
}
