package com.useless.core.system;

import com.useless.core.manager.StageManager;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class CatalogueController {

    @FXML
    void calculator(MouseEvent event) {
        StageManager.changeScene("calculator");
    }

    @FXML
    void guessNumber(MouseEvent event) {
        StageManager.changeScene("guessNumber");
    }

    @FXML
    void perimeterAndArea(MouseEvent event) {
        StageManager.changeScene("perimeterAndArea");
    }

    @FXML
    void goBack(MouseEvent event) {
//        StageManager.changeScene("login");
    }

}

