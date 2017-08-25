package org.liquidengine.legui.system.renderer.nvg.component;

import static org.liquidengine.legui.system.renderer.nvg.NvgRenderer.renderBorder;
import static org.liquidengine.legui.system.renderer.nvg.util.NVGUtils.rgba;
import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.createScissor;
import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.drawRectStroke;
import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.resetScissor;
import static org.lwjgl.nanovg.NanoVG.NVG_ROUND;
import static org.lwjgl.nanovg.NanoVG.nvgBeginPath;
import static org.lwjgl.nanovg.NanoVG.nvgFill;
import static org.lwjgl.nanovg.NanoVG.nvgFillColor;
import static org.lwjgl.nanovg.NanoVG.nvgLineCap;
import static org.lwjgl.nanovg.NanoVG.nvgLineJoin;
import static org.lwjgl.nanovg.NanoVG.nvgLineTo;
import static org.lwjgl.nanovg.NanoVG.nvgMoveTo;
import static org.lwjgl.nanovg.NanoVG.nvgRoundedRect;
import static org.lwjgl.nanovg.NanoVG.nvgSave;
import static org.lwjgl.nanovg.NanoVG.nvgStroke;
import static org.lwjgl.nanovg.NanoVG.nvgStrokeColor;
import static org.lwjgl.nanovg.NanoVG.nvgStrokeWidth;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.liquidengine.legui.component.Slider;
import org.liquidengine.legui.component.optional.Orientation;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.renderer.nvg.NvgComponentRenderer;
import org.lwjgl.nanovg.NVGColor;

/**
 * Renderer for Slider components.
 */
public class NvgSliderRenderer<T extends Slider> extends NvgComponentRenderer<T> {

    public static final float SLIDER_WIDTH = 4.0f;

    /**
     * Used to render slider component.
     *
     * @param slider slider to render.
     * @param leguiContext context.
     * @param context nanoVG context.
     */
    @Override
    public void renderComponent(T slider, Context leguiContext, long context) {
        createScissor(context, slider);
        {
            nvgSave(context);
            Vector2f pos = slider.getScreenPosition();
            Vector2f size = slider.getSize();
            float x = pos.x;
            float y = pos.y;
            float w = size.x;
            float h = size.y;
            Vector4f backgroundColor = new Vector4f(slider.getBackgroundColor());

            float value = slider.getValue();
            boolean vertical = Orientation.VERTICAL.equals(slider.getOrientation());
            Vector4f sliderInactiveColor = slider.getSliderColor();
            Vector4f sliderColor = slider.getSliderActiveColor();
            float cornerRadius = slider.getCornerRadius();
            float sliderSize = slider.getSliderSize();

            drawBackground(context, x, y, w, h, backgroundColor, cornerRadius);

            float lx,
                rx,
                ty,
                by,
                px,
                py;
            if (vertical) {
                px = lx = rx = x + (w) / 2f;
                ty = y + sliderSize / 2f;
                by = y + h - sliderSize / 2f;
                py = by - (by - ty) * value / 100f;
            } else {
                py = ty = by = y + (h) / 2f;
                lx = x + sliderSize / 2f;
                rx = x + w - sliderSize / 2f;
                px = lx + (rx - lx) * value / 100f;
            }

            // draw inactive color
            drawLine(context, sliderInactiveColor, lx, by, rx, ty, SLIDER_WIDTH);

            // draw active part
            drawLine(context, sliderColor, lx, by, px, py, SLIDER_WIDTH);

            // draw slider button
            float xx = px - sliderSize / 2f;
            float yy = py - sliderSize / 2f;

            drawBackground(context, xx, yy, sliderSize, sliderSize, sliderColor, cornerRadius);

            drawRectStroke(context, xx + 0.5f, yy + 0.5f, sliderSize, sliderSize, sliderInactiveColor, cornerRadius, 1);

            renderBorder(slider, leguiContext);
        }
        resetScissor(context);
    }

    private void drawBackground(long context, float x, float y, float w, float h, Vector4f backgroundColor, float cornerRadius) {
        NVGColor colorA = NVGColor.calloc();
        nvgBeginPath(context);
        nvgRoundedRect(context, x, y, w, h, cornerRadius);
        nvgFillColor(context, rgba(backgroundColor, colorA));
        nvgFill(context);
        colorA.free();
    }

    /**
     * Used to render line.
     *
     * @param context nanoVG context.
     * @param color color to render
     * @param x1 left x
     * @param x2 right x
     * @param y2 top y
     * @param y1 bottom y
     * @param width line width
     */
    private void drawLine(long context, Vector4f color, float x1, float y1, float x2, float y2, float width) {

        NVGColor colorA = NVGColor.calloc();
        nvgLineCap(context, NVG_ROUND);
        nvgLineJoin(context, NVG_ROUND);
        nvgStrokeWidth(context, width);
        nvgStrokeColor(context, rgba(color, colorA));
        nvgBeginPath(context);
        nvgMoveTo(context, x1, y1);
        nvgLineTo(context, x2, y2);
        nvgStroke(context);
        colorA.free();
    }

}