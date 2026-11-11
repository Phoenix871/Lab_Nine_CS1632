import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Carter Jorgensen
 * Date: 11 / 08 / 2026
 * Purpose: A main class to run my code
 */
public class Main {
    public static void main(String[] args) {

        // file that 100% works: src/Files/Test.txt

        try {
            //Actual user interaction
            Scanner input = new Scanner(System.in);

            System.out.println("Please enter a file to be read from: ");
            String fileName = input.next();

            //create objects using user input
            GraphAdjMatrix userMatrixGraph = new GraphAdjMatrix(fileName);
            GraphAdjList userListGraph = new GraphAdjList(fileName);

            System.out.println("\nMatrix Representation: ");
            userMatrixGraph.printAdjMatrix();

            System.out.println("\nList Representation: ");
            userListGraph.printAdjList();


            //Prompt user for a breadth-first search starting at a character of their choosing
            System.out.println("\nPlease enter a node to start searching: ");

            //Gets user input
            String userChar = input.next();
            char charToSearch = userChar.toUpperCase().charAt(0);

            //Print out results
            System.out.println("\nBreadth First Search List At " + charToSearch + ": " + userListGraph.breadthFirstSearch(charToSearch));
            System.out.println("\nBreadth First Search Matrix At " + charToSearch + ": " + userMatrixGraph.breadthFirstSearch(charToSearch));
        }
        catch (FileNotFoundException | IllegalArgumentException e) { //Catch's any stray errors that may occur
            System.out.println("Something went wrong: " + e);
        }
    }

}
