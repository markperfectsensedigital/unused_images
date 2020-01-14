package sample;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

public class DeleteCandidate {

    private BooleanProperty deleteFlag;
    private SimpleStringProperty fileName;
    private SimpleStringProperty fileSize;

    public DeleteCandidate(File file) {
        try {
            this.deleteFlag = new SimpleBooleanProperty(Boolean.FALSE);
            this.fileName = new SimpleStringProperty(file.getCanonicalPath());
            this.fileSize = new SimpleStringProperty(Utilities.formattedNumber(file.length()));
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

    public String getFileSize() {
        return fileSize.get();
    }

    public Long getLongFileSize() {
        NumberFormat format = NumberFormat.getInstance(Locale.US);
        Number number = null;
        try {
            number = format.parse(fileSize.get());
        } catch (ParseException e) {
            number = 0;
            System.out.println(e.getMessage());
        }
        return number.longValue() ;
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag.set(deleteFlag);
    }




}
