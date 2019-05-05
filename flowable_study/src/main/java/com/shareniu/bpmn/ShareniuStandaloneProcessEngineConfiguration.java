package com.shareniu.bpmn;

import org.flowable.common.engine.impl.interceptor.CommandInterceptor;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;

public class ShareniuStandaloneProcessEngineConfiguration extends StandaloneProcessEngineConfiguration {

    @Override
    public void init() {
        System.out.println("ShareniuStandaloneProcessEngineConfiguration:init方法");
        super.init();
    }

    @Override
    protected void initDataSource() {
        super.initDataSource();
    }
}
