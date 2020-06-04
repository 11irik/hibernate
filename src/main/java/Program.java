import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import domain.Account;
import domain.Group;
import service.AccountService;
import service.GroupService;
import service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;
import java.util.logging.Level;


public class Program {
    private static final String[] LOGIN_MENU = {"LOGIN", "REGISTER", "EXIT"};
    private static final String[] MAIN_MENU = {"ME", "GROUP", "ACCOUNT", "LOGOUT"};
    private static final String[] USER_MENU = {"INFO", "LOGIN", "PASSWORD", "BACK"};
    private static final String[] GROUP_MENU = {"CREATE", "GET", "GETBYID", "RENAME", "DELETE", "ADDUSER", "REMOVEUSER", "BACK"};
    private static final String[] ACCOUNT_MENU = {"CREATE", "GET", "GETBYID", "RENAME", "DELETE", "BACK"};
    private static final String WELCOME_MESSAGE = "+----------------------------+\n" +
            "|      Welcome to wallet     |\n" +
            "|                            |\n" +
            "+----------------------------+";

    private BufferedReader reader;
    private ObjectMapper mapper;

    private UserService userService;
    private GroupService groupService;
    private AccountService accountService;

    private Stack<String> path;
    private Boolean loggedIn;
    private String login;


    public Program() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));

        this.mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        this.userService = new UserService();
        this.groupService = new GroupService();
        this.accountService = new AccountService();

        this.path = new Stack<>();
        this.path.push("wallet>");
        this.loggedIn = false;
        this.login = "";
    }


    public static void main(String[] args) throws IOException {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        Program program = new Program();

        program.printWelcomeMessage();
        program.printLoginMenu();

        String answer;
        while (true) {
            program.printPath();
            answer = program.reader.readLine().toLowerCase();

            if ("exit".equals(answer)) {
                break;
            }

            if (program.loggedIn) {
                program.getMain(answer);
            } else {
                program.getAuth(answer);
            }
        }
    }


    private void getAuth(String answer) throws IOException {

        String login;
        String password;
        String rePassword;

        switch (answer) {

            case "login":
                System.out.println("Enter login");
                login = reader.readLine();
                System.out.println("Enter password");
                password = reader.readLine();

                try {
                    userService.login(login, password);
                    this.login = login;
                    this.loggedIn = true;
                    System.out.println("You successfully logged in");
                    printMainMenu();
                    return;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;

            case "register":
                System.out.println("Enter login");
                login = reader.readLine();
                System.out.println("Enter password");
                password = reader.readLine();
                System.out.println("Repeat password");
                rePassword = reader.readLine();
                try {
                    userService.createUser(login, password, rePassword);
                    System.out.println("Successfully registered user");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;

            case "help":
                printLoginMenu();
                break;

            default:
                System.out.println("Wrong input. Enter 'help' for info");
        }
    }

    private void getMain(String answer) throws IOException {
        switch (answer) {

            case "me":
                printUserMenu();
                path.push("user>");
                getUserMenu();
                path.pop();
                break;

            case "group":
                printGroupMenu();
                path.push("group>");
                getGroupMenu();
                path.pop();
                break;

            case "account":
                printAccountMenu();
                path.push("account>");
                getAccountMenu();
                path.pop();
                break;

            case "logout":
                this.loggedIn = false;
                this.login = "";
                System.out.println("You're logged out");
                break;

            case "help":
                printMainMenu();
                break;

            default:
                System.out.println("Wrong input. Enter 'help' for info");
        }
    }

    private void getUserMenu() throws IOException {
        String answer;

        while (true) {
            printPath();
            answer = reader.readLine().toLowerCase();

            switch (answer) {

                case "info":
                    System.out.println(this.mapper.writeValueAsString(userService.findByLogin(this.login)));
                    break;

                case "login":
                    try {
                        System.out.println("Enter new login");
                        String newLogin = reader.readLine();
                        userService.updateLogin(this.login, newLogin);
                        this.login = newLogin;
                        System.out.println("Login successfully changed");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "password":
                    try {
                        System.out.println("Enter new password");
                        String newPassword = reader.readLine();
                        System.out.println("Re-enter new password");
                        String reNewPassword = reader.readLine();

                        userService.updatePassword(this.login, newPassword, reNewPassword);
                        System.out.println("Password successfully changed");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "help":
                    printUserMenu();
                    break;

                case "back":
                    return;

                default:
                    System.out.println("Wrong input. Enter 'help' for info");
            }
        }
    }

    private void getGroupMenu() throws IOException {
        String answer;
        String groupName;
        Long groupId;
        Long userId;
        Group group;

        while (true) {
            printPath();
            answer = reader.readLine().toLowerCase();

            switch (answer) {

                case "create":
                    System.out.println("Enter group name:");
                    groupName = reader.readLine();
                    group = groupService.create(groupName);
                    System.out.println("Group successfully created with id = " + group.getId());
                    break;

                case "get":
                    try {
                        System.out.println(this.mapper.writeValueAsString(groupService.findAll()));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "rename":
                    try {
                        System.out.println("Enter group id");
                        groupId = Long.valueOf(reader.readLine());
                        System.out.println("Enter new name");
                        groupName = reader.readLine();
                        groupService.renameGroup(groupId, groupName);
                        System.out.println("Successfully renamed group");

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "getbyid":
                    System.out.println("Enter group id");
                    try {
                        groupId = Long.valueOf(reader.readLine());

                        System.out.println(this.mapper.writeValueAsString(groupService.findById(groupId)));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "adduser":
                    try {
                        System.out.println("Enter group id");
                        groupId = Long.valueOf(reader.readLine());

                        System.out.println("Enter user id");
                        userId = Long.valueOf(reader.readLine());

                        groupService.addUser(groupId, userId);
                        System.out.println("User successfully added");

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "removeuser":
                    try {
                        System.out.println("Enter group id");
                        groupId = Long.valueOf(reader.readLine());

                        System.out.println("Enter user id");
                        userId = Long.valueOf(reader.readLine());

                        groupService.removeUser(groupId, userId);
                        System.out.println("User successfully removed");

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "delete":
                    System.out.println("Enter group id");
                    try {
                        groupId = Long.valueOf(reader.readLine());
                        boolean confirmation;
                        while (true) {
                            System.out.println("Do you really want to delete a group? y/n");
                            String bool = reader.readLine();
                            if ("y".equals(bool)) {
                                confirmation = true;
                                break;
                            } else if ("n".equals(bool)) {
                                confirmation = false;
                                break;
                            }
                        }

                        if (confirmation) {
                            groupService.delete(groupId);
                            System.out.println("Group successfully deleted");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "help":
                    printGroupMenu();
                    break;

                case "back":
                    return;

                default:
                    System.out.println("Wrong input. Enter 'help' for info");
            }
        }
    }

    private void getAccountMenu() throws IOException {
        String answer;
        Long accountId;
        Long groupId;
        String accountName;
        Double startBalance;
        boolean confirmation;

        while (true) {
            printPath();
            answer = reader.readLine().toLowerCase();

            switch (answer) {

                case "create":
                    System.out.println("Enter account name:");
                    accountName = reader.readLine();

                    try {
                        System.out.println("Enter account start balance");
                        startBalance = Double.valueOf(reader.readLine());

                        System.out.println("Enter group id");
                        groupId = Long.valueOf(reader.readLine());

                        Account account = accountService.create(accountName, startBalance, groupId);
                        System.out.println("Account successfully created with id = " + account.getId());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "get":
                    try {
                        System.out.println(this.mapper.writeValueAsString(accountService.findAll()));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "getbyid":
                    System.out.println("Enter account id");
                    try {
                        accountId = Long.valueOf(reader.readLine());

                        System.out.println(this.mapper.writeValueAsString(accountService.findById(accountId)));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "rename":
                    try {
                        System.out.println("Enter account id");
                        accountId = Long.valueOf(reader.readLine());
                        System.out.println("Enter new name");
                        accountName = reader.readLine();
                        accountService.renameAccount(accountId, accountName);
                        System.out.println("Successfully renamed account");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "delete":
                    System.out.println("Enter account id");
                    try {
                        Long id = Long.valueOf(reader.readLine());
                        while (true) {
                            System.out.println("Do you really want to delete an account? y/n");
                            String bool = reader.readLine();
                            if ("y".equals(bool)) {
                                confirmation = true;
                                break;
                            } else if ("n".equals(bool)) {
                                confirmation = false;
                                break;
                            }
                        }

                        if (confirmation) {
                            accountService.delete(id);
                            System.out.println("Account successfully deleted");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "help":
                    printAccountMenu();
                    break;

                case "back":
                    return;

                default:
                    System.out.println("Wrong input. Enter 'help' for info");
            }
        }
    }

    private void printPath() {
        for (String line : path) {
            System.out.print(line + ">");
        }
    }

    private void printWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
    }

    private void printLoginMenu() {
        for (String line : LOGIN_MENU) {
            System.out.println("---" + line);
        }
    }

    private void printMainMenu() {
        for (String line : MAIN_MENU) {
            System.out.println("---" + line);
        }
    }

    private void printUserMenu() {
        for (String line : USER_MENU) {
            System.out.println("---" + line);
        }
    }

    private void printGroupMenu() {
        for (String line : GROUP_MENU) {
            System.out.println("---" + line);
        }
    }

    private void printAccountMenu() {
        for (String line : ACCOUNT_MENU) {
            System.out.println("---" + line);
        }
    }
}
