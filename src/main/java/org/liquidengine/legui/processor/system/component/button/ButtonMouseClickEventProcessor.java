package org.liquidengine.legui.processor.system.component.button;

import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.context.LeguiContext;
import org.liquidengine.legui.event.system.SystemMouseClickEvent;
import org.liquidengine.legui.processor.system.component.LeguiComponentEventProcessor;

/**
 * Created by Shcherbin Alexander on 9/30/2016.
 */
public class ButtonMouseClickEventProcessor implements LeguiComponentEventProcessor<Button,SystemMouseClickEvent> {
    @Override
    public void process(Button button, SystemMouseClickEvent event, LeguiContext leguiContext) {
    }
}