package service;

import domain.User;
import exception.PasswordMatchException;
import repository.UserDao;
import utility.BCryptUtility;

import javax.persistence.EntityExistsException;
import java.util.List;

public class UserService {
    private UserDao userDao;

    public UserService() {
        userDao = new UserDao();
    }

    public User createUser(String login, String password, String rePassword) throws PasswordMatchException {

        if (userDao.findByLogin(login).size() != 0) {
            throw new EntityExistsException("User with this login already exists");
        }

        if (!password.equals(rePassword)) {
            throw new PasswordMatchException("Passwords don't match");
        }

        User user = new User();
        user.setLogin(login);
        user.setPassword(BCryptUtility.hash(password));
        return (User) userDao.create(user);
    }

    public Boolean login(String login, String password) {
        List<User> usersDb = userDao.findByLogin(login);

        if (usersDb.size() == 0) {
            throw new NullPointerException("There's no user with such login");
        }

        return BCryptUtility.verifyHash(password, usersDb.get(0).getPassword());
    }

    public void updateUser(User user, Long id) {
        userDao.update(user, id);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public User findById(Long id) {
        return userDao.findById(id);
    }

    public User findByLogin(String login) {
        return userDao.findByLogin(login).get(0);
   }
}
