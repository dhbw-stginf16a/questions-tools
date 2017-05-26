package stginf16a.pm.util;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

import java.util.function.Function;

/**
 * A Property which is unidirectional or bidirectionally bound to a property
 * of a property. What that means:
 *
 * @param <T>
 *            the type of the property itself
 * @param <D>
 *            the type of the property, that delivers the property to bind
 *            against
 */
public class NestedObjectProperty<T, D> extends SimpleObjectProperty<T> {
    /**
     * The binding partner of this instance
     */
    private Property<T> boundProperty;

    /**
     * Creates a new instance
     *
     * @param dependantProperty
     *            the property that delivers the property to bind against
     * @param getFunc
     *            a {@link java.util.function.Function} of D and ObjectProperty
     *            of T that returns the property to bind against
     * @param bidirectional
     *            boolean flag, if the binding should be bidirectional or
     *            unidirectional
     */
    public NestedObjectProperty(ObservableValue<D> dependantProperty, final Function<D, Property<T>> getFunc,
                                boolean bidirectional) {
        bindDependantProperty(dependantProperty.getValue(), getFunc, bidirectional);

        dependantProperty.addListener((observable, oldValue, newValue) -> {
            if (boundProperty != null) {
                if (bidirectional) {
                    unbindBidirectional(boundProperty);
                } else {
                    unbind();
                }
                boundProperty = null;
            }
            bindDependantProperty(newValue, getFunc, bidirectional);
        });
    }

    /**
     * Binds this instance to the dependant property
     *
     * @param newValue
     *            D
     * @param getFunc
     *            Function<D and ObjectProperty of T
     * @param bidirectional
     *            boolean flag, if the binding should be bidirectional or
     *            unidirectional
     */
    private void bindDependantProperty(D newValue, Function<D, Property<T>> getFunc, boolean bidirectional) {
        if (newValue != null) {
            Property<T> newProp = getFunc.apply(newValue);
            boundProperty = newProp;
            if (newProp != null) {
                if (bidirectional) {
                    bindBidirectional(newProp);
                } else {
                    bind(newProp);
                }
            } else {
                set(null);
            }
        } else {
            set(null);
        }
    }
}
