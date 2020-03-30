package dto;

import model.domain.*;

import java.util.Date;

public class DTOUtils {
    public static Client getFromDTO(UserDTO usdto){
        String id=usdto.getId();
        String pass=usdto.getPasswd();
        Client client = new Client(id, pass);
        client.setNume(usdto.getNume());
        return client;
    }
    public static UserDTO getDTO(Client user){
        String id=user.getId();
        String pass=user.getPassword();
        String nume=user.getNume();
        return new UserDTO(id, pass, nume);
    }

    public static UserDTO[] getDTO(Client[] users){
        UserDTO[] frDTO=new UserDTO[users.length];
        for(int i=0;i<users.length;i++)
            frDTO[i]=getDTO(users[i]);
        return frDTO;
    }

    public static Client[] getFromDTO(UserDTO[] users){
        Client[] friends=new Client[users.length];
        for(int i=0;i<users.length;i++){
            friends[i]=getFromDTO(users[i]);
        }
        return friends;
    }


    public static Meci getFromDTO(MeciDTO meciDTO) {
        String id = meciDTO.getId();
        String home = meciDTO.getHome();
        String away = meciDTO.getAway();
        Date date = meciDTO.getDate();
        TipMeci tip = meciDTO.getTip();
        int numarBileteDisponibile = meciDTO.getNumarBilete();
        return new Meci(id, home, away, date, tip, numarBileteDisponibile);
    }
    public static Meci[] getFromDTO(MeciDTO[] meciDTOs) {
        Meci[] meciuri = new Meci[meciDTOs.length];
        for (int i = 0; i < meciDTOs.length; i++) {
            meciuri[i] = getFromDTO(meciDTOs[i]);
        }
        return meciuri;
    }
    public static MeciDTO getDTO(Meci meci){
        String id = meci.getId();
        String home = meci.getHome();
        String away = meci.getAway();
        Date date = meci.getDate();
        TipMeci tip = meci.getTip();
        int numarBileteDisponibile = meci.getNumarBileteDisponibile();
        return new MeciDTO(id, home, away, date, tip, numarBileteDisponibile);
    }

    public static MeciDTO[] getDTO(Meci[] meciuri){
        MeciDTO[] meciuriDTO = new MeciDTO[meciuri.length];
        for (int i = 0; i < meciuri.length; i++) {
            meciuriDTO[i] = getDTO(meciuri[i]);
        }
        return meciuriDTO;
    }



    public static Bilet[] getFromDTO(BiletDTO[] bileteDTOs) {
        Bilet[] bilete = new Bilet[bileteDTOs.length];
        for (int i = 0; i < bileteDTOs.length; i++) {
            bilete[i] = getFromDTO(bileteDTOs[i]);
        }
        return bilete;
    }
    public static BiletDTO[] getDTO(Bilet[] bilete){
        BiletDTO[] biletdtos = new BiletDTO[bilete.length];
        for (int i = 0; i < bilete.length; i++) {
            biletdtos[i] = getDTO(bilete[i]);
        }
        return biletdtos;
    }
    public static BiletDTO getDTO(Bilet bilet){
        String id=bilet.getId();
        String numeClient = bilet.getNumeClient();
        float pret = bilet.getPret();
        String idMeci = bilet.getIdMeci();
        String idClient = bilet.getIdClient();

        return new BiletDTO(id, numeClient, pret, idMeci, idClient);
    }
    public static Bilet getFromDTO(BiletDTO biletDTO){
        String id=biletDTO.getId();
        String numeClient = biletDTO.getNumeClient();
        float pret = biletDTO.getPret();
        String idMeci = biletDTO.getIdMeci();
        String idClient = biletDTO.getIdClient();

        return new Bilet(id, numeClient, pret, idMeci, idClient);
    }
}
