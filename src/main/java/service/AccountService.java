package service;

import domain.Account;
import domain.Group;
import domain.User;
import repository.AccountDao;
import repository.GroupDao;

import java.util.List;

public class AccountService {
    private AccountDao accountDao;
    private GroupService groupService;

    public AccountService() {
        this.accountDao = new AccountDao();
        this.groupService = new GroupService();
    }

    public Account create(String accountName, Double startBalance, Long groupId) {

        Group groupDB = groupService.findById(groupId);
        if (groupDB == null) {
            throw new NullPointerException("There is no group with this id");
        }

        Account account = new Account();

        account.setName(accountName);
        account.setBalance(startBalance);
        account.setGroup(groupDB);

        return accountDao.create(account);
    }

    public void delete(Long id) {
        accountDao.delete(id);
    }

    public List<Account> findAll() {
        return accountDao.findAll();
    }

    public Account findById(Long id) {
        return accountDao.findById(id);
    }
}
