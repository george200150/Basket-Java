package repositories;


import domain.Team;
import loggers.Log;
import validators.ValidationException;
import validators.Validator;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TeamDataBaseRepository implements CrudRepository<String, Team> {
    private Connection connection;
    private Validator<Team> validator;

    public TeamDataBaseRepository(Validator<Team> validator) {
        Properties serverProps=new Properties(); //TODO: this comes from the server
        try {
            serverProps.load(new FileReader("src/bd.config"));
        } catch (IOException e) {
            e.printStackTrace();
        } //TODO: this comes from the server
        try {
            Log.logger.traceEntry("entry constructor");
            Class.forName(serverProps.getProperty("jdbc.postgres.driver"));
            this.connection = DriverManager
                    .getConnection(serverProps.getProperty("jdbc.postgres.server"), serverProps.getProperty("jdbc.postgres.user"), serverProps.getProperty("jdbc.postgres.password"));
            Log.logger.info("successful connection");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.logger.error("connection failure"  + e.getMessage());
        }
        this.validator = validator;
        Log.logger.traceExit("successful exit", this.connection);
    }

    @Override
    public Team findOne(String id) throws IllegalArgumentException {
        Log.logger.traceEntry("entry find");
        if (id == null) {
            Log.logger.error("null id exception");
            throw new IllegalArgumentException("ID-ul NU POATE FI NULL");
        }
        try {
            Log.logger.traceEntry("entry query");
            ResultSet data = connection.createStatement().executeQuery("SELECT * FROM \"Echipe\"  WHERE id =" + "\'" + id + "\'");
            data.next();//TODO: sql injection prone
            //String id = data.getString(1);
            String nume = data.getString(2);
            Log.logger.info("successful query");
            Team team = new Team(id, nume);
            Log.logger.traceExit("successful exit", team);
            return team;
        } catch (SQLException ignored) {
            Log.logger.error("query exception" + ignored.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<Team> findAll() {
        Log.logger.traceEntry("entry findAll");
        List<Team> lst = new ArrayList<>();
        try {
            Log.logger.traceEntry("entry query");
            ResultSet data = connection.createStatement().executeQuery("SELECT * FROM \"Echipe\"");
            while (data.next()) {
                String id = data.getString(1);
                String nume = data.getString(2);
                Team team = new Team(id,nume);
                lst.add(team);
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
    public Team save(Team entity) throws ValidationException {
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
            connection.createStatement().execute("INSERT INTO \"Echipe\" VALUES (" +
                    entity.getId() + ",\'" + entity.getNume() + "\')"
            );

        } catch (SQLException e) {
            Log.logger.error("query error" + e.getMessage());
            e.printStackTrace();
        }
        Log.logger.traceExit("successful query", null);
        return null;
    }

    @Override
    public Team delete(String id) throws IllegalArgumentException {
        Log.logger.traceEntry("entry delete");
        if (id == null) {
            Log.logger.error("null id exception");
            throw new IllegalArgumentException("ID-ul nu poate fi NULL!");
        }
        Team team = findOne(id);
        Log.logger.info("found data");
        if (team != null) {
            try {
                Log.logger.traceEntry("entry query");
                connection.createStatement()
                        .execute("DELETE FROM \"Echipe\" WHERE id = " + "\'" +  id + "\'");
                Log.logger.traceExit("successful query", null);
            } catch (SQLException e) {
                Log.logger.error("query error" + e.getMessage());
                e.printStackTrace();
            }
        }
        Log.logger.traceExit("successful query", team);
        return team;
    }

    @Override
    public Team update(Team entity) throws ValidationException {
        Log.logger.traceEntry("entry update");
        if (entity == null){
            Log.logger.error("null entity exception");
            throw new IllegalArgumentException("Entitatea nu poate fi NULL!");
        }
        validator.validate(entity);
        Log.logger.info("validated data");
        if (findOne(entity.getId()) != null) {
            Team old = findOne(entity.getId());
            Log.logger.info("found data");
            try {
                Log.logger.traceEntry("entry query");
                connection.createStatement().execute("UPDATE \"Team\" SET " +
                        "nume = \'" + entity.getNume() + "\'" + "WHERE id =" + "\'" + entity.getId() + "\'"
                );
                Log.logger.traceExit("successful query", null);
            } catch (SQLException e) {
                Log.logger.error("query error" + e.getMessage());
                e.printStackTrace();
            }
            Log.logger.traceExit("successful query", old);
            return old;
        }
        Log.logger.traceExit("not found data. unsuccessful update", null);
        return null;
    }
}
