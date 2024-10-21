package service;

import model.AuthData;
import org.junit.jupiter.api.*;

import java.util.UUID;
import java.util.regex.Pattern;

public class AuthServiceTests {
    private static AuthService authService;

    private static AuthData expectedAuth;

    private static String uuid = UUID.randomUUID().toString();

    @BeforeAll
    public static void init() {
        authService = new AuthService();
    }

    @BeforeEach
    public void setup() {
        authService.deleteAll();
        expectedAuth = new AuthData(uuid, "username");
    }


    @Test
    public void createAuthSuccess() {
        AuthData actualAuth = null;
        try {
            actualAuth = authService.createAuth("username");
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        Assertions.assertNotNull(actualAuth);
        Assertions.assertEquals(expectedAuth.username(), actualAuth.username());
        Assertions.assertTrue(Pattern.matches("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})", actualAuth.authToken()));

    }

    @Test
    public void createAuthFailure() {
        Assertions.assertThrows(Exception.class, () -> authService.createAuth(null));
    }

    @Test
    public void getAuthSuccess() {
        AuthData actualAuth = null;
        try {
            expectedAuth = authService.createAuth("username");
            actualAuth = authService.getAuth(expectedAuth.authToken());
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        Assertions.assertNotNull(actualAuth);
        Assertions.assertEquals(expectedAuth, actualAuth);
    }

    @Test
    public void getAuthFailure() {
        AuthData actualAuth = null;
        try {
            expectedAuth = authService.createAuth("username");
            actualAuth = authService.getAuth("invalid");
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        Assertions.assertNull(actualAuth);
    }

    @Test
    public void deleteAuthSuccess() {
        AuthData actualAuth = null;
        try {
            expectedAuth = authService.createAuth("username");
            authService.deleteAuth(expectedAuth.authToken());
            actualAuth = authService.getAuth(expectedAuth.authToken());
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        Assertions.assertNull(actualAuth);
    }

    @Test
    public void deleteAuthFailure() {
        try {
            expectedAuth = authService.createAuth("username");
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        Assertions.assertThrows(Exception.class, () -> authService.deleteAuth("invalid"));
    }

    @Test
    public void deleteAllSuccess() {
        AuthData actualAuth = null;
        try {
            expectedAuth = authService.createAuth("username");
            authService.deleteAll();
            actualAuth = authService.getAuth(expectedAuth.authToken());
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        Assertions.assertNull(actualAuth);
    }
}
