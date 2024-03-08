import login.LoginFrame;
import repository.PersonRepository;
import service.PersonService;
import javax.swing.*;
public class Main {

    private static PersonService personService =
            new PersonService(new PersonRepository());

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
