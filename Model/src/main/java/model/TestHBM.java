package model;

import model.domain.Bilet;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class TestHBM {

    //INSERT
    void addBilet() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Bilet bilet = new Bilet("", "", 1, "", "");
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
                Bilet bilet = (Bilet) session.load(Bilet.class, new Long(69));

                Bilet biletNou = (Bilet) session.load(Bilet.class, new Long(50));

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

                Bilet crit = session.createQuery("from Bilet where numeClient like 'New Hello%'", Bilet.class)
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

                List<Bilet> bilete = session.createQuery("from Bilet as b order by b.numeClient asc", Bilet.class)
                        //.setFirstResult(11).setMaxResults(20) // paginat
                        .list();

                System.out.println(bilete.size() + " ticket(s) found");
                for (Bilet b : bilete) {
                    System.out.println(b.getNumeClient() + " " + b.getId());
                }
                tx.commit();
            } catch (RuntimeException e) {
                if (tx != null)
                    tx.rollback();
            }
        }
    }

//    public static void main(String[] args) {
//        try {
//            initialize();
//
//            try (Session session = sessionFactory.openSession()) {
//                session.beginTransaction();
//                session.save(new Bilet("1", "ION", 1, "-10", "1"));
//                session.save(new Bilet("2", "REBREANU", 2, "-20", "1"));
//                session.getTransaction().commit();
//            }
//        } catch (Exception e) {
//            System.err.println("Exception " + e);
//            e.printStackTrace();
//        } finally {
//            close();
//        }
//    }

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
