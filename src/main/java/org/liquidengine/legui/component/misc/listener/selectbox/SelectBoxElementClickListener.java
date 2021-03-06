package org.liquidengine.legui.component.misc.listener.selectbox;

import static org.liquidengine.legui.event.MouseClickEvent.MouseClickAction.CLICK;

import org.liquidengine.legui.component.SelectBox;
import org.liquidengine.legui.component.event.selectbox.SelectBoxChangeSelectionEvent;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.input.Mouse;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.listener.processor.EventProcessor;

/**
 * @author ShchAlexander.
 */
public class SelectBoxElementClickListener implements MouseClickEventListener {

    private SelectBox selectBox;

    public SelectBoxElementClickListener(SelectBox selectBox) {
        this.selectBox = selectBox;
    }

    @Override
    public void process(MouseClickEvent event) {
        SelectBox.SelectBoxElement component = (SelectBox.SelectBoxElement) event.getTargetComponent();
        if (event.getAction() == CLICK && event.getButton().equals(Mouse.MouseButton.MOUSE_BUTTON_1)) {
            String selection = selectBox.getSelection();
            String newValue = component.getText();
            selectBox.setSelected(newValue, true);
            EventProcessor.getInstance().pushEvent(new SelectBoxChangeSelectionEvent(selectBox, event.getContext(), event.getFrame(), selection, newValue));
            selectBox.setCollapsed(true);
            event.getFrame().removeLayer(selectBox.getSelectBoxLayer());
        }
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && ((obj == this) || ((obj != this) && (obj.getClass() == this.getClass())));
    }
}
