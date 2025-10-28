package com.zhang;

import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.changes.CommitContext;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory;

public class CodeReviewCheckinHandlerFactory extends CheckinHandlerFactory {
    @Override
    public CheckinHandler createHandler(CheckinProjectPanel panel, CommitContext commitContext) {
        return new CodeReviewCheckinHandler(panel);
    }
}
