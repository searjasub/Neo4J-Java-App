package group20.dbt230;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {

        boolean isDone = false;
        System.out.println("Welcome to the sample app for Neo4j\n");
        while (!isDone) {
            System.out.println(
                    "What would you like to do?\n" +
                            "[0] Create\n" +
                            "[1] Read\n" +
                            "[2] Update\n" +
                            "[3] Delete\n" +
                            "[5] Exit");

            int selection = Integer.parseInt(in.readLine());
            switch (selection) {
                case 0:
                    createContact(promptForName(), promptForLastName(), promptForHireYear());
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

                    break;
                case 5:
                    isDone = true;
                    break;
            }
        }

    }

    private static void deleteContact() {

    }

    private static void updateContact(int promptForId) {

    }

    private static void readContact(int promptForId) {

    }

    private static void createContact(String name, String lastName, int hireYear){


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
