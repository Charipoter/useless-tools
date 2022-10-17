package com.useless.core.manager;

import com.useless.core.constant.ConfigConsts;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;

public class StageManager {

    private static Stage primaryStage;

    private static final Map<String, Scene> scenes = new HashMap<>();

    private static final String[] FXMLS = ConfigConsts.FXMLS;

    static  {
        try {
            for (String fxml: FXMLS) {
                Parent root = FXMLLoader.load(Objects.requireNonNull(
                        StageManager.class.getResource("/ui/" + fxml + ".fxml")
                ));
                scenes.put(fxml, new Scene(root));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initAndRun(Stage primaryStage) {
        setPrimaryStage(primaryStage);
        initPrimaryStage();
    }

    private static void setPrimaryStage(Stage primaryStage) {
        StageManager.primaryStage = primaryStage;
    }

    private static void initPrimaryStage() {
        changeScene("catalogue");
        primaryStage.show();
    }

    public static void changeScene(String name) {
        if (scenes.containsKey(name)) {
            primaryStage.setScene(scenes.get(name));
        }
    }

    private StageManager() {}

}
