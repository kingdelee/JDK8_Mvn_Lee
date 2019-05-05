package com.shareniu.bpmn;

import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.junit.After;
import org.junit.Test;

import java.io.InputStream;
import java.util.Map;

public class ProcessEngineTest {
    ProcessEngine processEngine;

    @Test
    public void testProcessEngine() {
        // 读取的是flowable.cfg.xml
        processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println("流程引擎类：" + processEngine);
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


        Map<Object, Object> beans = processEngineConfiguration.getBeans();
        Object testA = beans.get("testA");
        System.out.println(testA);
        boolean testA1 = beans.containsKey("testA");
        System.out.println("是否包含testA:" + testA1);
        Object dataSource = beans.containsKey("dataSource");
        System.out.println("是否包含dataSource:" + dataSource);
        Object dataSource1 = beans.containsKey("dataSource1");
        System.out.println("是否包含dataSource1:" + dataSource1);
        //不支持
//        Set<Map.Entry<Object, Object>> entries = beans.entrySet();
//        for (Map.Entry<Object, Object> entry :entries){
//            System.out.println(entry.getKey());
//            System.out.println(entry.getValue());
//        }


        ProcessEngine aDefault = ProcessEngines.getProcessEngine("default");
        System.out.println(aDefault);
        //流程引擎类：org.flowable.engine.impl.ProcessEngineImpl@6c67e137
        //          org.flowable.engine.impl.ProcessEngineImpl@6c67e137
    }

    @After
    public void close() {
        processEngine.close();
    }

    /**
     * 手工构造一个流程引擎配置类以及引擎类
     */
    @Test
    public void buildEngine1() {

        ProcessEngineConfiguration standaloneProcessEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        standaloneProcessEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
        standaloneProcessEngineConfiguration.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/shareniu-idm?useUnicode=true&amp;characterEncoding=UTF-8");
        standaloneProcessEngineConfiguration.setJdbcUsername("root");
        standaloneProcessEngineConfiguration.setJdbcPassword("123");
        processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        System.out.println(processEngine);

    }

    @Test
    public void buildEngine2() {
        StandaloneProcessEngineConfiguration standaloneProcessEngineConfiguration = new StandaloneProcessEngineConfiguration();
        standaloneProcessEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
        standaloneProcessEngineConfiguration.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/shareniu-idm?useUnicode=true&amp;characterEncoding=UTF-8");
        standaloneProcessEngineConfiguration.setJdbcUsername("root");
        standaloneProcessEngineConfiguration.setJdbcPassword("123");
        processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        System.out.println(processEngine);

    }

    @Test
    public void buildEngine3() {
        InputStream inputStream = ProcessEngineTest.class.getClassLoader().getResourceAsStream("flowable.cfg.xml2");
        ProcessEngineConfiguration processEngineConfigurationFromInputStream = ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream(inputStream);
        processEngine = processEngineConfigurationFromInputStream.buildProcessEngine();
        System.out.println(processEngine);
    }
    @Test
    public void buildEngine4() {
        InputStream inputStream = ProcessEngineTest.class.getClassLoader().getResourceAsStream("flowable.cfg.xml");
        String beanName="processEngineConfiguration1";
        ProcessEngineConfiguration processEngineConfigurationFromInputStream = ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream(inputStream,beanName);
        processEngine = processEngineConfigurationFromInputStream.buildProcessEngine();
        System.out.println(processEngine);
    }
    @Test
    public void createProcessEngineConfigurationFromResourceDefault1() {
        ProcessEngineConfiguration processEngineConfigurationFromInputStream = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResourceDefault();
        processEngine = processEngineConfigurationFromInputStream.buildProcessEngine();
        System.out.println(processEngine);
    }
    @Test
    public void createProcessEngineConfigurationFromResourceDefault2() {
        String  resource= "flowable.cfg.xml2";
        ProcessEngineConfiguration processEngineConfigurationFromInputStream = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource(resource);
        processEngine = processEngineConfigurationFromInputStream.buildProcessEngine();
        System.out.println(processEngine);
    }
    @Test
    public void createProcessEngineConfigurationFromResourceDefault3() {
        String  resource= "flowable.cfg.xml2";
        String beanName="processEngineConfiguration";
        ProcessEngineConfiguration processEngineConfigurationFromInputStream = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource(resource,beanName);
        processEngine = processEngineConfigurationFromInputStream.buildProcessEngine();
        System.out.println(processEngine);
    }
    @Test
    public void createProcessEngineConfigurationFromResourceDefault4() {
        String  resource= "flowable.cfg.xml2";
        String beanName="processEngineConfiguration";
        ProcessEngineConfiguration processEngineConfigurationFromInputStream = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource(resource,beanName);
        processEngine = processEngineConfigurationFromInputStream.buildProcessEngine();
        System.out.println(processEngine);
        RepositoryService repositoryService = processEngine.getRepositoryService();

        repositoryService.createDeployment()
                .addClasspathResource("Complex_compensation.manualmodif.importInFlowable_NOSHAPE.bpmn20.xml")
                .deploy();
    }
}
