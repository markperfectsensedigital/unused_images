package sample;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class MainPane {

    private static final String INITIAL_DIRECTORY = System.getProperty("user.home") + "/Documents/docs";
    private static ObservableList<DeleteCandidate> deleteCandidates = FXCollections.observableArrayList();
    private static SelectionStatus selectionStatus = new SelectionStatus();
    private static ImageView imagePreview = new ImageView();
    private static Button btnCopy = new Button("Copy to clipboard");

    public static Pane makeMainPane(Stage primaryStage) {
        Pane root = new Pane();
        VBox vboxRoot = new VBox(20);
        vboxRoot.setAlignment(Pos.CENTER);
        vboxRoot.getChildren().addAll(makeHboxProject(primaryStage),
                makeHboxUnusedFiles(primaryStage),
                makeHboxDeleteFiles(),
                makeVboxStatus());
        vboxRoot.setPadding(new Insets(15, 12, 15, 12));
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
                    public void handle(final ActionEvent event) {
                        /*
                        Retrieve the value of the selected project root.
                         */
                        TextField textField = (TextField) primaryStage.getScene().lookup("#tfProjectRoot");
                        String projectRoot = textField.getText();
                        /* Check if the value in textField is a project's root directory. */
                        if (Utilities.isRootDirectory(projectRoot)) {
                            /*   Feed into a List all of the project's image files.
                             */
                            Set<File> imageDeletionCandidates = OrphanedImages.getAllImageFiles(projectRoot);


                        /*
                        Retrieve all source files.
                         */
                            Set<File> sourceFiles = Utilities.sourceFiles(projectRoot);

                            sourceFiles.forEach(sourceFile -> {
                                List<String> sourceLines = Collections.emptyList();
                                /* Get the path without the file name */
                                String fullFilePath = FilenameUtils.getFullPath(sourceFile.getPath());
                                try {
                                    sourceLines = Files.readAllLines(sourceFile.toPath(), StandardCharsets.UTF_8);
                                    sourceLines.forEach(sourceLine -> {
                                        Matcher matcher = Utilities.imagePattern.matcher(sourceLine);
                                        while (matcher.find()) {
                                            String normalizedPath = FilenameUtils.normalize(fullFilePath + matcher.group(2));
                                            File cannonicalFileReference = new File(normalizedPath);
                                            imageDeletionCandidates.remove(cannonicalFileReference);
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            TableView tableView = (TableView) primaryStage.getScene().lookup("#tableView");
                            imageDeletionCandidates.forEach(deleteCandidate -> {
                                selectionStatus.setNumberFilesFound(selectionStatus.getNumberFilesFound() + 1);
                                selectionStatus.setSizeFileFound(selectionStatus.getSizeFileFound() + deleteCandidate.length());
                                tableView.getItems().add(new DeleteCandidate(deleteCandidate));

                            });
                            SelectionStatus.updateStatusLabel(primaryStage, selectionStatus);
                            btnCopy.setDisable(false);
                        } else {

                            Utilities.updateStatusLabel(primaryStage, "The directory you selected is not a project root. Try again.");

                        }

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

    private static HBox makeHboxUnusedFiles(Stage primaryStage) {

        TableColumn colDelete = new TableColumn("Delete");
        TableColumn<DeleteCandidate, String> colFilename = new TableColumn("Filename");
        TableColumn colSize = new TableColumn("Size");
        TableView tableUnusedFiles = new TableView();
        tableUnusedFiles.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.7f));
        tableUnusedFiles.setId("tableView");

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

                        if (newValue) { /*If turn the check box ON so that we want to delete the file */
                            selectionStatus.setNumberFilesSelected(selectionStatus.getNumberFilesSelected() + 1);
                            selectionStatus.setSizeFilesSelected(selectionStatus.getSizeFilesSelected() + deleteCandidate.getLongFileSize());

                        } else {/*If turn the check box OFF so that we want to retain the file */
                            selectionStatus.setNumberFilesSelected(selectionStatus.getNumberFilesSelected() - 1);
                            selectionStatus.setSizeFilesSelected(selectionStatus.getSizeFilesSelected() - deleteCandidate.getLongFileSize());
                        }
                        SelectionStatus.updateStatusLabel(primaryStage, selectionStatus);
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

        tableUnusedFiles.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<DeleteCandidate>() {
                    @Override
                    public void changed(ObservableValue<? extends DeleteCandidate> observable, DeleteCandidate oldValue, DeleteCandidate newValue) {
                        System.out.println("You selected filename " + newValue.getFileName());

                        Image image = new Image("file://" + newValue.getFileName());
                        imagePreview.setImage(image);

                    }
                }

        );

        Image image = new Image("file:///Users/mlautman/Desktop/fall-by-the-lake-14767797082J2.jpg");
        imagePreview.setImage(image);
        imagePreview.setPreserveRatio(true);
        imagePreview.setFitHeight(100.0);

        HBox hboxUnusedFiles = new HBox();
        hboxUnusedFiles.getChildren().addAll(tableUnusedFiles,imagePreview);

        return hboxUnusedFiles;
    }

    /*
    This method lays out the third hbox, which has a button that deletes the selected files.
     */
    private static HBox makeHboxDeleteFiles() {

        HBox hboxButtons = new HBox();

        Button btnDelete = new Button("Delete");
        btnDelete.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        deleteCandidates.forEach(deleteCandidate -> {
                            if (deleteCandidate.isDeleteFlag()) {

                                try {
                                    Files.deleteIfExists(Paths.get(deleteCandidate.getFileName()));
                                } catch (IOException x) {
                                    System.out.println("Could not delete file " + deleteCandidate.getFileName());

                                }
                            }
                        });
                    }
                });

        btnCopy = new Button("Copy to clipboard");

        btnCopy.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        Clipboard clipboard = Clipboard.getSystemClipboard();
                        ClipboardContent content = new ClipboardContent();

                        String fileString = deleteCandidates
                                .stream()
                                .map(s -> s.getFileName().substring(0))
                                .collect(Collectors.joining("\n"));

                        content.putString(fileString);
                        clipboard.setContent(content);

                    }
                }
        );

        btnCopy.setDisable(true);

        Button btnClose = new Button("Close");

        btnClose.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        Platform.exit();
                        System.exit(0);

                    }
                }
        );

        hboxButtons.getChildren().addAll(btnDelete, btnCopy, btnClose);
        return hboxButtons;
    }

    /*
    This method lays out the bottom hbox, which is a status bar.
     */
    private static VBox makeVboxStatus() {

        VBox vboxStatus = new VBox(10);
        Label statusLabel = new Label("Select the root directory and click Start.");
        statusLabel.setId("statusLabel");
        vboxStatus.getChildren().add(statusLabel);
        return vboxStatus;
    }

}
