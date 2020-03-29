package model.validators;

import model.domain.Echipa;

public class EchipaValidator extends AbstractValidator<Echipa> {

    private EchipaValidator() {
    }

    @Override
    public void validate(Echipa entity) throws ValidationException {
        String exceptions = "";
        if(entity.getNume().equals("")){
            exceptions += "Numele nu poate fi gol\n";
        }

        if(exceptions.length() > 0)
            throw new ValidationException(exceptions);
    }

    public static AbstractValidator getInstance(){
        return AbstractValidator.getInstance();
    }
}
