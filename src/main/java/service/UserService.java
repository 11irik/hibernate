package service;

import domain.User;
import repository.UserDao;

import java.util.List;

public class UserService {
    private UserDao userDao;

    public UserService() {
        userDao = new UserDao();
    }

    public User createUser(String login, String password) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        return (User) userDao.create(user);
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
}
