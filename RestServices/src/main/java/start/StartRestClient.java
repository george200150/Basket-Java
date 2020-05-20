package start;

import basket.model.domain.Meci;
import basket.model.domain.TipMeci;
import basket.services.rest.ServiceException;
import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
import rest.client.MeciClient;

import java.util.Date;


public class StartRestClient {
    private final static MeciClient meciuriClient = new MeciClient();

    public static void main(String[] args) {
        //RestTemplate restTemplate = new RestTemplate();
        Meci meciT = new Meci("0", "+99999", "-99999", new Date(), TipMeci.CALIFICARE, 98765);
        try {
            show(() -> System.out.println(meciuriClient.create(meciT)));
            show(() -> {
                Meci[] res = meciuriClient.getAll();
                for (Meci u : res) {
                    System.out.println(u.getId() + ": " + u.toString());
                }
            });
            show(() -> meciuriClient.getById(meciT.getId()));
            show(() -> meciuriClient.update(new Meci(meciT.getId(),"-000000","+000000",new Date(),TipMeci.FINALA,43210)));
            show(() -> meciuriClient.getById(meciT.getId()));
            show(() -> meciuriClient.delete(meciT.getId()));
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
