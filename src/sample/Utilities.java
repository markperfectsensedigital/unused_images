package sample;

import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;

public class Utilities {

    static final String[] TOP_LEVEL_DIRECTORIES = {"/cms", "/dari","/releases","/styleguide"};

    static final boolean isRootDirectory (String directory ) {

        /* Assume this is a documentation directory if the following is true:
        a) Path ends in doc
        b) Path is a directory
        c) Path contains all the directories /cms, /dari,/releases,/styleguide
         */

        File candidateDirectory = new File (directory);
        if (candidateDirectory.isDirectory()) { /* Test if passed directory is indeed a directory. */
            Path directoryPath = candidateDirectory.toPath();
            if (directoryPath.endsWith("docs")) { /* Test if passed directory's path ends with docs. */
                String[] requiredDirectories = candidateDirectory.list(new MyFileNameFilter()) ;
                return requiredDirectories.length == TOP_LEVEL_DIRECTORIES.length;
                }
            }

        return false;
    }


static final void updateStatusLabel(Stage primaryStage, String newText) {
    Label statusLabel = (Label) primaryStage.getScene().lookup("#statusLabel");
    statusLabel.setText(newText);
}

}
