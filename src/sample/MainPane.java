package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class MainPane {

    private static final String INITIAL_DIRECTORY=System.getProperty("user.home") + "/Documents/docs";
    public static Pane makeMainPane(Stage primaryStage) {
        Pane root = new Pane();
        VBox vboxRoot = new VBox(50);
        vboxRoot.setAlignment(Pos.CENTER);
        vboxRoot.getChildren().addAll(makeHboxProject(primaryStage),
                makeHboxUnusedFiles(),
                makeHboxDeleteFiles(),
                makeVboxStatus());
        root.getChildren().addAll(vboxRoot);
        return root;
    }

     private static HBox makeHboxProject(Stage primaryStage) {
/*
This method lays out the top hbox, which has the following:

1) A label
2) A text field
3) A button to open a directory picker
4) A button to start the search routine
 */

    Label labelProjectRoot = new Label("Project root");
    TextField tfProjectRoot = new TextField(INITIAL_DIRECTORY);
    tfProjectRoot.setId("tfProjectRoot");
    Button btnProjectRoot = new Button("Browse...");

         File initialDirectory = new File (INITIAL_DIRECTORY);
         final DirectoryChooser directoryChooser = new DirectoryChooser();
         directoryChooser.setTitle("Sphinx Project Directory");
         directoryChooser.setInitialDirectory(initialDirectory);


        /*
        Add event handler to open directory chooser dialog when clicking the project root button.
         */
         btnProjectRoot.setOnAction(
                 new EventHandler<ActionEvent>() {
                     @Override
                     public void handle(final ActionEvent e) {
                         File file = directoryChooser.showDialog(primaryStage);
                         if (file != null) {
                             tfProjectRoot.setText(file.getPath());

                         }
                     }
                 });

         Button btnStart = new Button("Start");
         btnStart.setOnAction(
                 new EventHandler<ActionEvent>() {
                     @Override
                     public void handle(final ActionEvent e) {
                         TextField textField =  (TextField) primaryStage.getScene().lookup("#tfProjectRoot");
                         OrphanedImages.getAllImageFiles(textField.getText());
                         String entireDocumentationText = OrphanedImages.loadAllTextIntoSingleString(textField.getText());
                     }
                 });


    HBox hboxProjectRoot = new HBox();
    hboxProjectRoot.getChildren().addAll(labelProjectRoot,tfProjectRoot,btnProjectRoot,btnStart);
    return hboxProjectRoot;
    }

/*
This method lays out the second hbox, which has 1) a
table listing the unused graphics files and 2) a preview of the currently
selected file.
 */

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


    /*
    This method lays out the third hbox, which has a button that deletes the selected files.
     */
    private static HBox makeHboxDeleteFiles() {

        HBox hboxButtons = new HBox();
        hboxButtons.getChildren().add(new Button("Delete"));
        return hboxButtons;
    }


    /*
    This method lays out the bottom hbox, which is a status bar.
     */
    private static VBox makeVboxStatus() {

        VBox vboxStatus = new VBox(10);
        Label statusLabel = new Label("Selected 0 of 100 files, 0 of 100 kb");
        vboxStatus.getChildren().add(statusLabel);
        return vboxStatus;
    }

}
