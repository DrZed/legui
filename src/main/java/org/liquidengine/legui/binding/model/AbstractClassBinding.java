package org.liquidengine.legui.binding.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Class binding. Used to map java class to external view.
 *
 * @param <T> type of class binding.
 *
 * @author ShchAlexander.
 */
public abstract class AbstractClassBinding<T> {

    /**
     * Class and it's child classes for which could be used this binding.
     */
    private Class<? extends T> bindingForType;

    /**
     * Is this binding should be used by default. (Used mostly during adding this class to {@link org.liquidengine.legui.binding.BindingRegistry
     * BindingRegistry})
     */
    private boolean byDefault;

    /**
     * Default binding name which should be used if other not specified in field binding.
     */
    private String toName;

    /**
     * Field bindings saved as map for easy access by field name.
     */
    private Map<String, Binding> bindings = new LinkedHashMap<>();


    /**
     * Constructs class binding.
     *
     * @param bindingForType type for which binding is created.
     * @param toName name which should be used as default element name.
     * @param byDefault should this binding used as default or not.
     */
    public AbstractClassBinding(Class<? extends T> bindingForType, String toName, boolean byDefault) {
        if (bindingForType == null) {
            throw new IllegalArgumentException("bindingForType cannot be null.");
        }
        if (toName == null) {
            throw new IllegalArgumentException("toName cannot be null.");
        }
        this.bindingForType = bindingForType;
        this.toName = toName;
        this.byDefault = byDefault;
    }

    /**
     * Returns name which should be used as default element name.
     *
     * @return name which should be used as default element name.
     */
    public String getToName() {
        return toName;
    }

    /**
     * Used to set name which should be used as default element name.
     *
     * @param toName name which should be used as default element name.
     */
    public void setToName(String toName) {
        this.toName = toName;
    }

    /**
     * Returns class for which binding created.
     *
     * @return class for which binding created.
     */
    public Class<? extends T> getBindingForType() {
        return bindingForType;
    }

    /**
     * Used to set class for which binding created.
     *
     * @param bindingForType class for which binding created.
     */
    public void setBindingForType(Class<? extends T> bindingForType) {
        this.bindingForType = bindingForType;
    }

    /**
     * Used to add field binding.
     *
     * @param binding field binding.
     */
    public void putBinding(Binding binding) {
        if (binding != null) {
            bindings.put(binding.getJavaFieldName(), binding);
        }
    }

    /**
     * Returns field binding by java field name.
     *
     * @param fieldName field name to search binding.
     *
     * @return field binding by java field name or null if not found.
     */
    public Binding getBinding(String fieldName) {
        if (fieldName != null) {
            return bindings.get(fieldName);
        } else {
            return null;
        }
    }

    /**
     * Returns all field bindings as list.
     *
     * @return all field bindings as list.
     */
    public List<Binding> getBindingList() {
        return new ArrayList<>(bindings.values());
    }

    /**
     * Returns all field bindings as map where key is java field name.
     *
     * @return all field bindings as map where key is java field name.
     */
    public Map<String, Binding> getBindings() {
        return new LinkedHashMap<>(bindings);
    }

    /**
     * Returns true if binding should be used as default binding for specified class. Mostly used while parsing bindings.
     *
     * @return true if binding should be used as default binding for specified class.
     */
    public boolean isByDefault() {
        return byDefault;
    }

    /**
     * Set true if binding should be used as default binding for specified class. Mostly used while parsing bindings.
     *
     * @param byDefault true if binding should be used as default binding for specified class.
     */
    public void setByDefault(boolean byDefault) {
        this.byDefault = byDefault;
    }

    /**
     * Used to create instance of class binding type.
     *
     * @param clazz class to create instance.
     *
     * @return created instance.
     */
    public T createInstance(Class<T> clazz) {
        T instance = null;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * Used to made some operations after constructing and filling instance.
     *
     * @param instance instance to operate.
     */
    public void postConstruct(T instance) {
        // by default is empty.
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("bindingForType", bindingForType)
            .append("byDefault", byDefault)
            .append("toName", toName)
            .append("bindings", bindings)
            .toString();
    }
}
