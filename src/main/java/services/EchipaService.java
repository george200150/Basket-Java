package services;

import model.domain.Echipa;
import repositories.CrudRepository;
import model.validators.ValidationException;

public class EchipaService {
    private CrudRepository<String, Echipa> echipaRepository = null;
    Echipa findOne(String id){
        return echipaRepository.findOne(id);
    }
    public EchipaService(CrudRepository echipaRepository) {
        this.echipaRepository = echipaRepository;
    }
    Iterable<Echipa> findAll() {
        return echipaRepository.findAll();
    }
    Echipa save(Echipa entity) throws ValidationException {
        return echipaRepository.save(entity);
    }
    Echipa delete(String id){
        return echipaRepository.delete(id);
    }
    Echipa update(Echipa entity){
        return echipaRepository.update(entity);
    }
}