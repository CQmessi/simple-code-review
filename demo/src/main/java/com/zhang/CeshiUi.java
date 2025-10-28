package com.zhang;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class CeshiUi {

    // 新增的标签和文本框
    private JTextField githubReviewLogUriField;
    private JTextField githubTokenField;
    private JTextField commitProjectField;
    private JTextField commitBranchField;
    private JTextField commitAuthorField;
    private JTextField commitMessageField;
    private JTextField gitUrlField;
    private JTextField gitUserField;
    private JTextField gitPasswordField;
    private JTextField gitFileAddressField;
    private JTextField weixinAppidField;
    private JTextField weixinSecretField;
    private JTextField weixinTouserField;
    private JTextField weixinTemplateIdField;
    private JTextField modelKeyField;
    private JTextField modelUrlField;
    private JTextField modelField;

    // 在 CeshiUi 类中添加以下字段
    private JButton saveButton;
    private static final String CONFIG_FILE = "plugin_config.properties";

    // 修改构造函数，添加保存按钮
    public CeshiUi() {
        // 创建主面板
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // 添加标签和文本框（现有代码不变）
        addComponent(panel, gbc, new JLabel("GITHUB_REVIEW_LOG_URI:"), 0, 0);
        githubReviewLogUriField = new JTextField(20);
        addComponent(panel, gbc, githubReviewLogUriField, 1, 0);

        addComponent(panel, gbc, new JLabel("GITHUB_TOKEN:"), 0, 1);
        githubTokenField = new JTextField(20);
        addComponent(panel, gbc, githubTokenField, 1, 1);

        addComponent(panel, gbc, new JLabel("COMMIT_PROJECT:"), 0, 2);
        commitProjectField = new JTextField(20);
        addComponent(panel, gbc, commitProjectField, 1, 2);

        addComponent(panel, gbc, new JLabel("COMMIT_BRANCH:"), 0, 3);
        commitBranchField = new JTextField(20);
        addComponent(panel, gbc, commitBranchField, 1, 3);

        addComponent(panel, gbc, new JLabel("COMMIT_AUTHOR:"), 0, 4);
        commitAuthorField = new JTextField(20);
        addComponent(panel, gbc, commitAuthorField, 1, 4);

        addComponent(panel, gbc, new JLabel("COMMIT_MESSAGE:"), 0, 5);
        commitMessageField = new JTextField(20);
        addComponent(panel, gbc, commitMessageField, 1, 5);

        addComponent(panel, gbc, new JLabel("GIT_URL:"), 0, 6);
        gitUrlField = new JTextField(20);
        addComponent(panel, gbc, gitUrlField, 1, 6);

        addComponent(panel, gbc, new JLabel("GIT_USER:"), 0, 7);
        gitUserField = new JTextField(20);
        addComponent(panel, gbc, gitUserField, 1, 7);

        addComponent(panel, gbc, new JLabel("GIT_PASSWORD:"), 0, 8);
        gitPasswordField = new JTextField(20);
        addComponent(panel, gbc, gitPasswordField, 1, 8);

        addComponent(panel, gbc, new JLabel("GIT_FILE_ADDRESS:"), 0, 9);
        gitFileAddressField = new JTextField(20);
        addComponent(panel, gbc, gitFileAddressField, 1, 9);

        addComponent(panel, gbc, new JLabel("WEIXIN_APPID:"), 0, 10);
        weixinAppidField = new JTextField(20);
        addComponent(panel, gbc, weixinAppidField, 1, 10);

        addComponent(panel, gbc, new JLabel("WEIXIN_SECRET:"), 0, 11);
        weixinSecretField = new JTextField(20);
        addComponent(panel, gbc, weixinSecretField, 1, 11);

        addComponent(panel, gbc, new JLabel("WEIXIN_TOUSER:"), 0, 12);
        weixinTouserField = new JTextField(20);
        addComponent(panel, gbc, weixinTouserField, 1, 12);

        addComponent(panel, gbc, new JLabel("WEIXIN_TEMPLATE_ID:"), 0, 13);
        weixinTemplateIdField = new JTextField(20);
        addComponent(panel, gbc, weixinTemplateIdField, 1, 13);

        addComponent(panel, gbc, new JLabel("MODEL_KEY:"), 0, 14);
        modelKeyField = new JTextField(20);
        addComponent(panel, gbc, modelKeyField, 1, 14);

        addComponent(panel, gbc, new JLabel("MODEL_URL:"), 0, 15);
        modelUrlField = new JTextField(20);
        addComponent(panel, gbc, modelUrlField, 1, 15);

        addComponent(panel, gbc, new JLabel("MODEL:"), 0, 16);
        modelField = new JTextField(20);
        addComponent(panel, gbc, modelField, 1, 16);

        // 添加保存按钮
        saveButton = new JButton("保存配置");
        addComponent(panel, gbc, saveButton, 1, 17);

        // 添加事件监听器
        setupEventListeners();

        // 加载已保存的配置
        loadSavedConfig();

        // 创建主框架
        JFrame frame = new JFrame("配置界面");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // 添加事件监听器方法
    private void setupEventListeners() {
        saveButton.addActionListener(e -> saveConfig());
    }

    // 保存配置到文件
    private void saveConfig() {
        try {
            Properties props = new Properties();

            // 保存所有配置项
            props.setProperty("GITHUB_REVIEW_LOG_URI", githubReviewLogUriField.getText());
            props.setProperty("GITHUB_TOKEN", githubTokenField.getText());
            props.setProperty("COMMIT_PROJECT", commitProjectField.getText());
            props.setProperty("COMMIT_BRANCH", commitBranchField.getText());
            props.setProperty("COMMIT_AUTHOR", commitAuthorField.getText());
            props.setProperty("COMMIT_MESSAGE", commitMessageField.getText());
            props.setProperty("GIT_URL", gitUrlField.getText());
            props.setProperty("GIT_USER", gitUserField.getText());
            props.setProperty("GIT_PASSWORD", gitPasswordField.getText());
            props.setProperty("GIT_FILE_ADDRESS", gitFileAddressField.getText());
            props.setProperty("WEIXIN_APPID", weixinAppidField.getText());
            props.setProperty("WEIXIN_SECRET", weixinSecretField.getText());
            props.setProperty("WEIXIN_TOUSER", weixinTouserField.getText());
            props.setProperty("WEIXIN_TEMPLATE_ID", weixinTemplateIdField.getText());
            props.setProperty("MODEL_KEY", modelKeyField.getText());
            props.setProperty("MODEL_URL", modelUrlField.getText());
            props.setProperty("MODEL", modelField.getText());

            try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
                props.store(fos, "Plugin Configuration");
            }

            JOptionPane.showMessageDialog(null, "配置保存成功！");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "配置保存失败: " + ex.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 加载已保存的配置
    private void loadSavedConfig() {
        try {
            File configFile = new File(CONFIG_FILE);
            if (configFile.exists()) {
                Properties props = new Properties();
                try (FileInputStream fis = new FileInputStream(configFile)) {
                    props.load(fis);

                    // 加载所有配置项
                    githubReviewLogUriField.setText(props.getProperty("GITHUB_REVIEW_LOG_URI", ""));
                    githubTokenField.setText(props.getProperty("GITHUB_TOKEN", ""));
                    commitProjectField.setText(props.getProperty("COMMIT_PROJECT", ""));
                    commitBranchField.setText(props.getProperty("COMMIT_BRANCH", ""));
                    commitAuthorField.setText(props.getProperty("COMMIT_AUTHOR", ""));
                    commitMessageField.setText(props.getProperty("COMMIT_MESSAGE", ""));
                    gitUrlField.setText(props.getProperty("GIT_URL", ""));
                    gitUserField.setText(props.getProperty("GIT_USER", ""));
                    gitPasswordField.setText(props.getProperty("GIT_PASSWORD", ""));
                    gitFileAddressField.setText(props.getProperty("GIT_FILE_ADDRESS", ""));
                    weixinAppidField.setText(props.getProperty("WEIXIN_APPID", ""));
                    weixinSecretField.setText(props.getProperty("WEIXIN_SECRET", ""));
                    weixinTouserField.setText(props.getProperty("WEIXIN_TOUSER", ""));
                    weixinTemplateIdField.setText(props.getProperty("WEIXIN_TEMPLATE_ID", ""));
                    modelKeyField.setText(props.getProperty("MODEL_KEY", ""));
                    modelUrlField.setText(props.getProperty("MODEL_URL", ""));
                    modelField.setText(props.getProperty("MODEL", ""));
                }
            }
        } catch (Exception ex) {
            // 静默处理加载错误
        }
    }

    private void addComponent(JPanel panel, GridBagConstraints gbc, Component component, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(component, gbc);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CeshiUi::new);
    }

    // 在 com.zhang.CeshiUi 类中添加以下静态方法
    public static Properties getSavedConfig() {
        Properties props = new Properties();
        try {
            File configFile = new File(CONFIG_FILE);
            if (configFile.exists()) {
                try (FileInputStream fis = new FileInputStream(configFile)) {
                    props.load(fis);
                }
            }
        } catch (Exception ex) {
            // 返回空的Properties对象，避免null指针异常
        }
        return props;
    }

    public static String getConfigValue(String key) {
        return getSavedConfig().getProperty(key, "");
    }
}
