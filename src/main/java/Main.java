
public class Main {
    public static void main(String[] args) {
        MainApp.main(args);
    }
}

    /*
    1. Login. Dupa autentificarea cu succes, o noua fereastra se deschide in care sunt afișate toate meciurile
    (Echipa A vs Echipa B, Echipa B vs Echipa C, etc., Semifinala 1, Semifinala 2, Finala), pretul unui bilet si
    numarul locurilor disponibile. (V)

    2. Vanzare. Dupa autentificarea cu succes persoana de la casa poate vinde bilete la un anumit meci. Pentru
    vanzarea unui bilet se introduce numele clientului si numarul de locuri dorite. Dupa efectuarea unei vanzari,
    persoanele de la celelalte case vad lista actualizata a meciurilor si numarul locurilor disponibile. Daca la un
    meci nu mai sunt locuri disponbile, mesajul "SOLD OUT" este afișat langa meci cu culoare roșie. (V)

    3. Cautare. Persoana poate vizualiza meciurile pentru care mai sunt locuri disponibile, in ordine descrescatoare
    dupa numarul locurilor disponibile. (V)

    4. Logout. (V)

    */










/*
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        Validator<Bilet> biletValidator = BiletValidator.getInstance();
        Validator<Client> clientValidator = ClientValidator.getInstance();
        Validator<Meci> meciValidator = MeciValidator.getInstance();
        Validator<Echipa> teamValidator = EchipaValidator.getInstance();


        CrudRepository Brepo = new BiletDataBaseRepository(biletValidator);
        CrudRepository Crepo = new ClientDataBaseRepository(clientValidator);
        CrudRepository Mrepo = new MeciDataBaseRepository(meciValidator);
        CrudRepository Trepo = new EchipaDataBaseRepository(teamValidator);


        Bilet b1 = new Bilet("1", (float) 103.33, "1");
        Brepo.save(b1);
        Iterable<Bilet> Biterable = Brepo.findAll();
        List<Bilet> bilete = StreamSupport
                .stream(Biterable.spliterator(), false)
                .collect(Collectors.toList());
        for (Bilet b : bilete) {
            System.out.println(b);
        };
        Brepo.delete("1");
        System.out.println(Brepo.findOne("1"));




        Client c1 = new Client("1");
        Crepo.save(c1);
        Iterable<Client> Citerable = Crepo.findAll();
        List<Client> clienti = StreamSupport
                .stream(Citerable.spliterator(), false)
                .collect(Collectors.toList());
        for (Client c : clienti) {
            System.out.println(c);
        };
        Crepo.delete("1");
        System.out.println(Crepo.findOne("1"));




        Meci m1 = new Meci("1", "1", "2", Date.from(Instant.now()), TipMeci.CALIFICARE, 100);
        Mrepo.save(m1);
        Iterable<Meci> Miterable = Mrepo.findAll();
        List<Meci> meciuri = StreamSupport
                .stream(Miterable.spliterator(), false)
                .collect(Collectors.toList());
        for (Meci m : meciuri) {
            System.out.println(m);
        };
        Mrepo.delete("1");
        System.out.println(Mrepo.findOne("1"));




        Echipa t1 = new Echipa("1", "ana");
        Trepo.save(t1);
        Iterable<Echipa> iterable = Trepo.findAll();
        List<Echipa> echipe = StreamSupport
                .stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
        for (Echipa t : echipe) {
            System.out.println(t);
        };
        Trepo.delete("1");
        System.out.println(Trepo.findOne("1"));
    }

    public int max(int a, int b){
        return Math.max(a,b);
    }
    //tema 1 proiect - diagrama de clase (hartie / star uml) - pentru proiectul pe care il vom folosi intregul semestru
    //TODO: PROBLEMA 10 PROBLEMA 10 PROBLEMA 10 PROBLEMA 10 PROBLEMA 10 PROBLEMA 10 PROBLEMA 10
    //Enterprise Application Integration - pentru trimis obiecte prin socket
    //TREBUIE SA EXISTE ACELEEASI CLASE IN AMBELE LIMBAJE
}
*/