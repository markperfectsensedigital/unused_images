package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Pane root = new Pane();
        primaryStage.setTitle("Unused Images");


        Label labelProjectRoot = new Label("Project root");
        TextField tfProjectRoot = new TextField("~/Documents/docs/");
        Button btnProjectRoot = new Button("Browse...");
        HBox hboxProjectRoot = new HBox();
        hboxProjectRoot.getChildren().addAll(labelProjectRoot,tfProjectRoot,btnProjectRoot);



        CheckBox checkBoxDeleteAll = new CheckBox();

        TableColumn colDelete = new TableColumn("Delete");
        TableColumn colFilename = new TableColumn("Filename");
        TableColumn colSize = new TableColumn("Size");
        TableView tableUnusedFiles = new TableView();


        final ObservableList<DeleteCandidate> deleteCandidates = FXCollections.observableArrayList(
                new DeleteCandidate(true,"/tmp/barf",500),
                new DeleteCandidate(true,"/tmp/barf",500),
                new DeleteCandidate(true,"/tmp/barf",500)
        );

        colDelete.setCellValueFactory(
                new PropertyValueFactory<DeleteCandidate,Boolean>("deleteFlag")
        );
        colFilename.setCellValueFactory(
                new PropertyValueFactory<DeleteCandidate,String>("fileName")
        );
        colSize.setCellValueFactory(
                new PropertyValueFactory<DeleteCandidate,Integer>("fileSize")
        );

        tableUnusedFiles.getColumns().addAll(colDelete,colFilename,colSize);

        tableUnusedFiles.setItems(deleteCandidates);

        ImageView imagePreview = new ImageView();
        Image image = new Image("file:///Users/mlautman/Desktop/fall-by-the-lake-14767797082J2.jpg");
        imagePreview.setImage(image);
        imagePreview.setPreserveRatio(true);
        imagePreview.setFitHeight(100.0);

        HBox hboxUnusedFiles = new HBox();
        hboxUnusedFiles.getChildren().addAll(tableUnusedFiles, imagePreview);

        HBox hboxButtons = new HBox();
        hboxButtons.getChildren().add(new Button("Delete"));


        VBox vboxStatus = new VBox(10);
        Label statusLabel = new Label("Selected 0 of 100 files, 0 of 100 kb");
        vboxStatus.getChildren().add(statusLabel);


        VBox vboxRoot = new VBox(50);
        vboxRoot.setAlignment(Pos.CENTER);
        vboxRoot.getChildren().addAll(hboxProjectRoot,hboxUnusedFiles,hboxButtons,vboxStatus);

        root.getChildren().addAll(vboxRoot);

        primaryStage.setScene(new Scene(root, 300, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
