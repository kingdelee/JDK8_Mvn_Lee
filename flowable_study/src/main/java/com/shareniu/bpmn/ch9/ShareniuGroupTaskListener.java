package com.shareniu.bpmn.ch9;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

public class ShareniuGroupTaskListener implements TaskListener {
    public void notify(DelegateTask delegateTask) {
    delegateTask.addCandidateUser("分享牛8");
    delegateTask.addCandidateUser("分享牛9");
    }
}
