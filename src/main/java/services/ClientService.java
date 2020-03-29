package services;

import model.domain.Client;
import repositories.CrudRepository;
import model.validators.ValidationException;

public class ClientService {
    private CrudRepository<String, Client> clientRepository = null;
    Client findOne(String id){
        return clientRepository.findOne(id);
    }

    public ClientService(CrudRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    Iterable<Client> findAll() {
        return clientRepository.findAll();
    }
    Client save(Client entity) throws ValidationException {
        return clientRepository.save(entity);
    }
    Client delete(String id){
        return clientRepository.delete(id);
    }
    Client update(Client entity){
        return clientRepository.update(entity);
    }
}