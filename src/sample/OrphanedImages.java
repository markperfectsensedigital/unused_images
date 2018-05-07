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

 //   https://stackoverflow.com/questions/14676407/list-all-files-in-the-folder-and-also-sub-folders

    private static final String[] TOP_LEVEL_DIRECTORIES = {"/cms", "/dari","/releases","/styleguide"};

    public static List<String> getAllImageFiles(String projectRootDirectory) {
        List<String> hugeImageList = new ArrayList<>();
        for (String localDirectory : TOP_LEVEL_DIRECTORIES) {
            hugeImageList.addAll(getLocalImageFiles(projectRootDirectory + localDirectory));
        }
        return hugeImageList;
    }
	private static List<String> getLocalImageFiles(String projectRootDirectory) {

        List<String> resultList = new ArrayList<>();


        File directory = new File(projectRootDirectory );

        // get all the files from a directory
        File[] fList = directory.listFiles();
        try {
            for (File file : fList) {
                if (isGraphic(file)) {
                    System.out.println(file.getCanonicalPath());
                    //   resultList.add()
                } else if (file.isDirectory()) {
                    resultList.addAll(getLocalImageFiles(file.getCanonicalPath()));
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return resultList;
    }



    private static boolean isGraphic(File file) {

        if (!file.isFile()) {
            return false;
        }
        Pattern p = Pattern.compile("\\.svg$|\\.png$|\\.jpg$|\\.jpeg$");

        Matcher m = p.matcher(file.getName());
        return m.find();
    }

    public static List<DeleteCandidate> listf(String projectRootDirectory) {
        File directory = new File(projectRootDirectory);

        List<DeleteCandidate> resultList = new ArrayList<>();

        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                System.out.println(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                resultList.addAll(listf(file.getAbsolutePath()));
            }
        }
        //System.out.println(fList);
        return resultList;
    }


    private String getFileContents(String path) {
        String contents = null;
        try {
            contents = new String(Files.readAllBytes(Paths.get(path)));
            System.out.println(contents);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return contents;
    }
}

