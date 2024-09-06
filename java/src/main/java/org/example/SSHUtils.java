package org.example;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SSHUtils {

    public static void sshAndPull(String host, int port, String username, String keyPath, String remoteDirectory) {
        printCurrentTime();
        System.out.println("--- 连接到 " + host + " 以拉取最新更改...");

        JSch jsch = new JSch();
        Session session = null;
        try {
            // 添加私钥
            jsch.addIdentity(keyPath);
            session = jsch.getSession(username, host, port);

            // 跳过主机密钥检查
            session.setConfig("StrictHostKeyChecking", "no");

            // 连接到远程主机
            session.connect();
            System.out.println("--- 已连接到 " + host);

            // 切换到远程目录并执行git pull
            String command = "cd " + remoteDirectory + " && pwd && git status && git pull";
            printCurrentTime();
            System.out.println("--- 执行命令: " + command);

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            channel.setOutputStream(outputStream);
            channel.setErrStream(errorStream);

            channel.connect();

            // 等待命令执行完成
            while (!channel.isClosed()) {
                Thread.sleep(100);
            }

            String stdoutOutput = outputStream.toString();
            String stderrOutput = errorStream.toString();
            System.out.println(stdoutOutput);
            System.out.println(stderrOutput);

            if (stderrOutput.contains("fatal")) {
                throw new Exception("执行命令时出错: " + command + "\n" + stderrOutput);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }

    private static void printCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        System.out.println(formatter.format(date));
    }

    public static void main(String[] args) {
        String host = "xxx"; // 替换为你的主机地址
        int port = 22; // 替换为你的端口号
        String username = "xx"; // 替换为你的用户名
        String keyPath = "xx"; // 替换为你的私钥路径
        String remoteDirectory = "xx"; // 替换为你的远程目录

        sshAndPull(host, port, username, keyPath, remoteDirectory);
    }
}

