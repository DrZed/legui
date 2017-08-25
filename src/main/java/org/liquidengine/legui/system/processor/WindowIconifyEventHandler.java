package org.liquidengine.legui.system.processor;

import java.util.List;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.Container;
import org.liquidengine.legui.component.Frame;
import org.liquidengine.legui.component.Layer;
import org.liquidengine.legui.event.WindowIconifyEvent;
import org.liquidengine.legui.listener.EventProcessor;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.event.SystemWindowIconifyEvent;

/**
 * Created by ShchAlexander on 03.02.2017.
 */
public class WindowIconifyEventHandler extends AbstractSystemEventHandler<SystemWindowIconifyEvent> {

    @Override
    protected boolean handle(SystemWindowIconifyEvent event, Layer layer, Context context, Frame frame) {
        pushEvent(layer.getContainer(), event, context, frame);
        return false;
    }


    private void pushEvent(Component component, SystemWindowIconifyEvent event, Context context, Frame frame) {
        if (!(component.isVisible())) {
            return;
        }
        EventProcessor.getInstance().pushEvent(new WindowIconifyEvent(component, context, frame, event.iconified));
        if (component instanceof Container) {
            List<Component> childs = ((Container) component).getChilds();
            for (Component child : childs) {
                pushEvent(child, event, context, frame);
            }
        }
    }
}