import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import domain.User;
import service.UserService;

public class Program {
    public static void main(String[] args) throws JsonProcessingException {
        UserService userService = new UserService();

        userService.createUser("admin", "password");

        User user = userService.findById(Long.valueOf(1));

        System.out.println(user);

//        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
//        String json = mapper.writeValueAsString(user);
//        System.out.println(json);
    }
}
