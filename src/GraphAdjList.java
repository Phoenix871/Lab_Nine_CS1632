import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * @author Carter Jorgensen
 * Date: 11 / 09 / 2026
 * Purpose: To create a class that can model a graph using a adjancency list
 */
public class GraphAdjList {
    //Attributes (gonna try List's for this one)
    private final List<List<AbstractMap.SimpleEntry<Character, Double>>> adjList;


    //Constructors
    public GraphAdjList(String fName) throws FileNotFoundException{
        //The list where everything will be stored
        adjList = new ArrayList<>();

        //What will read the file the first time through
        Scanner firstScanner = new Scanner(new FileReader(fName));

        //Check if the file is empty
        if (!firstScanner.hasNext()) {
            throw new FileNotFoundException("There is nothing in this file!");
        }

        //An arraylist to keep track of the characters that show up
        ArrayList<Integer> characters = new ArrayList<>();

        //A loop to run through the whole file=============================
        while (firstScanner.hasNext()) {
            //All the relevant information for this current loop===========
            char fromNode = firstScanner.next().charAt(0); //to get an accurate index
            char toNode = firstScanner.next().charAt(0); //store simply as a character
            Double edgeWeight = firstScanner.nextDouble(); //not used right now but helps skip a character in the file
            //=============================================================

            if (!characters.contains(fromNode - 'A')) {
                characters.add(fromNode - 'A');
            }
            if (!characters.contains(toNode - 'A')) {
                characters.add(toNode - 'A');
            }
        }
        //=================================================================

        //need to find out how many lists to have in adjList
        int largest = characters.getFirst(); //keeps track of the 'largest' letter
        for (Integer i : characters) { //loops through the arraylist
            if (i > largest) {
                largest = i;
            }
        }

        for (int i = 0; i <= largest; i++) {
            adjList.add(new ArrayList<>()); //adds a new list for every letter up to the largest
        }

        //Now to add the values============================================
        Scanner secondScanner = new Scanner(new FileReader(fName)); //New scanner for adding data

        while (secondScanner.hasNext()) {
            //All the relevant information for this current loop===========
            char fromNode = secondScanner.next().charAt(0); //to get an accurate index
            char toNode = secondScanner.next().charAt(0); //store simply as a character
            Double edgeWeight = secondScanner.nextDouble(); //not used right now but helps skip a character in the file
            //=============================================================

            adjList.get(fromNode - 'A').add(new AbstractMap.SimpleEntry<>(toNode, edgeWeight)); //adds the edge in one direction
            adjList.get(toNode - 'A').add(new AbstractMap.SimpleEntry<>(fromNode, edgeWeight)); //adds the edge in the other direction

        }

        //just to save some memory, good practice
        firstScanner.close();
        secondScanner.close();
    }

    //Methods
    /**
     * A small method to simply print out the contents of the adjacency list
     */
    public void printAdjList() {
        for (int i = 0; i < adjList.size(); i++) {
            System.out.print(((char) (i + 'A')) + ": " + adjList.get(i).toString());

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

        if (start - 'A' < 0 || start - 'A' >= adjList.size()) { //check if the character is valid
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

            //Loops through each entry in adjList
            for (AbstractMap.SimpleEntry<Character, Double> c : adjList.get(currentCharacter - 'A')) {
                if (!visited.contains(c.getKey())) {
                    //Add character to visited
                    visited.add(c.getKey());

                    //add character to queue
                    queue.add(c.getKey());
                }
            }
        }

        //return the result
        return visited;
    }


    /**
     * A method to calculate the shortest path to all vertices from the starting vertex
     * @param startChar The vertex we start at
     * @throws IllegalArgumentException If the starting Vertex is not in our list
     */
    public void djikstraAlgorithm(char startChar) throws IllegalArgumentException {
        //Set startNode to an integer value
        startChar = Character.toUpperCase(startChar); //makes sure we get the right index
        int startNode = startChar - 'A';

        //Quick Check
        if (startChar - 'A' < 0 || startChar - 'A' >= adjList.size()) { //check if the character is valid
            throw new IllegalArgumentException("This character does not exist in the graph!");
        }

        //initialize distance arraylist needed for the algorithm
        ArrayList<Double> distance = new ArrayList<>();

        //initialize a previous node visited arraylist
        ArrayList<Character> prevNode = new ArrayList<>();

        //Loops through and assigns values of infinity to each letter and null to each previous character
        for (int i = 0; i < adjList.size(); i++) {
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


            //=======================================================================
            //Run a for loop through adjList
            for (AbstractMap.SimpleEntry<Character, Double> entry : adjList.get(currentIndex)) {
                double nodeWeight = entry.getValue();

                //Checks to make sure there is a non-negative or zero value
                if (nodeWeight > 0.0) {
                    //Store altDistance
                    double altDistance = distance.get(currentIndex) + nodeWeight;

                    //Check if the new distance is less than the old
                    if (altDistance < distance.get(entry.getKey() - 'A')) {
                        //add values for distance and prevNode
                        distance.set(entry.getKey() - 'A', altDistance);
                        prevNode.set(entry.getKey() - 'A', temp.node);

                        //Add new entry to priority queue
                        priorQueue.add(new Vertex(entry.getKey(), altDistance));
                    }
                }
            }

            //=======================================================================

        }
        //Print out results from the algorithm
        for (int i = 0; i < adjList.size(); i++) {
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
     * A method to simply get the alphabetical representation of the nodes in adjList
     * @return An arraylist containing the nodes of adjList
     */
    public ArrayList<Character> listOfNodes() {
        //The Arraylist to be returned
        ArrayList<Character> result = new ArrayList<>();

        //A loop to run through each list in adjList
        for (List<AbstractMap.SimpleEntry<Character, Double>> simpleEntries : adjList) {

            //A loop to run through each simple entry in 'simpleEntries'
            for (AbstractMap.SimpleEntry<Character, Double> j : simpleEntries) {

                //Makes sure that the character isn't already in result
                if (!result.contains(j.getKey())) {
                    result.add(j.getKey()); //Adds the character to result
                }
            }
        }

        //Return the result
        return  result;
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
