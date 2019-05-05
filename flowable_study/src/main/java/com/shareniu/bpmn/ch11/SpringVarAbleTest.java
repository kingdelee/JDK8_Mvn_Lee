package com.shareniu.bpmn.ch11;

import org.flowable.common.engine.api.history.HistoricData;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.ProcessInstanceHistoryLog;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.service.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 历史相关的操作
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:flowable-context.xml")
public class SpringVarAbleTest {
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
    public void deploy() {
        DeploymentBuilder deploymentBuilder = repositoryService
                .createDeployment()
                .category("历史数据查询相关的测试")
                .addClasspathResource("历史数据查询.bpmn20.xml");
        //.addClasspathResource("dataobject.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();
        System.out.println(deploy.getId());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstanceByKey() {
        String processDefinitionKey = "history";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        System.out.println(processInstance.getId() + "," + processInstance.getActivityId());
    }
    @Test
    public void sleep() throws InterruptedException {
       Thread.sleep(5000L);
    }

    /**
     * 查询历史流程实例表
     *   select distinct RES.* , DEF.KEY_ as PROC_DEF_KEY_,
     *   DEF.NAME_ as PROC_DEF_NAME_, DEF.VERSION_
     *   as PROC_DEF_VERSION_,
     *   DEF.DEPLOYMENT_ID_ as DEPLOYMENT_ID_
     *   from ACT_HI_PROCINST RES
     *   left outer join ACT_RE_PROCDEF DEF
     *   on RES.PROC_DEF_ID_ = DEF.ID_
     *
     *   WHERE RES.END_TIME_ is not NULL order by RES.ID_ asc
     */
    @Test
    public void createHistoricProcessInstanceQuery() {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
               .finished()
                .list();

        for(HistoricProcessInstance hpi :list){
            System.out.println(hpi.getId());
            System.out.println(hpi.getStartActivityId());
            System.out.println(hpi.getDeploymentId());
        }
    }
    @Test
    public void createNativeHistoricProcessInstanceQuery() {
        List<HistoricProcessInstance> list = historyService.
                createNativeHistoricProcessInstanceQuery()
               .sql("select * from ACT_HI_PROCINST ")
                .list();

        for(HistoricProcessInstance hpi :list){
            System.out.println(hpi.getId());
            System.out.println(hpi.getStartActivityId());
            System.out.println(hpi.getDeploymentId());
        }
    }

    /**
     * select RES.* from ACT_HI_ACTINST RES order by RES.ID_ asc
     */
    @Test
    public void createHistoricActivityInstanceQuery() {
        List<HistoricActivityInstance> list = historyService
                .createHistoricActivityInstanceQuery()
                .list();

        for (HistoricActivityInstance hai:list){
            System.out.println(hai.getId());
            System.out.println(hai.getActivityId());
        }

    }

    /**
     */
    @Test
    public void createNativeHistoricActivityInstanceQuery() {
        List<HistoricActivityInstance> list = historyService
               .createNativeHistoricActivityInstanceQuery()
                .sql("select RES.* from ACT_HI_ACTINST RES order by RES.ID_ asc").list();
        for (HistoricActivityInstance hai:list){
            System.out.println(hai.getId());
            System.out.println(hai.getAssignee());
            System.out.println(hai.getStartTime());
        }

    }
//    @Test
//    public void createHistoricTaskInstanceQuery() {
//        List<HistoricTaskInstance> list = historyService
//               .createNativeHistoricProcessInstanceQuery()
//
//        for (HistoricTaskInstance hai:list){
//            System.out.println(hai.getId());
//            System.out.println(hai.getAssignee());
//            System.out.println(hai.getStartTime());
//        }
//
//    }

    /**
     * select RES.* from ACT_HI_VARINST RES order by RES.ID_ asc
     */
    @Test
    public void createHistoricVariableInstanceQuery() {
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().list();
        for(HistoricVariableInstance hvi:list){
            System.out.println(hvi.getVariableName());
            System.out.println(hvi.getValue());
            System.out.println(hvi.getVariableTypeName());
        }
    }

    /**
     *  select * from ACT_HI_PROCINST where PROC_INST_ID_ = ?
     */
    @Test
    public void createProcessInstanceHistoryLogQuery() {
        String processInstanceId="2501";
        ProcessInstanceHistoryLog processInstanceHistoryLog = historyService
                .createProcessInstanceHistoryLogQuery(processInstanceId)
                .includeTasks()
                .includeActivities()
                .singleResult();
        List<HistoricData> historicData = processInstanceHistoryLog.getHistoricData();
        for (HistoricData historicData1 :historicData){
            if (historicData1 instanceof HistoricTaskInstanceEntity ){
                HistoricTaskInstanceEntity historicTaskInstanceEntity= (HistoricTaskInstanceEntity) historicData1;
                System.out.println(historicTaskInstanceEntity.getIdPrefix());
                System.out.println(historicTaskInstanceEntity.getAssignee());
            }
            if (historicData1 instanceof  HistoricActivityInstance){
                HistoricActivityInstance hai= (HistoricActivityInstance) historicData1;
                System.out.println(hai.getActivityId());
            }


            System.out.println(historicData1.getTime());
        }
        System.out.println(processInstanceHistoryLog.getId());
        System.out.println(processInstanceHistoryLog.getEndTime());
        System.out.println(processInstanceHistoryLog.getStartActivityId());
    }

    /**
     * select * from ACT_HI_TASKINST where ID_ = ?
     * select * from ACT_HI_IDENTITYLINK where TASK_ID_ = ?
     */
    @Test
    public void getHistoricIdentityLinksForTask() {
        List<HistoricIdentityLink> historicIdentityLinksForTask = historyService.getHistoricIdentityLinksForTask("5003");
        for(HistoricIdentityLink hi:historicIdentityLinksForTask){
            System.out.println(hi.getUserId());
        }
    }

    /**
     *  select * from ACT_HI_IDENTITYLINK where PROC_INST_ID_ = ?
     */
    @Test
    public void getHistoricIdentityLinksForProcessInstance() {
        List<HistoricIdentityLink> historicIdentityLinksForProcessInstance = historyService
                .getHistoricIdentityLinksForProcessInstance("2501");
        for(HistoricIdentityLink hi:historicIdentityLinksForProcessInstance){
            System.out.println(hi.getUserId());
        }
    }

    @Test
    public void complete() {
        Map<String,Object> vars=new HashMap<String, Object>();
        vars.put("day",3);
       taskService.complete("2505",vars);
    }






}
