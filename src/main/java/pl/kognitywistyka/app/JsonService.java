package pl.kognitywistyka.app;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.apache.deltaspike.core.api.config.ConfigProperty;

/**
 * Created by Jakub on 21.06.2017.
 */
@ApplicationScoped
public class JsonService {

    @Inject
    @ConfigProperty(name = "usosuw.apikey")
    private String apikey;

    private Client client;
    private WebTarget target;

    @PostConstruct
    protected void init() {
        client = ClientBuilder.newClient();
        //example query params: ?q=Turku&cnt=10&mode=json&units=metric
        target = client.target(
                "http://api.openweathermap.org/data/2.5/forecast/daily")
                .queryParam("cnt", "10")
                .queryParam("mode", "json")
                .queryParam("units", "metric")
                .queryParam("appid", apikey)
        ;
    }
}

