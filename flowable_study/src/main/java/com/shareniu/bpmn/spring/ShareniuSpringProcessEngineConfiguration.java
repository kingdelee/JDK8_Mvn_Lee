package com.shareniu.bpmn.spring;

import org.flowable.spring.SpringProcessEngineConfiguration;

public class ShareniuSpringProcessEngineConfiguration extends SpringProcessEngineConfiguration {
    @Override
    public void init() {
        System.out.println("ShareniuSpringProcessEngineConfiguration:init");
        super.init();
    }

    @Override
    public void initClock() {
        super.initClock();
    }
}
