package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MainPane {


    public static Pane makeMainPane() {
        Pane root = new Pane();
        VBox vboxRoot = new VBox(50);
        vboxRoot.setAlignment(Pos.CENTER);
        vboxRoot.getChildren().addAll(makeHboxProject(),
                makeHboxUnusedFiles(),
                makeHboxDeleteFiles(),
                makeVboxStatus());
        root.getChildren().addAll(vboxRoot);
        return root;
    }

     private static HBox makeHboxProject() {
/*
This method lays out the top hbox, which has a lable, text field, and button to open a file picker.
 */

    Label labelProjectRoot = new Label("Project root");
    TextField tfProjectRoot = new TextField("~/Documents/docs/");
    Button btnProjectRoot = new Button("Browse...");
    HBox hboxProjectRoot = new HBox();
    hboxProjectRoot.getChildren().addAll(labelProjectRoot,tfProjectRoot,btnProjectRoot);
    return hboxProjectRoot;
    }


    private static HBox makeHboxUnusedFiles() {

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

        return hboxUnusedFiles;
    }

    private static HBox makeHboxDeleteFiles() {

        HBox hboxButtons = new HBox();
        hboxButtons.getChildren().add(new Button("Delete"));
        return hboxButtons;
    }


    private static VBox makeVboxStatus() {

        VBox vboxStatus = new VBox(10);
        Label statusLabel = new Label("Selected 0 of 100 files, 0 of 100 kb");
        vboxStatus.getChildren().add(statusLabel);
        return vboxStatus;
    }

}
