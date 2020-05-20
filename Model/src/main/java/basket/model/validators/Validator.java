package basket.model.validators;

public interface Validator<E> {
    void validate(E entity) throws ValidationException;
}