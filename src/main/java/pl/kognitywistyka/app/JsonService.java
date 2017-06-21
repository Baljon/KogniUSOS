//package pl.kognitywistyka.app;
//
//import javax.annotation.PostConstruct;
//import javax.enterprise.context.ApplicationScoped;
//import javax.inject.Inject;
//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.MediaType;
//import org.apache.deltaspike.core.api.config.ConfigProperty;
//
///**
// * Created by Jakub on 21.06.2017.
// */
//@ApplicationScoped
//public class JsonService {
//
//    @Inject
//    @ConfigProperty(name = "usosuw.apikey")
//    private String apikey;
//
//    @Inject
//    @ConfigProperty(name = "usosuw.service")
//    private String service;
//
//    @Inject
//    @ConfigProperty(name = "usosuw.service.course")
//    private String course;
//
//    @Inject
//    @ConfigProperty(name = "usosuw.service.fac")
//    private String fac;
//
//    @Inject
//    @ConfigProperty(name = "usosuw.service.course.fields")
//    private String courseFields;
//
//    @Inject
//    @ConfigProperty(name = "usosuw.service.fac.fields")
//    private String facFields;
//
//    private Client client;
//    private WebTarget target;
//
//
//    public String CourseRESTtoBASE(String courseID) {
//        client = ClientBuilder.newClient();
//
//        target = client.target(
//                service + course)
//                .queryParam("id", courseID)
//                .queryParam("format", "json")
//                .queryParam("fields", courseFields)
//        ;
//        String TestOutput = "TEST";
//        return TestOutput;
//    }
//}
//
