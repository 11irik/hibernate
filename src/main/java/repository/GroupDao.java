package repository;

import domain.Group;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utility.HibernateUtility;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class GroupDao {
    private SessionFactory sessionFactory;

    public GroupDao() {
        this.sessionFactory =  HibernateUtility.getSessionFactory();
    }

    public Group create(Group group) {
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

    public void update(Group entity, Long id) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Group groupDB = (Group) session.get(Group.class, id);
            if (groupDB.getId() == id) {
                groupDB.setName(entity.getName());
                groupDB.setUsers(entity.getUsers());
            }
            session.update(groupDB);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public Group findById(Long id) {
        Session session = sessionFactory.openSession();

        Group groupDB = session.get(Group.class, id);
        session.close();

        return groupDB;
    }

    public void delete(Long id) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Group groupDB = session.get(Group.class, id);
            session.delete(groupDB);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public List<Group> findAll() {
        Transaction transaction = null;
        List<Group> groups = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Group> query = builder.createQuery(Group.class);
            Root<Group> root = query.from(Group.class);
            query.select(root);
            Query<Group> q = session.createQuery(query);
            groups = q.getResultList();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return groups;
    }
}
