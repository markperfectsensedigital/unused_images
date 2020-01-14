package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
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

    public DeleteCandidate(File file) {
        try {
            this.deleteFlag = new SimpleBooleanProperty(Boolean.FALSE);
            this.fileName = new SimpleStringProperty(file.getCanonicalPath());
            this.fileSize = new SimpleLongProperty(file.length());
        } catch (IOException e) {
            System.out.println("Could not create a DeleteCandidate object");
            System.exit(-5);
        }
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
