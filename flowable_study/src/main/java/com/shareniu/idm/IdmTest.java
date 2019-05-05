package com.shareniu.idm;

import org.flowable.common.engine.api.management.TableMetaData;
import org.flowable.common.engine.api.management.TablePage;
import org.flowable.idm.api.*;
import org.flowable.idm.engine.IdmEngine;
import org.flowable.idm.engine.IdmEngineConfiguration;
import org.flowable.idm.engine.impl.persistence.entity.GroupEntityImpl;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IdmTest {

    IdmEngine idmEngine;
    IdmIdentityService idmIdentityService;
    IdmEngineConfiguration idmEngineConfiguration;
    IdmManagementService idmManagementService;
    @Before
    public void init() {
        InputStream inputStream = IdmTest.class.getClassLoader().getResourceAsStream("flowable.idm.cfg.xml");
        idmEngine =
            IdmEngineConfiguration.createIdmEngineConfigurationFromInputStream(inputStream).buildIdmEngine();
        idmIdentityService =
            idmEngine.getIdmIdentityService();
        idmEngineConfiguration = idmEngine.getIdmEngineConfiguration();
        idmManagementService =
            idmEngine.getIdmManagementService();
        String name = idmEngine.getName();
        System.out.println("引擎的名稱：" + name);

    }

    @Test
    public void testInit() {

    }
    @Test
    public void testAddUser() {
        UserEntityImpl user = new UserEntityImpl();
        user.setEmail("shareniuc@qq.com");
        user.setId("shareniu-c");
        user.setPassword("1");
        user.setRevision(0);
        idmIdentityService.saveUser(user);
    }
    @Test
    public void saveGroup() {
        GroupEntityImpl group = new GroupEntityImpl();
        group.setId("研发部");
        group.setName("研发部");
        group.setRevision(0);
        idmIdentityService.saveGroup(group);
    }
    @Test
    public void createMembership() {
        String userId = "shareniu-b";
        String groupId = "研发部";
        idmIdentityService.createMembership(userId, groupId);
    }
    @Test
    public void createMembership2() {
        String userId = "shareniu-c";
        String groupId = "研发部";
        idmIdentityService.createMembership(userId, groupId);
    }
    @Test
    public void createUserQuery() {
        List<User> list = idmIdentityService.createUserQuery().list();
        for (User u : list) {
            System.out.println(u.getId());
            System.out.println(u.getPassword());
        }
    }
    @Test
    public void createGroupQuery() {
        List<Group> list = idmIdentityService.createGroupQuery().list();
        for (Group u : list) {
            System.out.println(u.getId());
            System.out.println(u.getName());
        }
    }
    @Test
    public void createPrivilegeQuery() {
        List<Privilege> list = idmIdentityService.createPrivilegeQuery().list();
        for (Privilege u : list) {
            System.out.println(u.getId());
            System.out.println(u.getName());
        }
    }
    @Test
    public void addUserPrivilegeMapping() {
        String privilegeId = "52a2a4ed-6efe-11e9-8edb-001c42000009";
        String userId = "shareniu-b";
        idmIdentityService.addUserPrivilegeMapping(privilegeId, userId);
    }
    @Test
    public void addGroupPrivilegeMapping() {
        String privilegeId = "52a2a4ed-6efe-11e9-8edb-001c42000009";
        String groupId = "研发部";
        idmIdentityService.addGroupPrivilegeMapping(privilegeId, groupId);
    }
    @Test
    public void getTableCount() {
        Map<String, Long> tableCount = idmManagementService.getTableCount();
        Set<Map.Entry<String, Long>> entries = tableCount.entrySet();
        for (Map.Entry<String, Long> entry : entries) {
            System.out.println("key:" + entry.getKey());
            System.out.println("value:" + entry.getValue());
        }
    }
    @Test
    public void getTableName() {
        String tableCount = idmManagementService.getTableName(User.class);
        System.out.println(tableCount);
    }
    @Test
    public void getTableName2() {
        String tableCount = idmManagementService.getTableName(Group.class);
        System.out.println(tableCount);
    }
    @Test
    public void getTableMetaData() {
        TableMetaData tableCount = idmManagementService.getTableMetaData("ACT_ID_GROUP");
        System.out.println(tableCount.getTableName());
        System.out.println(tableCount.getColumnNames());
        System.out.println(tableCount.getColumnTypes());
    }

    /**
     * TODO 框架bug 后续解决
     */
    @Test
    public void getProperties() {
        Map<String, String> properties = idmManagementService.getProperties();
        Set<Map.Entry<String, String>> entries = properties.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println("key:" + entry.getKey());
            System.out.println("val:" + entry.getValue());
        }
    }

    @Test
    public void createTablePageQuery() {
        TablePage tablePage = idmManagementService.createTablePageQuery()
            .tableName("ACT_ID_GROUP")
            .listPage(0, 10);
        System.out.println(tablePage.getRows());
        System.out.println(tablePage.getSize());
        System.out.println(tablePage.getTableName());
    }

}
