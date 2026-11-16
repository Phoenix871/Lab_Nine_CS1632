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

    public void djikstraAlgorithm(char startChar) throws IllegalArgumentException {
        //Set startNode to an integer value
        startChar = Character.toUpperCase(startChar); //makes sure we get the right index
        int startNode = startChar - 'A';

        //Quick Check
        if (startChar - 'A' < 0 || startChar - 'A' >= adjMatrix.size()) { //check if the character is valid
            throw new IllegalArgumentException("This character does not exist in the graph!");
        }

        //initialize distance arraylist needed for the algorithm
        ArrayList<Double> distance = new ArrayList<>();

        //initialize a previous node visited arraylist
        ArrayList<Character> prevNode = new ArrayList<>();

        //Loops through and assigns values of infinity to each letter and null to each previous character
        for (int i = 0; i < adjMatrix.size(); i++) {
            distance.add(Double.POSITIVE_INFINITY); //marks each distance as infinity to start with because we don't know if a path exists
            prevNode.add(null); //marks the previous node as null
        }

        //initialize priority Queue as a min priority queue
        PriorityQueue<Vertex> priorQueue = new PriorityQueue<>(Comparator.comparingDouble(value -> value.distance));

        //add start node to priority queue and add distance
        priorQueue.add(new Vertex(startChar, 0.0));
        distance.set(startNode, 0.0);

        //The loop runs while the priority queue is not empty
        while (!priorQueue.isEmpty()) {
            Vertex temp = priorQueue.poll(); //take the top element out

            //Check if this vertex is even relevant
            if (temp.distance > distance.get(temp.node - 'A')) {
                continue; //skips to next loop around
            }

            //keep track of which index we are at
            int currentIndex = temp.node - 'A';

            //Run a for loop through adjMatrix
            for (int i = 0; i < adjMatrix.size(); i++) {
                double nodeWeight = adjMatrix.get(currentIndex).get(i);

                //Checks to make sure there is a non-negative or zero value
                if (nodeWeight > 0.0) {
                    //Store altDistance
                    double altDistance = distance.get(currentIndex) + nodeWeight;

                    //Check if the new distance is less than the old
                    if (altDistance < distance.get(i)) {
                        //add values for distance and prevNode
                        distance.set(i, altDistance);
                        prevNode.set(i, temp.node);

                        //Add new entry to priority queue
                        priorQueue.add(new Vertex((char) (i + 'A'), altDistance));
                    }
                }
            }

        }
        //Print out results from the algorithm
        for (int i = 0; i < adjMatrix.size(); i++) {
            //Check if the node is disconnected or not
            if (!Double.isInfinite(distance.get(i))) {
                LinkedList<Character> path = makePath(i, prevNode);

                //Time to actually print
                for (int j = 0; j < path.size(); j++) {
                    System.out.print(path.get(j));

                    if (j < path.size() - 1) {
                        System.out.print(" -> ");
                    }
                }
                System.out.println(" Dist: " + distance.get(i));
            }
        }
    }

    /**
     * A helper method for Djikstra's algorithm
     * @param i The current index we are at
     * @param prevNode The previous nodes found earlier in Djikstra's algorithm
     * @return The path to that node
     */
    private static LinkedList<Character> makePath(int i, ArrayList<Character> prevNode) {
        char pathDestination = (char) (i + 'A'); //The character we are trying to arrive at

        //The actual path that was taken
        LinkedList<Character> path = new LinkedList<>();

        //Time to work through prevNode
        Character currentChar = pathDestination;

        while (currentChar != null) {
            //add current char to the path
            path.addFirst(currentChar);

            //make current char the previous character
            currentChar = prevNode.get(currentChar - 'A');
        }

        //Returns the path to the node
        return path;
    }


    /**
     * A method to simply get the alphabetical representation of the nodes in adjMatrix
     * @return An arraylist containing the nodes of adjMatrix
     */
    public ArrayList<Character> listOfNodes() {
        //The Arraylist to be returned
        ArrayList<Character> result = new ArrayList<>();

        //A loop that goes through each index and adds it to the arraylist to return
        for (int i = 0; i < adjMatrix.size(); i++) {
            //checks to make sure that it is not just an empty node that doesn't interact with the graph
            if (!adjMatrix.get(i).isEmpty()) {
                result.add((char) (i + 'A'));
            }
        }

        //Return the result
        return result;
    }


    /**
     * A small helper class to help with implementing Dijkstra's algorithm
     */
    private static class Vertex {
        //attributes
        char node;
        double distance;

        //Constructor
        /**
         * A basic constructor for the vertex helper subclass
         * @param node The 'character' for the vertex/node
         * @param distance The distance the node has to a previous node
         */
        public Vertex(char node, double distance) {
            this.node = node;
            this.distance = distance;
        }
    }
}
