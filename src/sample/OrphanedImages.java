package sample;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;

public class OrphanedImages {

    public static Set<File> getAllImageFiles(String projectRootDirectory) {
        Set<File> hugeImageSet = new HashSet<>();
        for (String localDirectory : Utilities.TOP_LEVEL_DIRECTORIES) {

            hugeImageSet.addAll(FileUtils.listFiles(new File(projectRootDirectory + localDirectory), Utilities.GRAPHIC_FILE_EXTENSION,Boolean.TRUE ));
        }
        return hugeImageSet;
    }

    /*private static Set<File> getLocalImageFiles(String projectRootDirectory) {

        Set<File> resultList = new HashSet<>();
        File directory = new File(projectRootDirectory);
        File[] fList = FileUtils.listFiles();
        try {
            for (File file : fList) {
                if (isGraphic(file)) {
                    // System.out.println(file.getCanonicalPath());
                    resultList.add(new File(file.getCanonicalPath()));
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return resultList;
    }*/

    /*
    A graphic file is one that ends with svg, png, jpg, or jpeg
     */
    private static boolean isGraphic(File file) {

        if (!file.isFile()) {
            return false;
        }
        Pattern p = Pattern.compile("\\.svg$|\\.png$|\\.jpg$|\\.jpeg$");
        Matcher m = p.matcher(file.getName());
        return m.find();
    }

    protected static String loadAllTextIntoSingleString(String projectRootDirectory) {
        String localString = new String();

        for (String localDirectory : Utilities.TOP_LEVEL_DIRECTORIES) {
            localString = localString.concat(getLocalTextFiles(projectRootDirectory + localDirectory));
        }
        //   System.out.println(localString);
        return localString;
    }

    private static String getLocalTextFiles(String projectRootDirectory) {

        String localString = new String();
        File directory = new File(projectRootDirectory);
        File[] fList = directory.listFiles();
        try {
            for (File file : fList) {
                if (isRst(file)) {
                    //         System.out.println(file.getCanonicalPath());

                    localString = localString.concat(new String(Files.readAllBytes(Paths.get(file.getCanonicalPath()))));
                } else if (file.isDirectory()) {
                    localString = localString.concat(getLocalTextFiles(file.getCanonicalPath()));
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return localString;
    }

    /*
A text file is one that ends with txt or tsr
 */
    private static boolean isRst(File file) {
        if (!file.isFile()) {
            return false;
        }
        Pattern p = Pattern.compile("\\.txt$|\\.tsr$");
        Matcher m = p.matcher(file.getName());
        return m.find();
    }

}

