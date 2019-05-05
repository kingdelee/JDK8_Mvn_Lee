package com.shareniu.bpmn.ch9;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

public class ShareniuSingleTaskListener implements TaskListener {
    public void notify(DelegateTask delegateTask) {

        delegateTask.setAssignee("分享牛");
    }
}
