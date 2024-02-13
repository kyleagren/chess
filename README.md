# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)]([https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDAEooDmSAzmFMARDQVqhFHXyFiwUgBF+wAIIgQKLl0wATeQCNgXFDA3bMmdlAgBXbDADEaYFQCerDt178kg2wHcAFkjAxRFRSAFoAPnJKGigALhgAbQAFAHkyABUAXRgAegt9KAAdNABvfMp7AFsUABoYXDVvaA06lErgJAQAX0xhGJgIl04ePgEhaNF4qFceSgAKcqgq2vq9LiaoFpg2joQASkw2YfcxvtEByLkwRWVVLnj2FDAAVQKFguWDq5uVNQvDbTxMgAUQAMsC4OkYItljAAGbmSrQgqYb5KX5cAaDI5uUaecYiFTxNAWBAIQ4zE74s4qf5o25qeIgab8FCveYw4DVOoNdbNL7ydF3f5GeIASQAciCWFDOdzVo1mq12p0YJL0ilkbQcSMPIIaQZBvSMUyWYEFBYwL53hUuSgBdchX9BqK1VLgTKtUs7XVgJbfOkIABrdBujUwP1W1GChmY0LYyl4-UTIkR-2BkNoCnHJMEqjneORPqUeKRgPB9C9aKULGRYLoMDxABMAAYW8USmWM+geugNCYzJZrDZoNJHjBQRBOGgfP5Aph62Ei9W4olUhlsjl9Gp8R25SteRsND1i7AIjqqcnCSh4ggp0g0DbvfLD-zs7i9XmC5cYyaYI8XjefcHR+YUXUBcgwQhWUPjteFES9TRtFrIYP1OFMbxgdkoHfXV0OvC5jTuU0UFZC0rSfT5o0dWMIldEFwUhL1YQRCAkTLJCIiIxk0ytbss24rhz0TT8DVLdMKyzC9cwNCJT3EvjJKrahKAiRdGxgVt21KLtJN7NB+1McwrFsMwUFDSd2EsZgbD8AIgmQBt-nkxIZCg9JgU3bcuF3ewJMzZSYmEnNRIw28p2suZdMzA5pNC68uN-Yj-yecjrWi9AQKdITwnoqCmP3eC2N43xOPCQT4jS6jQLUYK0OpMKYCquL8PzFREpov8NBQBAnhQNK5jSrLaNyiC3PBDymv9Ir2P9Ti6rwhrr2JUlyRapa2pQOSV1iLg2IMbxgGcCA4RgIM0AgbwH3YGArQMbrerGGBDsxLgLFjOFSRgYo3pAXwI0xBBQCDGATu2KBzFgao1GAR5AprQZ1ObNtijmC6YG0CANEcA4+wHYzh2mDQJzcGAAHE7UxWy5wckJmEGFyEjJ4Eslydg7WKDKs1PC51qvTaHieCnoai-zMtwy8v3a8qkp4gCBq54aMToiCGOg5i4NY2aoyMDqavuKao0EhbJbEw3Sr5qWtplzrkoA4XVDmJW7hVoF8qhYAyRm-9KbKiqfeh6rspNmTGtBNw5nZ6pvlikTWtEbaVNXAApMgUglZ6chgZAeFB06IG0AArFBwHUU81McjBke0koACIo9UWv4gSOuG7FGQm5gWuAEYmwAZgAFi6AA6UfMn0wzBxMmxsAsKBsF6+AzQMB3Z3shdK-p5ck+b9dWZyBvObFtAOwbiU7RPFcQ-igWYGZUjAgd0XFMzOppgARwsVQwAAISxnGJahwSjbfWgswAK2Ps7Wqo03aMRgraao3sOK6xAdlSqc1jbhEtmbZqccNoJ1QbGEirIn4fy-jwP+2Nh5nztFAnKrpJTSihDQxBD5IAB1UH7WWBs24yCDiNbBjU0AoG8Bw74kc7TtzqCw+0gCb4EJcg3b4MBU7pxgNMbA0x9AYE-MUTOMAChIE9kgAAXigYmDcMYQDwP2cu4QkaaRRqUeukiO7xB7v3AetcajFFrnZQI2Flid1rl3Yoo9h4wAnvjIcthHA9TvKI5OEAHzkzgjYbQQMQBBg3nTZyO01zPH3ofPyL90AdnUnACAd4oDSNcZfJO1946pkLsktAT8uZvxQJ-b+lCAGCOAf7eW-pn7lhivw5WMDIJwI1ogrWJUuG2x4lVTB-Tb64JCk062gyhZ2jmGQnp-9qGuLoa7KZ6teHewbuoFBqzJgcLkZsxp+DUxwF8CXEGSBTp3WmDAbgEYEAsmxhGeoQM4mwDWEgdgwjibsKtPwO+VToDDz0WKU6AQADkmI7oGLggvI6lA6iF3yMwaYYA54zmDKDKGwAQx6Oxfs3OsMOhoDqBAb5V19DFEoJDUGloqURg0MTPQoNtAFBiMPB5zytncPiFYLQj9dkNzqBUxFUAAC8XNh7AXGS7SZzwkgyAUJNXhdR-FslgtUMqtzUx7UQUGB8xMwaUp4PwfIrLYDYsySDMG0hwY8sqJC3wzAUA0BnNoAwVksbbAuhYdgFs8H8wUfkl1ZKy5X3sZvauqN0aY2xrjAy0Tp5mCOvAXqeAIzYAXoQT8a95zqTyTvVy7lPK5CMPDKATzE2phAGWqATtJVdulYsg2PbSJQAUGSftmDXTjRZsCEqtToZ1EWNc7QnarbEggH66AvzMSRv7Na62Ll0bcp3X8-d7aK50yzWgCUW7c2OEwHjIAA))

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
