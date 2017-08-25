package org.liquidengine.legui.system.renderer.nvg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.liquidengine.legui.border.Border;
import org.liquidengine.legui.border.SimpleLineBorder;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.CheckBox;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.Container;
import org.liquidengine.legui.component.ImageView;
import org.liquidengine.legui.component.Label;
import org.liquidengine.legui.component.PasswordInput;
import org.liquidengine.legui.component.ProgressBar;
import org.liquidengine.legui.component.RadioButton;
import org.liquidengine.legui.component.ScrollBar;
import org.liquidengine.legui.component.Slider;
import org.liquidengine.legui.component.TextArea;
import org.liquidengine.legui.component.TextInput;
import org.liquidengine.legui.component.ToggleButton;
import org.liquidengine.legui.component.Tooltip;
import org.liquidengine.legui.icon.CharIcon;
import org.liquidengine.legui.icon.Icon;
import org.liquidengine.legui.icon.ImageIcon;
import org.liquidengine.legui.image.Image;
import org.liquidengine.legui.image.LoadableImage;
import org.liquidengine.legui.system.renderer.BorderRenderer;
import org.liquidengine.legui.system.renderer.ComponentRenderer;
import org.liquidengine.legui.system.renderer.IconRenderer;
import org.liquidengine.legui.system.renderer.ImageRenderer;
import org.liquidengine.legui.system.renderer.RendererProvider;
import org.liquidengine.legui.system.renderer.nvg.border.NvgDefaultBorderRenderer;
import org.liquidengine.legui.system.renderer.nvg.border.NvgSimpleLineBorderRenderer;
import org.liquidengine.legui.system.renderer.nvg.component.NvgButtonRenderer;
import org.liquidengine.legui.system.renderer.nvg.component.NvgCheckBoxRenderer;
import org.liquidengine.legui.system.renderer.nvg.component.NvgContainerRenderer;
import org.liquidengine.legui.system.renderer.nvg.component.NvgDefaultComponentRenderer;
import org.liquidengine.legui.system.renderer.nvg.component.NvgImageViewRenderer;
import org.liquidengine.legui.system.renderer.nvg.component.NvgLabelRenderer;
import org.liquidengine.legui.system.renderer.nvg.component.NvgPasswordInputRenderer;
import org.liquidengine.legui.system.renderer.nvg.component.NvgProgressBarRenderer;
import org.liquidengine.legui.system.renderer.nvg.component.NvgRadioButtonRenderer;
import org.liquidengine.legui.system.renderer.nvg.component.NvgScrollBarRenderer;
import org.liquidengine.legui.system.renderer.nvg.component.NvgSliderRenderer;
import org.liquidengine.legui.system.renderer.nvg.component.NvgTextAreaRenderer;
import org.liquidengine.legui.system.renderer.nvg.component.NvgTextInputRenderer;
import org.liquidengine.legui.system.renderer.nvg.component.NvgToggleButtonRenderer;
import org.liquidengine.legui.system.renderer.nvg.component.NvgTooltipRenderer;
import org.liquidengine.legui.system.renderer.nvg.icon.NvgCharIconRenderer;
import org.liquidengine.legui.system.renderer.nvg.icon.NvgDefaultIconRenderer;
import org.liquidengine.legui.system.renderer.nvg.icon.NvgImageIconRenderer;
import org.liquidengine.legui.system.renderer.nvg.image.NvgDefaultImageRenderer;
import org.liquidengine.legui.system.renderer.nvg.image.NvgLoadableImageRenderer;

/**
 * Created by Aliaksandr_Shcherbin on 1/26/2017.
 */
public class NvgRendererProvider extends RendererProvider {

    private Map<Class<? extends Component>, NvgComponentRenderer<? extends Component>> componentRendererMap = new ConcurrentHashMap<>();
    private Map<Class<? extends Border>, NvgBorderRenderer<? extends Border>> borderRendererMap = new ConcurrentHashMap<>();
    private Map<Class<? extends Icon>, NvgIconRenderer<? extends Icon>> iconRendererMap = new ConcurrentHashMap<>();
    private Map<Class<? extends Image>, NvgImageRenderer<? extends Image>> imageRendererMap = new ConcurrentHashMap<>();

    private NvgComponentRenderer defaultComponentRenderer = new NvgDefaultComponentRenderer();
    private NvgBorderRenderer defaultBorderRenderer = new NvgDefaultBorderRenderer();
    private NvgIconRenderer defaultIconRenderer = new NvgDefaultIconRenderer();
    private NvgImageRenderer defaultImageRenderer = new NvgDefaultImageRenderer();

