package org.liquidengine.legui.processor;

import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.event.LeguiEvent;
import org.liquidengine.legui.listener.LeguiEventListener;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Shcherbin Alexander on 10/5/2016.
 */
public class LeguiEventListenerProcessor {

    private Queue<LeguiEvent> componentEvents = new ConcurrentLinkedQueue<>();

    public LeguiEventListenerProcessor() {
    }

    public void processEvent() {
        LeguiEvent event = componentEvents.poll();
        if (event != null) {
            Component component = event.getComponent();
            List<? extends LeguiEventListener> listenersByEvent = component.getLeguiEventListeners().getListeners(event.getClass());
            for (LeguiEventListener LeguiEventListener : listenersByEvent) {
                LeguiEventListener.update(event);
            }
        }
    }

    public void pushEvent(LeguiEvent event) {
        componentEvents.add(event);
    }
}