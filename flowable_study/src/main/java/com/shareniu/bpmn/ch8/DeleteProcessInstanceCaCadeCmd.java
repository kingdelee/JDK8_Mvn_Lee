package com.shareniu.bpmn.ch8;

import org.flowable.common.engine.impl.AbstractEngineConfiguration;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;

public class DeleteProcessInstanceCaCadeCmd implements Command<Void> {

    String processInstanceId = null;
    String deleteReason = "分享牛删除";

    public DeleteProcessInstanceCaCadeCmd(String processInstanceId, String deleteReason) {
        this.processInstanceId = processInstanceId;
        this.deleteReason = deleteReason;
    }

    public Void execute(CommandContext commandContext) {
        AbstractEngineConfiguration currentEngineConfiguration = commandContext.getCurrentEngineConfiguration();
        if (currentEngineConfiguration != null) {
            ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) currentEngineConfiguration;
            processEngineConfiguration.getExecutionEntityManager().deleteProcessInstance(processInstanceId, deleteReason, true);

        }

        return null;
    }
}
