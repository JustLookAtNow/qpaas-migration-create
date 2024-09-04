package com.github.justlookatnow.qpaasmigrationcreate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateFlywayMigrationAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;

        String version = Messages.showInputDialog(project, "请输入版本号:", "创建Flyway迁移文件", null);
        if (version == null || version.trim().isEmpty()) return;

        String fileName = Messages.showInputDialog(project, "请输入文件名:", "创建Flyway迁移文件", null);
        if (fileName == null || fileName.trim().isEmpty()) return;

        FlywayMigrationSettings settings = FlywayMigrationSettings.getInstance();
        String format = settings.fileNameFormat;

        Date now = new Date();
        String fullFileName = format;

        // 使用正则表达式匹配所有 {yyyy...} 格式的日期占位符
        Pattern pattern = Pattern.compile("\\{([yMdhHmsS.-]+)\\}");
        Matcher matcher = pattern.matcher(format);
        
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String dateFormat = matcher.group(1);
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            matcher.appendReplacement(sb, sdf.format(now));
        }
        matcher.appendTail(sb);
        
        fullFileName = sb.toString()
            .replace("{fileName}", fileName)
            .replace("{version}", version)
            + ".sql";

        VirtualFile selectedDir = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE);
        if (selectedDir == null || !selectedDir.isDirectory()) return;

        PsiDirectory directory = PsiManager.getInstance(project).findDirectory(selectedDir);
        if (directory == null) return;

        PsiFileFactory factory = PsiFileFactory.getInstance(project);
        PsiFile file = factory.createFileFromText(fullFileName, PlainTextFileType.INSTANCE , "-- Flyway migration script");
        WriteAction.run(() -> {
            // write data
            directory.add(file);
        });
        Messages.showInfoMessage(project, "已创建Flyway迁移文件: " + fullFileName, "文件创建成功");
    }
}