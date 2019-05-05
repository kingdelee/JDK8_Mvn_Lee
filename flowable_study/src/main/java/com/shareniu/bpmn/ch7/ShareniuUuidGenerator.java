package com.shareniu.bpmn.ch7;

import org.flowable.common.engine.impl.cfg.IdGenerator;

import java.util.UUID;

public class ShareniuUuidGenerator implements IdGenerator {
    public String getNextId() {
        return "shareniu_"+UUID.randomUUID().toString();
    }
}