    private NvgRendererProvider() {

        // register component renderers
        componentRendererMap.put(Container.class, new NvgContainerRenderer());
        componentRendererMap.put(Button.class, new NvgButtonRenderer());
        componentRendererMap.put(ToggleButton.class, new NvgToggleButtonRenderer());
        componentRendererMap.put(ImageView.class, new NvgImageViewRenderer());
        componentRendererMap.put(CheckBox.class, new NvgCheckBoxRenderer());
        componentRendererMap.put(Label.class, new NvgLabelRenderer());
        componentRendererMap.put(ProgressBar.class, new NvgProgressBarRenderer());
        componentRendererMap.put(RadioButton.class, new NvgRadioButtonRenderer());
        componentRendererMap.put(ScrollBar.class, new NvgScrollBarRenderer());
        componentRendererMap.put(Slider.class, new NvgSliderRenderer());
        componentRendererMap.put(TextArea.class, new NvgTextAreaRenderer());
        componentRendererMap.put(TextInput.class, new NvgTextInputRenderer());
        componentRendererMap.put(PasswordInput.class, new NvgPasswordInputRenderer());
        componentRendererMap.put(Tooltip.class, new NvgTooltipRenderer());

        // register border renderers
        borderRendererMap.put(SimpleLineBorder.class, new NvgSimpleLineBorderRenderer());

        // register icon renderers
        iconRendererMap.put(ImageIcon.class, new NvgImageIconRenderer<>());
        iconRendererMap.put(CharIcon.class, new NvgCharIconRenderer<>());

        // register image renderers
        imageRendererMap.put(LoadableImage.class, new NvgLoadableImageRenderer<>());
    }

    public static NvgRendererProvider getInstance() {
        return NRPH.I;
    }

    @Override
    public <C extends Component> ComponentRenderer<C> getComponentRenderer(Class<C> componentClass) {
        return this.<C, ComponentRenderer<C>>cycledSearchOfRenderer(componentClass, componentRendererMap, defaultComponentRenderer);
    }

    @Override
    public <B extends Border> BorderRenderer<B> getBorderRenderer(Class<B> borderClass) {
        return this.<B, BorderRenderer<B>>cycledSearchOfRenderer(borderClass, borderRendererMap, defaultBorderRenderer);
    }

    @Override
    public <C extends Icon> IconRenderer getIconRenderer(Class<C> iconClass) {
        return this.<C, IconRenderer<C>>cycledSearchOfRenderer(iconClass, iconRendererMap, defaultIconRenderer);
    }

    @Override
    public <I extends Image> ImageRenderer getImageRenderer(Class<I> imageClass) {
        return this.<I, ImageRenderer<I>>cycledSearchOfRenderer(imageClass, imageRendererMap, defaultImageRenderer);
    }

    private <C, R> R cycledSearchOfRenderer(Class<C> componentClass, Map map, R defaultRenderer) {
        R renderer = null;
        Class cClass = componentClass;
        while (renderer == null) {
            renderer = ((Map<Class<C>, R>) map).get(cClass);
            if (cClass.isAssignableFrom(Component.class)) {
                break;
            }
            cClass = cClass.getSuperclass();
        }
        if (renderer == null) {
            renderer = defaultRenderer;
        }
        return renderer;
    }

    public <I extends Component, R extends NvgComponentRenderer<I>> void putComponentRenderer(Class<I> imageClass, R renderer) {
        if (imageClass == null || renderer == null) {
            return;
        }
        componentRendererMap.put(imageClass, renderer);
    }

    public <I extends Border, R extends NvgBorderRenderer<I>> void putBorderRenderer(Class<I> imageClass, R renderer) {
        if (imageClass == null || renderer == null) {
            return;
        }
        borderRendererMap.put(imageClass, renderer);
    }


    public <I extends Icon, R extends NvgIconRenderer<I>> void putIconRenderer(Class<I> imageClass, R renderer) {
        if (imageClass == null || renderer == null) {
            return;
        }
        iconRendererMap.put(imageClass, renderer);
    }


    public <I extends Image, R extends NvgImageRenderer<I>> void putImageRenderer(Class<I> imageClass, R renderer) {
        if (imageClass == null || renderer == null) {
            return;
        }
        imageRendererMap.put(imageClass, renderer);
    }

    @Override
    public List<ComponentRenderer> getComponentRenderers() {
        return new ArrayList<>(componentRendererMap.values());
    }

    private static final class NRPH {

        private static final NvgRendererProvider I = new NvgRendererProvider();
    }
}