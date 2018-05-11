package sample;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SelectionStatus {

    private int numberFilesFound;
    private int numberFilesSelected;
    private long sizeFileFound;
    private long sizeFilesSelected;

    public SelectionStatus() {
        this.numberFilesFound = numberFilesFound;
        this.numberFilesSelected = 0;
        this.sizeFileFound = sizeFileFound;
        this.sizeFilesSelected = 0;
    }

    public int getNumberFilesFound() {
        return numberFilesFound;
    }

    public void setNumberFilesFound(int numberFilesFound) {
        this.numberFilesFound = numberFilesFound;
    }

    public int getNumberFilesSelected() {
        return numberFilesSelected;
    }

    public void setNumberFilesSelected(int numberFilesSelected) {
        this.numberFilesSelected = numberFilesSelected;
    }

    public long getSizeFileFound() {
        return sizeFileFound;
    }

    public void setSizeFileFound(long sizeFileFound) {
        this.sizeFileFound = sizeFileFound;
    }

    public long getSizeFilesSelected() {
        return sizeFilesSelected;
    }

    public void setSizeFilesSelected(long sizeFilesSelected) {
        this.sizeFilesSelected = sizeFilesSelected;
    }

    static void updateStatusLabel(Stage primaryStage, SelectionStatus selectionStatus) {
        Label statusLabel = (Label) primaryStage.getScene().lookup("#statusLabel");

        statusLabel.setText(String.format("Selected 0 of %d files, 0 of %d bytes",selectionStatus.getNumberFilesFound(), selectionStatus.getSizeFileFound() ));
    }
}
