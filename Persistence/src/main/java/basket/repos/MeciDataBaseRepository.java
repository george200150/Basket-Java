package basket.repos;

import basket.model.domain.Meci;
import basket.model.domain.TipMeci;
import basket.model.validators.ValidationException;
import basket.model.validators.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class MeciDataBaseRepository implements CrudRepository<String, Meci> {
    static final Logger logger = LogManager.getLogger(MeciDataBaseRepository.class);
    private Connection connection;
    @Autowired
    private Validator<Meci> validator;

    public MeciDataBaseRepository(Validator<Meci> validator) {
        logger.traceEntry("entry constructor");
        this.connection = JDBCInvariant.getConnection();
        this.validator = validator;
        logger.traceExit("successful constructor exit");
    }

    @Override
    public Meci findOne(String id) throws IllegalArgumentException {
        logger.traceEntry("entry find");
        if (id == null) {
            logger.error("null id exception");
            throw new IllegalArgumentException("ID-ul NU POATE FI NULL");
        }
        try {
            logger.traceEntry("entry query");
            ResultSet data = connection.createStatement().executeQuery("SELECT * FROM \"Meciuri\"  WHERE id =" + "\'" + id + "\'");
            data.next();//TODO: sql injection prone
            //String id = data.getString(1);
            String homeId = data.getString(2);
            String awayId = data.getString(3);
            Date date = data.getDate(4);
            int value = data.getInt(5);
            TipMeci tip = null;
            if( value == 1){
                tip = TipMeci.CALIFICARE;
            } else if( value == 2){
                tip = TipMeci.SAISPREZECIME;
            } else if( value == 3){
                tip = TipMeci.OPTIME;
            }else if( value == 4){
                tip = TipMeci.SFERT;
            }else if( value == 5) {
                tip = TipMeci.SEMIFINALA;
            } else if( value == 6) {
                tip = TipMeci.FINALA;
            }
            int numarBileteDisponibile = data.getInt(6);
            logger.info("successful query");
            Meci meci = new Meci(id,homeId,awayId,date,tip, numarBileteDisponibile);
            logger.traceExit("successful exit", meci);
            return meci;
        } catch (SQLException ignored) {
            logger.error("query exception" + ignored.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<Meci> findAll() {
        logger.traceEntry("entry findAll");
        List<Meci> lst = new ArrayList<>();
        try {
            logger.traceEntry("entry query");
            ResultSet data = connection.createStatement().executeQuery("SELECT * FROM \"Meciuri\"");
            while (data.next()) {
                String id = data.getString(1);
                String homeId = data.getString(2);
                String awayId = data.getString(3);
                Date date = data.getDate(4);
                int value = data.getInt(5);
                TipMeci tip = null;
                if( value == 1){
                    tip = TipMeci.CALIFICARE;
                } else if( value == 2){
                    tip = TipMeci.SAISPREZECIME;
                } else if( value == 3){
                    tip = TipMeci.OPTIME;
                }else if( value == 4){
                    tip = TipMeci.SFERT;
                }else if( value == 5) {
                    tip = TipMeci.SEMIFINALA;
                } else if( value == 6) {
                    tip = TipMeci.FINALA;
                }
                int numarBileteDisponibile = data.getInt(6);
                Meci meci = new Meci(id,homeId,awayId,date,tip, numarBileteDisponibile);
                lst.add(meci);
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
    public Meci save(Meci entity) throws ValidationException {
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
            connection.createStatement().execute("INSERT INTO \"Meciuri\" VALUES (" +
                    entity.getId() + ",\'" +
                    entity.getHome() + "\',\'" +
                    entity.getAway() + "\',\'" +
                    entity.getDate() + "\',\'" +
                    entity.getTip().getNumVal() + "\',\'" +
                    entity.getNumarBileteDisponibile() + "\')"
            );

        } catch (SQLException e) {
            logger.error("query error" + e.getMessage());
            e.printStackTrace();
        }
        logger.traceExit("successful query", null);
        return null;
    }

    @Override
    public Meci delete(String id) throws IllegalArgumentException {
        logger.traceEntry("entry delete");
        if (id == null) {
            logger.error("null id exception");
            throw new IllegalArgumentException("ID-ul nu poate fi NULL!");
        }
        Meci meci = findOne(id);
        logger.info("found data");
        if (meci != null) {
            try {
                logger.traceEntry("entry query");
                connection.createStatement()
                        .execute("DELETE FROM \"Meciuri\" WHERE id = " + "\'" +  id + "\'");
                logger.traceExit("successful query", null);
            } catch (SQLException e) {
                logger.error("query error" + e.getMessage());
                e.printStackTrace();
            }
        }
        logger.traceExit("successful query", meci);
        return meci;
    }

    @Override
    public Meci update(Meci entity) throws ValidationException {
        logger.traceEntry("entry update");
        if (entity == null){
            logger.error("null entity exception");
            throw new IllegalArgumentException("Entitatea nu poate fi NULL!");
        }
        validator.validate(entity);
        logger.info("validated data");
        if (findOne(entity.getId()) != null) {
            Meci old = findOne(entity.getId());
            logger.info("found data");
            try {
                logger.traceEntry("entry query");
                connection.createStatement().execute("UPDATE \"Meciuri\" SET " +
                        "home = \'" + entity.getHome() + "\'" +
                        ",away = \'" + entity.getAway() + "\'" +
                        ",\"dataCalendar\" = \'" + entity.getDate() + "\'" + // trebuie cu escape backslash unde e nevoie de case sensitivity
                        ",tip = \'" + entity.getTip().getNumVal() + "\'" +
                        ",\"numarBilete\" = \'" + entity.getNumarBileteDisponibile() + "\'" + "WHERE id =" + "\'" + entity.getId() + "\'"
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
