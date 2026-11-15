package project1;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class finderMap {
    String[][] map;
    int[][] visited;    
    boolean finished = false;
    static final int[][] dirSet = {{-1,0},{0,-1},{+1,0},{0,+1}};
    int[] entrance = new int[2];
    
    public finderMap(String map){
        //getting sizes and fixing formatting
        String[] rows = map.split("\n");
        int columns = rows[0].length();
        //setting up arrays
        this.map = new String[rows.length][columns];
        this.visited = new int[rows.length][columns];
        //parsing map
        for(int i = 0; i < rows.length; i++){
            char[] line = rows[i].toCharArray();
            for(int j = 0; j < line.length; j++){
                if(line[j] == '#'){
                    entrance[0] = i;
                    entrance[1] = j;
                    System.out.println(entrance[0]);
                    System.out.println(entrance[1]);
                }
                this.visited[i][j] = 0;
                this.map[i][j] = String.valueOf(line[j]);
            }
        }
    }
    public boolean getPath(int x, int y){
        if (map[x][y].equals("x")){
            return false;
        }return true;
    }
   /*  public boolean getVisited(int x, int y){
        if(visited[x][y] > 0){
            return true;
        }
        return false;
    }*/
    public int getVisited(int x, int y){
        return visited[x][y];
    }
    public void setVisited (int row, int col, int move){ //knightsTour code
        if(finished) return;
		if ( row < 0 || row >= map.length || col < 0 || col >= map[row].length ) return;
		if (getVisited(row, col) > 0) return;
        if (!getPath(row, col) ) return;

		visited[row][col] = move;
		
		if ( map[row][col].equals("!") ) {
			System.out.println( "Finished successfully!" );
			finished = true;
			return;
		}
		for (int i = 0 ; i < dirSet.length; i++ ) {
			if (! finished) setVisited( row+dirSet[i][0], col+dirSet[i][1], move + 1 );
		}
		if (! finished) {
            visited[row][col] = 0;
        }
	}
    public int getCost(int x, int y){

        if (map[x][y].equals("#") || map[x][y].equals("!") || map[x][y].equals("x") || map[x][y].equals("p")){
            return 0;
        }
        return Integer.parseInt(map[x][y], 16);
    }
    public void getString(){
        int size = map.length;
        int sum = 0;
        String[][] current = new String[size][map[0].length];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < map[i].length; j++){
                if(map[i][j].equals("x")){
                    current[i][j] = "x";
                }
                if(getPath(i,j) && visited[i][j] > 0){
                    current[i][j] = Integer.toString( visited[i][j]);
                    sum += getCost(i,j);
                }
                if(getPath(i,j) && visited[i][j] == 0){
                    current[i][j] = Integer.toString( visited[i][j]);
                }
            }
        }
        for (int i = 0; i < size; i++) { 
            for (int j = 0; j < map[i].length; j++) { 
                System.out.print(current[i][j]);
            }
            System.out.println(); 
        }
        System.out.println("Total Cost: " + sum);
    }
    public void getMoves(){
        int size = visited.length;
        int[][] current = new int[size][visited[0].length];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < visited[i].length; j++){
                current[i][j] = visited[i][j];
            }
        }
        for (int i = 0; i < size; i++) { 
            for (int j = 0; j < map[i].length; j++) { 
                System.out.print(current[i][j]);
            }
            System.out.println(); 
        }
    }
    public static void main(String[] args){
        if (args.length < 1) {
            System.out.println("Error: Please provide a file path.");
            System.exit(1); 
        }
        File file = new File(args[0]);
        StringBuilder test = new StringBuilder();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                test.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: The file '" + args[0] + "' could not be found.");
        }
        finderMap testmap = new finderMap(test.toString());
        testmap.getString();
        testmap.setVisited(testmap.entrance[0], testmap.entrance[1], 1);
        testmap.getString();
    }
}
 