package com.shareniu.bpmn.spring;

import org.flowable.engine.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:flowable-context.xml")
public class SpringTest {

    @Test
    public  void  buildProcessEngine(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(processEngine);
        RepositoryService repositoryService = processEngine.getRepositoryService();
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
        RuntimeService runtimeService = processEngine.getRuntimeService();
        System.out.println(runtimeService);
        TaskService taskService = processEngine.getTaskService();
        System.out.println(taskService);


    }
}
