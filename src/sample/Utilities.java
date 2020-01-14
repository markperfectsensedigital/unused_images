package sample;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class Utilities {

    static final String[] TOP_LEVEL_DIRECTORIES = {"/cms", "/dari", "/releases", "/styleguide", "/themes"};
    static final String[] SOURCE_FILE_EXTENSIONS = {"txt", "tsr"};
    static final String[] GRAPHIC_FILE_EXTENSION = {"svg", "png", "jpg", "jpeg"};

    static final Pattern imagePattern = Pattern.compile("\\.\\.\\s+(image|figure):: ([\\w\\-./]+)");

    static final boolean isRootDirectory(String directory) {

        /* Assume this is a documentation directory if the following is true:
        a) Path ends in doc
        b) Path is a directory
        c) Path contains all the directories /cms, /dari,/releases,/styleguide
         */

        File candidateDirectory = new File(directory);
        if (candidateDirectory.isDirectory()) { /* Test if passed directory is indeed a directory. */
            Path directoryPath = candidateDirectory.toPath();
            if (directoryPath.endsWith("docs")) { /* Test if passed directory's path ends with docs. */
                String[] requiredDirectories = candidateDirectory.list(new MyFileNameFilter());
                return requiredDirectories.length == TOP_LEVEL_DIRECTORIES.length;
            }
        }

        return false;
    }

    static final Set<File> sourceFiles(String projectRoot) {

        Set<File> files = new HashSet<>();

        for (String directory : TOP_LEVEL_DIRECTORIES) {
            try {
                Collection<File> myFiles = FileUtils.listFiles(new File(projectRoot + directory),
                        SOURCE_FILE_EXTENSIONS,
                        Boolean.TRUE);
                files.addAll(myFiles);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                System.exit(-1);
            }
        }
        return files;
    }

    static final void updateStatusLabel(Stage primaryStage, String newText) {
        Label statusLabel = (Label) primaryStage.getScene().lookup("#statusLabel");
        statusLabel.setText(newText);
    }


}
