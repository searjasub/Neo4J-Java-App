package group20.dbt230;

import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.NoSuchRecordException;
import org.neo4j.driver.v1.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Main {

    public static Connection connection;

    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private static Driver driver;


    public static void main(String[] args) throws IOException, SQLException {

        connection = DriverManager.getConnection("jdbc:neo4j:bolt://localhost:7687", "neo4j", "password");
        driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "password"));


        boolean isDone = false;
        System.out.println("Welcome to the sample app for Neo4j\n");
        while (!isDone) {
            System.out.println(
                    "What would you like to do?\n" +
                            "[0] Create Contact\n" +
                            "[1] Read\n" +
                            "[2] Update\n" +
                            "[3] Delete\n" +
                            "[4] Exit");

            int selection = Integer.parseInt(in.readLine());
            switch (selection) {
                case 0:
                    createNodes();
                    break;
                case 1:
                    readContact(promptForId());
                    break;
                case 2:
                    updateContact(promptForId());
                    break;
                case 3:
                    deleteContact();
                    break;
                case 4:
                    isDone = true;
                    break;
            }
        }
    }

    private static void deleteContact() throws IOException {
        int id = promptForId();
        String cql = "MATCH (c:Contact) WHERE ID(c)=" + id + " Delete c";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(cql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateContact(int id) throws IOException {

        System.out.println("What would you like to change?");
        System.out.println("[0] First Name\n" +
                "[1] Last Name\n" +
                "[2] Hire Year");
        int selection = Integer.parseInt(in.readLine());
        switch (selection) {
            case 0:
                String newName = promptForName();
                String cql = "MATCH (c:Contact) WHERE ID(c)=" + id + " SET c.firstName = '" + newName + "'";
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(cql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                String newLastName = promptForLastName();
                String cql1 = "MATCH (c:Contact) WHERE ID(c)=" + id + " SET c.lastName = '" + newLastName + "'";
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(cql1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                int newYear = promptForHireYear();
                String cql2 = "MATCH (c:Contact) WHERE ID(c)=" + id + " SET c.hireYear = '" + newYear + "'";
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(cql2);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private static void readContact(int id) {
        String cql = "MATCH (c:Contact) WHERE ID(c)=" + id + " RETURN c";
        try (Session session = driver.session()) {
            StatementResult rs = session.run(cql);
            Record record = rs.next();

            List<Pair<String, Value>> values = record.fields();
            for (Pair<String, Value> nameValue : values) {
                Value value = nameValue.value();
                String firstName = value.get("firstName").asString();
                String lastName = value.get("lastName").asString();
                String hireYear = value.get("hireYear").asString();

                Employee employee = new Employee(id, firstName, lastName, Integer.parseInt(hireYear));
                System.out.println(employee);
            }
        } catch (NoSuchRecordException ex) {
            System.out.println("Id not valid");
        }
    }

    private static void createNodes() throws IOException {

        String firstName = promptForName();
        String lastName = promptForLastName();
        int hireYear = promptForHireYear();

        StringBuilder cql = new StringBuilder("CREATE (n:Contact { firstName: '" + firstName + "', lastName: '" + lastName + "', hireYear: '" + hireYear + "' })");

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(String.valueOf(cql));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int promptForId() throws IOException {
        System.out.println("Please enter the id of the contact: ");
        return Integer.parseInt(in.readLine());
    }

    private static String promptForName() throws IOException {
        System.out.println("First Name: ");
        return in.readLine();
    }

    private static String promptForLastName() throws IOException {
        System.out.println("Last Name: ");
        return in.readLine();
    }

    private static int promptForHireYear() throws IOException {
        System.out.println("Hire date: ");
        return Integer.parseInt(in.readLine());
    }

}
