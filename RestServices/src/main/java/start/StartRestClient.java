package start;

import basket.model.domain.Meci;
import basket.model.domain.TipMeci;
import basket.services.rest.ServiceException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import rest.client.MeciClient;

import java.util.Date;


public class StartRestClient {
    private final static MeciClient meciuriClient = new MeciClient();

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        Meci meciT = new Meci("1", "-10", "-20", new Date(), TipMeci.CALIFICARE, 49);
        try {
            show(() -> System.out.println(meciuriClient.create(meciT)));
            show(() -> {
                Meci[] res = meciuriClient.getAll();
                for (Meci u : res) {
                    System.out.println(u.getId() + ": " + u.getTip());
                }
            });
        } catch (RestClientException ex) {
            System.out.println("Exception ... " + ex.getMessage());
        }
    }


    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            System.out.println("Service exception" + e);
        }
    }
}
