package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.stream.Collectors;

public class TOCGenerator {

    private static StringBuilder sidebarToc = new StringBuilder();
    private static StringBuilder readmeToc = new StringBuilder();
    static List<String> excludeFiles = Arrays.asList("_coverpage","_navbar", "README", "_sidebar");
    public static StringBuilder generateSideBar(String rootFolder, File directory, int indentLevel, List<String> excludeFolders) {

        String indent = repeat("  ", indentLevel);
        List<File> items = Utils.getSortedItems(directory, excludeFolders);

        for (File item : items) {
            if (item.isDirectory()) {
                sidebarToc.append(String.format("%s* %s  \n", indent, item.getName()));
                generateSideBar(rootFolder, item, indentLevel + 1, excludeFolders);
            } else if (item.isFile() && item.getName().endsWith(".md")) {

                String fileName = item.getName().replace(".md", "");
                // 如果 excludeFiles 数组中任意一个元素包含这个文件名，则跳过。
                if(excludeFiles.stream().anyMatch(fileName::contains)) {
                    continue;
                }
                // 获取该文件的相对路径
                String filePath = item.getPath().replace(rootFolder, "").replace("\\", "/");
                sidebarToc.append(String.format("%s* [%s](%s)  \n", indent, fileName, filePath));
            }
        }
        return sidebarToc;
    }

    public static StringBuilder generateReadMe(String rootFolder, File directory, int indentLevel, List<String> excludeFolders) {
        List<File> items = Utils.getSortedItems(directory, excludeFolders);

        for (File item : items) {
            if (item.isDirectory()) {
                // 判断该目录下是否有 md 文件，如果有，则取该目录下的第一个文件，拼接链接。否则拼接目录 "#文件夹名"，根据目录的深度，#可能有多个。
                boolean isExistMdFile = Arrays.stream(Objects.requireNonNull(item.listFiles())).map(x-> x.getName().contains(".md")).findFirst().orElse(false);
                // 获取 item 的名字
                String directoryName = item.getName();
                if (isExistMdFile) {
                    // 获取该目录下的第一个文件，文件列表需要先按照创建时间排序
                    File[] files = item.listFiles(x -> x.getName().endsWith(".md"));
                    if(files != null && files.length > 0) {
                        List<File> sortedFiles = Arrays.stream(files)
                            .sorted(
                                    // true -> false
                                    Comparator.comparing((File file) -> !file.getName().contains("开篇"))
                                    .thenComparing(TOCGenerator::getCreationTime))
                            .collect(Collectors.toList());
                        // 获取第一个文件的文件名，去掉后缀名
                        String fileName = sortedFiles.get(0).getName().replace(".md", "");
                        // 获取该文件的相对路径
                        String filePath = sortedFiles.get(0).getPath().replace(rootFolder, "").replace("\\", "/");
                        // 在 filePath 的签名拼接 http 链接
                        String filePathWithHttpLink = ("http://你的网站地址/#" + filePath).replace(".md", "");
                        readmeToc.append(String.format("* [%s](%s)  \n", directoryName, filePathWithHttpLink));
                    }
                } else {
                    readmeToc.append(String.format("\n%s %s  \n", repeat("#", indentLevel + 2), item.getName()));
                }
                generateReadMe(rootFolder, item, indentLevel + 1, excludeFolders);
            }
        }
        return readmeToc;
    }
    public static String repeat(String str, int times) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < times; i++) {
            builder.append(str);
        }
        return builder.toString();
    }
    private static FileTime getCreationTime(File file) {
        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            return attr.creationTime();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

