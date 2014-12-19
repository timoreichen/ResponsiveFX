package com.canoo.responsivefx;

import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.stage.Window;

/**
 * A ResponsiveHandler instance adds support for responsive design to a JavaFX window. To do so
 * only one line of code is needed:
 * <code>new ResponsiveHandler(myWindow).start()</code>
 *
 * By doing so the ResponsiveHandler will listen to the width of the window and define a responsive type for
 * the windows that matches its width. At the moment there are 4 different response types that are designed
 * as defined in twitter boostrap:
 * Extra-Small
 * Small
 * Medium
 * Large
 *
 * You can react on the change of the type in two different ways. First of all you can add a listener to the
 * ResponsiveHandler that will fire an event whenever the type changed
 * (see {@link #setOnDeviceTypeChanged(javafx.beans.value.ChangeListener)}). All types are defined by the
 * {@link ResponsiveType} enum. This should be only used for some special uses cases.
 * Normally CSS should be used to define the layout of an skin of an application. By using the
 * ResponsiveHandler responsive design can be managed in CSS. The handler will dynamically add and remove
 * CSS rules to the stylesheets of the window. These rules define the visibility of matching nodes in the
 * scene graph of the window by using the same classes as in twitter boostrap:
 *
 * .visible-xs
 * .visible-sm
 * .visible-md
 * .visible-lg
 * .hidden-xs
 * .hidden-sm
 * .hidden-md
 * .hidden-lg
 *
 * By adding these CSS classes to nodes in the window these nodes will be dynamically collapsed or shown depending
 * on the responsive type / width of the window.
 *
 * TODO: Example
 *
 * Next to the shown CSS classes ResponsiveFX supports the use of pseudo classes for responsive design. By doing so
 * you can define different skins / CSS rules for a node based on the responsive type. ResponsiveFX supports the
 * following 4 pseudo classes:
 *
 * extreme-small-device
 * small-device
 * medium-device
 * large-device
 *
 * The pseudo classes are defined as static fields in the {@link ResponsiveType} class but normally
 * you don't need to use them in code. By defining different CSS rules for a node (or node class) based on these
 * pseudo classes you can simply define size dependend skins for your node.
 *
 * TODO: Example
 *
 */
public class ResponsiveHandler {

    private static String PROP_MANAGED_STATE = "responsivefx-preset-managed-state";

    private  InvalidationListener managedListener;

    private ObjectProperty<ResponsiveType> deviceTypeProperty;

    private Window window;

    /**
     * Default constructor
     * @param window the window that should be handled.
     */
    public ResponsiveHandler(Window window) {
        deviceTypeProperty = new SimpleObjectProperty<>(ResponsiveType.NONE);
        this.window = window;

        managedListener = e -> {
            BooleanProperty managed = (BooleanProperty) e;
            Node node = (Node) managed.getBean();
            node.getProperties().put(PROP_MANAGED_STATE, node.isManaged());
        };
    }

    /**
     * This method starts the responsive design handling.
     */
    public void start() {
        StringProperty stylesheet = new SimpleStringProperty(getCurrentResponsiveStylesheet(window));
        Util.bindStyleSheetToWindow(window, stylesheet);

        updatePseudoClassesForAllChildren(window);
        //TODO: HACK managed property can` be effected by CSS
        updateManagedPropertyForAllChildren(window);

        Util.registerRecursiveChildObserver(window, n -> removeAllPseudoClasses(n), n -> updatePseudoClasses(n, getTypeForWindow(window)));

        window.widthProperty().addListener(e -> {
            stylesheet.setValue(getCurrentResponsiveStylesheet(window));
            updatePseudoClassesForAllChildren(window);
            updateManagedPropertyForAllChildren(window);
        });
        window.getScene().getRoot().layout();
    }

    private ResponsiveType getTypeForWindow(Window window) {
        return ResponsiveType.getForWidth(window.getWidth());
    }

    private String getCurrentResponsiveStylesheet(Window window) {
        return getTypeForWindow(window).getStylesheet();
    }

    private void updateManagedPropertyForAllChildren(Window window) {
        Util.getAllNodesInWindow(window).forEach(n -> {
            updateManagedProperty(n, getTypeForWindow(window));
        });
    }

    private void updateManagedProperty(Node n, ResponsiveType type) {
        // first time we've set this invisible => store the preset
      if ( !n.getProperties().containsKey(PROP_MANAGED_STATE)) {
          n.getProperties().put(PROP_MANAGED_STATE, n.isManaged()) ;         
      }
      // don't track changes through this
       n.managedProperty().removeListener(managedListener);
       // If it's visible we use the stored value for "managed" property
       n.setManaged(n.isVisible()? (Boolean)n.getProperties().get(PROP_MANAGED_STATE):false);
       // need to track changes through API
       n.managedProperty().addListener(managedListener);
    }

    private void updatePseudoClassesForAllChildren(Window window) {
        Util.getAllNodesInWindow(window).forEach(n -> {
            updatePseudoClasses(n, getTypeForWindow(window));
        });
    }

    private void updatePseudoClasses(Node n, ResponsiveType type) {
        type.getInactiveClasses().forEach(pseudoClass -> n.pseudoClassStateChanged(pseudoClass, false));
        type.getActiveClasses().forEach(pseudoClass -> n.pseudoClassStateChanged(pseudoClass, true));
        deviceTypeProperty.set(type);
    }

    private void removeAllPseudoClasses(Node n) {
        ResponsiveType.getAllClasses().forEach(pseudoClass -> n.pseudoClassStateChanged(pseudoClass, false));
    }

    /**
     * Register a listener that will be called whenever the device type of the window changes.
     * @param listener the listener
     */
    public void setOnDeviceTypeChanged(final InvalidationListener listener) {
        deviceTypeProperty.addListener(listener);
    }

    /**
     * Register a listener that will be called whenever the device type of the window changes.
     * @param listener the listener
     */
    public void setOnDeviceTypeChanged(final ChangeListener<ResponsiveType> listener) {
        deviceTypeProperty.addListener(listener);
    }
}
