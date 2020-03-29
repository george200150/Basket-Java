package repositories;

import model.domain.Bilet;
import model.loggers.Log;
import model.validators.ValidationException;
import model.validators.Validator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BiletDataBaseRepository implements CrudRepository<String, Bilet> {
    private Connection connection;
    private Validator<Bilet> validator;

    public BiletDataBaseRepository(Validator<Bilet> validator) {
        Log.logger.traceEntry("entry constructor");
        this.connection = JDBCInvariant.getConnection();
        this.validator = validator;
        Log.logger.traceExit("successful constructor exit");
    }

    @Override
    public Bilet findOne(String id) throws IllegalArgumentException {
        Log.logger.traceEntry("entry find");
        if (id == null) {
            Log.logger.error("null id exception");
            throw new IllegalArgumentException("ID-ul NU POATE FI NULL");
        }
        try {
            Log.logger.traceEntry("entry query");
            ResultSet data = connection.createStatement().executeQuery("SELECT * FROM \"Bilete\"  WHERE id =" + "\'" + id + "\'");
            data.next();//TODO: sql injection prone
            //String id = data.getString(1);
            String numeClient = data.getString(2);
            float pret = data.getFloat(3);
            String idMeci = data.getString(4);
            String idClient = data.getString(5);
            Log.logger.info("successful query");
            Bilet bilet = new Bilet(id, numeClient, pret, idMeci, idClient);
            Log.logger.traceExit("successful exit", bilet);
            return bilet;
        } catch (SQLException ignored) {
            Log.logger.error("query exception" + ignored.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<Bilet> findAll() {
        Log.logger.traceEntry("entry findAll");
        List<Bilet> lst = new ArrayList<>();
        try {
            Log.logger.traceEntry("entry query");
            ResultSet data = connection.createStatement().executeQuery("SELECT * FROM \"Bilete\"");
            while (data.next()) {
                String id = data.getString(1);
                String numeClient = data.getString(2);
                float pret = data.getFloat(3);
                String idMeci = data.getString(4);
                String idClient = data.getString(5);
                Bilet bilet = new Bilet(id, numeClient, pret, idMeci, idClient);
                lst.add(bilet);
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
    public Bilet save(Bilet entity) throws ValidationException {
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
            connection.createStatement().execute("INSERT INTO \"Bilete\" VALUES (" +
                    entity.getId() + ",\'" + entity.getNumeClient() + "\',\'" + entity.getPret() + "\',\'" + entity.getIdMeci() + "\',\'" + entity.getIdClient() + "\')"
            );

        } catch (SQLException e) {
            Log.logger.error("query error" + e.getMessage());
            e.printStackTrace();
        }
        Log.logger.traceExit("successful query", null);
        return null;
    }

    @Override
    public Bilet delete(String id) throws IllegalArgumentException {
        Log.logger.traceEntry("entry delete");
        if (id == null) {
            Log.logger.error("null id exception");
            throw new IllegalArgumentException("ID-ul nu poate fi NULL!");
        }
        Bilet bilet = findOne(id);
        Log.logger.info("found data");
        if (bilet != null) {
            try {
                Log.logger.traceEntry("entry query");
                connection.createStatement()
                        .execute("DELETE FROM \"Bilete\" WHERE id = " + "\'" +  id + "\'");
                Log.logger.traceExit("successful query", null);
            } catch (SQLException e) {
                Log.logger.error("query error" + e.getMessage());
                e.printStackTrace();
            }
        }
        Log.logger.traceExit("successful query", bilet);
        return bilet;
    }

    @Override
    public Bilet update(Bilet entity) throws ValidationException {
        Log.logger.traceEntry("entry update");
        if (entity == null){
            Log.logger.error("null entity exception");
            throw new IllegalArgumentException("Entitatea nu poate fi NULL!");
        }
        validator.validate(entity);
        Log.logger.info("validated data");
        if (findOne(entity.getId()) != null) {
            Bilet old = findOne(entity.getId());
            Log.logger.info("found data");
            try {
                Log.logger.traceEntry("entry query");
                connection.createStatement().execute("UPDATE \"Bilete\" SET " +
                        "pret = \'" + entity.getPret() + "\'" +
                        ",\"numeClient\" = \'" + entity.getNumeClient() + "\'" +
                        ",\"idMeci\" = \'" + entity.getIdMeci() + "\'" +
                        ",\"idClient\" = \'" + entity.getIdClient() + "\'" + "WHERE id =" + "\'" + entity.getId() + "\'"
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

