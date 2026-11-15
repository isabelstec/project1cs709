package project1;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.geometry.Pos;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;

import java.io.File;
import java.nio.file.Files;


public class finderMapGUI extends Application {

    private finderMap maze;
    private GridPane mazeGrid = new GridPane();
    private Label costLabel = new Label("Total Cost: __ ");
    private Label fileLabel = new Label("No file selected.");
    private Label statusLabel = new Label("Choose a maze file.");
    private Button solveButton = new Button("Find Cheapest Route");
    
    @Override
    public void start(Stage stage) {
        Button loadButton = new Button("Choose File");
        solveButton.setDisable(true);

        mazeGrid.setAlignment(Pos.CENTER);
        mazeGrid.setHgap(2);
        mazeGrid.setVgap(2);

        ScrollPane scrollPane = new ScrollPane(mazeGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefSize(550,350);

        loadButton.setOnAction(evt ->{
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Maze File");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            File file = chooser.showOpenDialog(stage);

            if(file != null){
                try{
                    String mapText = Files.readString(file.toPath());
                    maze = new finderMap(mapText);

                    fileLabel.setText(file.getName());
                    costLabel.setText("Total Cost: __");
                    statusLabel.setText("Maze loaded.");
                    solveButton.setDisable(false);
                    displayMaze(false);
                } catch(Exception except){
                    maze = null;
                    solveButton.setDisable(true);
                    statusLabel.setText("enter loading file: " + except.getMessage());
                }
            }
        });

        solveButton.setOnAction(evt -> {
            if (maze == null){
                return;
            }
            maze = new finderMap(getOriginalMap());
            maze.setVisited(maze.entrance[0], maze.entrance[1],1);
            if (maze.cheapest != null){
                displayMaze(true);
                costLabel.setText("Total Cost: " + maze.cheapest.getCompleteCost());
                statusLabel.setText("Cheapest route found!");
            } else{
                statusLabel.setText("No valid route found.");
            }
        });

        HBox fileControls = new HBox(10, loadButton, fileLabel);
        fileControls.setAlignment(Pos.CENTER_LEFT);

        HBox bottomControls = new HBox(20, solveButton, costLabel);
        bottomControls.setAlignment(Pos.CENTER);

        VBox root = new VBox(15, new Label("Maze Path Finder"), fileControls, scrollPane, bottomControls, statusLabel);

        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 640, 480);

        stage.setTitle("Maze Path Finder");
        stage.setScene(scene);
        stage.show();
    }

    private String getOriginalMap(){
        StringBuilder result = new StringBuilder();

        for(int i = 0; i < maze.map.length; i++){
            for(int j = 0; j < maze.map[i].length;j++){
                result.append(maze.map[i][j]);
            }
            result.append("\n");
        }
        return result.toString();
    }

    private void displayMaze(boolean showSolution){
        mazeGrid.getChildren().clear();
        for(int i = 0; i < maze.map.length; i++){
            for(int j = 0; j < maze.map[i].length; j++){
                String spot = maze.map[i][j];
                Rectangle rectangle = new Rectangle(45, 45);
                Color color = Color.WHITE;

                if(spot.equals("x")){
                    color = Color.DARKGRAY;
                } else if (spot.equals("#")){
                    color = Color.LIGHTBLUE;
                }else if(spot.equals("!")){
                    color = Color.LIGHTCORAL;
                } else if(showSolution && maze.cheapest.route[i][j].matches("[1-9][0-9]*")){
                    color = Color.LIGHTGREEN;
                }
                rectangle.setFill(color);
                rectangle.setStroke(Color.BLACK);

                Label text = new Label(spot);

                if(showSolution && maze.cheapest.route[i][j].matches("[1-9][0-9]*")){
                    text.setText(maze.cheapest.route[i][j]);
                }
                StackPane square = new StackPane(rectangle, text);
                mazeGrid.add(square, j, i);
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }

}
