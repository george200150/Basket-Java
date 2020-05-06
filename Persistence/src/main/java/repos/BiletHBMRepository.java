package repos;

import model.domain.Bilet;
import model.validators.ValidationException;
import model.validators.Validator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

public class BiletHBMRepository {
    private Validator<Bilet> validator;
    static SessionFactory sessionFactory;

    public BiletHBMRepository(Validator<Bilet> validator) {
        initialize();
        this.validator = validator;
    }

    //INSERT
    public Bilet save(Bilet entity) {
        if (entity == null) {
            throw new IllegalArgumentException("ENTITATEA NU POATE FI NULL");
        }
        validator.validate(entity);

        if (this.findOne(entity.getId()) != null)
            throw new ValidationException("DUPLICAT GASIT!");

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Bilet bilet = new Bilet(entity.getId(), entity.getNumeClient(), entity.getPret(), entity.getIdMeci(), entity.getIdClient());
                session.save(bilet);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return null; // I sometimes wonder why this function isn't just void...
    }

    //UPDATE
    public Bilet update(Bilet entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entitatea nu poate fi NULL!");
        }
        validator.validate(entity);
        if (this.findOne(entity.getId()) != null) {
            Bilet old = this.findOne(entity.getId());
            try (Session session = sessionFactory.openSession()) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();

                    Query query =  session.createQuery("update Bilet b set b.numeClient = :nume, b.idClient = :id where b.id = :cod");
                    query.setParameter("nume", entity.getNumeClient());
                    query.setParameter("id", entity.getIdClient());
                    query.setParameter("cod", entity.getId());
                    query.executeUpdate();

                    tx.commit();
                    return old;
                } catch (RuntimeException e) {
                    if (tx != null)
                        tx.rollback();
                    return null;
                }
            }
        }
        return null;
    }

    //DELETE
    public Bilet remove(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID-ul nu poate fi NULL!");
        }
        Bilet bilet = findOne(id);
        if (bilet != null) { // if it makes sense looking for it
            try (Session session = sessionFactory.openSession()) {
                Transaction tx = null;
                try {
                    tx = session.beginTransaction();

                    Query query = session.createQuery("from Bilet where id = :cod", Bilet.class);
                    query.setParameter("cod", id);
                    Bilet crit = (Bilet) query.setMaxResults(1).uniqueResult();

                    System.err.println("Sterge biletul " + crit.getId());
                    session.delete(crit);
                    tx.commit();
                    return bilet;
                } catch (RuntimeException e) {
                    if (tx != null)
                        tx.rollback();
                    return null;
                }
            }
        }
        return null;
    }

    //SELECT
    public List<Bilet> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                List<Bilet> bilete = session.createQuery("from Bilet", Bilet.class)
                        .list();

                System.out.println(bilete.size() + " ticket(s) found");
                for (Bilet b : bilete) {
                    System.out.println(b.getNumeClient() + " " + b.getId());
                }
                tx.commit();
                return bilete;
            } catch (RuntimeException e) {
                if (tx != null)
                    tx.rollback();
                return null;
            }
        }
    }

    //SELECT
    public Bilet findOne(String id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                Query query = session.createQuery("from Bilet where id = :cod", Bilet.class);
                query.setParameter("cod", id);
                Bilet bilet = (Bilet) query.setMaxResults(1).uniqueResult();

                System.out.println(bilet + " ticket found");
                tx.commit();
                return bilet;
            } catch (RuntimeException e) {
                if (tx != null)
                    tx.rollback();
                return null;
            }
        }
    }

    public static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.out.println("Exceptie " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
