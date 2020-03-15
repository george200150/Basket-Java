package services;

import domain.Bilet;
import repositories.CrudRepository;
import validators.ValidationException;

public class BiletService {
    private CrudRepository<String, Bilet> biletRepository = null;
    Bilet findOne(String id){
        return biletRepository.findOne(id);
    }
    public BiletService(CrudRepository biletRepository) {
        this.biletRepository = biletRepository;
    }
    Iterable<Bilet> findAll() {
        return biletRepository.findAll();
    }
    Bilet save(Bilet entity) throws ValidationException{
        return biletRepository.save(entity);
    }
    Bilet delete(String id){
        return biletRepository.delete(id);
    }
    Bilet update(Bilet entity){
        return biletRepository.update(entity);
    }
}
