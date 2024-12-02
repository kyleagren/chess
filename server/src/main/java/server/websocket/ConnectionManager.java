package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, Integer> usernameGameIDMap = new ConcurrentHashMap<>();

    public void add(String username, Session session, int gameID) {
        var connection = new Connection(username, session);
        connections.put(username, connection);
        usernameGameIDMap.put(username, gameID);
    }

    public void remove(String username) {
        connections.remove(username);
        usernameGameIDMap.remove(username);
    }

    public void broadcast(String excludeUsername, String message) throws IOException {
        var removeList = new ArrayList<Connection>();
        int gameID = usernameGameIDMap.get(excludeUsername);
        var usersInCurrentGame = usernameGameIDMap.entrySet().stream()
                .filter(entry -> entry.getValue() == gameID && !entry.getKey().equals(excludeUsername))
                .map(Map.Entry::getKey)
                .toList();

        notify(message, usersInCurrentGame, removeList);
    }

    private void notify(String message, List<String> usersInCurrentGame,
                        ArrayList<Connection> removeList) throws IOException {
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (usersInCurrentGame.contains(c.username)) {
                    c.send(message);
                }
            } else {
                removeList.add(c);
            }
        }
        for (var c : removeList) {
            connections.remove(c.username);
        }
    }

    public void sendToAll(String sender, String message) throws IOException {
        var removeList = new ArrayList<Connection>();
        int gameID = usernameGameIDMap.get(sender);
        var usersInCurrentGame = usernameGameIDMap.entrySet().stream()
                .filter(entry -> entry.getValue() == gameID)
                .map(Map.Entry::getKey)
                .toList();

        notify(message, usersInCurrentGame, removeList);
    }

    public void send(String includeUsername, String message) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.username.equals(includeUsername)) {
                    c.send(message);
                }
            } else {
                removeList.add(c);
            }
        }
        for (var c : removeList) {
            connections.remove(c.username);
        }
    }
}
