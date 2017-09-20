package org.liquidengine.legui.system.renderer.nvg.icon;

import static org.lwjgl.nanovg.NanoVG.nvgBeginPath;
import static org.lwjgl.nanovg.NanoVG.nvgFill;
import static org.lwjgl.nanovg.NanoVG.nvgFillColor;
import static org.lwjgl.nanovg.NanoVG.nvgRect;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.liquidengine.legui.color.ColorConstants;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.icon.Icon;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.renderer.nvg.NvgIconRenderer;
import org.liquidengine.legui.system.renderer.nvg.util.NvgShapes;
import org.lwjgl.nanovg.NVGColor;

/**
 * Created by ShchAlexander on 11.02.2017.
 */
public class NvgDefaultIconRenderer extends NvgIconRenderer {

    @Override
    protected void renderIcon(Icon icon, Component component, Context context, long nanovg) {
        if (!component.isVisible()) {
            return;
        }
        // render simple rectangle border
        Vector2f position = component.getScreenPosition();
        Vector2f size = component.getSize();
        Vector2f iconSize = icon.getSize();

        float x = position.x;
        float y = position.y;
        if (icon.getPosition() == null) {
            x += icon.getHorizontalAlign().index * (size.x - iconSize.x) / 2f;
            y += icon.getVerticalAlign().index * (size.y - iconSize.y) / 2f;
        } else {
            x += icon.getPosition().x;
            y += icon.getPosition().y;
        }
        float w = iconSize.x;
        float h = iconSize.y;

        NvgShapes.drawRect(nanovg, new Vector4f(x, y, w, h), ColorConstants.red, component.getCornerRadius());
        NvgShapes.drawRectStroke(nanovg, new Vector4f(x, y, w, h), ColorConstants.black, 1, component.getCornerRadius());
    }
}
