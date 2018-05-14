package sample;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrphanedImages {



    public static List<String> getAllImageFiles(String projectRootDirectory) {
        List<String> hugeImageList = new ArrayList<>();
        for (String localDirectory : Utilities.TOP_LEVEL_DIRECTORIES) {
            hugeImageList.addAll(getLocalImageFiles(projectRootDirectory + localDirectory));
        }
        return hugeImageList;
    }
	private static List<String> getLocalImageFiles(String projectRootDirectory) {

        List<String> resultList = new ArrayList<>();
        File directory = new File(projectRootDirectory );
        File[] fList = directory.listFiles();
        try {
            for (File file : fList) {
                if (isGraphic(file)) {
                   // System.out.println(file.getCanonicalPath());
                       resultList.add(file.getCanonicalPath());
                } else if (file.isDirectory()) {
                    resultList.addAll(getLocalImageFiles(file.getCanonicalPath()));
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return resultList;
    }


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
        File directory = new File(projectRootDirectory );
        File[] fList = directory.listFiles();
        try {
            for (File file : fList) {
                if (isRst(file)) {
           //         System.out.println(file.getCanonicalPath());

                    localString = localString.concat (new String(Files.readAllBytes(Paths.get(file.getCanonicalPath()))));
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

