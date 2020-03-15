package repositories;

import domain.Client;
import domain.Team;
import loggers.Log;
import validators.ValidationException;
import validators.Validator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDataBaseRepository implements CrudRepository<String, Client> {
    private Connection connection;
    private Validator<Client> validator;

    public ClientDataBaseRepository(Validator<Client> validator) {
        try {
            Log.logger.traceEntry("entry constructor");
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/MPP", "postgres", "admin");
            Log.logger.info("successful connection");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.logger.error("connection failure" + e.getMessage());
        }
        this.validator = validator;
        Log.logger.traceExit("successful exit", this.connection);
    }

    @Override
    public Client findOne(String id) throws IllegalArgumentException {
        Log.logger.traceEntry("entry find");
        if (id == null) {
            Log.logger.error("null id exception");
            throw new IllegalArgumentException("ID-ul NU POATE FI NULL");
        }
        try {
            Log.logger.traceEntry("entry query");
            ResultSet data = connection.createStatement().executeQuery("SELECT * FROM \"Clienti\"  WHERE id =" + "\'" + id + "\'");
            data.next();
            String idd = data.getString(1); // daca nu putem obtine id-ul, nu exista clientul
            Log.logger.info("successful query");
            Client client = new Client(idd);
            Log.logger.traceExit("successful exit", client);
            return client;
        } catch (SQLException ignored) {
            Log.logger.error("query exception" + ignored.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<Client> findAll() {
        Log.logger.traceEntry("entry findAll");
        List<Client> lst = new ArrayList<>();
        try {
            Log.logger.traceEntry("entry query");
            ResultSet data = connection.createStatement().executeQuery("SELECT * FROM \"Clienti\"");
            while (data.next()) {
                String id = data.getString(1);
                Client client = new Client(id);
                lst.add(client);
            }
            Log.logger.info("successful query");
        } catch (SQLException ignored) {
            Log.logger.error("query error" + ignored.getMessage());
            throw new IllegalArgumentException("Error: Could not connect to the database");
        }
        Log.logger.traceExit("successful exit", lst);
        return lst;
    }


    @Override
    public Client save(Client entity) throws ValidationException {
        Log.logger.traceEntry("entry save");
        if (entity == null) {
            Log.logger.error("null id exception");
            throw new IllegalArgumentException("ENTITATEA NU POATE FI NULL");
        }
        validator.validate(entity);
        Log.logger.info("validated data");
        if (findOne(entity.getId()) != null) {
            Log.logger.error("duplicate found exception");
            throw new ValidationException("DUPLICAT GASIT!");
        }

        try {
            Log.logger.traceEntry("entry query");
            connection.createStatement().execute("INSERT INTO \"Clienti\" VALUES (" +
                    entity.getId() + ")" // TODO: CHECK IF QUERY CORRECT !!!!!!!!!!!!!
            );

        } catch (SQLException e) {
            Log.logger.error("query error" + e.getMessage());
            e.printStackTrace();
        }
        Log.logger.traceExit("successful query", null);
        return null;
    }

    @Override
    public Client delete(String id) throws IllegalArgumentException {
        Log.logger.traceEntry("entry delete");
        if (id == null) {
            Log.logger.error("null id exception");
            throw new IllegalArgumentException("ID-ul nu poate fi NULL!");
        }
        Client client = findOne(id);
        Log.logger.info("found data");
        if (client != null) {
            try {
                Log.logger.traceEntry("entry query");
                connection.createStatement()
                        .execute("DELETE FROM \"Clienti\" WHERE id = " + "\'" + id + "\'");
                Log.logger.traceExit("successful query", null);
            } catch (SQLException e) {
                Log.logger.error("query error" + e.getMessage());
                e.printStackTrace();
            }
        }
        Log.logger.traceExit("successful query", client);
        return client;
    }

    @Override
    public Client update(Client entity) throws ValidationException {
        Log.logger.traceEntry("entry update");
        if (entity == null) {
            Log.logger.error("null entity exception");
            throw new IllegalArgumentException("Entitatea nu poate fi NULL!");
        }
        validator.validate(entity);
        Log.logger.info("validated data");
        if (findOne(entity.getId()) != null) {
            Client old = findOne(entity.getId());
            Log.logger.info("found data");
//            try {
//                Log.logger.traceEntry("entry query");
//
//                // TODO: cannot update anything yet
//
//                Log.logger.traceExit("successful query", null);
//            } catch (SQLException e) {
//                Log.logger.error("query error" + e.getMessage());
//                e.printStackTrace();
//            }
            Log.logger.traceExit("successful query", old);
            return old;
        }
        Log.logger.traceExit("not found data. unsuccessful update", null);
        return null;
    }
}
