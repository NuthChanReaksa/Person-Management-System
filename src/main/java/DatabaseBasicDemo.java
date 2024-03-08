import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Deprecated
public class DatabaseBasicDemo {
    public static void main(String[] args) {
        String databaseUrl="jdbc:postgresql://localhost:5432/postgres";
        String username="postgres";
        String password="1234567890";

        String getAllPersonSQL= """
                SELECT * FROM person_tb;
                """;
        try(
                Connection con = DriverManager.getConnection(databaseUrl,username,password);
                Statement statement = con.createStatement();
        ){
          var resultSet=   statement.executeQuery(getAllPersonSQL);
          while(resultSet.next()){
              System.out.println("Person ID:"+resultSet.getInt("id"));
              System.out.println("Person Name:"+resultSet.getString("fullname"));
              System.out.println("Person Gender :"+resultSet.getString("gender"));
              System.out.println("-----------------------------------------");
          }

            System.out.println("We are able to connect to it now !");
        }catch (SQLException ex ){
            ex.printStackTrace();
        }



    }
}
