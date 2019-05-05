package com.shareniu.bpmn.ch8;

import org.apache.commons.io.FileUtils;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.common.engine.impl.util.IoUtil;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.DataObject;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipInputStream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:flowable-context.xml")
public class SpringTest {
    ProcessEngine processEngine;
    RepositoryService repositoryService;
    RuntimeService runtimeService;
    TaskService taskService;
    HistoryService historyService;
    IdentityService identityService;
    ManagementService managementService;

    @Before
    public void buildProcessEngine() {
        processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(processEngine);
        repositoryService = processEngine.getRepositoryService();
        System.out.println(repositoryService);
        String name = processEngine.getName();
        System.out.println("流程引擎的名称：" + name);
        DynamicBpmnService dynamicBpmnService = processEngine.getDynamicBpmnService();
        System.out.println(dynamicBpmnService);
        FormService formService = processEngine.getFormService();
        System.out.println(formService);

        historyService = processEngine.getHistoryService();
        System.out.println(historyService);
        identityService = processEngine.getIdentityService();
        System.out.println(identityService);
        managementService = processEngine.getManagementService();
        System.out.println(managementService);
        ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        System.out.println(processEngineConfiguration);
        runtimeService = processEngine.getRuntimeService();
        System.out.println(runtimeService);
        taskService = processEngine.getTaskService();
        System.out.println(taskService);
    }

    @Test
    public void DeploymentBuild() {
        DeploymentBuilder deploymentBuilder = repositoryService
                .createDeployment()
                .category("测试分类")
                .name("名称");
        System.out.println(deploymentBuilder);
    }

    @Test
    public void deploy() {
        DeploymentBuilder deploymentBuilder = repositoryService
                .createDeployment()
                .category("leave")
                .name("请假流程")
                .addClasspathResource("leave.bpmn")
                .tenantId("001");
        //.addClasspathResource("dataobject.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();
        System.out.println(deploy.getId());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstanceByKey() {
         String processDefinitionKey = "leave";
       // String processDefinitionKey = "dataobject";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        System.out.println(processInstance.getId() + "," + processInstance.getActivityId());
    }

    /**
     * select distinct RES.* from ACT_RU_TASK RES inner join ACT_RE_PROCDEF D
     * on RES.PROC_DEF_ID_ = D.ID_
     * WHERE RES.ASSIGNEE_ = ? and D.KEY_ = ? order by RES.ID_ asc
     * <p>
     * 张三1(String), leave(String)
     */
    @Test
    public void queryMyTask() {

        String processDefinitionKey = "leave";
        String assignee = "王五";
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .processDefinitionKey(processDefinitionKey)
                .list();
        for (Task task : list) {
            System.out.println(task.getId());
            System.out.println(task.getName());
            System.out.println(task.getTaskDefinitionKey());
            System.out.println(task.getExecutionId());
            System.out.println(task.getProcessInstanceId());
            System.out.println(task.getProcessDefinitionId());
            System.out.println(task.getCreateTime());
        }
    }

    @Test
    public void completeMyTask() {
        String taskId = "54502";
        taskService.complete(taskId);
    }

    /**
     * select distinct RES.* , P.KEY_ as ProcessDefinitionKey, P.ID_ as
     * ProcessDefinitionId, P.NAME_ as ProcessDefinitionName,
     * P.VERSION_ as ProcessDefinitionVersion,
     * P.DEPLOYMENT_ID_ as DeploymentId
     * from ACT_RU_EXECUTION RES
     * inner join ACT_RE_PROCDEF P on RES.PROC_DEF_ID_ = P.ID_
     * <p>
     * WHERE RES.PARENT_ID_ is null and RES.ID_ = ? and RES.PROC_INST_ID_ = ? order by RES.ID_ asc
     */
    @Test
    public void queryProcessInstanceState() {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId("59501")
                //.deploymentId()
                .singleResult();
        if (processInstance != null) {
            System.out.println("当前的流程实例正在运行");
        } else {
            System.out.println("当前的流程实例已经结束");
        }
    }

    /**
     * select distinct RES.* , P.KEY_ as ProcessDefinitionKey, P.ID_
     * as ProcessDefinitionId from ACT_RU_EXECUTION RES
     * inner join ACT_RE_PROCDEF P on RES.PROC_DEF_ID_ = P.ID_ order by RES.ID_ asc
     * 查询执行实例
     */
    @Test
    public void createExecutionQuery() {
        List<Execution> executions = runtimeService.createExecutionQuery()
                .list();
        for (Execution execution : executions) {
            System.out.println(execution.getId() + "," + execution.getActivityId());
        }
    }

    /**
     * 查询历史流程实例
     * select distinct RES.* , DEF.KEY_ as PROC_DEF_KEY_,
     * DEF.NAME_ as PROC_DEF_NAME_, DEF.VERSION_
     * as PROC_DEF_VERSION_, DEF.DEPLOYMENT_ID_ as DEPLOYMENT_ID_
     * from ACT_HI_PROCINST RES left outer join ACT_RE_PROCDEF DEF
     * on RES.PROC_DEF_ID_ = DEF.ID_ WHERE RES.PROC_INST_ID_ = ? order by RES.ID_ asc
     */
    @Test
    public void createHistoricProcessInstanceQuery() {
        String processInstanceId = "59501";
        HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).
                        singleResult();
        System.out.println("流程定义ID：" + hpi.getProcessDefinitionId());
        System.out.println("流程实例ID：" + hpi.getId());
        System.out.println(hpi.getStartTime());
        System.out.println(hpi.getStartActivityId());
        System.out.println(hpi.getEndTime());
    }

