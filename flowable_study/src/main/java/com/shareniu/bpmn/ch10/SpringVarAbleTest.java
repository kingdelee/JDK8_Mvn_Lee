package com.shareniu.bpmn.ch10;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;
import org.flowable.task.api.Task;
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
 * 变量相关的操作
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
                .category("变量相关的测试")
                .addClasspathResource("变量测试流程.bpmn20.xml");
        //.addClasspathResource("dataobject.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();
        System.out.println(deploy.getId());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstanceByKey() {
        String processDefinitionKey = "varible";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        System.out.println(processInstance.getId() + "," + processInstance.getActivityId());
    }

    /**
     * 10:13:57,721 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl.selectVariableInstanceByTaskAndName  - ==> Parameters: 2505(String), variables(String)
     * 10:13:57,722 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl.selectVariableInstanceByTaskAndName  - <==      Total: 0
     * 10:13:57,722 [main] DEBUG org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl.selectExecution  - ==>  Preparing: select * from ACT_RU_EXECUTION where ID_ = ?
     * 10:13:57,723 [main] DEBUG org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl.selectExecution  - ==> Parameters: 2502(String)
     * 10:13:57,725 [main] DEBUG org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl.selectExecution  - <==      Total: 1
     * 10:13:57,726 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl.selectVariableInstanceByExecutionAndName  - ==>  Preparing: select * from ACT_RU_VARIABLE where EXECUTION_ID_ = ? and NAME_= ? and TASK_ID_ is null
     * 10:13:57,726 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl.selectVariableInstanceByExecutionAndName  - ==> Parameters: 2502(String), variables(String)
     * 10:13:57,727 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl.selectVariableInstanceByExecutionAndName  - <==      Total: 0
     * 10:13:57,727 [main] DEBUG org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl.selectExecution  - ==>  Preparing: select * from ACT_RU_EXECUTION where ID_ = ?
     * 10:13:57,728 [main] DEBUG org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl.selectExecution  - ==> Parameters: 2501(String)
     * 10:13:57,730 [main] DEBUG org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl.selectExecution  - <==      Total: 1
     * 10:13:57,730 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl.selectVariableInstanceByExecutionAndName  - ==>  Preparing: select * from ACT_RU_VARIABLE where EXECUTION_ID_ = ? and NAME_= ? and TASK_ID_ is null
     * 10:13:57,731 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl.selectVariableInstanceByExecutionAndName  - ==> Parameters: 2501(String), variables(String)
     * 10:13:57,732 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl.selectVariableInstanceByExecutionAndName  - <==      Total: 0
     * 10:13:57,732 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl.selectVariableInstanceByTaskAndName  - ==>  Preparing: select * from ACT_RU_VARIABLE where TASK_ID_ = ? and NAME_= ?
     * 10:13:57,733 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl.selectVariableInstanceByTaskAndName  - ==> Parameters: 2505(String), 请假日期(String)
     * 10:13:57,733 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl.selectVariableInstanceByTaskAndName  - <==      Total: 0
     * 10:13:57,734 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl.selectVariableInstanceByExecutionAndName  - ==>  Preparing: select * from ACT_RU_VARIABLE where EXECUTION_ID_ = ? and NAME_= ? and TASK_ID_ is null
     * 10:13:57,734 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl.selectVariableInstanceByExecutionAndName  - ==> Parameters: 2502(String), 请假日期(String)
     * 10:13:57,735 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl.selectVariableInstanceByExecutionAndName  - <==      Total: 0
     * 10:13:57,735 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl.selectVariableInstanceByExecutionAndName  - ==>  Preparing: select * from ACT_RU_VARIABLE where EXECUTION_ID_ = ? and NAME_= ? and TASK_ID_ is null
     * 10:13:57,736 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl.selectVariableInstanceByExecutionAndName  - ==> Parameters: 2501(String), 请假日期(String)
     * 10:13:57,737 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl.selectVariableInstanceByExecutionAndName  - <==      Total: 0
     * 10:13:57,750 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.HistoricVariableInstanceEntityImpl.bulkInsertHistoricVariableInstance  - ==>  Preparing: insert into ACT_HI_VARINST (ID_, PROC_INST_ID_, EXECUTION_ID_, TASK_ID_, NAME_, REV_, VAR_TYPE_, SCOPE_ID_, SUB_SCOPE_ID_, SCOPE_TYPE_, BYTEARRAY_ID_, DOUBLE_, LONG_ , TEXT_, TEXT2_, CREATE_TIME_, LAST_UPDATED_TIME_) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) , ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )
     * 10:13:57,753 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.HistoricVariableInstanceEntityImpl.bulkInsertHistoricVariableInstance  - ==> Parameters: 5003(String), 2501(String), 2501(String), null, variables(String), 0(Integer), string(String), null, null, null, null, null, null, taskService.setVariables(String), null, 2018-11-20 10:13:57.732(Timestamp), 2018-11-20 10:13:57.732(Timestamp), 5004(String), 2501(String), 2501(String), null, 请假日期(String), 0(Integer), date(String), null, null, null, null, null, 1542680037717(Long), null, null, 2018-11-20 10:13:57.738(Timestamp), 2018-11-20 10:13:57.738(Timestamp)
     * 10:13:57,754 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.HistoricVariableInstanceEntityImpl.bulkInsertHistoricVariableInstance  - <==    Updates: 2
     * 10:13:57,757 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl.bulkInsertVariableInstance  - ==>  Preparing: INSERT INTO ACT_RU_VARIABLE (ID_, REV_, TYPE_, NAME_, PROC_INST_ID_, EXECUTION_ID_, TASK_ID_, SCOPE_ID_, SUB_SCOPE_ID_, SCOPE_TYPE_, BYTEARRAY_ID_, DOUBLE_, LONG_ , TEXT_, TEXT2_) VALUES ( ?, 1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) , ( ?, 1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )
     * 10:13:57,759 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl.bulkInsertVariableInstance  - ==> Parameters: 5003(String), string(String), variables(String), 2501(String), 2501(String), null, null, null, null, null, null, null, taskService.setVariables(String), null, 5004(String), date(String), 请假日期(String), 2501(String), 2501(String), null, null, null, null, null, null, 1542680037717(Long), null, null
     * 10:13:57,760 [main] DEBUG org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl.bulkInsertVariableInstance  - <==    Updates: 2
     * 10:13:57,761 [main] DEBUG org.flowable.task.service.impl.persistence.entity.TaskEntityImpl.updateTask  - ==>  Preparing: update ACT_RU_TASK SET REV_ = ? where ID_= ? and REV_ = ?
     * 10:13:57,762 [main] DEBUG org.flowable.task.service.impl.persistence.entity.TaskEntityImpl.updateTask  - ==> Parameters: 4(Integer), 2505(String), 3(Integer)
     * 10:13:57,762 [main] DEBUG org.flowable.task.service.impl.persistence.entity.TaskEntityImpl.updateTask  - <==    Updates: 1
     * 10:13:57,783 [Thread-1] INFO  com.alibaba.druid.pool.DruidDataSource  - {dataSource-1} closed
     *
     * Process finished with exit code 0
     *
     * setVariableLocal方式设置的变量 变量表中的task_id有值
     */
    @Test
    public void createTaskQuery() {
        Task task = taskService.createTaskQuery().singleResult();
        System.out.println(task.getId());

        taskService.setVariable(task.getId(),"请假人","张三");
        taskService.setVariableLocal(task.getId(),"请假天数",10);
        Map<String, Object> variables=new HashMap<String, Object>();
        variables.put("请假日期",new Date());
        variables.put("variables","taskService.setVariables");

        taskService.setVariables(task.getId(),variables);
    }
    @Test
    public void complete() {
        Task task = taskService.createTaskQuery().singleResult();
        Map<String, Object> variables=new HashMap<String, Object>();
        variables.put("完成人","张三");
        taskService.complete(task.getId(),variables);
    }
    @Test
    public void createTaskQuery2() {
        Task task = taskService.createTaskQuery().singleResult();
        System.out.println(task.getId());

        Map<String, Object> variables = taskService.getVariables(task.getId());
        System.out.println(variables);
        /**
         * select * from ACT_RU_VARIABLE where EXECUTION_ID_ = ? and NAME_= ? and TASK_ID_ is null
         */
        Object 完成人 = taskService.getVariable(task.getId(), "完成人");
        System.out.println(完成人);
        /**
         * select * from ACT_RU_VARIABLE where TASK_ID_ = ? and NAME_= ?
         */
        Object day = taskService.getVariableLocal(task.getId(), "请假天数");
        System.out.println(day);
    }

    @Test
    public  void  setVariables(){
        Task task = taskService.createTaskQuery().singleResult();
        runtimeService.setVariable(task.getExecutionId(),"分享牛1","分享牛1");
        runtimeService.setVariable(task.getExecutionId(),"分享牛2","分享牛2");

        Map<String, Object> variables=new HashMap<String, Object>();
        variables.put("分享牛3","分享牛3");
        Person p=new Person();
        p.setAge(18);
        p.setName("分享牛");
        variables.put("p",p);
        runtimeService.setVariables(task.getExecutionId(),variables);

    }

    /**
     * select * from ACT_GE_BYTEARRAY where ID_ = ?
     */
    @Test
    public  void  getVariables(){
        Task task = taskService.createTaskQuery().singleResult();
        Object p = runtimeService.getVariable(task.getExecutionId(), "p");
        System.out.println(p);
        if (p instanceof  Person){
            Person person= (Person) p;
            System.out.println(person.getAge());
            System.out.println(person.getName());
        }
    }

    /**
     *  select RES.* from ACT_HI_VARINST RES order by RES.ID_ asc
     *
     *  select RES.* from ACT_HI_VARINST RES WHERE RES.PROC_INST_ID_ = ? order by RES.ID_ asc
     *
     *  select RES.* from ACT_HI_VARINST RES WHERE RES.PROC_INST_ID_ = ? and RES.NAME_ = ? order by RES.ID_ asc
     */
    @Test
    public  void  getHistoryVariables(){

        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId("2501")
                .variableName("p")
                .list();
        if (list!=null && list.size()>0){
            for(HistoricVariableInstance hvi:list){
                System.out.println(hvi.getCreateTime());
                System.out.println(hvi.getTaskId());
                System.out.println(hvi.getVariableName());
                System.out.println(hvi.getVariableTypeName());
                System.out.println(hvi.getValue());
            }
        }
    }
    @Test
    public  void  createProcessInstanceBuilder(){
        Map<String, Object> transientVariables=new HashMap<String, Object>();
        transientVariables.put("a","a");
        transientVariables.put("b","b");
        String processDefinitionKey = "varible";
        ProcessInstanceBuilder pib = runtimeService.createProcessInstanceBuilder();
        ProcessInstance processInstance = pib.processDefinitionKey(processDefinitionKey)
                .name("分享牛1")
                .transientVariables(transientVariables)
                .start();
        System.out.println(processInstance);
    }
    @Test
    public  void  completeWithTransientVariables(){
        Map<String, Object> transientVariables=new HashMap<String, Object>();
        transientVariables.put("a","a");
        transientVariables.put("b","b");
       taskService.complete("15005",null,transientVariables);
    }


}
