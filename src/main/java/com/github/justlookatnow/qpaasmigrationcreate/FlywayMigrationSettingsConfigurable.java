package com.github.justlookatnow.qpaasmigrationcreate;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class FlywayMigrationSettingsConfigurable implements Configurable {
    private JTextField fileNameFormatField;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Flyway Migration Settings";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        JPanel panel = new JPanel();
        fileNameFormatField = new JTextField(FlywayMigrationSettings.getInstance().fileNameFormat, 30);
        panel.add(new JLabel("文件名格式:"));
        panel.add(fileNameFormatField);
        return panel;
    }

    @Override
    public boolean isModified() {
        return !fileNameFormatField.getText().equals(FlywayMigrationSettings.getInstance().fileNameFormat);
    }

    @Override
    public void apply() throws ConfigurationException {
        String newFormat = fileNameFormatField.getText();
        if (!newFormat.contains("{fileName}")) {
            throw new ConfigurationException("文件名格式必须包含 {fileName} 占位符");
        }
        FlywayMigrationSettings.getInstance().fileNameFormat = newFormat;
    }

    @Override
    public void reset() {
        fileNameFormatField.setText(FlywayMigrationSettings.getInstance().fileNameFormat);
    }
}