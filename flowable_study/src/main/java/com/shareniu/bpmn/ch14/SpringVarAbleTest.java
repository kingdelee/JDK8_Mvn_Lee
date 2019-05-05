package com.shareniu.bpmn.ch14;

import org.apache.commons.io.FileUtils;
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
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.form.DateFormType;
import org.flowable.engine.impl.form.EnumFormType;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.TaskServiceImpl;
import org.flowable.task.service.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 图片相关的操作
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

    /**
     *  public InputStream generateDiagram
     *  (BpmnModel bpmnModel, String imageType,
     *  List<String> highLightedActivities,
     *  List<String> highLightedFlows,
     *             String activityFontName,
     *             String labelFontName,
     *             String annotationFontName,
     *             ClassLoader customClassLoader,
     *             double scaleFactor,
     *             boolean drawSequenceFlowNameWithNoLabelDI);
     */
    @Test
    public  void generateDiagram() throws IOException {
        String processDefinitionId="formkey2:1:7";
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        String imageType="PNG";
        List<String> highLightedActivities=new ArrayList<String>();
        List<String> highLightedFlows=new ArrayList<String>();
        String activityFontName="宋体";
        String labelFontName="宋体";
        String annotationFontName="宋体";
        ClassLoader customClassLoader=null;
        double scaleFactor=1.0D;
        boolean drawSequenceFlowNameWithNoLabelDI=true;
        ProcessDiagramGenerator processDiagramGenerator=new DefaultProcessDiagramGenerator();
        InputStream inputStream = processDiagramGenerator.generateDiagram(bpmnModel, imageType, highLightedActivities, highLightedFlows
                , activityFontName, labelFontName, annotationFontName, customClassLoader, scaleFactor, drawSequenceFlowNameWithNoLabelDI);

        FileUtils.copyInputStreamToFile(inputStream,new File("/Users/shareniu/Downloads/"+"1.png"));
    }
    @Test
    public  void generateHighLightedActivitiesDiagram() throws IOException {
        String processDefinitionId="formkey2:1:7";
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        String imageType="PNG";
        List<String> highLightedActivities=new ArrayList<String>();
        highLightedActivities.add("sid-03FFE656-10E2-4E8A-A92B-1EA78ED614B4");
        highLightedActivities.add("startEvent1");
        highLightedActivities.add("sid-30100D78-929C-45A7-9C06-1589C50E7E19");
        List<String> highLightedFlows=new ArrayList<String>();
        highLightedFlows.add("sid-4152E673-0E8F-437B-8DCB-D35A664F6201");
        String activityFontName="宋体";
        String labelFontName="宋体";
        String annotationFontName="宋体";
        ClassLoader customClassLoader=null;
        double scaleFactor=1.0D;
        boolean drawSequenceFlowNameWithNoLabelDI=true;
        ShareniuDefaultProcessDiagramGenerator processDiagramGenerator=new ShareniuDefaultProcessDiagramGenerator();
        InputStream inputStream = processDiagramGenerator.generateDiagram(bpmnModel, imageType, highLightedActivities, highLightedFlows
                , activityFontName, labelFontName, annotationFontName, customClassLoader, scaleFactor, drawSequenceFlowNameWithNoLabelDI);

        FileUtils.copyInputStreamToFile(inputStream,new File("/Users/shareniu/Downloads/"+"1.png"));
    }

    /**
     * select RES.*, VAR.ID_ as VAR_ID_, VAR.NAME_ as VAR_NAME_, VAR.TYPE_ as VAR_TYPE_, VAR.REV_ as VAR_REV_,
     * VAR.PROC_INST_ID_ as VAR_PROC_INST_ID_, VAR.EXECUTION_ID_ as VAR_EXECUTION_ID_, VAR.TASK_ID_ as VAR_TASK_ID_,
     * VAR.BYTEARRAY_ID_ as VAR_BYTEARRAY_ID_, VAR.DOUBLE_ as VAR_DOUBLE_, VAR.TEXT_ as VAR_TEXT_, VAR.TEXT2_
     * as VAR_TEXT2_, VAR.LONG_ as VAR_LONG_ from ACT_RU_TASK RES left outer join ACT_RU_VARIABLE VAR
     * ON RES.PROC_INST_ID_ = VAR.EXECUTION_ID_ and VAR.TASK_ID_ is null
     * WHERE (RES.ASSIGNEE_ = ? or ( RES.ASSIGNEE_ is null and exists(select LINK.ID_ from ACT_RU_IDENTITYLINK LINK
     * where LINK.TASK_ID_ = RES.ID_ and LINK.TYPE_ = 'candidate' and (LINK.USER_ID_ = ? ))))
     * and ( (RES.ASSIGNEE_ = ? or ( RES.ASSIGNEE_ is null and
     * exists(select LINK.ID_ from ACT_RU_IDENTITYLINK LINK where LINK.TASK_ID_ = RES.ID_ and LINK.TYPE_ = 'candidate' and (LINK.USER_ID_ = ? )))) ) order by RES.ID_ asc LIMIT ? OFFSET ?
     * @throws IOException
     */
    @Test
    public  void getProcessDiagramGenerator() throws IOException {
        ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator processDiagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        System.out.println(processDiagramGenerator);

    }
    @Test
    public  void includeProcessVariables() throws IOException {
        List<Task> taskList = (taskService).createTaskQuery()
                .taskCandidateOrAssigned("zhangsan").includeProcessVariables().or()
                .taskCandidateOrAssigned("lisi") .includeProcessVariables().endOr()
                .taskCandidateOrAssigned("wangwu").includeProcessVariables().list();


    }

}


