package repository;

import domain.Account;
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

public class AccountDao {
    private SessionFactory sessionFactory;

    public AccountDao() {
        this.sessionFactory =  HibernateUtility.getSessionFactory();
    }

    public Account create(Account account) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.save(account);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }

        return account;
    }

    public void update(Account entity, Long id) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Account accountDB = session.get(Account.class, id);
            if (accountDB.getId() == id) {
                accountDB.setName(entity.getName());
            }
            session.update(accountDB);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public Account findById(Long id) {
        Session session = sessionFactory.openSession();

        Account accountDB = session.get(Account.class, id);
        session.close();

        return accountDB;
    }

    public void delete(Long id) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Account accountDB = session.get(Account.class, id);
            session.delete(accountDB);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public List<Account> findAll() {
        Transaction transaction = null;
        List<Account> accounts = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Account> query = builder.createQuery(Account.class);
            Root<Account> root = query.from(Account.class);
            query.select(root);
            Query<Account> q = session.createQuery(query);
            accounts = q.getResultList();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return accounts;
    }
}
