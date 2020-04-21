package model.validators;

import model.domain.Bilet;

import java.io.Serializable;

public class BiletValidator extends AbstractValidator<Bilet> implements Serializable {

    private BiletValidator() {
    }

    @Override
    public void validate(Bilet entity) throws ValidationException {
        String exceptions = "";
        if(entity.getId().equals("")){
            exceptions += "Id-ul nu poate fi vid!\n";
        }
        if(entity.getIdMeci().equals("")){
            exceptions += "Id-ul meciului nu poate fi vid!\n";
        }
        if(entity.getPret() < 0){
            exceptions += "Pretul biletului nu poate fi negativ!";
        }

        if (exceptions.length() > 0){
            throw new ValidationException(exceptions);
        }
    }

    public static AbstractValidator getInstance(){
        return AbstractValidator.getInstance();
    }

}
