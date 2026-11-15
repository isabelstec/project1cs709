import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class testGoal2 {

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

    public static void main(String[] args)
            throws FileNotFoundException {

        if (args.length != 1) {
            System.out.println(
                    "Usage: java -jar prjAtestGoal2.jar datafile.txt");
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

        boolean solved = maze.solve();

        if (solved) {
            System.out.println("Exit found!");
        } else {
            System.out.println("No route to the exit was found.");
        }

        System.out.println(maze.getString());
    }
}