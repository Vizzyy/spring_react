package vizzyy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vizzyy.controller.responses.RoleResponse;
import vizzyy.domain.User;
import vizzyy.service.AuthenticationService;
import vizzyy.service.KeyService;
import vizzyy.service.LoggingService;
import vizzyy.service.UserService;

import java.io.IOException;
import java.util.List;

import static vizzyy.service.AuthenticationService.getUserName;
import static vizzyy.service.AuthenticationService.getUserRole;

@RestController
@RequestMapping(value = "/users")
public class UsersController {

    @Autowired
    UserService userService;

    @Autowired
    KeyService keyService;

    @Autowired
    LoggingService loggingService;

    @RequestMapping(value= "/list")
    public List<User> users(){
        loggingService.addEntry(String.format("%s calling /users/list", AuthenticationService.getUserName()));
        return userService.getUsers();
    }

    @RequestMapping(value = "/generate")
    @PreAuthorize("hasAnyAuthority('ROLE_OWNER')")
    public void generate(@RequestParam String CN, @RequestParam String role, @RequestParam String pw) throws IOException, InterruptedException {
        loggingService.addEntry(String.format("Calling /users/generate?CN=%s&role=%s&pw=%s", CN, role, pw));
        //keyService.generateUser(CN, pw);
        User newUser = userService.createUser(CN, role, CN, pw);
        loggingService.addEntry(String.format("Successfully created user: %s", newUser.toString()));
    }

    @RequestMapping(value = "/delete")
    @PreAuthorize("hasAnyAuthority('ROLE_OWNER')")
    public void delete(@RequestParam String CN) {
        loggingService.addEntry(String.format("Calling /users/remove?CN=%s", CN));
        userService.deleteUser(CN);
        userService.expireUserSessions(CN);
        loggingService.addEntry(String.format("Successfully delete user: %s", CN));
    }

    @RequestMapping(value = "/role")
    @PreAuthorize("hasAnyAuthority('ROLE_USER, ROLE_POWER, ROLE_ADMIN, ROLE_OWNER')")
    public RoleResponse getRole() {
        loggingService.addEntry(String.format("Checking %s's role... %s", getUserName(), getUserRole()));
        return new RoleResponse(getUserRole(), getUserName());
    }
}
