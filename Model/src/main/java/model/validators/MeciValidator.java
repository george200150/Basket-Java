package model.validators;

import model.domain.Meci;

import java.io.Serializable;

public class MeciValidator extends AbstractValidator<Meci> implements Serializable {

    private MeciValidator() {
    }

    @Override
    public void validate(Meci entity) throws ValidationException {
        String exceptions = "";
        if(entity.getId().equals("")){
            exceptions += "Id-ul nu poate fi vid!\n";
        }
        if(entity.getHome().equals("")){
            exceptions += "Echipa 1 nu poate fi vida\n";
        }
        if(entity.getAway().equals("")){
            exceptions += "Echipa 2 nu poate fi vida\n";
        }
        if(entity.getTip() == null){
            exceptions += "Tipul meciului trebuie specificat!\n";
        }
        if(entity.getNumarBileteDisponibile() < 0){
            exceptions += "Numarul de bilete trebuie sa fie pozitiv!";
        }

        if (exceptions.length() > 0){
            throw new ValidationException(exceptions);
        }
    }

    public static AbstractValidator getInstance(){
        return AbstractValidator.getInstance();
    }

}
