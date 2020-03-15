package validators;

import domain.Team;

public class TeamValidator extends AbstractValidator<Team> {

    private TeamValidator() {
    }

    @Override
    public void validate(Team entity) throws ValidationException {
        String exception = "";
        if(entity.getNume().equals("")){
            exception += "Numele nu poate fi gol\n";
        }

        if(exception.length() > 0)
            throw new ValidationException(exception);
    }

    public static AbstractValidator getInstance(){
        return AbstractValidator.getInstance();
    }
}
