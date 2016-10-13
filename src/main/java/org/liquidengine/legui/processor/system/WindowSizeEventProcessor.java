package org.liquidengine.legui.processor.system;


import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.context.LeguiContext;
import org.liquidengine.legui.event.system.SystemWindowSizeEvent;

/**
 * Created by Alexander on 01.07.2016.
 */
public class WindowSizeEventProcessor extends SystemEventProcessor<SystemWindowSizeEvent> {

    public WindowSizeEventProcessor(LeguiContext context) {
        super(context);
    }

    @Override
    public void processEvent(SystemWindowSizeEvent event, Component mainGui) {
        Component target = context.getMouseTargetGui();
        if (target != null) {
            target.getProcessors().getWindowSizeEventProcessor().process(target, event, context);
        } else {
            mainGui.getProcessors().getWindowSizeEventProcessor().process(target, event, context);
        }
    }
}