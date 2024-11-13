package service;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import response.EmptySuccessResponse;

import java.util.UUID;
import java.util.regex.Pattern;

public class UserServiceTests {
    private static UserService userService;

    private static UserData newUser;

    @BeforeAll
    public static void init() {
        userService = new UserService();
    }

    @BeforeEach
    public void setup() {
        userService.deleteAll();
        newUser = new UserData("username1", "password1", "email@place.com");
    }

    @Test
    public void registerSuccess() {
        AuthData expectedAuth = new AuthData(UUID.randomUUID().toString(), "username1");
        AuthData actualAuth = null;
        try {
            actualAuth = userService.register(newUser);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        Assertions.assertNotNull(actualAuth);
        Assertions.assertEquals(expectedAuth.username(), actualAuth.username());
        Assertions.assertTrue(Pattern.matches("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})", actualAuth.authToken()));
    }

    @Test
    public void registerFailure() {
        try {
            userService.register(newUser);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        Assertions.assertThrows(Exception.class, () -> userService.register(newUser));
    }

    @Test
    public void loginSuccess() {
        AuthData expectedAuth = null;
        AuthData actualAuth = null;
        try {
            expectedAuth = userService.register(newUser);
            userService.logout(expectedAuth.authToken());
            actualAuth = userService.login(newUser.username(), newUser.password());
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        Assertions.assertNotNull(actualAuth);
        Assertions.assertEquals(expectedAuth.username(), actualAuth.username());
        Assertions.assertTrue(Pattern.matches("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})", actualAuth.authToken()));
    }

    @Test
    public void loginFailure() {
        AuthData expectedAuth;
        try {
            expectedAuth = userService.register(newUser);
            userService.logout(expectedAuth.authToken());
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        Assertions.assertThrows(Exception.class, () -> userService.login(newUser.username(), "badPassword"));
    }

    @Test
    public void logoutSuccess() {
        AuthData expectedAuth = null;
        Object result = null;
        try {
            expectedAuth = userService.register(newUser);
            result = userService.logout(expectedAuth.authToken());
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        Assertions.assertNotNull(expectedAuth);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getClass(), EmptySuccessResponse.class);
    }

    @Test
    public void logoutFailure() {
        AuthData expectedAuth = null;
        try {
            expectedAuth = userService.register(newUser);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        Assertions.assertNotNull(expectedAuth);
        Assertions.assertThrows(Exception.class, () -> userService.logout("asdf"));
    }

    @Test
    public void deleteAllSuccess() {
        try {
            userService.register(newUser);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        userService.deleteAll();

        Assertions.assertThrows(Exception.class, () -> userService.login(newUser.username(), newUser.password()));
    }

}
