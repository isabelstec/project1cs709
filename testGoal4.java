import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class testGoal4 {

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
                    "Usage: java -jar prjAtestGoal4.jar multipleRoutes.txt");
            return;
        }

        String map = readMap(args[0]);

        if (map.length() == 0) {
            System.out.println("The maze file is empty.");
            return;
        }

        finderMap maze = new finderMap(map);

        System.out.println("Original maze:");
        System.out.println(map);
        System.out.println();

        boolean solved = maze.solve();

        if (solved) {
            System.out.println("Routes found: " + maze.getRouteCount());

            for (int i = 0; i < maze.getRouteCount(); i++) {
                completeRoute route = maze.getRoute(i);

                System.out.println("Route " + (i + 1)
                        + ": cost = " + route.getTotalCost()
                        + ", moves = " + route.getTotalMoves());
            }

            System.out.println();
            System.out.println("Lowest-cost route:");
            System.out.println(maze.getString());
            System.out.println();

            System.out.println("Total moves: " + maze.getTotalMoves());
            System.out.println("Total cost: " + maze.getTotalCost());
        } else {
            System.out.println("No route to the exit was found.");
            System.out.println(maze.getString());
        }
    }
}