package org.liquidengine.legui.system.renderer.nvg.component;

import static org.liquidengine.legui.system.renderer.nvg.NvgRenderer.renderBorder;
import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.createScissor;
import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.resetScissor;
import static org.lwjgl.nanovg.NanoVG.nvgBeginPath;
import static org.lwjgl.nanovg.NanoVG.nvgFill;
import static org.lwjgl.nanovg.NanoVG.nvgFillColor;
import static org.lwjgl.nanovg.NanoVG.nvgRoundedRect;
import static org.lwjgl.nanovg.NanoVG.nvgSave;

import org.joml.Vector2f;
import org.liquidengine.legui.component.ProgressBar;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.renderer.nvg.NvgComponentRenderer;
import org.liquidengine.legui.system.renderer.nvg.util.NVGUtils;
import org.lwjgl.nanovg.NVGColor;

/**
 * Created by ShchAlexander on 11.02.2017.
 */
public class NvgProgressBarRenderer extends NvgComponentRenderer<ProgressBar> {

    @Override
    public void renderComponent(ProgressBar progressBar, Context leguiContext, long context) {
        createScissor(context, progressBar);
        {
            nvgSave(context);
            Vector2f pos = progressBar.getScreenPosition();
            Vector2f size = progressBar.getSize();

            float cornerRadius = progressBar.getCornerRadius();

            NVGColor colorA = NVGColor.calloc();
            nvgBeginPath(context);
            nvgRoundedRect(context, pos.x, pos.y, size.x, size.y, cornerRadius);
            nvgFillColor(context, NVGUtils.rgba(progressBar.getBackgroundColor(), colorA));
            nvgFill(context);

            nvgBeginPath(context);
            nvgRoundedRect(context, pos.x, pos.y, size.x * progressBar.getValue() / ProgressBar.MAX_VALUE, size.y, cornerRadius);
            nvgFillColor(context, NVGUtils.rgba(progressBar.getProgressColor(), colorA));
            nvgFill(context);
            colorA.free();

            renderBorder(progressBar, leguiContext);

        }
        resetScissor(context);
    }
}