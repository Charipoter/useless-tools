package com.useless.core;

import com.useless.core.manager.StageManager;
import com.useless.core.manager.CloseManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launch extends Application {
    @Override
    public void start(Stage primaryStage) {
        CloseManager.setClose(primaryStage);
        StageManager.initAndRun(primaryStage);
    }
}
