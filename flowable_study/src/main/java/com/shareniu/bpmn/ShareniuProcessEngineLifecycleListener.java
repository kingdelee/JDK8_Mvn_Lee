package com.shareniu.bpmn;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineLifecycleListener;

public class ShareniuProcessEngineLifecycleListener implements ProcessEngineLifecycleListener {
    /**
     * 监听引擎启动
     * @param processEngine
     */
    public void onProcessEngineBuilt(ProcessEngine processEngine) {
        System.out.println("ShareniuProcessEngineLifecycleListener:onProcessEngineBuilt:"+processEngine);
    }

    /**
     * 监听引擎关闭
     * @param processEngine
     */
    public void onProcessEngineClosed(ProcessEngine processEngine) {
        System.out.println("ShareniuProcessEngineLifecycleListener:onProcessEngineClosed:"+processEngine);
    }
}
