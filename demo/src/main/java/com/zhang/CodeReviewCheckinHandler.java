package com.zhang;

import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.changes.CommitExecutor;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.util.PairConsumer;
import com.zhang.ai.openaicodesdk.service.ReviewService;
import com.zhang.ai.openaicodesdk.service.ReviewService2;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;

public class CodeReviewCheckinHandler extends CheckinHandler {

    private final CheckinProjectPanel myPanel;

    public CodeReviewCheckinHandler(CheckinProjectPanel panel) {
        this.myPanel = panel;
    }

    @Override
    public ReturnResult beforeCheckin(CommitExecutor executor, PairConsumer<Object, Object> additionalDataConsumer) {
        // 自动在commit前调用
        Properties config = CeshiUi.getSavedConfig();
        // 执行您的逻辑
        ReviewService2 reviewService = new ReviewService2();
        try {
            reviewService.checkCode();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ReturnResult.COMMIT; // 或ReturnResult.CANCEL阻止提交
    }
}