package com.shareniu.bpmn.ch13;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.api.history.HistoricData;
import org.flowable.engine.*;
import org.flowable.engine.form.FormProperty;
import org.flowable.engine.form.FormType;
import org.flowable.engine.form.StartFormData;
import org.flowable.engine.form.TaskFormData;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.ProcessInstanceHistoryLog;
import org.flowable.engine.impl.form.DateFormType;
import org.flowable.engine.impl.form.EnumFormType;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.task.service.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.SimpleFormatter;

/**
 * 动态表单相关的操作
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
    FormService formService;

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
        formService = processEngine.getFormService();
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
                .category("表单相关相关的测试")
                .addClasspathResource("form.bpmn20.xml");
        //.addClasspathResource("dataobject.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();
        System.out.println(deploy.getId());
    }


    @Test
    public void getStartFormData() {
        String processDefinitionId = "form:1:35004";
        StartFormData startFormData =
                formService.getStartFormData(processDefinitionId);
        System.out.println(startFormData.getProcessDefinition());
        System.out.println(startFormData.getDeploymentId());
        System.out.println(startFormData.getFormKey());
        List<FormProperty> formProperties = startFormData.getFormProperties();
        for (FormProperty fm : formProperties){
            System.out.println("############################");
            System.out.println(fm.getId());
            System.out.println(fm.getName());
            FormType formType = fm.getType();
            System.out.println(formType);
            String key="";
            if (formType instanceof EnumFormType){
                key="values";
            }else if (formType instanceof DateFormType){
                key="datePattern";
            }
            Object information = formType.getInformation(key);
            System.out.println("information:"+information);
            System.out.println(fm.getValue());
            System.out.println("############################");
        }

    }

    /**
     *  <extensionElements>
     *                 <flowable:formProperty id="start_date" name="开始时间" type="date" datePattern="yyyy-MM-dd"
     *                                        required="true"></flowable:formProperty>
     *                 <flowable:formProperty id="end_date" name="结束时间" type="date" datePattern="yyyy-MM-dd"
     *                                        required="true"></flowable:formProperty>
     *                 <flowable:formProperty id="reason" name="请假原因" type="string" required="true"></flowable:formProperty>
     *                 <flowable:formProperty id="days" name="请假的天数" type="long" required="true"></flowable:formProperty>
     *             </extensionElements>
     */
    @Test
    public void submitStartFormData() {
        String processDefinitionId = "form:1:35004";
        Map<String, String> vars=new HashMap<String, String>();
        vars.put("start_date",getDate());
        vars.put("end_date",getDate());
        vars.put("reason","我想出去玩玩");
        vars.put("days","3");
        formService.submitStartFormData(processDefinitionId,vars);
    }
    public  String getDate(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String s = simpleDateFormat.format(new Date());

        return  s;
    }



    @Test
    public void getTaskFormData() {
        String taskId="42507";
        TaskFormData startFormData = formService.getTaskFormData(taskId);
        System.out.println(startFormData.getDeploymentId());
        System.out.println(startFormData.getFormKey());
        List<FormProperty> formProperties = startFormData.getFormProperties();
        for (FormProperty fm : formProperties){
            System.out.println("############################");
            System.out.println(fm.getId());
            System.out.println(fm.getName());
            FormType formType = fm.getType();
            System.out.println(formType);
            String key="";
            if (formType instanceof EnumFormType){
                key="values";
            }else if (formType instanceof DateFormType){
                key="datePattern";
            }
            Object information = formType.getInformation(key);
            System.out.println("information:"+information);
            System.out.println(fm.getValue());
            System.out.println("############################");
        }

    }

    @Test
    public void saveFormData() {
        Map<String, String> vars=new HashMap<String, String>();
        vars.put("分享牛1",getDate());
        vars.put("分享牛2",getDate());
        vars.put("reason","我想出去玩玩2222");
        vars.put("days","5");
        String taskId="42507";
        formService.saveFormData(taskId,vars);
    }
    @Test
    public void submitTaskFormData() {
        Map<String, String> vars=new HashMap<String, String>();
        vars.put("分享牛1",getDate());
        vars.put("分享牛2",getDate());
        vars.put("reason","我想出去玩玩2222");
        vars.put("days","5");
        String taskId="42507";
        formService.submitTaskFormData(taskId,vars);
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
     * select distinct RES.* , DEF.KEY_ as PROC_DEF_KEY_,
     * DEF.NAME_ as PROC_DEF_NAME_, DEF.VERSION_
     * as PROC_DEF_VERSION_,
     * DEF.DEPLOYMENT_ID_ as DEPLOYMENT_ID_
     * from ACT_HI_PROCINST RES
     * left outer join ACT_RE_PROCDEF DEF
     * on RES.PROC_DEF_ID_ = DEF.ID_
     * <p>
     * WHERE RES.END_TIME_ is not NULL order by RES.ID_ asc
     */
    @Test
    public void createHistoricProcessInstanceQuery() {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .finished()
                .list();

        for (HistoricProcessInstance hpi : list) {
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

        for (HistoricProcessInstance hpi : list) {
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

        for (HistoricActivityInstance hai : list) {
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
        for (HistoricActivityInstance hai : list) {
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
        for (HistoricVariableInstance hvi : list) {
            System.out.println(hvi.getVariableName());
            System.out.println(hvi.getValue());
            System.out.println(hvi.getVariableTypeName());
        }
    }

    /**
     * select * from ACT_HI_PROCINST where PROC_INST_ID_ = ?
     */
    @Test
    public void createProcessInstanceHistoryLogQuery() {
        String processInstanceId = "2501";
        ProcessInstanceHistoryLog processInstanceHistoryLog = historyService
                .createProcessInstanceHistoryLogQuery(processInstanceId)
                .includeTasks()
                .includeActivities()
                .singleResult();
        List<HistoricData> historicData = processInstanceHistoryLog.getHistoricData();
        for (HistoricData historicData1 : historicData) {
            if (historicData1 instanceof HistoricTaskInstanceEntity) {
                HistoricTaskInstanceEntity historicTaskInstanceEntity = (HistoricTaskInstanceEntity) historicData1;
                System.out.println(historicTaskInstanceEntity.getIdPrefix());
                System.out.println(historicTaskInstanceEntity.getAssignee());
            }
            if (historicData1 instanceof HistoricActivityInstance) {
                HistoricActivityInstance hai = (HistoricActivityInstance) historicData1;
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
        for (HistoricIdentityLink hi : historicIdentityLinksForTask) {
            System.out.println(hi.getUserId());
        }
    }

    /**
     * select * from ACT_HI_IDENTITYLINK where PROC_INST_ID_ = ?
     */
    @Test
    public void getHistoricIdentityLinksForProcessInstance() {
        List<HistoricIdentityLink> historicIdentityLinksForProcessInstance = historyService
                .getHistoricIdentityLinksForProcessInstance("2501");
        for (HistoricIdentityLink hi : historicIdentityLinksForProcessInstance) {
            System.out.println(hi.getUserId());
        }
    }

    @Test
    public void complete() {
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("day", 3);
        taskService.complete("2505", vars);
    }

    /**
     * 外置表单部署
     */
    @Test
    public void deploy2() {
        DeploymentBuilder deploymentBuilder = repositoryService
                .createDeployment()
                .addClasspathResource("formkey2.bpmn20.xml")
                .addClasspathResource("start.html")
                .addClasspathResource("task.html")
                .addClasspathResource("task2.html");
        Deployment deploy = deploymentBuilder.deploy();
        System.out.println(deploy.getId());
    }
    @Test
    public void getStartFormKey() {
        String processDefinitionId="formkey2:1:7";
        String startFormKey = formService.getStartFormKey(processDefinitionId);
        System.out.println("开始节点定义的表单key:"+startFormKey);
    }
    @Test
    public void getRenderedStartForm() {
        String processDefinitionId="formkey2:1:7";
        Object startFormKey = formService.getRenderedStartForm(processDefinitionId);
        System.out.println("开始节点定义的表单key:"+startFormKey);
    }
    @Test
    public void getRenderedStartForm2() {
        String processDefinitionId="formkey2:1:7";
        Object startFormKey = formService.getRenderedStartForm(processDefinitionId,null);
        System.out.println("开始节点定义的表单key:"+startFormKey);
    }
    @Test
    public void getRenderedStartForm3() {
        String processDefinitionId="formkey2:1:7";
        Object startFormKey = formService.getRenderedStartForm(processDefinitionId,"juel");
        System.out.println("开始节点定义的表单key:"+startFormKey);
    }
    @Test
    public void submitStartFormData2() {
        String processDefinitionId="formkey2:1:7";

        Map<String, String> vars=new HashMap<String, String>();
        vars.put("startDate",getDate());
        vars.put("endDate",getDate());
        vars.put("reason","我想出去玩玩");
        vars.put("days","3");
        formService.submitStartFormData(processDefinitionId,vars);
    }
    @Test
    public void getRenderedTaskForm() {
        String taskId="2507";
        Object renderedTaskForm = formService.getRenderedTaskForm(taskId);
        System.out.println(renderedTaskForm);
    }
    @Test
    public void getRenderedTaskForm2() {
        String taskId="2507";
        Object renderedTaskForm = formService.getRenderedTaskForm(taskId,null);
        System.out.println(renderedTaskForm);
    }
    @Test
    public void getRenderedTaskForm3() {
        String taskId="5001";
        Object renderedTaskForm = formService.getRenderedTaskForm(taskId,"juel");
        System.out.println(renderedTaskForm);
    }
    @Test
    public void submitTaskFormData2() {
        String taskId="2507";
        Map<String, String> vars=new HashMap<String, String>();
        vars.put("startDate",getDate());
        vars.put("endDate",getDate());
        vars.put("reason","我想出去玩玩");
        vars.put("days","3");
        formService.submitTaskFormData(taskId,vars);
    }
    @Test
    public void getBpmnModel() {
        String processDefinitionId="formkey2:1:7";
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Process mainProcess = bpmnModel.getMainProcess();
        List<UserTask> flowElementsOfType = mainProcess.findFlowElementsOfType(UserTask.class);
        for(UserTask userTask:flowElementsOfType){
            String formKey = userTask.getFormKey();
            System.out.println("任务的ID："+userTask.getId()+",formkey:"+formKey);
        }
    }
    @Test
    public void getBpmnModel2() {
        String processDefinitionId="formkey2:1:7";
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Process mainProcess = bpmnModel.getMainProcess();
        List<StartEvent> flowElementsOfType = mainProcess.findFlowElementsOfType(StartEvent.class);
        for(StartEvent startEvent:flowElementsOfType){
            String formKey = startEvent.getFormKey();
            System.out.println("开始节点的ID："+startEvent.getId()+",formkey:"+formKey);
        }
    }


}
