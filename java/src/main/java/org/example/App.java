package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // 网站代码的根目录
        String rootFolder = "/Users/wukong/xxx/docs/";
        List<String> excludeFolders = Arrays.asList(".git", "material", "images", "docsify", "script");

        String sideBar = String.valueOf(TOCGenerator.generateSideBar(rootFolder, new File(rootFolder), 0, excludeFolders));
        System.out.println("完成_sidebar.md");
        String readme = String.valueOf(TOCGenerator.generateReadMe(rootFolder, new File(rootFolder), 0, excludeFolders));
        System.out.println("完成README.md");

        try {
            Files.write(Paths.get(rootFolder, "_sidebar.md"), sideBar.getBytes());
            Files.write(Paths.get(rootFolder, "README.md"), readme.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 获取 sideBar 最后一行非空内容
        String sideBarLastLine = sideBar.split("\n")[sideBar.split("\n").length - 1];
        GitUtils.commitAndPushChanges(rootFolder, sideBarLastLine);

        String host = "服务器ip地址 xx";
        int port = 22;
        String username = "ubuntu";
        // 服务器密钥路径
        String keyPath = "~/xxx";
        // 服务器上的网站部署路径
        String remoteDirectory = "~/jay/geek/docs";
        SSHUtils.sshAndPull(host, port, username, keyPath, remoteDirectory);


        System.out.println( "上传完成" );
    }
}
