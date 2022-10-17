package com.useless.core.discard.system;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import com.useless.core.manager.SingletonManager;
import com.useless.core.manager.StageManager;
import com.useless.core.discard.constant.StateConsts;
import com.useless.core.discard.model.User;
import com.useless.core.discard.service.WorkerPoolService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.sql.Date;
import java.time.ZoneOffset;

public class RegisterController {

    private final RegisterService registerService = SingletonManager.getInstance(RegisterService.class);
    private final WorkerPoolService workerPoolService = SingletonManager.getInstance(WorkerPoolService.class);

    @FXML
    private TextField accountField;

    @FXML
    private DatePicker birthField;

    @FXML
    private TextField chekPasswordField;

    @FXML
    private RadioButton femaleButton;

    @FXML
    private TextArea interestField;

    @FXML
    private RadioButton maleButton;

    @FXML
    private TextField passwordField;

    @FXML
    private TextArea remarkField;

    @FXML
    private Label registerMessage;

    @FXML
    void tryRegister(MouseEvent event) {
        beforeRegister();

        String account = accountField.getText();
        String password = passwordField.getText();
        String interest = interestField.getText();
        String remark = remarkField.getText();
        String checkPswd = chekPasswordField.getText();
        boolean gender = maleButton.isSelected();
        Date birth;
        // 判断必填项
        try {
            Assert.isTrue(CharSequenceUtil.isNotEmpty(account),
                    "请填写账号");
            Assert.isTrue(CharSequenceUtil.isNotEmpty(password),
                    "请填写密码");
            Assert.isTrue(CharSequenceUtil.isNotEmpty(checkPswd),
                    "请填写确认密码");
            Assert.isTrue(maleButton.isSelected() || femaleButton.isSelected(),
                    "请选择性别");
            Assert.isTrue(birthField.getValue() != null,
                    "请填写出生日期");
            Assert.isTrue(password.equals(checkPswd),
                    "确认密码错误");
        } catch (IllegalArgumentException e) {
            registerMessage.setText(e.getMessage());
            return;
        }
        birth = new Date(birthField
                .getValue()
                .atStartOfDay(ZoneOffset.ofHours(8))
                .toInstant()
                .toEpochMilli()
        );

        registerMessage.setText("注册中...");

        workerPoolService.process(() -> {
            int state = registerService.accountExist(account);
            if (state == StateConsts.ACCOUNT_NOT_EXIST) {
                // 将信息持久化到sql数据库
                registerService.register(new User(account, password, gender, birth, interest, remark));
            }
            Platform.runLater(() -> {
                if (state == StateConsts.ACCOUNT_EXIST) {
                    registerMessage.setText("账号已存在!");
                } else if (state == StateConsts.ACCOUNT_NOT_EXIST) {
                    afterRegisterSuccess();
                } else if (state == StateConsts.DB_ERROR) {
                    registerMessage.setText("数据库故障!");
                }
            });
        });
    }

    void beforeRegister() {
        // 没什么可写的
    }

    void afterRegisterSuccess() {
        clear();
        StageManager.changeScene("catalogue");
    }

    void clear() {
        registerMessage.setText("");
        birthField.setValue(null);
        maleButton.setSelected(false);
        femaleButton.setSelected(false);
        chekPasswordField.clear();
        interestField.clear();
        remarkField.clear();
        accountField.clear();
        passwordField.clear();
    }

    @FXML
    void goBack(MouseEvent event) {
        clear();
        StageManager.changeScene("login");
    }

    @FXML
    void selectFemale(MouseEvent event) {
        if (maleButton.isSelected()) {
            maleButton.setSelected(false);
        }
    }

    @FXML
    void selectMale(MouseEvent event) {
        if (femaleButton.isSelected()) {
            femaleButton.setSelected(false);
        }
    }

}
