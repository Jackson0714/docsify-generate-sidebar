package org.example;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GitUtils {
    public static void commitAndPushChanges(String rootFolder, String commitMessage) {
        try {
            // Change directory
            File directory = new File(rootFolder);
            if (!directory.exists() || !directory.isDirectory()) {
                System.out.println("Invalid directory path");
                return;
            }
            System.setProperty("user.dir", directory.getAbsolutePath());

            // Print current time
            printCurrentTime();

            // Commit and push changes
            System.out.println("--- Committing and pushing changes to git repository...");
            runCommand("git add .", directory);
            runCommand(String.format("git commit -m \"%s\"", commitMessage), directory);
            runCommand("git push origin main", directory);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void runCommand(String command, File directory) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("sh", "-c", command);
        processBuilder.directory(directory);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            System.out.println("Error executing command: " + command);
        }
    }

    private static void printCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        System.out.println(formatter.format(date));
    }

    public static void main(String[] args) {
        String rootFolder = "your_directory_path"; // 替换为你的目录路径
        String commitMessage = "Your commit message"; // 替换为你的提交信息
        commitAndPushChanges(rootFolder, commitMessage);
    }
}
