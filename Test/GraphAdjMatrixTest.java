import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
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
}