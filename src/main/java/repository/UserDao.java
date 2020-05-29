package repository;

import domain.User;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utility.HibernateUtility;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

public class UserDao implements Dao<User> {

    private SessionFactory sessionFactory;

    public UserDao() {
        sessionFactory = HibernateUtility.getSessionFactory();
    }

    public Serializable create(User user) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            session.save(user);

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }

        return user;
    }

    public void update(User user, Long id) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            User userDb = (User) session.get(User.class, id);

            if (user.getId() == id) {
                userDb.setLogin(user.getLogin());
            }

            session.update(userDb);

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public User findById(Long id) {
        Session session = sessionFactory.openSession();

        User user = session.get(User.class, id);
        session.close();

        return user;
    }

    public void delete(Long id) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

        public List<User> findAll() {
                Transaction transaction = null;
                List<User> users = null;

                try (Session session = sessionFactory.openSession()) {
                    transaction = session.beginTransaction();

                    CriteriaBuilder builder = session.getCriteriaBuilder();
                    CriteriaQuery<User> query = builder.createQuery(User.class);
                    Root<User> root = query.from(User.class);
                    query.select(root);
                    Query<User> q = session.createQuery(query);
                    users = q.getResultList();
                    transaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (transaction != null) {
                        transaction.rollback();
                    }
                }
                return users;
    }

//    public List<User> findAll() {
//        Session session = sessionFactory.openSession();
//        Transaction tx = null;
//        List<User> users = null;
//
//        try {
//            tx = session.beginTransaction();
//            users = (List<User>)session.createQuery("FROM User").list();//fixme
//            tx.commit();
//
//        } catch (HibernateException e) {
//            if (tx!=null) tx.rollback();
//            e.printStackTrace();
//        } finally {
//            session.close();
//        }
//
//        return users;
//    }
}
