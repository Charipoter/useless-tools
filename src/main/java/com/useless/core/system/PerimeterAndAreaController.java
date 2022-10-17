package com.useless.core.system;

import cn.hutool.core.text.CharSequenceUtil;
import com.useless.core.manager.SingletonManager;
import com.useless.core.manager.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class PerimeterAndAreaController {

    private final PerimeterAndAreaService perimeterAndAreaService
            = SingletonManager.getInstance(PerimeterAndAreaService.class);

    @FXML
    private TextArea circleMessage;

    @FXML
    private TextField lengthField;

    @FXML
    private TextField radiusField;

    @FXML
    private TextArea squareMessage;

    @FXML
    void calCircle(MouseEvent event) {
        if (CharSequenceUtil.isNotEmpty(radiusField.getText())) {
            double radius = Double.parseDouble(radiusField.getText());
            circleMessage.appendText(String.format("半径为%f的圆的周长为%f,面积为%f%n", radius,
                    perimeterAndAreaService.getCirclePerimeter(radius),
                    perimeterAndAreaService.getCircleArea(radius)));
        }
    }

    @FXML
    void calSquare(MouseEvent event) {
        if (CharSequenceUtil.isNotEmpty(lengthField.getText())) {
            double length = Double.parseDouble(lengthField.getText());
            squareMessage.appendText(String.format("边长为%f的正方形的周长为%f,面积为%f%n", length,
                    perimeterAndAreaService.getSquarePerimeter(length),
                    perimeterAndAreaService.getSquareArea(length)));
        }
    }

    @FXML
    void goBack(MouseEvent event) {
        StageManager.changeScene("catalogue");
    }

}