    @Test
    public void queryProcessInstanceState2() {
        String processInstanceId = "59501";
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        if (historicProcessInstance.getEndTime() == null) {
            System.out.println("当前的流程实例正在运行");
        } else {
            System.out.println("当前的流程实例已经结束");
        }
    }

    /**
     * : select RES.* from ACT_HI_ACTINST RES order by RES.ID_ asc
     */
    @Test
    public void createHistoricActivityInstanceQuery() {
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .list();
        for (HistoricActivityInstance hai : list) {
            System.out.println(hai.getId());
        }
    }

    /**
     * select distinct RES.* from ACT_HI_TASKINST RES order by RES.ID_ asc
     */
    @Test
    public void createHistoricTaskInstanceQuery() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .list();
        for (HistoricTaskInstance ht : list) {
            System.out.println(ht.getId());
            System.out.println(ht.getName());
            System.out.println(ht.getAssignee());
        }
    }

    /**
     * 设置流程实例的启动人
     */
    @Test
    public void setAuthenticatedUserId1() {
        String authenticatedUserId = "分享牛";
        identityService.setAuthenticatedUserId(authenticatedUserId);
        String processDefinitionKey = "leave";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        System.out.println(processInstance.getId() + "," + processInstance.getActivityId());
    }

    @Test
    public void setAuthenticatedUserId2() {
        String authenticatedUserId = "分享牛2";
        Authentication.setAuthenticatedUserId(authenticatedUserId);
        String processDefinitionKey = "leave";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        System.out.println(processInstance.getId() + "," + processInstance.getActivityId());
    }

    /**
     * select * from ACT_RU_VARIABLE where EXECUTION_ID_ = ? and NAME_= ? and TASK_ID_ is null
     * - ==> Parameters: 77001(String), day(String)
     */
    @Test
    public void getDataObject() {
        String executionId = "77001";
        String dataObject = "天数";
        DataObject dataObject1 = runtimeService.getDataObject(executionId, dataObject);
        if (dataObject1 != null) {
            System.out.println(dataObject1.getDataObjectDefinitionKey());
            System.out.println(dataObject1.getDescription());
            System.out.println(dataObject1.getExecutionId());
            System.out.println(dataObject1.getName());
            System.out.println(dataObject1.getValue());
            System.out.println(dataObject1.getType());
        }
    }

    /**
     * select * from ACT_RU_VARIABLE where EXECUTION_ID_ = ? and TASK_ID_ is null
     */
    @Test
    public void getDataObject2() {
        String executionId = "77001";
        Map<String, DataObject> dataObject1 = runtimeService.getDataObjects(executionId);
        Set<Map.Entry<String, DataObject>> entries = dataObject1.entrySet();
        for (Map.Entry<String, DataObject> dataObjectEntry : entries) {
            DataObject dataObject = dataObjectEntry.getValue();
            if (dataObject != null) {
                System.out.println(dataObject.getDataObjectDefinitionKey());
                System.out.println(dataObject.getDescription());
                System.out.println(dataObject.getExecutionId());
                System.out.println(dataObject.getName());
                System.out.println(dataObject.getValue());
                System.out.println(dataObject.getType());
            }
        }
    }

    @Test
    public void deleteProcessInstance() {
        String processInstanceId = "72001";
        String deleteReason = "分享牛删除";
        runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
    }

    @Test
    public void deleteProcessInstanceCascade() {
        String processInstanceId = "69501";
        String deleteReason = "分享牛删除";
        // ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();
        //processEngineConfiguration.getExecutionEntityManager().deleteProcessInstance(processInstanceId,deleteReason,true);

        //DeleteProcessInstanceCaCadeCmd
        managementService.executeCommand(new DeleteProcessInstanceCaCadeCmd(processInstanceId, deleteReason));
    }

    @Test
    public void getActiveActivityIds() {
        String executionId = "77005";
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(executionId);
        for (String s : activeActivityIds) {
            System.out.println(s);
        }
    }

    @Test
    public void startProcessInstanceById() {
        String processDefinitionId = "dataobject:1:74504";
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId);
    }

    /**
     * org.flowable.common.engine.api.FlowableObjectNotFoundException: no processes deployed with key 'leave' for tenant identifier 'oo1'
     */
    @Test
    public void startProcessInstanceByKeyAndTenantId() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId("leave", "001");
    }

    @Test
    public void isProcessDefinitionSuspended() {
        String processDefinitionId = "leave:2:67004";
        boolean processDefinitionSuspended = repositoryService.isProcessDefinitionSuspended(processDefinitionId);
        System.out.println(processDefinitionSuspended);

    }

    /**
     * 流程定义表状态是2 表示已经被挂起，1的话是没有被挂起。
     */
    @Test
    public void suspendProcessDefinitionById() {
        String processDefinitionId = "leave:2:67004";
    repositoryService
                .suspendProcessDefinitionById(processDefinitionId);

    }
    @Test
    public void startProcessInstanceById2() {
        String processDefinitionId = "leave:2:67004";
    runtimeService.startProcessInstanceById(processDefinitionId);

    }
    @Test
    public void activateProcessDefinitionById() {
        String processDefinitionId = "leave:2:67004";
        repositoryService.activateProcessDefinitionById(processDefinitionId);
    }
    @Test
    public void suspendProcessDefinitionById2() {
        String processDefinitionId = "leave:2:67004";
      repositoryService.suspendProcessDefinitionById(processDefinitionId,true,null);
    }


}
