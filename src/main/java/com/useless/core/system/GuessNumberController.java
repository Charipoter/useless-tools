package com.useless.core.system;

import com.useless.core.manager.SingletonManager;
import com.useless.core.manager.StageManager;
import com.useless.core.discard.service.WorkerPoolService;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class GuessNumberController {

    private final WorkerPoolService workerPoolService = SingletonManager.getInstance(WorkerPoolService.class);
    private final GuessNumberService guessNumberService = SingletonManager.getInstance(GuessNumberService.class);

    @FXML
    private TextField guessField;

    @FXML
    private TextArea message;

    @FXML
    void start(MouseEvent event) {
        message.clear();
        guessNumberService.start();
        workerPoolService.process(guessNumberService::check);
        message.appendText("游戏开始\n");
    }

    @FXML
    void stop(MouseEvent event) {
        stop();
    }

    void stop() {
        guessNumberService.stop();
        message.appendText("游戏结束\n");
    }

    @FXML
    void goBack(MouseEvent event) {
        stop();
        message.clear();
        StageManager.changeScene("catalogue");
    }

    @FXML
    void peopleGuess(MouseEvent event) {
        int guessNumber;
        try {
            guessNumber = Integer.parseInt(guessField.getText());
        } catch (Exception e) {
            message.appendText("请输入数字\n");
            return;
        }
        doGuess(guessNumber);
    }

    @FXML
    void cpuGuess(MouseEvent event) {
        int left = 1;
        int right = 100;
        // 猜数字符合二分思想
        while (left <= right) {
            int guessNumber = (left + right) >> 1;
            int state = doGuess(guessNumber);
            if (state == GuessNumberService.NOT_START
                    || state == GuessNumberService.NO_TARGET_VALUE
                    || state == GuessNumberService.EQUAL) {
                break;
            } else if (state == GuessNumberService.BIGGER) {
                right = guessNumber - 1;
            } else if (state == GuessNumberService.SMALLER) {
                left = guessNumber + 1;
            }
        }
    }

    private int doGuess(int guessNumber) {
        int state = guessNumberService.guess(guessNumber);

        String p = guessNumberService.getGuessThreadName() + ":猜了" + guessNumber +"\n"
                + guessNumberService.getCheckThreadName() + ":%s\n";

        if (state == GuessNumberService.EQUAL) {
            message.appendText(String.format(p, "猜对了"));
            stop();
        } else if (state == GuessNumberService.BIGGER) {
            message.appendText(String.format(p, "猜大了"));
        } else if (state == GuessNumberService.SMALLER) {
            message.appendText(String.format(p, "猜小了"));
        } else if (state == GuessNumberService.NOT_START) {
            message.appendText("游戏未开始\n");
        } else if (state == GuessNumberService.NO_TARGET_VALUE) {
            message.appendText("电脑性能太差了\n");
        } else {
            message.appendText("意外错误\n");
        }

        return state;
    }

}
