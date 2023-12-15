package com.nyu.java.reminderui.frames;

import org.springframework.context.ApplicationEvent;

public class SwitchFrameEvent extends ApplicationEvent {
    private final String frameName;

    public SwitchFrameEvent(Object source, String frameName) {
        super(source);
        this.frameName = frameName;
    }

    public String getFrameName() {
        return frameName;
    }
}
