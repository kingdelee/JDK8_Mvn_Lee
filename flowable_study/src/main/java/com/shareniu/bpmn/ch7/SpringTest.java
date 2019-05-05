package com.shareniu.bpmn.ch7;

import org.apache.commons.io.FileUtils;
import org.flowable.common.engine.impl.util.IoUtil;
import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:flowable-context.xml")
public class SpringTest {
    ProcessEngine processEngine;
    RepositoryService repositoryService;
    @Before
    public  void  buildProcessEngine(){
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

    @Test
    public  void  DeploymentBuild(){
        DeploymentBuilder deploymentBuilder = repositoryService
                .createDeployment()
                .category("测试分类")
                .name("名称");
        System.out.println(deploymentBuilder);
    }
    @Test
    public  void  deploy(){
        DeploymentBuilder deploymentBuilder = repositoryService
                .createDeployment()
                .category("测试分类")
                .name("名称")
                .addClasspathResource("Complex_compensation.manualmodif.importInFlowable_NOSHAPE.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();
        System.out.println(deploy.getId());
    }

    /**
     * 文本方式部署
     * 资源的名称必须是String[] { "bpmn20.xml", "bpmn" }; 结尾的才可以部署到流程定义表
     */
    @Test
    public  void  addString(){
        String text= IoUtil.readFileAsString("Complex_compensation.manualmodif.importInFlowable_NOSHAPE.bpmn20.xml");
        DeploymentBuilder deploymentBuilder = repositoryService
                .createDeployment()
                .category("测试分类")
                .name("名称")
                .key("测试的key")
                .addString("shareniu.bpmn1",text);
        Deployment deploy = deploymentBuilder.deploy();
        System.out.println(deploy.getId());
    }
    @Test
    public  void  addInputStream(){
        InputStream resourceAsStream = SpringTest.class.getClassLoader()
                .getResourceAsStream("Complex_compensation.manualmodif.importInFlowable_NOSHAPE.bpmn20.xml");
        DeploymentBuilder deploymentBuilder = repositoryService
                .createDeployment()
                .category("测试分类")
                .name("名称")
                .key("测试的key")
                .addInputStream("shareniu.bpmn",resourceAsStream);
        Deployment deploy = deploymentBuilder.deploy();
        System.out.println(deploy.getId());
    }
    @Test
    public  void  addZipInputStream(){
        InputStream resourceAsStream = SpringTest.class.getClassLoader()
                .getResourceAsStream("1.zip");

        ZipInputStream zipInputStream=new ZipInputStream(resourceAsStream);
        DeploymentBuilder deploymentBuilder = repositoryService
                .createDeployment()
                .category("测试分类")
                .name("名称")
                .key("测试的key")
               .addZipInputStream(zipInputStream);
        Deployment deploy = deploymentBuilder.deploy();
        System.out.println(deploy.getId());
    }
    @Test
    public  void  addBytes(){
        InputStream resourceAsStream = SpringTest.class.getClassLoader()
                .getResourceAsStream("Complex_compensation.manualmodif.importInFlowable_NOSHAPE.bpmn20.xml");
        String inputStreamName="分享牛";
        byte[] bytes = IoUtil.readInputStream(resourceAsStream, inputStreamName);
        DeploymentBuilder deploymentBuilder = repositoryService
                .createDeployment()
                .category("测试分类")
                .name("名称")
                .key("测试的key")
              .addBytes("shareniu.bpmn",bytes);
        Deployment deploy = deploymentBuilder.deploy();
        System.out.println(deploy.getId());
    }
    @Test
    public  void  createProcessDefinitionQuery(){
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .latestVersion() //查询的时候最后的一个版本
                .processDefinitionName("Complex_compensation")
               // .processDefinitionCategory("")
                .list();
        for (ProcessDefinition pd:list){
            System.out.println("###################");
            System.out.println(pd.getId());
            System.out.println(pd.getCategory());
            System.out.println(pd.getDeploymentId());
        }
    }
    @Test
    public  void  deleteDeployment(){
        String deploymentId="42501";
       repositoryService.deleteDeployment(deploymentId);
    }

    /**
     * 级联删除会删除当前流程定义下面所有的流程实例
     */
    @Test
    public  void  deleteDeploymentCaseCade(){
        String deploymentId="40001";
       repositoryService.deleteDeployment(deploymentId,true);
    }
    @Test
    public  void  viewImage() throws IOException {
        String deploymentId="37501";
        List<String> deploymentResourceNames = repositoryService
                .getDeploymentResourceNames(deploymentId);

        System.out.println(deploymentResourceNames);

        String imageName=null;
        for (String name : deploymentResourceNames){
            if (name.indexOf(".png")>0){
                imageName=name;
            }
        }
        System.out.println(imageName);
        if (imageName!=null){
            File file=new File("~/Downloads/idm-study/"+imageName);
            InputStream resourceAsStream = repositoryService.getResourceAsStream(deploymentId, imageName);
            FileUtils.copyInputStreamToFile(resourceAsStream,file);
        }


    }

    /**
     * select RES.* from ACT_RE_DEPLOYMENT RES WHERE RES.ID_ = ? and RES.CATEGORY_ = ? order by RES.ID_ asc
     */
    @Test
    public  void  createDeploymentQuery(){
        List<Deployment> list = repositoryService.createDeploymentQuery()
                .deploymentCategory("测试分类")
                .deploymentId("37501")
                .list();
        for(Deployment deployment :list){
            System.out.println("###################");
            System.out.println(deployment.getId());
            System.out.println(deployment.getKey());
        }
    }
    @Test
    public  void  createNativeDeploymentQuery(){
        List<Deployment> list = repositoryService.createNativeDeploymentQuery()
                .sql("select * from ACT_RE_DEPLOYMENT").list();

        for(Deployment deployment :list){
            System.out.println("###################");
            System.out.println(deployment.getId());
            System.out.println(deployment.getKey());
        }
    }
}
