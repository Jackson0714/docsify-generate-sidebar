package org.example;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Utils {

    public static List<File> getSortedItems(File directory, List<String> excludeFolders) {
        List<File> items = new ArrayList<>();

        File[] files = directory.listFiles();
        if (files == null) return items;

        Arrays.stream(files)
                .filter(file -> !excludeFolders.contains(file.getName()))
                .forEach(items::add);

        items.sort((a, b) -> {
            if (a.isDirectory() && b.isDirectory()) {
                return a.getName().compareTo(b.getName());
            } else if (a.isFile() && b.isFile()) {
                return Long.compare(a.lastModified(), b.lastModified());
            } else {
                return a.isDirectory() ? -1 : 1;
            }
        });

        return items;
    }
}
