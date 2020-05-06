namespace java transformer
namespace netstd transformer

struct ClientDTO{
1:string sessionId,
2:string id,
3:string password,
4:string nume,
5:string ip,
6:i32 port
}

enum TipMeciDTO{
CALIFICARE = 1,
SAISPREZECIME = 2,
OPTIME = 3,
SFERT = 4,
SEMIFINALA = 5,
FINALA = 6
}


struct MeciDTO{
1:string id,
2:string home,
3:string away,
4:i64 date, // convert from long to Date ez pz (int is deprecated)
5:TipMeciDTO tip,
6:i32 numarBileteDisponibile
}


service TransformerService {
    string login(1:string id, 2:string username, 3:string password, 4:string host, 5:i32 port)
    list<MeciDTO> findAllMeciWithTickets()
    list<MeciDTO> findAllMeci(),
    void ticketsSold(1:MeciDTO meci, 2:ClientDTO loggedInClient)
}

// cd C:\Users\George\Documents\GitHub\Basket-Java-copy\Shared

// thrift-0.13.0.exe -gen java transformer.thrift
// thrift-0.13.0.exe -gen java message.thrift
