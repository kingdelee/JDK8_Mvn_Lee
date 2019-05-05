package com.shareniu.bpmn;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;

public class LiquiBaseTest {
    public static void main(String[] args) throws SQLException, LiquibaseException {
        ApplicationContext context=new ClassPathXmlApplicationContext("dataBase.xml");

        DruidDataSource ds= (DruidDataSource) context.getBean("dataSource");
        DruidPooledConnection connection = ds.getConnection();
        DatabaseConnection databaseConnection=new JdbcConnection(connection);
        Database db = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(databaseConnection);
        Liquibase liquibase=new Liquibase("flowable-form-db-changelog.xml"
                ,new ClassLoaderResourceAccessor(),db
        );
        liquibase.update("flowable");

    }
}
