package com.zhang.ai.openaicodesdk.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class GitDemand {

    private final Logger logger = LoggerFactory.getLogger(GitDemand.class);

    private final String project;

    private final String branch;

    private final String author;

    private final String gitUrl;

    private final String gitUser;

    private final String gitPassword;

    private final String gitFileAddress;

    public GitDemand(String project, String branch, String author, String gitUrl, String gitUser, String gitPassword, String gitFileAddress) {
        this.project = project;
        this.branch = branch;
        this.author = author;
        this.gitUrl = gitUrl;
        this.gitUser = gitUser;
        this.gitPassword = gitPassword;
        this.gitFileAddress = gitFileAddress;
    }

    public String getDiffCode() throws Exception {
        logger.info("start to get different code");
        // 获取最后一次提交的hash值
        ProcessBuilder logProcessBuilder = new ProcessBuilder("git", "log", "-1", "--pretty=format:%H");
        // 设置为当前目录
        logProcessBuilder.directory(new File("."));
        // 开始执行git命令
        Process logProcess = logProcessBuilder.start();

        // 这里只是获取一个hash值
        BufferedReader logReader = new BufferedReader(new InputStreamReader(logProcess.getInputStream()));
        String latestCommitHash = logReader.readLine();
        logReader.close();
        logProcess.waitFor();

        // 获取最近两次提交的差异
        ProcessBuilder diffProcessBuilder = new ProcessBuilder("git", "diff", latestCommitHash + "^", latestCommitHash);
        diffProcessBuilder.directory(new File("."));
        Process diffProcess = diffProcessBuilder.start();

        StringBuilder diffCode = new StringBuilder();
        try (BufferedReader diffReader = new BufferedReader(new InputStreamReader(diffProcess.getInputStream()))) {
            String line;
            while ((line = diffReader.readLine()) != null) {
                diffCode.append(line).append("\n");
            }
        } catch (IOException e) {
            // 处理异常，但不需要手动关闭资源
            throw new RuntimeException("read diff failed", e);
        }

        int exitCode = diffProcess.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Failed to get diff, exit code:" + exitCode);
        }
        logger.info("get different code success");
        return diffCode.toString();
    }

    public String pushCode(String result) throws Exception {
        logger.info("start to push code");
        Git git = null;
        try {
            logger.info("start to push code");
            logger.info("Cloning repository from: {}", gitUrl);

            // 在克隆之前添加清理逻辑
            File repoDir = new File("repo");
            if (repoDir.exists()) {
                // 删除已存在的目录
                deleteDirectory(repoDir);
            }

            // 在现有系统属性基础上添加更多配置
            System.setProperty("jgit.http.userAgent", "git/2.0 (JGit)");
            System.setProperty("http.keepAlive", "false");
            System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
            System.setProperty("jgit.http.preferDualMode", "false");
            System.setProperty("jgit.transport.disableSmartHttp", "true");
            git = Git.cloneRepository()
                    .setURI(gitUrl)
                    .setDirectory(new File("repo"))
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitUrl, gitPassword))
                    .setCloneAllBranches(false)
                    .setTransportConfigCallback(transport -> {
                        if (transport instanceof org.eclipse.jgit.transport.TransportHttp) {
                            org.eclipse.jgit.transport.TransportHttp httpTransport =
                                    (org.eclipse.jgit.transport.TransportHttp) transport;
                            // 可以设置其他可用属性
                            // 注意：5.13.1版本没有setUseChunkedEncoding方法
                        }
                    })
                    .call();
            System.out.println("clone");

            // 把生成的评价写成一个文件
            String dateFolderName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            File dateFolder = new File("repo/" + dateFolderName);
            if (!dateFolder.exists()) {
                dateFolder.mkdirs();
            }
            System.out.println("create folder");

            String fileName = project + "-" + branch + "-" + author + UUID.randomUUID() + ".md";
            File newFile = new File(dateFolder, fileName);
            try (FileWriter writer = new FileWriter(newFile)) {
                writer.write(result);
            }

            System.out.println("write file");
            // 提交内容
            git.add().addFilepattern(dateFolderName + "/" + fileName).call();
            git.commit().setMessage("add code review new file" + fileName).call();
            git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitUser, gitPassword)).call();

            logger.info("git commit and push done! {}", fileName);

            return gitFileAddress + "/" + dateFolderName + "/" + fileName;

        } catch (TransportException e) {
            logger.error("Git transport error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Git operation failed: ", e);
            throw e;
        } finally {
            // 正确关闭 JGit 资源，防止线程泄露
            if (git != null) {
                try {
                    git.close();
                } catch (Exception e) {
                    logger.warn("Failed to close Git repository: {}", e.getMessage());
                }
            }
        }
    }

    private boolean deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            // 获取目录下的所有文件和子目录
            File[] files = directory.listFiles();
            if (files != null) {
                // 递归删除所有子文件和子目录
                for (File file : files) {
                    if (file.isDirectory()) {
                        // 递归删除子目录
                        deleteDirectory(file);
                    } else {
                        // 删除文件
                        file.delete();
                    }
                }
            }
        }
        // 删除空目录或文件
        return directory.delete();
    }

    // 添加静态方法用于关闭JGit全局资源
    public static void shutdownJGit() {
        try {
            // 清理系统属性
            System.clearProperty("jgit.http.userAgent");
            System.clearProperty("http.keepAlive");
            System.clearProperty("sun.net.http.allowRestrictedHeaders");
            System.clearProperty("jgit.http.preferDualMode");
            System.clearProperty("jgit.transport.disableSmartHttp");
            
            // 触发垃圾回收以帮助清理线程资源
            System.gc();
        } catch (Exception e) {
            // 忽略清理错误
        }
    }

    public String getProject() {
        return project;
    }

    public String getBranch() {
        return branch;
    }

    public String getAuthor() {
        return author;
    }
}