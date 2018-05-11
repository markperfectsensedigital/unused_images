package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.util.Map;

public class DeleteCandidate {

    private BooleanProperty deleteFlag;
    private SimpleStringProperty fileName;
    private SimpleLongProperty fileSize;

    public DeleteCandidate(Boolean deleteFlag, String fileName, Long fileSize) {
        this.deleteFlag = new SimpleBooleanProperty(deleteFlag);
        this.fileName = new SimpleStringProperty(fileName);
        this.fileSize = new SimpleLongProperty(fileSize);
    }

    public Boolean isDeleteFlag() {
        return deleteFlag.get();
    }

    public String getFileName() {
        return fileName.get();
    }

    public Long getFileSize() {
        return fileSize.get();
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag.set(deleteFlag);
    }

    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    public void setFileSize(long fileSize) {
        this.fileSize.set(fileSize);
    }


}
