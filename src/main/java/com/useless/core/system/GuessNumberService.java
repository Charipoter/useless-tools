package com.useless.core.system;

import java.util.Random;

public class GuessNumberService {

    public static final int BIGGER = 2;

    public static final int SMALLER = 1;

    public static final int EQUAL = 0;

    public static final int NOT_START = -1;
    // 目标数未给出，该状态只能存在极短的时间
    public static final int NO_TARGET_VALUE = -2;
    // 未猜数字
    public static final int NO_GUESS_VALUE = -3;
    // 未检查数字
    public static final int NO_CHECK_VALUE = -4;
    // 中断异常，暂时不会出现，因此不做处理
    public static final int INTERRUPT_EX = -5;
    // 有排他锁保证原子修改，无需 volatile
    private int guessNumber;

    private int state = NOT_START;

    private final Random random = new Random();

    private String guessThreadName;
    private String checkThreadName;

    public synchronized int guess(int guessNumber) {
        guessThreadName = Thread.currentThread().getName();
        try {
            // 游戏未开始或者没有目标数则不猜
            if (state == NOT_START || state == NO_TARGET_VALUE) {
                return state;
            }
            this.guessNumber = guessNumber;
            state = NO_CHECK_VALUE;
            // 等待check
            notifyAll();
            while (state == NO_CHECK_VALUE) {
                wait();
            }
            int ret = state;
            // 如果相等，说明游戏结束了，否则等待下一轮猜测
            state = ret == EQUAL ? NOT_START : NO_GUESS_VALUE;
            return ret;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return INTERRUPT_EX;
        } finally {
            // 唤醒check线程
            notifyAll();
        }
    }

    public synchronized void check() {
        checkThreadName = Thread.currentThread().getName();
        try {
            // 当一轮游戏开始时该线程运行，该线程结束就代表一轮游戏结束
            if (state != NOT_START) {
                int targetNumber = random.nextInt(100) + 1;
                state = NO_GUESS_VALUE;
                // 一轮游戏的循环
                while (state != NOT_START) {
                    // 等待猜测
                    notifyAll();
                    while (state == NO_GUESS_VALUE) {
                        wait();
                    }
                    // 可能中途结束
                    if (state != NOT_START) {
                        // check
                        if (guessNumber == targetNumber) {
                            // 游戏已经结束了，让其他线程结束游戏
                            state = EQUAL;
                        } else if (guessNumber > targetNumber) {
                            state = BIGGER;
                        } else {
                            state = SMALLER;
                        }
                    }
                    // 等待猜测线程将状态传给业务
                    notifyAll();
                    while (state != NO_GUESS_VALUE && state != NOT_START) {
                        wait();
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void start() {
        if (state == NOT_START) {
            state = NO_TARGET_VALUE;
        }
    }

    public synchronized void stop() {
        state = NOT_START;
        notifyAll();
    }

    public String getGuessThreadName() {
        return guessThreadName;
    }

    public String getCheckThreadName() {
        return checkThreadName;
    }

}
