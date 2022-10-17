package com.useless.core.system;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CalculatorService {
    // 已完成的表达式
    StringBuilder confirmedExpression = new StringBuilder();
    // 如果正在输入数字，就由该对象维护
    StringBuilder numberBuilder = new StringBuilder();
    // numBuilder 中是否有小数点
    boolean pointerInNumBuilder = false;

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '=';
    }

    private boolean isHighPriorityOp(char c) {
        return c == '/' || c == '*';
    }

    private boolean isHighPriorityOp(String s) {
        return s.length() == 1 && isHighPriorityOp(s.charAt(0));
    }

    private boolean isDigit(String s) {
        char c = s.charAt(0);
        return c >= '0' && c <= '9';
    }

    public String toStringExpression() {
        return confirmedExpression.toString() + numberBuilder.toString();
    }

    public void removeLastNumber() {
        // numBuilder 为空，说明当前并没有输入数字，或者压根啥也没输入过，就无需移除
        if (numberBuilder.length() > 0) {
            numberBuilder.setLength(0);
            pointerInNumBuilder = false;
        }
    }

    public void removeAll() {
        confirmedExpression.setLength(0);
        numberBuilder.setLength(0);
        pointerInNumBuilder = false;
    }

    public void backspace() {
        // 回退numBuilder或confirmedExpression
        if (numberBuilder.length() > 0) {
            if (numberBuilder.charAt(numberBuilder.length() - 1) == '.') {
                pointerInNumBuilder = false;
            }
            numberBuilder.deleteCharAt(numberBuilder.length() - 1);
        } else if (confirmedExpression.length() > 0) {
            confirmedExpression.deleteCharAt(confirmedExpression.length() - 1);
        }
    }

    public boolean appendExpression(char c) {
        if (isOperator(c)) {
            // 判断是否可以加入运算符
            if (numberBuilder.length() == 0
                    && (confirmedExpression.length() == 0
                    || isOperator(confirmedExpression.charAt(confirmedExpression.length() - 1)))
            ) {
                return false;
            }
            // 维护中的数字入队
            String num = numberBuilder.toString();
            numberBuilder.setLength(0);
            pointerInNumBuilder = false;

            confirmedExpression.append(num);
            if (c != '=') {
                confirmedExpression.append(c);
            }
        } else if (c != '.') {
            numberBuilder.append(c);
        } else if (numberBuilder.length() == 0 || pointerInNumBuilder) {
            // 一个数字显然不可能有两个小数点
            return false;
        } else {
            pointerInNumBuilder = true;
            numberBuilder.append(c);
        }
        return true;
    }
    // 对表达式进行划分
    private List<String> splitExpression() {
        int i = 0;
        List<String> splitList = new ArrayList<>();
        StringBuilder num = new StringBuilder();
        while (i < confirmedExpression.length()) {
            char c = confirmedExpression.charAt(i);
            if (isOperator(c)) {
                splitList.add(String.valueOf(c));
                i++;
            } else {
                // 读取一个完整的数字
                // 形式：5.5 or 5 or 5.
                while (i < confirmedExpression.length() && !isOperator(c = confirmedExpression.charAt(i))) {
                    num.append(c);
                    i++;
                }
                splitList.add(num.toString());
                num.setLength(0);
            }
        }
        return splitList;
    }
    // 将前缀表达式转换成后缀表达式，思路在下面
    private Deque<String> getSuffixExpression() {
        Deque<String> stack = new ArrayDeque<>();
        Deque<String> suffix = new ArrayDeque<>();
        List<String> splitList = splitExpression();

        for (String s: splitList) {
            if (isDigit(s)) {
                suffix.addLast(s);
            } else if (isHighPriorityOp(s)) {
                while (!stack.isEmpty() && isHighPriorityOp(stack.peekLast())) {
                    suffix.addLast(stack.pollLast());
                }
                stack.addLast(s);
            } else {
                while (!stack.isEmpty()) {
                    suffix.addLast(stack.pollLast());
                }
                stack.addLast(s);
            }
        }
        while (!stack.isEmpty()) {
            suffix.addLast(stack.pollLast());
        }
        return suffix;
    }
    // 执行后缀表达式，通过栈实现
    private String executeSuffixExpression(Deque<String> suffix) {
        Deque<String> stack = new ArrayDeque<>();
        for (String s: suffix) {
            if (isDigit(s)) {
                stack.addLast(s);
            } else if (stack.size() >= 2) {
                double num2 = Double.parseDouble(stack.pollLast());
                double num1 = Double.parseDouble(stack.pollLast());
                double result = 0;
                char op = s.charAt(0);
                if (op == '+') {
                    result = num1 + num2;
                } else if (op == '-') {
                    result = num1 - num2;
                } else if (op == '*') {
                    result = num1 * num2;
                } else if (op == '/') {
                    result = num1 / num2;
                }
                stack.addLast(String.valueOf(result));
            }
        }
        removeAll();
        return stack.peekLast();
    }

    // 此时 numBuilder 一定是空的
    // 思路:
    // 从左向右遍历中缀表达式，转换为后缀表达式，规则:
    //  1.遇到数字，直接加入后缀表达式
    //  2.遇到高优先级运算符，若栈为空入栈，若栈不为空，则将当前运算符与栈顶元素比较:
    //    栈顶元素也为高优先级运算符：栈顶元素出栈加入后缀表达式，当前运算符入栈                                                                                    中不会出现连续的高优先级运算符
    //    栈顶元素为低优先级运算符：将当前元素入栈
    //  5.遇到低优先级运算符，若栈为空直接入栈，若栈不为空，则将当前运算符与栈顶元素比较:
    //    栈顶元素也为低优先级运算符：栈顶元素出栈加入后缀表达式，当前运算符入栈。这样的操作使得栈中                                                                                   不会出现连续的低优先级运算符
    //    栈顶元素为高优先级运算符：栈顶元素出栈加入后缀表达式，当前运算符入栈
    // 执行后缀表达式
    public String executeExpression() {
        return executeSuffixExpression(getSuffixExpression());
    }
}
