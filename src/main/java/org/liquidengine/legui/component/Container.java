package org.liquidengine.legui.component;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Basic abstract Container object is a component
 * that can contain other components.
 * <p>
 * The base of container is <b><span style="color:red">SetUniqueList</span>
 * created on base of <span style="color:red">CopyOnWriteArrayList</span></b>,
 * that's little restriction which determines that child can exist in parent only one time.
 */
public abstract class Container<T extends Component> extends Controller {
    /**
     * List of child components
     */
    private List<T> components = new CopyOnWriteArrayList<>();

    /**
     * Default constructor. Used to create component instance without any parameters.
     * <p>
     * Also if you want to make it easy to use with
     * Json marshaller/unmarshaller component should contain empty constructor.
     */
    public Container() {
    }

    /**
     * Constructor with position and size parameters.
     *
     * @param x      x position position in parent component.
     * @param y      y position position in parent component.
     * @param width  width of component.
     * @param height height of component.
     */
    public Container(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    /**
     * Constructor with position and size parameters.
     *
     * @param position position position in parent component.
     * @param size     size of component.
     */
    public Container(Vector2f position, Vector2f size) {
        super(position, size);
    }

    /**
     * Returns count of child components.
     *
     * @return count of child components.
     * @see List#size()
     */
    public int count() {
        return components.size();
    }

    /**
     * Returns true if layerFrame contains no elements.
     *
     * @return true if layerFrame contains no elements.
     * @see List#isEmpty()
     */
    public boolean isEmpty() {
        return components.isEmpty();
    }

    /**
     * Returns true if layerFrame contains specified component.
     *
     * @param component component to check.
     * @return true if layerFrame contains specified component.
     * @see List#contains(Object)
     */
    public boolean contains(T component) {
        return components.contains(component);
    }

    /**
     * Returns an iterator over the elements in this layerFrame.
     * The elements are returned in no particular order.
     *
     * @return an iterator over the elements in this layerFrame.
     * @see List#iterator()
     */
    public Iterator<T> containerIterator() {
        return components.iterator();
    }

    /**
     * Used to add component to layerFrame.
     *
     * @param component component to add.
     * @return true if component is added.
     * @see List#add(Object)
     */
    public boolean add(T component) {
        if (component == null || component == this || components.contains(component)) return false;
        changeParent(component);
        return components.add(component);
    }

    /**
     * Used to add components.
     *
     * @param components components nodes to add.
     * @return true if added.
     * @see List#addAll(Collection)
     */
    public boolean addAll(Collection<? extends T> components) {
        if (components != null) {
            List<T> toAdd = new ArrayList<>();
            components.forEach(component -> {
                if (component != null && component != this && !components.contains(component)) {
                    changeParent(component);
                    toAdd.add(component);
                }
            });
            return this.components.addAll(toAdd);
        } else {
            return false;
        }
    }

    /**
     * Used to change parent of added component.
     *
     * @param component component to change.
     */
    private void changeParent(T component) {
        Container parent = component.getParent();
        if (parent != null) {
            parent.remove(component);
        }
        component.setParent(this);
    }

    /**
     * Used to remove component.
     *
     * @param component component to remove.
     * @return true if removed.
     * @see List#remove(Object)
     */
    public boolean remove(T component) {
        if (component != null) {
            Container parent = component.getParent();
            if (parent != null && parent == this && components.contains(component)) {
                component.setParent(null);
                return components.remove(component);
            }
        }
        return false;
    }

    /**
     * Used to remove components.
     *
     * @param components components to remove.
     * @see List#removeAll(Collection)
     */
    public void removeAll(Collection<? extends T> components) {
        List<T> toRemove = new ArrayList<>();
        components.forEach(compo -> {
            if (compo != null) {
                compo.setParent(null);
                toRemove.add(compo);
            }
        });
        this.components.removeAll(toRemove);
    }

    /**
     * Removes all of the elements of this layerFrame
     * that satisfy the given predicate.
     * Errors or runtime exceptions
     * thrown during iteration or by
     * the predicate are relayed to the caller.
     *
     * @param filter a predicate which returns true for elements to be removed.
     * @return true if any components were removed.
     * @see List#removeIf(Predicate)
     */
    public boolean removeIf(Predicate<? super T> filter) {
        components.stream().filter(filter).forEach(compo -> compo.setParent(null));
        return components.removeIf(filter);
    }

    /**
     * Used to remove all child components from layerFrame.
     *
     * @see List#clear()
     */
    public void clearChilds() {
        components.forEach(compo -> compo.setParent(null));
        components.clear();
    }

    /**
     * Returns true if this Container contains all of the elements of the specified collection.
     *
     * @param components components collection to check.
     * @return true if this Container contains all of the elements of the specified collection.
     * @see List#containsAll(Collection)
     */
    public boolean containsAll(Collection<T> components) {
        return this.components.containsAll(components);
    }

    /**
     * Returns a sequential Stream with this collection as its source.
     *
     * @return a sequential Stream with this collection as its source.
     * @see List#stream()
     */
    public Stream<T> stream() {
        return components.stream();
    }

    /**
     * Returns a possibly parallel Stream with this collection as its source.
     * It is allowable for this method to return a sequential stream.
     *
     * @return possibly parallel Stream with this collection as its source.
     * @see List#parallelStream()
     */
    public Stream<T> parallelStream() {
        return components.parallelStream();
    }

    /**
     * Performs the given action for each element of the Iterable
     * until all elements have been processed or the action throws an exception.
     *
     * @param action The action to be performed for each element.
     */
    public void forEach(Consumer<? super T> action) {
        components.forEach(action);
    }

    /**
     * Used to retrieve child components as {@link List}
     * <p>
     * <p>
     * <span style="color:red">NOTE: this method returns NEW {@link List} of components</span>
     *
     * @return list of child components.
     */
    public List<? extends Component> getChilds() {
        return new ArrayList<>(components);
    }

}