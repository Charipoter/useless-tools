package com.useless.core.discard.system;

import com.useless.core.manager.SingletonManager;
import com.useless.core.manager.StageManager;
import com.useless.core.discard.constant.StateConsts;
import com.useless.core.discard.service.WorkerPoolService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginController {

    private final LoginService loginService = SingletonManager.getInstance(LoginService.class);
    private final WorkerPoolService workerPoolService = SingletonManager.getInstance(WorkerPoolService.class);

    @FXML
    private TextField passwordField;

    @FXML
    private TextField accountField;

    @FXML
    private Label loginMessage;

    @FXML
    void register(MouseEvent event) {
        // 界面切换
        StageManager.changeScene("register");
    }

    @FXML
    void tryLogin(MouseEvent event) {
        beforeLogin();

        String account = accountField.getText();
        String password = passwordField.getText();
        // 留个管理员账号
        if (account.equals("root") && password.equals("root")) {
            afterLoginSuccess();
            return;
        }
        // 防止主线程阻塞
        workerPoolService.process(() -> {
            int state = loginService.tryLogin(account, password);
            // 让主线程异步执行
            Platform.runLater(() -> {
                if (state == StateConsts.ACCOUNT_EXIST) {
                    afterLoginSuccess();
                } else if (state == StateConsts.ACCOUNT_NOT_EXIST) {
                    loginMessage.setText("账号不存在!");
                } else if (state == StateConsts.PASSWORD_ERROR) {
                    loginMessage.setText("密码错误!");
                } else if (state == StateConsts.DB_ERROR) {
                    loginMessage.setText("数据库故障!");
                }
            });
        });
    }

    void beforeLogin() {
        loginMessage.setText("登陆中...");
    }

    void afterLoginSuccess() {
        loginMessage.setText("");
        StageManager.changeScene("catalogue");
    }

}
