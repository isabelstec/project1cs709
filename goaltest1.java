package project1;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class finderMap {
    char[][] map;
    boolean[][] visited; 
    
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
        for(int i = 0; i < rows.length; i++){
            char[] line = rows[i].toCharArray();
            for(int j = 0; j < line.length; j++){
                this.visited[i][j] = false;
                if(line[j] != '#' && line[j] != '!' && line[j] != 'x' && line[j] != 'p'&&line[j] != '\n' ){
                    System.err.println("Map must not contain characters other than: #, !, x, or p");
                }
                this.map[i][j] = line[j];
            }
        }
    }
    public boolean getPath(int x, int y){
        if (map[x][y] == 'x'){
            return false;
        }
        return true;
    }
    public boolean getVisited(int x, int y){
        return visited[x][y];
    }
    public void setVisited(int x, int y){
        if(this.visited[x][y] != true){
                this.visited[x][y] = true;
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
                    current[i][j] = 'p';
                }
                if(map[i][j] == '!' && visited[i][j] == true){
                    current[i][j] = 'v';
                }
                if(map[i][j] == '!' && visited[i][j] !=true){
                    current[i][j] = map[i][j];
                }
                if(map[i][j] == '#' && visited[i][j] == true){
                    current[i][j] = 'v';
                }
                if(map[i][j] == '#' && visited[i][j] != true){
                    current[i][j] = map[i][j];
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
        System.out.println();
        //not allowed on example
        testmap.setVisited(1,2);
        testmap.setVisited(3,4);
        //valid path on example
        testmap.setVisited(1,5);
        testmap.setVisited(2,1);
        testmap.setVisited(2,2);
        testmap.getString();
    }
}
