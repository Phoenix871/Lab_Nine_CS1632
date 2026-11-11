package src;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Carter Jorgensen
 * Date: 11 / 09 / 2026
 * Purpose: To create a class that can model a graph using a matrix (or 2-dimensional arraylist)
 */
public class GraphAdjMatrix {
    //Attributes
    private final ArrayList<ArrayList<Double>> adjMatrix;

    //Constructors
    /**
     * A constructor that builds a graph using an Adjacency matrix from a given file
     * @param fName The name of the file being read
     */
    public GraphAdjMatrix(String fName) throws FileNotFoundException {
        try {
            //Create a FileReader and a Scanner to be able to read the file in bits and pieces
            Scanner zeroScanner = new Scanner(new FileReader(fName));

            //Check if the file is empty
            if (!zeroScanner.hasNext()) {
                throw new FileNotFoundException("There is nothing in this file!");
            }

            //Need to initialize adjMatrix to have all zeros===================
            int adjMatSize = findLargestChar(zeroScanner);
            adjMatrix = new ArrayList<>(adjMatSize);

            //loops through each column of the matrix
            for (int col = 0; col < adjMatSize; col++) {
                ArrayList<Double> tempRow = new ArrayList<>(adjMatSize); //The arraylist to be added to the matrix

                //loops through each row of the matrix
                for (int row = 0; row < adjMatSize; row++) {
                    tempRow.add(row, 0.0); //presets rows to 0.0
                }
                //Adds the row to the column
                adjMatrix.add(tempRow);
            }
            //=================================================================

            //now to add weights to the existing edges=========================
            Scanner weightScanner = new Scanner(new FileReader(fName));

            //loop runs while there is a next element to be read
            while (weightScanner.hasNext()) {
                //Get the first and second index's as integers
                int firstIndex = weightScanner.next().charAt(0) - 'A';
                int secondIndex = weightScanner.next().charAt(0) - 'A';

                //The weight of the edge
                double weight = weightScanner.nextDouble();

                //Graph is bidirectional so it has to go both ways
                adjMatrix.get(firstIndex).set(secondIndex, weight);
                adjMatrix.get(secondIndex).set(firstIndex,weight);
            }
            //=================================================================

            //close both scanners to save space
            zeroScanner.close();
            weightScanner.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Methods
    /**
     * A helper method to find the size of the 2-dimensional matrix
     * @param s The scanner that reads the file
     * @return The size of the matrix
     */
    private int findLargestChar(Scanner s) {
        //The first character is the 'largest'
        int first = s.next().charAt(0);

        //Sets the largest to the first int
        int largest = first;

        //loops while there is a next value within scanner s
        while (s.hasNext()) {
            int temp = s.next().charAt(0); //gets the int value of the character

            //checks if the temp is larger than the current largest
            if (temp > largest) {
                largest = temp;
            }

        }
        //returns the largest minus the first to account for the index values of characters
        return largest - first + 1;
    }

    /**
     * A method that simply prints out the info for the adjacency matrix
     */
    public void printAdjMatrix() {
        //loops through each array in adjMatrix
        for (ArrayList<Double> array : adjMatrix) {
            //loops through each double in the array
            for (Double aDouble : array) {
                System.out.print(aDouble + " ");
            }
            System.out.println();
        }
    }

    /**
     * A method that uses breadth-first search to explore a graph that uses an adjacency list
     * @param start Where the algorithm will start at
     * @return The order at which each node is visited
     */
    public ArrayList<Character> breadthFirstSearch(char start) throws IllegalArgumentException {
        //Make sure start is uppercase
        start = Character.toUpperCase(start);

        if (start - 'A' < 0 || start - 'A' >= adjMatrix.size()) { //check if the character is valid
            throw new IllegalArgumentException("This character does not exist in the graph!");
        }

        //A queue to help keep track of where we are during the search
        Queue<Character> queue = new LinkedList<>();

        //The arraylist to be returned and used to let us know what has or hasn't been visited
        ArrayList<Character> visited = new ArrayList<>();

        //Add the start character to the queue and mark it as visited
        queue.add(start);
        visited.add(start);

        //loops for as long as the queue we have doesn't run empty, which would mean that we have been through every element
        while (!queue.isEmpty()) {
            char currentCharacter = queue.poll(); //Store the top-most element of the queue

            int currentIndex = currentCharacter - 'A';

            //Loops through each entry in adjMatrix
            for (int i = 0; i < adjMatrix.size(); i++) {
                //Create a character for i
                char c = (char) (i+'A');

                //checks if the entry is non-zero and whether it already has the character or not
                if (!visited.contains(c) && adjMatrix.get(currentIndex).get(i) != 0.0) {
                    //Add character to list and queue
                    visited.add(c);
                    queue.add(c);
                }
            }
        }

        //return the result
        return visited;
    }
}
