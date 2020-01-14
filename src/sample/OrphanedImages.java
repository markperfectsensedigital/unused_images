package sample;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class OrphanedImages {

    public static Set<File> getAllImageFiles(String projectRootDirectory) {
        Set<File> hugeImageSet = new HashSet<>();
        for (String localDirectory : Utilities.TOP_LEVEL_DIRECTORIES) {

            hugeImageSet.addAll(FileUtils.listFiles(new File(projectRootDirectory + localDirectory), Utilities.GRAPHIC_FILE_EXTENSION,Boolean.TRUE ));
        }
        return hugeImageSet;
    }


}

