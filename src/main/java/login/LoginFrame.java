package login;

import model.Person;
import repository.PersonRepository;
import service.PersonService;
import utils.TableUtils;
import view.MainView;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel emailLabel = new JLabel("Email:");
        panel.add(emailLabel);

        emailField = new JTextField();
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = getLoginButton();
        panel.add(loginButton);

        add(panel);

    }
    public static void executeMainCode() {
        Scanner input = new Scanner(System.in);
        int option;
        do {
            option = MainView.renderMain(input);
            switch (option) {
                case 1: {
                    input.nextLine(); // clear buffer
                    System.out.println(
                            personService.createPerson(input) > 0 ?
                                    "Successfully Created a New Person"
                                    : ""
                    );

                }
                break;
                case 2: {
                    System.out.println(
                            personService
                                    .updatePerson(input) > 0 ?
                                    "Successfully Update Person Info"
                                    : ""
                    );
                }
                break;
                case 3: {
                    System.out.println(
                            personService
                                    .deletePersonByID(input) > 0 ?
                                    "Successfully Remove the Person"
                                    : "");
                    ;
                }
                break;
                case 4: {
                    int showOption;
                    java.util.List<String> showMenu = new ArrayList<>(java.util.List.of(
                            "Show Original Order",
                            "Show Descending Order (ID)",
                            "Show Descending Order (name) ",
                            "Exit"));
                    do {
                        TableUtils.renderMenu(showMenu, "Show Person Information");
                        System.out.print("Choose your option: ");
                        showOption = input.nextInt();


                        switch (showOption) {
                            case 1:

                                TableUtils.renderObjectToTable(personService.getAllPerson());
                                break;
                            case 2:
                                // descending id
                                TableUtils.renderObjectToTable(
                                        personService.getAllPersonDescendingByID()
                                );
                                break;
                            case 3:
                                // descending name
                                TableUtils.renderObjectToTable(
                                        personService.getAllPersonDescendingByName()
                                );
                                break;
                            default:
                                System.out.println("Invalid option ...!!!!");
                                break;
                        }
                    } while (showOption != showMenu.size());
                }
                break;
                case 5: {
                    int searchOption;
                    List<String> searchMenu = new ArrayList<>(Arrays.asList(
                            "Search By ID",
                            "Search By Gender",
                            "Search By Country",
                            "Exit"));
                    do {
                        TableUtils.renderMenu(searchMenu, "Search for Person");
                        System.out.print("Choose your option:");
                        searchOption = input.nextInt();
                        switch (searchOption) {
                            case 1:
                                int searchID = 0;
                                System.out.println("Enter Person ID to search:");
                                searchID = input.nextInt();
                                int finalSearchID = searchID;
                                try {
                                    Person optionalPerson =
                                            personService.getAllPerson()
                                                    .stream()
                                                    .filter(person -> person.getId() == finalSearchID)
                                                    .findFirst()
                                                    .orElseThrow(() -> new ArithmeticException("Whatever exception!! "));
                                    TableUtils.renderObjectToTable(
                                            Collections.singletonList(optionalPerson));
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    System.out.println("There is no element with ID=" + searchID);
                                }

                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                        }

                    } while (searchOption != searchMenu.size());

                }
                break;
                case 6:
                    System.out.println("Exit from the program!!! ");
                    break;
                default:
                    System.out.println("Invalid Option!!!!!! ");
                    break;
            }
        } while (option != 6);

    }
    private JButton getLoginButton() {
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            if (authenticate(email, password)) {
                JOptionPane.showMessageDialog(null, "Login successful!");
                dispose(); // Close the login frame
                // Display the MainView

                SwingUtilities.invokeLater(() -> LoginFrame.main(null));
            } else {
                JOptionPane.showMessageDialog(null, "Invalid email or password!");
            }

        });
        return loginButton;
    }

    private boolean authenticate(String email, String password) {
         String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
         String DB_USERNAME = "postgres";
         String DB_PASSWORD = "1234567890";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("org.postgresql.Driver"); // Load PostgreSQL JDBC driver
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            String query = """
            SELECT * FROM person_db WHERE email = ?
""";

            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                // Assuming passwords are stored in plaintext
                if (password.equals(storedPassword)) {
                    // Successful authentication
                    return true;
                } else {
                    // Incorrect password
                    System.out.println("Incorrect password for email: " + email);
                    return false;
                }
            } else {
                // Email not found
                System.out.println("Email not found: " + email);
                return false;
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static PersonService personService =
            new PersonService(new PersonRepository());

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        Scanner input = new Scanner(System.in);
        int option;
        do {
            option = MainView.renderMain(input);
            switch (option) {
                case 1: {
                    input.nextLine(); // clear buffer
                    System.out.println(
                            personService.createPerson(input) > 0 ?
                                    "Successfully Created a New Person"
                                    : ""
                    );

                }
                break;
                case 2: {
                    System.out.println(
                            personService
                                    .updatePerson(input) > 0 ?
                                    "Successfully Update Person Info"
                                    : ""
                    );
                }
                break;
                case 3: {
                    System.out.println(
                            personService
                                    .deletePersonByID(input) > 0 ?
                                    "Successfully Remove the Person"
                                    : "");
                    ;
                }
                break;
                case 4: {
                    int showOption;
                    java.util.List<String> showMenu = new ArrayList<>(java.util.List.of(
                            "Show Original Order",
                            "Show Descending Order (ID)",
                            "Show Descending Order (name) ",
                            "Exit"));
                    do {
                        TableUtils.renderMenu(showMenu, "Show Person Information");
                        System.out.print("Choose your option: ");
                        showOption = input.nextInt();


                        switch (showOption) {
                            case 1:

                                TableUtils.renderObjectToTable(personService.getAllPerson());
                                break;
                            case 2:
                                // descending id
                                TableUtils.renderObjectToTable(
                                        personService.getAllPersonDescendingByID()
                                );
                                break;
                            case 3:
                                // descending name
                                TableUtils.renderObjectToTable(
                                        personService.getAllPersonDescendingByName()
                                );
                                break;
                            default:
                                System.out.println("Invalid option ...!!!!");
                                break;
                        }
                    } while (showOption != showMenu.size());
                }
                break;
                case 5: {
                    int searchOption;
                    List<String> searchMenu = new ArrayList<>(Arrays.asList(
                            "Search By ID",
                            "Search By Gender",
                            "Search By Country",
                            "Exit"));
                    do {
                        TableUtils.renderMenu(searchMenu, "Search for Person");
                        System.out.print("Choose your option:");
                        searchOption = input.nextInt();
                        switch (searchOption) {
                            case 1:
                                int searchID = 0;
                                System.out.println("Enter Person ID to search:");
                                searchID = input.nextInt();
                                int finalSearchID = searchID;
                                try {
                                    Person optionalPerson =
                                            personService.getAllPerson()
                                                    .stream()
                                                    .filter(person -> person.getId() == finalSearchID)
                                                    .findFirst()
                                                    .orElseThrow(() -> new ArithmeticException("Whatever exception!! "));
                                    TableUtils.renderObjectToTable(
                                            Collections.singletonList(optionalPerson));
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    System.out.println("There is no element with ID=" + searchID);
                                }

                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                        }

                    } while (searchOption != searchMenu.size());

                }
                break;
                case 6:
                    System.out.println("Exit from the program!!! ");
                    break;
                default:
                    System.out.println("Invalid Option!!!!!! ");
                    break;
            }
        } while (option != 6);


    }
}
