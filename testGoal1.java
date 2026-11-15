import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class testGoal1 {

    // Reads the file and returns its contents as one string.
    public static String readMap(String filename)
            throws FileNotFoundException {

        Scanner in = new Scanner(new File(filename));
        StringBuilder result = new StringBuilder();

        while (in.hasNextLine()) {
            result.append(in.nextLine());

            if (in.hasNextLine()) {
                result.append('\n');
            }
        }

        in.close();
        return result.toString();
    }

    // Attempts a visit and displays the result.
    public static void testVisit(finderMap maze, int x, int y) {
        System.out.println("Testing location (" + x + ", " + y + ")");
        System.out.println("Pathway: " + maze.getPath(x, y));
        System.out.println("Visited before: " + maze.getVisited(x, y));

        boolean accepted = maze.setVisited(x, y);

        System.out.println("Visit accepted: " + accepted);
        System.out.println("Visited after: " + maze.getVisited(x, y));
        System.out.println(maze.getString());
        System.out.println();
    }

    public static void main(String[] args)
            throws FileNotFoundException {

        if (args.length != 1) {
            System.out.println(
                    "Usage: java -jar prjAtestGoal1.jar datafile.txt");
            return;
        }

        String map = readMap(args[0]);

        if (map.length() == 0) {
            System.out.println("The maze file is empty.");
            return;
        }

        finderMap maze = new finderMap(map);

        System.out.println("Original maze:");
        System.out.println(maze.getString());
        System.out.println();

        String[] lines = map.split("\n");
        int count = 0;
        boolean testedWall = false;

        // Test up to three pathways and one wall.
        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length(); x++) {
                char square = lines[y].charAt(x);

                if (square == 'p' && count < 3) {
                    testVisit(maze, x, y);
                    count++;
                } else if (square == 'x' && !testedWall) {
                    testVisit(maze, x, y);
                    testedWall = true;
                }
            }
        }

        // A negative coordinate should not be accepted.
        testVisit(maze, -1, 0);
    }
}