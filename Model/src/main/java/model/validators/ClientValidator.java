package model.validators;

import model.domain.Client;

import java.io.Serializable;

public class ClientValidator extends AbstractValidator<Client> implements Serializable {

    private ClientValidator() {
    }

    @Override
    public void validate(Client entity) throws ValidationException {
        String exceptions = "";
        if(entity.getId().equals("")){
            exceptions += "Id-ul nu poate fi vid!";
        }

        if (exceptions.length() > 0){
            throw new ValidationException(exceptions);
        }
    }

    public static AbstractValidator getInstance(){
        return AbstractValidator.getInstance();
    }

}
