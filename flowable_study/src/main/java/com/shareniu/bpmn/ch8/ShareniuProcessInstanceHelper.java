package com.shareniu.bpmn.ch8;

import org.flowable.engine.impl.util.ProcessInstanceHelper;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;

import java.util.Map;

public class ShareniuProcessInstanceHelper extends ProcessInstanceHelper {

    @Override
    public ProcessInstance createProcessInstance(ProcessDefinition processDefinition, String businessKey, String processInstanceName, Map<String, Object> variables, Map<String, Object> transientVariables) {
        return super.createProcessInstance(processDefinition, businessKey, processInstanceName, variables, transientVariables);
    }

    @Override
    public ProcessInstance createProcessInstance(ProcessDefinition processDefinition, String businessKey, String processInstanceName,
        String overrideDefinitionTenantId, String predefinedProcessInstanceId, Map<String, Object> variables, Map<String, Object> transientVariables,
        String callbackId, String callbackType, boolean startProcessInstance) {
        System.out.println("ShareniuProcessInstanceHelper:createProcessInstance");

        return super
            .createProcessInstance(processDefinition, businessKey, processInstanceName, overrideDefinitionTenantId, predefinedProcessInstanceId, variables,
                transientVariables, callbackId, callbackType, startProcessInstance);
    }
}
