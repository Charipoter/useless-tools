package com.useless.core.system;

import com.useless.core.manager.SingletonManager;
import com.useless.core.manager.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class CalculatorController {

    private final CalculatorService calculatorService = SingletonManager.getInstance(CalculatorService.class);

    @FXML
    private TextField show;

    @FXML
    void back(MouseEvent event) {
        calculatorService.backspace();
        showExpression();
    }

    @FXML
    void clear(MouseEvent event) {
        calculatorService.removeAll();
        showExpression();
    }

    @FXML
    void clearCurNum(MouseEvent event) {
        calculatorService.removeLastNumber();
        showExpression();
    }

    @FXML
    void equation(MouseEvent event) {
        if (calculatorService.appendExpression('=')) {
            show.setText(calculatorService.executeExpression());
        }
    }

    @FXML
    void addition(MouseEvent event) {
        calculatorService.appendExpression('+');
        showExpression();
    }

    @FXML
    void subtraction(MouseEvent event) {
        calculatorService.appendExpression('-');
        showExpression();
    }

    @FXML
    void multiplication(MouseEvent event) {
        calculatorService.appendExpression('*');
        showExpression();
    }

    @FXML
    void division(MouseEvent event) {
        calculatorService.appendExpression('/');
        showExpression();
    }

    @FXML
    void point(MouseEvent event) {
        calculatorService.appendExpression('.');
        showExpression();
    }

    @FXML
    void zero(MouseEvent event) {
        calculatorService.appendExpression('0');
        showExpression();
    }

    @FXML
    void one(MouseEvent event) {
        calculatorService.appendExpression('1');
        showExpression();
    }

    @FXML
    void two(MouseEvent event) {
        calculatorService.appendExpression('2');
        showExpression();
    }

    @FXML
    void three(MouseEvent event) {
        calculatorService.appendExpression('3');
        showExpression();
    }

    @FXML
    void four(MouseEvent event) {
        calculatorService.appendExpression('4');
        showExpression();
    }

    @FXML
    void five(MouseEvent event) {
        calculatorService.appendExpression('5');
        showExpression();
    }

    @FXML
    void six(MouseEvent event) {
        calculatorService.appendExpression('6');
        showExpression();
    }

    @FXML
    void seven(MouseEvent event) {
        calculatorService.appendExpression('7');
        showExpression();
    }

    @FXML
    void eight(MouseEvent event) {
        calculatorService.appendExpression('8');
        showExpression();
    }

    @FXML
    void nine(MouseEvent event) {
        calculatorService.appendExpression('9');
        showExpression();
    }

    void showExpression() {
        show.setText(calculatorService.toStringExpression());
    }

    @FXML
    void goBack(MouseEvent event) {
        calculatorService.removeAll();
        show.clear();
        StageManager.changeScene("catalogue");
    }

}
