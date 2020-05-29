package repository;

import domain.Group;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import utility.HibernateUtility;

import java.io.Serializable;
import java.util.List;

public class GroupDao implements Dao<Group> {
    private SessionFactory sessionFactory;

    public GroupDao() {
        this.sessionFactory =  HibernateUtility.getSessionFactory();
    }

    @Override
    public Serializable create(Group group) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            session.save(group);

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }

        return group;
    }

    @Override
    public void update(Group entity, Long id) {

    }

    @Override
    public Group findById(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Group> findAll() {
        return null;
    }
}
