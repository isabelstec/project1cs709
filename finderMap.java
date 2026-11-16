public class finderMap {

    private final char[][] map;
    private final int[][] costs;
    private final int[][] visited;

    private int totalCost;
    private int totalMoves;

    public finderMap(String map) {
        if (map == null || map.length() == 0) {
            throw new IllegalArgumentException("The maze is empty.");
        }

        String[] lines = map.split("\n");

        int rows = lines.length;
        int columns = lines[0].length();

        if (columns == 0) {
            throw new IllegalArgumentException(
                    "The maze has an empty row.");
        }

        this.map = new char[rows][columns];
        this.costs = new int[rows][columns];
        this.visited = new int[rows][columns];

        String digits = "0123456789ABCDEF";
        int starts = 0;
        int exits = 0;

        for (int y = 0; y < rows; y++) {
            if (lines[y].length() != columns) {
                throw new IllegalArgumentException(
                        "Every row must have the same number of characters.");
            }

            for (int x = 0; x < columns; x++) {
                char square = lines[y].charAt(x);
                this.map[y][x] = square;

                if (square == 'x' || square == 'X') {
                    this.costs[y][x] = -1;
                } else if (square == '#') {
                    this.costs[y][x] = 0;
                    starts++;
                } else if (square == '!') {
                    this.costs[y][x] = 0;
                    exits++;
                } else {
                    int cost = digits.indexOf(square);

                    if (cost == -1) {
                        throw new IllegalArgumentException(
                                "Pathways must use 0-9 or A-F.");
                    }

                    this.costs[y][x] = cost;
                }
            }
        }

        if (starts != 1 || exits != 1) {
            throw new IllegalArgumentException(
                    "The maze must contain one # and one !.");
        }
    }

    // Returns whether the location is inside the maze.
    private boolean inBounds(int x, int y) {
        return y >= 0 && y < this.map.length
                && x >= 0 && x < this.map[y].length;
    }

    // Returns whether the square can be visited.
    public boolean getPath(int x, int y) {
        if (!inBounds(x, y)) {
            return false;
        }

        return this.costs[y][x] >= 0;
    }

    // Returns the cost, or -1 for a wall or invalid location.
    public int getCost(int x, int y) {
        if (!inBounds(x, y)) {
            return -1;
        }

        return this.costs[y][x];
    }

    // Returns the step number, or 0 if unvisited or outside the maze.
    public int getVisited(int x, int y) {
        if (!inBounds(x, y)) {
            return 0;
        }

        return this.visited[y][x];
    }

    // Stores a step number. A value of 0 erases a visit.
    public boolean setVisited(int x, int y, int visitNum) {
        if (!getPath(x, y) || visitNum < 0) {
            return false;
        }

        this.visited[y][x] = visitNum;
        return true;
    }

    // Returns the cost of the successful route.
    public int getTotalCost() {
        return this.totalCost;
    }

    // Returns the number of moves in the successful route.
    public int getTotalMoves() {
        return this.totalMoves;
    }

    // Clears previous visits and finds the starting square.
    public boolean solve() {
        this.totalCost = 0;
        this.totalMoves = 0;

        for (int y = 0; y < this.visited.length; y++) {
            for (int x = 0; x < this.visited[y].length; x++) {
                this.visited[y][x] = 0;
            }
        }

        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                if (this.map[y][x] == '#') {
                    return floodFill(x, y, 1, 0);
                }
            }
        }

        return false;
    }

    // Recursively searches for the exit and erases incorrect moves.
    private boolean floodFill(int x, int y, int visitNum,
            int currentCost) {

        if (!getPath(x, y)) {
            return false;
        }

        if (getVisited(x, y) != 0) {
            return false;
        }

        setVisited(x, y, visitNum);
        currentCost += getCost(x, y);

        if (this.map[y][x] == '!') {
            this.totalCost = currentCost;
            this.totalMoves = visitNum - 1;
            return true;
        }

        // Try up.
        if (floodFill(x, y - 1, visitNum + 1, currentCost)) {
            return true;
        }

        // Try down.
        if (floodFill(x, y + 1, visitNum + 1, currentCost)) {
            return true;
        }

        // Try left.
        if (floodFill(x - 1, y, visitNum + 1, currentCost)) {
            return true;
        }

        // Try right.
        if (floodFill(x + 1, y, visitNum + 1, currentCost)) {
            return true;
        }

        // All directions failed, so erase this move.
        setVisited(x, y, 0);
        return false;
    }

    // Returns the maze with numbered steps on the successful route.
    public String getString() {
        StringBuilder result = new StringBuilder();

        int maxSteps = this.map.length * this.map[0].length;
        int width = Integer.toString(maxSteps).length() + 1;

        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                String square;

                if (this.visited[y][x] > 0) {
                    square = Integer.toString(this.visited[y][x]);
                } else {
                    square = Character.toString(this.map[y][x]);
                }

                result.append(String.format("%" + width + "s", square));
            }

            if (y < this.map.length - 1) {
                result.append('\n');
            }
        }

        return result.toString();
    }
}