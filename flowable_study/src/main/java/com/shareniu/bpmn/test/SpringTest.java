package com.shareniu.bpmn.test;

import org.flowable.engine.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:flowable-context.xml")
public class SpringTest {
    ProcessEngine processEngine;
    RepositoryService repositoryService;
    RuntimeService runtimeService;
    @Before
    public  void  buildProcessEngine(){
        processEngine= ProcessEngines.getDefaultProcessEngine();
        System.out.println(processEngine);
         repositoryService = processEngine.getRepositoryService();
        System.out.println(repositoryService);
        String name = processEngine.getName();
        System.out.println("流程引擎的名称：" + name);
        DynamicBpmnService dynamicBpmnService = processEngine.getDynamicBpmnService();
        System.out.println(dynamicBpmnService);
        FormService formService = processEngine.getFormService();
        System.out.println(formService);

        HistoryService historyService = processEngine.getHistoryService();
        System.out.println(historyService);
        IdentityService identityService = processEngine.getIdentityService();
        System.out.println(identityService);
        ManagementService managementService = processEngine.getManagementService();
        System.out.println(managementService);
        ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        System.out.println(processEngineConfiguration);
         runtimeService = processEngine.getRuntimeService();
        System.out.println(runtimeService);
        TaskService taskService = processEngine.getTaskService();
        System.out.println(taskService);
    }

    @Test
    public  void  deploy(){
        repositoryService.createDeployment()
                .addClasspathResource("在线支撑流程.bpmn20.xml").deploy();
    }
    @Test
    public  void  startProcessInstanceByKey(){
        Map<String,Object> vars=new HashMap<String, Object>();
        String[]v={"shareniu1","shareniu2","shareniu3","shareniu4"};

        vars.put("supportUserList",  Arrays.asList(v));
        runtimeService.startProcessInstanceByKey("support_online",vars);
    }
    @Test
    public  void  signalEventReceived(){
       runtimeService.signalEventReceived("申请专家协助");
    }
}
