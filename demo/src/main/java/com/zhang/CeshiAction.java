package com.zhang;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class CeshiAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        new CeshiUi();
    }
}