package model;

import model.domain.BiletAnnot;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class TestAnnot {

    //INSERT
    void addBilet() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                BiletAnnot bilet = new BiletAnnot("", "", 1, "", "");
                session.save(bilet);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    //UPDATE
    void updateBilet() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                BiletAnnot bilet = (BiletAnnot) session.load(BiletAnnot.class, new Long(69));

                BiletAnnot biletNou = (BiletAnnot) session.load(BiletAnnot.class, new Long(50));

                bilet.setNumeClient("New Text 30 aprilie");
                //bilet.setNextBilet(???);
                tx.commit();
            } catch (RuntimeException e) {
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    //DELETE
    void deleteBilet() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                BiletAnnot crit = session.createQuery("from BiletAnnot where numeClient like 'New Hello%'", BiletAnnot.class)
                        .setMaxResults(1)
                        .uniqueResult();

                System.err.println("Sterge biletul " + crit.getId());
                session.delete(crit);
                tx.commit();
            } catch (RuntimeException e) {
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    //SELECT
    void selectBilet() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                List<BiletAnnot> bilete = session.createQuery("from BiletAnnot as b order by b.numeClient asc", BiletAnnot.class)
                        .setFirstResult(11).setMaxResults(20) // TODO: paginat
                        .list();

                System.out.println(bilete.size() + " ticket(s) found");
                for (BiletAnnot b : bilete) {
                    System.out.println(b.getNumeClient() + " " + b.getId());
                }
                tx.commit();
            } catch (RuntimeException e) {
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    public static void main(String[] args) {
        try {
            initialize();

            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                session.save(new BiletAnnot("1", "ION", 1, "-10", "1"));
                session.save(new BiletAnnot("2", "REBREANU", 2, "-20", "1"));
                session.getTransaction().commit();
            }
        } catch (Exception e) {
            System.err.println("Exception " + e);
            e.printStackTrace();
        } finally {
            close();
        }
    }

    static SessionFactory sessionFactory;

    static void initialize() {
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

    static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
