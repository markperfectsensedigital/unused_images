package sample;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class DeleteCandidate {

    private SimpleBooleanProperty deleteFlag;
    private SimpleStringProperty fileName;
    private SimpleIntegerProperty fileSize;

    public DeleteCandidate(Boolean deleteFlag, String fileName, Integer fileSize) {
        this.deleteFlag = new SimpleBooleanProperty(deleteFlag);
        this.fileName = new SimpleStringProperty(fileName);
        this.fileSize = new SimpleIntegerProperty(fileSize);
    }

    public Boolean getDeleteFlag() {
        return deleteFlag.get();
    }

    public String getFileName() {
        return fileName.get();
    }

    public Integer getFileSize() {
        return fileSize.get();
    }
}
