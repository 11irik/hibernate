import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import domain.Group;
import service.GroupService;
import service.UserService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Program {
    private static final String[] LOGIN_MENU = {"LOGIN", "REGISTER", "EXIT"};
    private static final String[] MAIN_MENU = {"ME", "GROUP", "ACCOUNT", "LOGOUT"};
    private static final String[] GROUP_MENU = {"CREATE", "GET", "GETBYID", "DELETE", "ADDUSER", "REMOVEUSER", "BACK"};
    private static final String[] ACCOUNT_MENU = {"CREATE", "GET", "GETBYID", "DELETE", "BACK"};
    private static final String WELCOME_MESSAGE = "+----------------------------+\n" +
            "|      Welcome to wallet     |\n" +
            "|                            |\n" +
            "+----------------------------+";

    private BufferedReader reader;
    private ObjectMapper mapper;

    private UserService userService;
    private GroupService groupService;

    private Boolean loggedIn;
    private String login;


    public Program() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        this.userService = new UserService();
        this.groupService = new GroupService();

        this.loggedIn = false;
        this.login = "";
    }



    public static void main(String[] args) throws IOException {
        Program program = new Program();

        program.printWelcomeMessage();
        program.printLoginMenu();

        String answer;
        while (true) {
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
                    loggedIn = userService.login(login, password);
                    this.login = login;
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
                getMe();
                break;

            case "group":
                getGroupMenu();
                printGroupMenu();
                break;

            case "account":
                getAccountMenu();
                printAccountMenu();
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

    private void getMe() throws JsonProcessingException {
        System.out.println(this.mapper.writeValueAsString(userService.findByLogin(this.login)));
    }

    private void getGroupMenu() throws IOException {
        String answer;

        while (true) {
            answer = reader.readLine().toLowerCase();

            switch (answer) {

                case "create":
                    System.out.println("Enter group name:");
                    String name = reader.readLine();
                    Group group = groupService.create(name);
                    System.out.println("Group successfully created with id = " + group.getId());
                    break;

                case "get":
                    try {
                        System.out.println(this.mapper.writeValueAsString(groupService.findAll()));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "getbyid":
                    System.out.println("Enter group id");
                    try {
                        Long id = Long.valueOf(reader.readLine());

                        System.out.println(this.mapper.writeValueAsString(groupService.findById(id)));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "adduser":
                    try {
                        System.out.println("Enter group id");
                        Long groupId = Long.valueOf(reader.readLine());

                        System.out.println("Enter user id");
                        Long userId = Long.valueOf(reader.readLine());

                        groupService.addUser(groupId, userId);

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "removeuser":
                    try {
                        System.out.println("Enter group id");
                        Long groupId = Long.valueOf(reader.readLine());

                        System.out.println("Enter user id");
                        Long userId = Long.valueOf(reader.readLine());

                        groupService.removeUser(groupId, userId);

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "delete":
                    System.out.println("Enter group id");
                    try {
                        Long id = Long.valueOf(reader.readLine());
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
                            groupService.delete(id);
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

        while (true) {
            answer = reader.readLine().toLowerCase();

            switch (answer) {

                case "create":
                    System.out.println("Enter group name:");
                    String name = reader.readLine();
                    Group group = groupService.create(name);
                    System.out.println("Group successfully created with id = " + group.getId());
                    break;

                case "get":
                    try {
                        System.out.println(this.mapper.writeValueAsString(groupService.findAll()));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "getbyid":
                    System.out.println("Enter account id");
                    try {
                        Long id = Long.valueOf(reader.readLine());

                        System.out.println(this.mapper.writeValueAsString(groupService.findById(id)));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "delete":
                    System.out.println("Enter account id");
                    try {
                        Long id = Long.valueOf(reader.readLine());
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
                            groupService.delete(id);
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
