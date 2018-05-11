package sample;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainPane {

    private static final String INITIAL_DIRECTORY = System.getProperty("user.home") + "/Documents/docs";
    private static ObservableList<DeleteCandidate> deleteCandidates = FXCollections.observableArrayList();
    private static SelectionStatus selectionStatus = new SelectionStatus();

    public static Pane makeMainPane(Stage primaryStage) {
        Pane root = new Pane();
        VBox vboxRoot = new VBox(20);
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

        File initialDirectory = new File(INITIAL_DIRECTORY);
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
                    /*
                    The following event handler occurs when clicking the start button.
                     */
                    @Override
                    public void handle(final ActionEvent e) {
                        /*
                        Retrieve the value of the selected project root.
                         */
                        TextField textField = (TextField) primaryStage.getScene().lookup("#tfProjectRoot");
                        /*
                        Feed into a List all of the project's image files.
                         */
                        List<String> allImagesOnDisk = OrphanedImages.getAllImageFiles(textField.getText());
                        /*
                        Feed into a single string all of the project's text files.
                         */
                        String entireDocumentationText = OrphanedImages.loadAllTextIntoSingleString(textField.getText());
                        /*
                        For each of the found image files in the List, use pattern matching to see if the
                        file appears inside the large string. If not, then the image is unused and add it
                        to the list of delete candidates.
                         */

                        allImagesOnDisk.forEach((fullFileName) -> {
                            File localImageFile = new File(fullFileName);
                            String filename = localImageFile.getName();
                            Pattern p = Pattern.compile(filename);
                            Matcher m = p.matcher(entireDocumentationText);
                            if (!m.find()) {
                                //    System.out.println(localImageFile.getAbsolutePath());
                                File localDeleteCandidate = new File(localImageFile.getAbsolutePath());
                                deleteCandidates.add(new DeleteCandidate(false, localImageFile.getAbsolutePath(), localDeleteCandidate.length()));

                            }

                        });

                        deleteCandidates.forEach(deleteCandidate -> {
                            selectionStatus.setNumberFilesFound( selectionStatus.getNumberFilesFound() + 1 );
                            selectionStatus.setSizeFileFound( selectionStatus.getSizeFileFound() + deleteCandidate.getFileSize() );

                        });
                        SelectionStatus.updateStatusLabel(primaryStage,selectionStatus);

                    }
                });

        HBox hboxProjectRoot = new HBox();
        hboxProjectRoot.getChildren().addAll(labelProjectRoot, tfProjectRoot, btnProjectRoot, btnStart);
        return hboxProjectRoot;
    }

/*
This method lays out the second hbox, which has 1) a
table listing the unused graphics files and 2) a preview of the currently
selected file.
 */

    private static HBox makeHboxUnusedFiles() {

        TableColumn colDelete = new TableColumn("Delete");
        TableColumn<DeleteCandidate,String> colFilename = new TableColumn("Filename");
        TableColumn colSize = new TableColumn("Size");
        TableView tableUnusedFiles = new TableView();

        colDelete.setCellValueFactory(new Callback<CellDataFeatures<DeleteCandidate, Boolean>, ObservableValue<Boolean>>() {

            @Override
            public ObservableValue<Boolean> call(CellDataFeatures<DeleteCandidate, Boolean> param) {
                DeleteCandidate deleteCandidate = param.getValue();

                SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(deleteCandidate.isDeleteFlag());

                booleanProp.addListener(new ChangeListener<Boolean>() {

                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                                        Boolean newValue) {
                        deleteCandidate.setDeleteFlag(newValue);
                        System.out.println("Changed the value");
                    }
                });
                return booleanProp;
            }
        });

        colDelete.setCellFactory(param -> new CheckBoxTableCell<>());


        colFilename.setCellValueFactory(
                new PropertyValueFactory<DeleteCandidate, String>("fileName")
        );




        colSize.setCellValueFactory(
                new PropertyValueFactory<DeleteCandidate, Integer>("fileSize")
        );

        tableUnusedFiles.getColumns().addAll(colDelete, colFilename, colSize);

        tableUnusedFiles.setItems(deleteCandidates);
        tableUnusedFiles.setEditable(true);

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
        statusLabel.setId("statusLabel");
        vboxStatus.getChildren().add(statusLabel);
        return vboxStatus;
    }

}
