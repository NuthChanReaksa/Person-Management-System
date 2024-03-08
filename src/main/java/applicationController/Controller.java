package applicationController;

import model.Person;
import repository.PersonRepository;
import service.PersonService;
import utils.TableUtils;

import java.util.*;


public class Controller {
    private static PersonService personService =
            new PersonService(new PersonRepository());
    Scanner input = new Scanner(System.in);
    public void addNewPersonCTL(){
        input.nextLine();
        System.out.println(
                personService.createPerson(input) > 0 ?
                        "Successfully Created a New Person"
                        : ""
        );
    }
    public void updatePersonCTL(){

        System.out.println(
                personService
                        .updatePerson(input) > 0 ?
                        "Successfully Update Person Info"
                        : ""
        );
    }
    public void deletePersonCTL(){

        System.out.println(
                personService
                        .deletePersonByID(input) > 0 ?
                        "Successfully Remove the Person"
                        : "");
    }
    public void showPersonInformationCTL(){

        int showOption;
        java.util.List<String> showMenu = new ArrayList<>(java.util.List.of(
                "================| Show Person Information |==================",
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
    public void searchPersonInformationCTL(){

        int searchOption;
        List<String> searchMenu = new ArrayList<>(Arrays.asList(
                "================| Search Person Information |==================",
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
}
//"Add New Person ",
//        "Update Person ",
//        "Delete Person",
//        "Show Person Information",
//        "Search Person Information",
//        "Exit"