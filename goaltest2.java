package project1;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class finderMap {
    char[][] map;
    boolean[][] visited; 
    int[] entrance;
    
    public finderMap(String map){
        String[] rows = map.split("\n");
        for (int i = 0; i < rows.length; i++) {
            if (rows[i] != null) {
                rows[i] = rows[i].toLowerCase();
            }
        }
        int columns = rows[0].length();
        this.map = new char[rows.length][columns];
        this.visited = new boolean[rows.length][columns];
        //this.moves = new int[rows.length][columns];
        for(int i = 0; i < rows.length; i++){
            char[] line = rows[i].toCharArray();
            for(int j = 0; j < line.length; j++){
                if(line[j] == '#'){
                    entrance[0] = i;
                    entrance[1] = j;
                }
                this.visited[i][j] = false;
                if(line[j] != '#' && line[j] != '!' && line[j] != 'x' && line[j] != 'p'&&line[j] != '\n' ){
                    System.err.println("Map must not contain characters other than: #, !, x, or p");
                }
                this.map[i][j] = line[j];
                
            }
        }
    }
    public boolean getPath(int x, int y){
        if (map[x][y] == '#'){
            return true;
        }
        else if(map[x][y] == '!'){
            return true;
        }
        else if(map[x][y] == 'p'){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean getVisited(int x, int y){
        return visited[x][y];
    }
    public void setVisited(int x, int y){
        if(this.visited[x][y] != true){
            if(this.map[x][y] == '#' || this.map[x][y] == '!' || this.map[x][y] == 'p'){
                this.visited[x][y] = true;
            }
        }
    }
    public void getString(){
        int size = map.length;
        char[][] current = new char[size][map[0].length];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < map[i].length; j++){
                if(map[i][j] == 'x'){
                    current[i][j] = 'x';
                }
                if(map[i][j] == 'p' && visited[i][j] == true){
                    current[i][j] = 'v';
                }
                if(map[i][j] == 'p' && visited[i][j] != true){
                    current[i][j] = map[i][j];
                }
                if(map[i][j] == '!' && visited[i][j] == true){
                    current[i][j] = 'v';
                }
                if(map[i][j] == '!' && visited[i][j] !=true){
                    current[i][j] = map[i][j];
                }
                if(map[i][j] == '#'){
                    if(visited[i][j] == true){
                        current[i][j] = 'v';
                    } else{
                        current[i][j] = map[i][j];
                    }
                    
                }
            }
        }
        for (int i = 0; i < size; i++) { 
            for (int j = 0; j < map[i].length; j++) { 
                System.out.print(current[i][j]);
            }
            System.out.println(); 
        }
    }
    
    public void floodFill( int row, int col ) {
        if (row < 0 || row >= map.length || col < 0 || col >= map[row].length) {
            return;
        }
        if (getVisited(row, col)) return;
        if (!getPath(row, col)) return;
        if(this.map[row][col] == '!') return;
        setVisited(row, col);
        floodFill(row - 1, col);
        floodFill(row + 1, col); 
        floodFill(row, col - 1);
        floodFill(row, col + 1);
    }
    
    public static void main(String[] args){
        //String test = "x!xxxxx\nxpxppp#\nxpppxxx\nxxpppxx\nxpppxxx";

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
        /*for (int i = 0; i < 4; i++){
            for (int j = 0; j < 7; j++){
                System.out.println("Allowable: " + testmap.getPath(i,j));
                testmap.setVisited(i,j);
                //System.out.println("Visited: " + testmap.getVisited(i,j));
            }
        }*/
        testmap.floodFill(testmap.entrance[0], testmap.entrance[1]);
        System.out.println();
        testmap.getString();
    }
}
