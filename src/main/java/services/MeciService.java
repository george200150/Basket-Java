package services;

import domain.Meci;
import repositories.CrudRepository;
import validators.ValidationException;

public class MeciService {
    private CrudRepository<String, Meci> meciRepository = null;
    Meci findOne(String id){
        return meciRepository.findOne(id);
    }
    public MeciService(CrudRepository meciRepository) {
        this.meciRepository = meciRepository;
    }
    Iterable<Meci> findAll() {
        return meciRepository.findAll();
    }
    Meci save(Meci entity) throws ValidationException {
        return meciRepository.save(entity);
    }
    Meci delete(String id){
        return meciRepository.delete(id);
    }
    Meci update(Meci entity){
        return meciRepository.update(entity);
    }
}