package basket.services.rest.repos;

import basket.model.domain.Bilet;
import basket.model.validators.ValidationException;
import basket.model.validators.Validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BiletDataBaseRepository implements CrudRepository<String, Bilet> {
    static final Logger logger = LogManager.getLogger(BiletDataBaseRepository.class);
    private Connection connection;
    private Validator<Bilet> validator;

    public BiletDataBaseRepository(Validator<Bilet> validator) {
        logger.traceEntry("entry constructor");
        this.connection = JDBCInvariant.getConnection();
        this.validator = validator;
        logger.traceExit("successful constructor exit");
    }

    @Override
    public Bilet findOne(String id) throws IllegalArgumentException {
        logger.traceEntry("entry find");
        if (id == null) {
            logger.error("null id exception");
            throw new IllegalArgumentException("ID-ul NU POATE FI NULL");
        }
        try {
            logger.traceEntry("entry query");
            ResultSet data = connection.createStatement().executeQuery("SELECT * FROM \"Bilete\"  WHERE id =" + "\'" + id + "\'");
            data.next();//TODO: sql injection prone
            //String id = data.getString(1);
            String numeClient = data.getString(2);
            float pret = data.getFloat(3);
            String idMeci = data.getString(4);
            String idClient = data.getString(5);
            logger.info("successful query");
            Bilet bilet = new Bilet(id, numeClient, pret, idMeci, idClient);
            logger.traceExit("successful exit", bilet);
            return bilet;
        } catch (SQLException ignored) {
            logger.error("query exception" + ignored.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<Bilet> findAll() {
        logger.traceEntry("entry findAll");
        List<Bilet> lst = new ArrayList<>();
        try {
            logger.traceEntry("entry query");
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
            logger.info("successful query");
        } catch (SQLException ignored) {
            logger.error("query error" + ignored.getMessage());
            throw new IllegalArgumentException("Error: Could not connect to the database");
        }
        logger.traceExit("successful exit", lst);
        return lst;
    }


    @Override
    public Bilet save(Bilet entity) throws ValidationException {
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
            connection.createStatement().execute("INSERT INTO \"Bilete\" VALUES (" +
                    entity.getId() + ",\'" + entity.getNumeClient() + "\',\'" + entity.getPret() + "\',\'" + entity.getIdMeci() + "\',\'" + entity.getIdClient() + "\')"
            );

        } catch (SQLException e) {
            logger.error("query error" + e.getMessage());
            e.printStackTrace();
        }
        logger.traceExit("successful query", null);
        return null;
    }

    @Override
    public Bilet delete(String id) throws IllegalArgumentException {
        logger.traceEntry("entry delete");
        if (id == null) {
            logger.error("null id exception");
            throw new IllegalArgumentException("ID-ul nu poate fi NULL!");
        }
        Bilet bilet = findOne(id);
        logger.info("found data");
        if (bilet != null) {
            try {
                logger.traceEntry("entry query");
                connection.createStatement()
                        .execute("DELETE FROM \"Bilete\" WHERE id = " + "\'" +  id + "\'");
                logger.traceExit("successful query", null);
            } catch (SQLException e) {
                logger.error("query error" + e.getMessage());
                e.printStackTrace();
            }
        }
        logger.traceExit("successful query", bilet);
        return bilet;
    }

    @Override
    public Bilet update(Bilet entity) throws ValidationException {
        logger.traceEntry("entry update");
        if (entity == null){
            logger.error("null entity exception");
            throw new IllegalArgumentException("Entitatea nu poate fi NULL!");
        }
        validator.validate(entity);
        logger.info("validated data");
        if (findOne(entity.getId()) != null) {
            Bilet old = findOne(entity.getId());
            logger.info("found data");
            try {
                logger.traceEntry("entry query");
                connection.createStatement().execute("UPDATE \"Bilete\" SET " +
                        "pret = \'" + entity.getPret() + "\'" +
                        ",\"numeClient\" = \'" + entity.getNumeClient() + "\'" +
                        ",\"idMeci\" = \'" + entity.getIdMeci() + "\'" +
                        ",\"idClient\" = \'" + entity.getIdClient() + "\'" + "WHERE id =" + "\'" + entity.getId() + "\'"
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

