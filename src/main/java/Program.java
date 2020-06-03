import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import domain.User;
import service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Program {
    private static final String[] LOGIN_MENU = {"LOGIN", "REGISTER", "EXIT"};
    private static final String[] MAIN_MENU = {"ME", "GROUP", "ACCOUNT", "LOGOUT"};
    private static final String WELCOME_MESSAGE = "+----------------------------+\n" +
            "|      Welcome to wallet     |\n" +
            "|                            |\n" +
            "+----------------------------+";

    private UserService userService;
    private BufferedReader reader;
    ObjectMapper mapper;
    private Boolean loggedIn;
    private String login;



    public Program() {
        this.userService = new UserService();
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.loggedIn = false;
        this.login = "";
        this.mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);;
    }

    public static void main(String[] args) throws IOException {
        Program program = new Program();

        program.printWelcomeMessage();
        program.printLoginMenu();

        String answer;
        boolean firstLogin = false;
        while (true) {
            answer = program.reader.readLine().toLowerCase();

            if ("exit".equals(answer.toLowerCase())) {
                break;
            }

            if (program.loggedIn) {
                if (!firstLogin) {
                    firstLogin = true;
                    program.printMainMenu();
                }

                program.userFuncs(answer);
            } else {
                program.doAuth(answer);
            }
        }
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

    private void printWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
    }

    private void doAuth(String answer) throws IOException {

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

            case "3":
                System.out.println(userService.findByLogin("q").getLogin());
                break;

            case "help":
                printLoginMenu();
                break;

            default:
                System.out.println("Wrong input. Enter 'help' for info");
        }
    }

    private void userFuncs(String answer) throws JsonProcessingException {
        switch (answer) {

            case "me":
                getMe();
                break;

            case "3":
                System.out.println(userService.findByLogin("q").getLogin());
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

    private void groupMenu() {

    }

    private void accountMenu() {

    }
}
