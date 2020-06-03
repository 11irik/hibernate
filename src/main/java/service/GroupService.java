package service;

import domain.Group;
import domain.User;
import repository.GroupDao;

import java.util.List;

public class GroupService {
    private GroupDao groupDao;
    private UserService userService;

    public GroupService() {
        this.groupDao = new GroupDao();
        this.userService = new UserService();
    }

    public Group create(String groupName) {
        Group group = new Group();
        group.setName(groupName);

        return (Group) groupDao.create(group);
    }

    public void delete(Long id) {
        groupDao.delete(id);
    }

    public void addUser(Long groupId, Long userId) {
        Group groupDB = groupDao.findById(groupId);
        User userDB = userService.findById(userId);

        if (groupDB == null || userDB == null) {
            throw new NullPointerException("There's no such user/group");
        }

        groupDB.getUsers().add(userDB);
        groupDao.update(groupDB, groupId);
    }

    public void removeUser(Long groupId, Long userId) {
        Group groupDB = groupDao.findById(groupId);
        User userDB = userService.findById(userId);

        if (groupDB == null || userDB == null) {
            throw new NullPointerException("There's no such user/group");
        }

        groupDB.getUsers().remove(userDB);
        System.out.println(groupDB.getUsers().size());
        groupDao.update(groupDB, groupId);
    }

    public List<Group> findAll() {
        return groupDao.findAll();
    }

    public Group findById(Long id) {
        return groupDao.findById(id);
    }
}
