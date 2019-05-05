package com.shareniu.bpmn.ch9;

import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.runtime.DataObject;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.idm.engine.impl.persistence.entity.GroupEntity;
import org.flowable.idm.engine.impl.persistence.entity.GroupEntityImpl;
import org.flowable.idm.engine.impl.persistence.entity.UserEntity;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public void deploy() {
        DeploymentBuilder deploymentBuilder = repositoryService
                .createDeployment()
                .category("个人任务")
                .name("singletask")

               // .addClasspathResource("接受任务测试.bpmn20.xml");
                //.addClasspathResource("个人任务测试3.bpmn20.xml");
                .addClasspathResource("组任务测试4.bpmn20.xml");
        //.addClasspathResource("dataobject.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();
        System.out.println(deploy.getId());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstanceByKey() {
        Map<String,Object> vars=new HashMap<String, Object>();
        vars.put("userIds","分享牛4,分享牛5,分享牛6");
        //String processDefinitionKey = "singletask";
        String processDefinitionKey = "grouptask";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        System.out.println(processInstance.getId() + "," + processInstance.getActivityId());
    }

    /**
     * 查询执行实例
     * <p>
     * select distinct RES.* , P.KEY_ as ProcessDefinitionKey, P.ID_ as ProcessDefinitionId
     * from ACT_RU_EXECUTION RES
     * inner join ACT_RE_PROCDEF P
     * on RES.PROC_DEF_ID_ = P.ID_
     * WHERE RES.ACT_ID_ = ? and RES.IS_ACTIVE_ = ? order by RES.ID_ asc
     * <p>
     * sid-43D1D00A-A9A6-4D4E-88CF-7CA2CCC359FD(String), true(Boolean)
     */
    @Test
    public void activityId() {
        String activityId = "sid-88B3ACC4-E163-41F4-8B1B-31F4978ED9D8";
        Execution execution = runtimeService.createExecutionQuery().activityId(activityId).singleResult();

        System.out.println(execution.getId());
    }

    /**
     * 触发执行实例继续往下运转
     */
    @Test
    public void trigger() {
        String executionId = "102002";
        runtimeService.trigger(executionId);
    }

    /**
     *
     * 查询指定人的待办任务
     * select distinct RES.* from ACT_RU_TASK RES
     * WHERE RES.ASSIGNEE_ = ? order by RES.ID_ asc
     */
    @Test
    public void findMyTask() {
        String assignee="王五";
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .list();
        for (Task task : tasks){
            System.out.println(task.getId());
            System.out.println(task.getName());
            System.out.println(task.getCreateTime());

        }
    }

    /**
     *
     *  select distinct RES.* from ACT_RU_TASK
     *  RES WHERE RES.ASSIGNEE_ is null
     *  and exists(
     *  select LINK.ID_ from ACT_RU_IDENTITYLINK LINK where LINK.TYPE_ = 'candidate'
     *  and LINK.TASK_ID_ = RES.ID_ and ( LINK.USER_ID_ = ? ) ) order by RES.ID_ asc
     *
     *
     *  select distinct RES.* from ACT_RU_TASK RES W
     *  HERE RES.ASSIGNEE_ is null
     *  and exists(
     *  select LINK.ID_ from ACT_RU_IDENTITYLINK LINK where LINK.TYPE_ = 'candidate'
     *  and LINK.TASK_ID_ = RES.ID_ and ( LINK.USER_ID_ = ? or LINK.GROUP_ID_ IN ( ? ) ) )
     *  order by RES.ID_ asc
     *  王五(String), 总经理(String)
     * 查询组任务
     */
    @Test
    public void findGroupTask() {
        String userId="张三";
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateUser(userId)
                .list();
        for (Task task : tasks){
            System.out.println(task.getId());
            System.out.println(task.getName());
            System.out.println(task.getCreateTime());

        }
    }
    @Test
    public void taskCandidateGroup() {
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateGroup("总经理")
                .list();
        for (Task task : tasks){
            System.out.println(task.getId());
            System.out.println(task.getName());
            System.out.println(task.getCreateTime());

        }
    }

    /**
     * select * from ACT_RU_IDENTITYLINK where TASK_ID_ = ?
     * 查询运行任务的处理人
     */
    @Test
    public void findGroupuser() {
        String taskId="40006";
        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(taskId);
        for (IdentityLink identityLink : identityLinksForTask){
            System.out.println("#####################");
            System.out.println(identityLink.getProcessDefinitionId());
            System.out.println(identityLink.getGroupId());
            System.out.println(identityLink.getUserId());
            System.out.println(identityLink.getTaskId());
            System.out.println("#####################");
        }
    }

    /**
     * 查询历史任务的处理人
     * select * from ACT_HI_IDENTITYLINK where TASK_ID_ = ?
     */
    @Test
    public void findHisGroupuser() {
        String taskId="40006";
        List<HistoricIdentityLink> identityLinksForTask = historyService.getHistoricIdentityLinksForTask(taskId);
        for (HistoricIdentityLink identityLink : identityLinksForTask){
            System.out.println("#####################");
            System.out.println(identityLink.getGroupId());
            System.out.println(identityLink.getUserId());
            System.out.println(identityLink.getTaskId());
            System.out.println("#####################");
        }
    }

    /**
     * anceEntityImpl.updateHistoricActivityInstance  - ==>  Preparing: update ACT_HI_ACTINST SET REV_ = ?, PROC_DEF_ID_ = ?, ASSIGNEE_ = ? where ID_ = ? and REV_ = ?
     * 02:32:57,856 [main] DEBUG org.flowable.engine.impl.persistence.entity.HistoricActivityInstanceEntityImpl.updateHistoricActivityInstance  - ==> Parameters: 2(Integer), grouptask:1:27504(String), 分享牛1(String), 30004(String), 1(Integer)
     * 02:32:57,857 [main] DEBUG org.flowable.engine.impl.persistence.entity.HistoricActivityInstanceEntityImpl.updateHistoricActivityInstance  - <==    Updates: 1
     * 02:32:57,859 [main] DEBUG org.flowable.task.service.impl.persistence.entity.HistoricTaskInstanceEntityImpl.updateHistoricTaskInstance  - ==>  Preparing: update ACT_HI_TASKINST SET REV_ = ?, ASSIGNEE_ = ?, CLAIM_TIME_ = ?, LAST_UPDATED_TIME_ = ? where ID_ = ? and REV_ = ?
     * 02:32:57,860 [main] DEBUG org.flowable.task.service.impl.persistence.entity.HistoricTaskInstanceEntityImpl.updateHistoricTaskInstance  - ==> Parameters: 2(Integer), 分享牛1(String), 2018-11-11 14:32:57.777(Timestamp), 2018-11-11 14:32:57.782(Timestamp), 30005(String), 1(Integer)
     * 02:32:57,861 [main] DEBUG org.flowable.task.service.impl.persistence.entity.HistoricTaskInstanceEntityImpl.updateHistoricTaskInstance  - <==    Updates: 1
     * 任务的认领/拾取
     */
    @Test
    public void claim() {
        String taskId="62505";
        String userId="分享牛8";
        taskService.claim(taskId,userId);
    }
    @Test
    public void complete() {
        String taskId="55005";
        taskService.complete(taskId);
    }
    @Test
    public void setAssigneTask() {
        String taskId="62505";
        String userId="张翠山";
        taskService.setAssignee(taskId,null);
    }
    @Test
    public void addCandidateUser() {
        String taskId="62505";
        String userId="分享牛";
        taskService.addCandidateUser(taskId,userId);
    }
    @Test
    public void deleteCandidateUser() {
        String taskId="62505";
        String userId="分享牛";
        taskService.deleteCandidateUser(taskId,userId);
    }
    @Test
    public void init() {
        GroupEntityImpl groupEntity1 = new GroupEntityImpl();
        groupEntity1.setRevision(0);
        groupEntity1.setName("部门经理");
        groupEntity1.setId("部门经理");

        identityService.saveGroup(groupEntity1);//建立组
        GroupEntityImpl groupEntity2 = new GroupEntityImpl();
        groupEntity2.setRevision(0);
        groupEntity2.setName("总经理");
        groupEntity2.setId("总经理");
        identityService.saveGroup(groupEntity2);//建立组

        UserEntityImpl userEntity1 = new UserEntityImpl();
        userEntity1.setRevision(0);
        userEntity1.setId("张三");
        identityService.saveUser(userEntity1);
        UserEntityImpl userEntity2 = new UserEntityImpl();
        userEntity2.setRevision(0);
        userEntity2.setId("李四");
        identityService.saveUser(userEntity2);
        UserEntityImpl userEntity3 = new UserEntityImpl();
        userEntity3.setRevision(0);
        userEntity3.setId("王五");
        identityService.saveUser(userEntity3);
        identityService.createMembership("张三", "部门经理");//建立组和用户关系
        identityService.createMembership("李四", "部门经理");//建立组和用户关系
        identityService.createMembership("王五", "总经理");//建立组和用户关系

    }


}
