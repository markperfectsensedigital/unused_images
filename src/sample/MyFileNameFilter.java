package sample;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MyFileNameFilter implements FilenameFilter {

    public MyFileNameFilter() {

    }

    @Override
    public boolean accept(File dir, String name) {
        List<String> topLevelDirectoriesNoSlashes = Arrays
                .asList(Utilities.TOP_LEVEL_DIRECTORIES)
                .stream()
                .map(e -> e.substring(1))
                .collect(Collectors.toList());


    //   This works:  return Arrays.asList("cms", "dari","releases","styleguide").contains(name);
        return topLevelDirectoriesNoSlashes.contains(name);
    }
}
