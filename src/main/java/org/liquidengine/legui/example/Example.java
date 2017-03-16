package org.liquidengine.legui.example;

import org.joml.Vector2i;
import org.liquidengine.legui.DefaultInitializer;
import org.liquidengine.legui.component.Frame;
import org.liquidengine.legui.event.WindowSizeEvent;
import org.liquidengine.legui.listener.WindowSizeEventListener;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.renderer.Renderer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;
import org.lwjgl.opengl.GL;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by Aliaksandr_Shcherbin on 1/25/2017.
 */
public class Example {
    public static final     int     WIDTH            = 800;
    public static final     int     HEIGHT           = 600;
    private static volatile boolean running          = false;
    private static          long[]  monitors         = null;
    private static          boolean toggleFullscreen = false;
    private static          boolean fullscreen       = false;

//    private static String json = IOUtil.loadResourceAsString("org/liquidengine/legui/example/json.json", 1024);

    public static void main(String[] args) throws IOException {
        System.setProperty("joml.nounsafe", Boolean.TRUE.toString());
        System.setProperty("java.awt.headless", Boolean.TRUE.toString());
        if (!glfwInit()) {
            throw new RuntimeException("Can't initialize GLFW");
        }
        long window = glfwCreateWindow(WIDTH, HEIGHT, "Example", NULL, NULL);
        glfwShowWindow(window);

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glfwSwapInterval(0);

        PointerBuffer pointerBuffer = glfwGetMonitors();
        int           remaining     = pointerBuffer.remaining();
        monitors = new long[remaining];
        for (int i = 0; i < remaining; i++) {
            monitors[i] = pointerBuffer.get(i);
        }

        // Firstly we need to create frame component for window.
        Frame frame = new Frame(WIDTH, HEIGHT);// new Frame(WIDTH, HEIGHT);
        createGuiElements(frame, WIDTH, HEIGHT);

        // also we can create frame for example just by unmarshal it
//        frame = GsonMarshalUtil.unmarshal(json);
//        frame.setSize(WIDTH, HEIGHT);

        // We need to create legui instance one for window
        // which hold all necessary library components
        // or if you want some customizations you can do it by yourself.
        DefaultInitializer initializer = new DefaultInitializer(window, frame);

        GLFWKeyCallbackI         glfwKeyCallbackI         = (w1, key, code, action, mods) -> running = !(key == GLFW_KEY_ESCAPE && action != GLFW_RELEASE);
        GLFWWindowCloseCallbackI glfwWindowCloseCallbackI = w -> running = false;

        // if we want to create some callbacks for system events you should create and put them to keeper
        //
        // Wrong:
        // glfwSetKeyCallback(window, glfwKeyCallbackI);
        // glfwSetWindowCloseCallback(window, glfwWindowCloseCallbackI);
        //
        // Right:
        initializer.getCallbackKeeper().getChainKeyCallback().add(glfwKeyCallbackI);
        initializer.getCallbackKeeper().getChainWindowCloseCallback().add(glfwWindowCloseCallbackI);


        // Initialization finished, so we can start render loop.
        running = true;

        // Everything can be done in one thread as well as in separated threads.
        // Here is one-thread example.

        // before render loop we need to initialize renderer
        Renderer renderer = initializer.getRenderer();
        renderer.initialize();

        long time    = System.currentTimeMillis();
        int  updCntr = 0;

        while (running) {

            // Before rendering we need to update context with window size and window framebuffer size
            //{
            //    int[] windowWidth = {0}, windowHeight = {0};
            //    GLFW.glfwGetWindowSize(window, windowWidth, windowHeight);
            //    int[] frameBufferWidth = {0}, frameBufferHeight = {0};
            //    GLFW.glfwGetFramebufferSize(window, frameBufferWidth, frameBufferHeight);
            //    int[] xpos = {0}, ypos = {0};
            //    GLFW.glfwGetWindowPos(window, xpos, ypos);
            //    double[] mx = {0}, my = {0};
            //    GLFW.glfwGetCursorPos(window, mx, my);
            //
            //    context.update(windowWidth[0], windowHeight[0],
            //            frameBufferWidth[0], frameBufferHeight[0],
            //            xpos[0], ypos[0],
            //            mx[0], my[0]
            //    );
            //}

            // Also we can do it in one line
            Context context = initializer.getContext();
            context.updateGlfwWindow();
            Vector2i windowSize = context.getWindowSize();

            glClearColor(1, 1, 1, 1);
            // Set viewport size
            glViewport(0, 0, windowSize.x, windowSize.y);
            // Clear screen
            glClear(GL_COLOR_BUFFER_BIT);

            // render frame
            renderer.render(frame);

            // poll events to callbacks
            glfwPollEvents();
            glfwSwapBuffers(window);

            // Now we need to handle events. Firstly we need to handle system events.
            initializer.getSystemEventProcessor().processEvent();

            // When system events are translated to GUI events we need to handle them.
            // This event processor calls listeners added to ui components
            initializer.getGuiEventProcessor().processEvent();
            if (toggleFullscreen) {
                if (fullscreen) {
                    glfwSetWindowMonitor(window, NULL, 100, 100, WIDTH, HEIGHT, GLFW_DONT_CARE);
                } else {
                    GLFWVidMode glfwVidMode = glfwGetVideoMode(monitors[0]);
                    glfwSetWindowMonitor(window, monitors[0], 0, 0, glfwVidMode.width(), glfwVidMode.height(), glfwVidMode.refreshRate());
                }
                fullscreen = !fullscreen;
                toggleFullscreen = false;
            }
            updCntr++;
            if (System.currentTimeMillis() >= time + 1000) {
                time += 1000;
                glfwSetWindowTitle(window, "LEGUI Example. Updates per second: " + updCntr);
                updCntr = 0;
            }
        }

        // And when rendering is ended we need to destroy renderer
        renderer.destroy();

        glfwDestroyWindow(window);
        glfwTerminate();
    }

    private static void createGuiElements(Frame frame, int w, int h) {
        ExampleGui component = new ExampleGui(w, h);
        component.getListenerMap().addListener(WindowSizeEvent.class, (WindowSizeEventListener) event -> component.setSize(event.getWidth(), event.getHeight()));
        frame.getContainer().add(component);
    }
}
