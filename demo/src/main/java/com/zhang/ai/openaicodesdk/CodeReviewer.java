package com.zhang.ai.openaicodesdk;// CodeReviewer.java

import com.zhang.ai.openaicodesdk.git.GitDemand;
import com.zhang.ai.openaicodesdk.service.ReviewService;

public class CodeReviewer {

    public static void main(String[] args) {
        try {
            ReviewService reviewService = new ReviewService();
            System.out.println("开始执行代码审查...");
            reviewService.checkCode();
            System.out.println("代码审查已完成");
        } catch (Exception e) {
            System.err.println("代码审查过程中发生错误: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 添加资源清理和线程管理
            // 这个地方如果没有这个的话会因为jgit一直没有释放资源提交代码会一直卡死，等这个地方线程断开之后才会提交
            cleanupResources();
        }
    }

    private static void cleanupResources() {
        // 清理系统资源，确保所有后台线程正确关闭
        try {
            // 清理JGit全局资源
            GitDemand.shutdownJGit();
            
            // 强制垃圾回收，帮助清理可能的线程引用
            System.gc();

            // 给线程一些时间来响应中断
            Thread.sleep(500);

        } catch (InterruptedException ie) {
            // 恢复中断状态
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("资源清理过程中发生错误: " + e.getMessage());
        }
    }

}