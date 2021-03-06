package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Unused Images");
        Pane root = MainPane.makeMainPane(primaryStage);
        primaryStage.setScene(new Scene(root, 500, 600));
        primaryStage.show();

    }


    public static void main(String[] args) {

        launch(args);
    }
}
