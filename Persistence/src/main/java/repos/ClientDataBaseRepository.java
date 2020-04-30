package repos;

import model.domain.Client;
import model.validators.ValidationException;
import model.validators.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDataBaseRepository implements CrudRepository<String, Client> {
    static final Logger logger = LogManager.getLogger(ClientDataBaseRepository.class);
    private Connection connection;
    private Validator<Client> validator;

    public ClientDataBaseRepository(Validator<Client> validator) {

        logger.traceEntry("entry constructor");
        this.connection = JDBCInvariant.getConnection();
        this.validator = validator;
        logger.traceExit("successful constructor exit");
    }

    public Client findClientByCredentials(String username, String password) throws IllegalArgumentException {
        logger.traceEntry("entry find");
        if (username == null || password == null) {
            logger.error("null id exception");
            throw new IllegalArgumentException("ID-UL SI PAROLA NU POT FI NULL");
        }
        try {
            logger.traceEntry("entry query");
            ResultSet data = connection.createStatement().executeQuery("SELECT * FROM \"Clienti\"  WHERE id =" + "\'" + username + "\' AND password =" + "\'" + password + "\'");
            data.next();
            String idd = data.getString(1); // daca nu putem obtine id-ul, nu exista clientul
            String passwd = data.getString(2);
            logger.info("successful query");
            Client client = new Client(idd, passwd,"null_nume","null_host",-1);
            logger.traceExit("successful exit", client);
            return client;
        } catch (SQLException ignored) {
            logger.error("query exception" + ignored.getMessage());
        }
        return null;
    }


    @Override
    public Client findOne(String id) throws IllegalArgumentException {
        logger.traceEntry("entry find");
        if (id == null) {
            logger.error("null id exception");
            throw new IllegalArgumentException("ID-ul NU POATE FI NULL");
        }
        try {
            logger.traceEntry("entry query");
            ResultSet data = connection.createStatement().executeQuery("SELECT * FROM \"Clienti\"  WHERE id =" + "\'" + id + "\'");
            data.next();
            String idd = data.getString(1); // daca nu putem obtine id-ul, nu exista clientul
            String passwd = data.getString(2);
            logger.info("successful query");
            Client client = new Client(idd, passwd,"null_nume","null_host",-1);
            logger.traceExit("successful exit", client);
            return client;
        } catch (SQLException ignored) {
            logger.error("query exception" + ignored.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<Client> findAll() {
        logger.traceEntry("entry findAll");
        List<Client> lst = new ArrayList<>();
        try {
            logger.traceEntry("entry query");
            ResultSet data = connection.createStatement().executeQuery("SELECT * FROM \"Clienti\"");
            while (data.next()) {
                String id = data.getString(1);
                String passwd = data.getString(2);
                Client client = new Client(id, passwd,"null_nume","null_host",-1);
                lst.add(client);
            }
            logger.info("successful query");
        } catch (SQLException ignored) {
            logger.error("query error" + ignored.getMessage());
            throw new IllegalArgumentException("Error: Could not connect to the database");
        }
        logger.traceExit("successful exit", lst);
        return lst;
    }


    @Override
    public Client save(Client entity) throws ValidationException {
        logger.traceEntry("entry save");
        if (entity == null) {
            logger.error("null id exception");
            throw new IllegalArgumentException("ENTITATEA NU POATE FI NULL");
        }
        validator.validate(entity);
        logger.info("validated data");
        if (findOne(entity.getId()) != null) {
            logger.error("duplicate found exception");
            throw new ValidationException("DUPLICAT GASIT!");
        }

        try {
            logger.traceEntry("entry query");
            connection.createStatement().execute("INSERT INTO \"Clienti\" VALUES (" +
                    entity.getId() + ",\'" + entity.getPassword() + "\')"
            );

        } catch (SQLException e) {
            logger.error("query error" + e.getMessage());
            e.printStackTrace();
        }
        logger.traceExit("successful query", null);
        return null;
    }

    @Override
    public Client delete(String id) throws IllegalArgumentException {
        logger.traceEntry("entry delete");
        if (id == null) {
            logger.error("null id exception");
            throw new IllegalArgumentException("ID-ul nu poate fi NULL!");
        }
        Client client = findOne(id);
        logger.info("found data");
        if (client != null) {
            try {
                logger.traceEntry("entry query");
                connection.createStatement()
                        .execute("DELETE FROM \"Clienti\" WHERE id = " + "\'" + id + "\'");
                logger.traceExit("successful query", null);
            } catch (SQLException e) {
                logger.error("query error" + e.getMessage());
                e.printStackTrace();
            }
        }
        logger.traceExit("successful query", client);
        return client;
    }

    @Override
    public Client update(Client entity) throws ValidationException {
        logger.traceEntry("entry update");
        if (entity == null) {
            logger.error("null entity exception");
            throw new IllegalArgumentException("Entitatea nu poate fi NULL!");
        }
        validator.validate(entity);
        logger.info("validated data");
        if (findOne(entity.getId()) != null) {
            Client old = findOne(entity.getId());
            logger.info("found data");
            try {
                logger.traceEntry("entry query");

                connection.createStatement().execute("UPDATE \"Clienti\" SET " +
                        ",\"password\" = \'" + entity.getPassword() + "\'" + "WHERE id =" + "\'" + entity.getId() + "\'"
                );

                logger.traceExit("successful query", null);
            } catch (SQLException e) {
                logger.error("query error" + e.getMessage());
                e.printStackTrace();
            }
            logger.traceExit("successful query", old);
            return old;
        }
        logger.traceExit("not found data. unsuccessful update", null);
        return null;
    }
}
