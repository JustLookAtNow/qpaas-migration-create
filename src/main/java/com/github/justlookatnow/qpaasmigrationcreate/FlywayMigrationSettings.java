package com.github.justlookatnow.qpaasmigrationcreate;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
    name = "FlywayMigrationSettings",
    storages = {@Storage("FlywayMigrationSettings.xml")}
)
@Service
public class FlywayMigrationSettings implements PersistentStateComponent<FlywayMigrationSettings> {
    public String fileNameFormat = "V{version}_{yyyyMMddHHmmss}__feature_qpaas_{fileName}";

    public static FlywayMigrationSettings getInstance() {
        return ApplicationManager.getApplication().getService(FlywayMigrationSettings.class);
    }

    @Nullable
    @Override
    public FlywayMigrationSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull FlywayMigrationSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public void setFileNameFormat(String format) {
        this.fileNameFormat = format;
    }
}