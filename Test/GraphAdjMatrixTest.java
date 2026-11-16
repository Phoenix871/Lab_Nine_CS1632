import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Carter Jorgensen
 * Date: 11 / 10 / 2026
 * Purpose: To test GraphAdjMatrix
 */
class GraphAdjMatrixTest {

    @Test
    void breadthFirstSearch() {
        //try block to catch any errors
        try {
            //Initialize the object
            GraphAdjMatrix matrixTest = new GraphAdjMatrix("src/Files/UnitTest.txt");

            //Get the actual value
            ArrayList<Character> test = matrixTest.breadthFirstSearch('A');

            //What the value should be
            ArrayList<Character> actual = new ArrayList<>();
            actual.add('A'); actual.add('C'); actual.add('F'); actual.add('B'); actual.add('D'); actual.add('E');

            //Test whether they are equal as a whole
            assertEquals(Arrays.toString(actual.toArray()), Arrays.toString(test.toArray()));

            //randomly test index's in test
            assertEquals('A', test.getFirst());
            assertNotEquals('Z', test.get(3));
            assertEquals('E', test.getLast());

        }
        catch (FileNotFoundException e) { //Catches any errors where the file is not found
            throw new RuntimeException(e);
        }
    }

    /**
     * A simple method to test the listOfNodes method
     */
    @Test
    public void listOfNodes() {
        try {
            //Initialize the object
            GraphAdjMatrix listTest = new GraphAdjMatrix("src/Files/UnitTest.txt");

            //The expected result
            ArrayList<Character> expected = new ArrayList<>();
            expected.add('A'); expected.add('B'); expected.add('C'); expected.add('D'); expected.add('E'); expected.add('F');

            //Test if they are equal
            assertArrayEquals(expected.toArray(), listTest.listOfNodes().toArray());

        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * A method to test the functionality of the dijkstraAlgorithm method
     * @throws Exception If some error occurs, it is thrown
     */
    @Test
    public void testDijkstraAlgorithm() throws Exception {
        //Create the graph object
        GraphAdjMatrix testGraph = new GraphAdjMatrix("src/Files/UnitTest.txt");

        //Capture the output sent to the console (found this method online, so I'm not taking full credit for it)
        ByteArrayOutputStream systemOutput = new ByteArrayOutputStream();
        PrintStream systemOriginal = System.out;

        System.setOut(new PrintStream(systemOutput));

        //try to use Dijkstra's algorithm
        try {
            testGraph.djikstraAlgorithm('A');
        }
        finally {
            System.setOut(systemOriginal);
        }

        //converts what the system printed into a string I can check
        String result = systemOutput.toString();

        //Run the assertTrue's
        assertTrue(result.contains("A Dist: 0.0"));
        assertTrue(result.contains("A -> C -> B Dist: 8.5"));
        assertTrue(result.contains("A -> C Dist: 2.3"));
        assertTrue(result.contains("A -> C -> D Dist: 6.3"));
        assertTrue(result.contains("A -> C -> E Dist: 12.2"));
        assertTrue(result.contains("A -> C -> F Dist: 5.3"));
    }
}