public class finderMap {

    private final char[][] map;
    private final int[][] costs;
    private final int[][] visited;

    private completeRoute[] routes;
    private int routeCount;
    private completeRoute bestRoute;

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

        this.routes = new completeRoute[10];

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

    public int getRouteCount() {
        return this.routeCount;
    }

    // Returns a saved route by its array index.
    public completeRoute getRoute(int index) {
        if (index < 0 || index >= this.routeCount) {
            throw new IllegalArgumentException(
                    "The route index is invalid.");
        }

        return this.routes[index];
    }

    public int getTotalCost() {
        if (this.bestRoute == null) {
            return 0;
        }

        return this.bestRoute.getTotalCost();
    }

    public int getTotalMoves() {
        if (this.bestRoute == null) {
            return 0;
        }

        return this.bestRoute.getTotalMoves();
    }

    // Saves a completed route and checks its cost.
    private void saveRoute(int currentCost, int totalMoves) {
        completeRoute route = new completeRoute(
                this.visited, currentCost, totalMoves);

        // Make a larger array if the current array is full.
        if (this.routeCount == this.routes.length) {
            completeRoute[] larger =
                    new completeRoute[this.routes.length * 2];

            for (int i = 0; i < this.routeCount; i++) {
                larger[i] = this.routes[i];
            }

            this.routes = larger;
        }

        this.routes[this.routeCount] = route;
        this.routeCount++;

        if (this.bestRoute == null
                || route.getTotalCost() < this.bestRoute.getTotalCost()) {
            this.bestRoute = route;
        }
    }

    // Searches all routes and restores the lowest-cost route for display.
    public boolean solve() {
        this.routes = new completeRoute[10];
        this.routeCount = 0;
        this.bestRoute = null;

        int startX = -1;
        int startY = -1;

        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                this.visited[y][x] = 0;

                if (this.map[y][x] == '#') {
                    startX = x;
                    startY = y;
                }
            }
        }

        floodFill(startX, startY, 1, 0);

        if (this.bestRoute == null) {
            return false;
        }

        // Copy the best saved route into the display array.
        for (int y = 0; y < this.visited.length; y++) {
            for (int x = 0; x < this.visited[y].length; x++) {
                this.visited[y][x] = this.bestRoute.getStep(x, y);
            }
        }

        return true;
    }

    // Explores every route without revisiting a square in the current route.
    private void floodFill(int x, int y, int visitNum,
            int currentCost) {

        if (!getPath(x, y)) {
            return;
        }

        if (getVisited(x, y) != 0) {
            return;
        }

        setVisited(x, y, visitNum);
        currentCost += getCost(x, y);

        if (this.map[y][x] == '!') {
            saveRoute(currentCost, visitNum - 1);
        } else {
            // Try up.
            floodFill(x, y - 1, visitNum + 1, currentCost);

            // Try down.
            floodFill(x, y + 1, visitNum + 1, currentCost);

            // Try left.
            floodFill(x - 1, y, visitNum + 1, currentCost);

            // Try right.
            floodFill(x + 1, y, visitNum + 1, currentCost);
        }

        // Erase this visit so another route can use the square.
        setVisited(x, y, 0);
    }

    // Returns the maze with the lowest-cost route numbered.
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