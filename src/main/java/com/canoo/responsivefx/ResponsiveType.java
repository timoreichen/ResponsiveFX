package com.canoo.responsivefx;

import javafx.css.PseudoClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A enum that defines the different types for responsive design. At the moment these types are the same as
 * defined by twitter bootstrap.
 */
public enum ResponsiveType {
    EXTRA_SMALL, SMALL, MEDIUM, LARGE, NONE;

    private static final String STYLESHEET_XS = "skin-xs.css";

    private static final String STYLESHEET_SM = "skin-sm.css";

    private static final String STYLESHEET_MD = "skin-md.css";

    private static final String STYLESHEET_LG = "skin-lg.css";

    private static final double MAX_SIZE_XS = 400;

    private static final double MAX_SIZE_SM = 768;

    private static final double MAX_SIZE_MD = 979;

    public static final PseudoClass PSEUDO_CLASS_XS = PseudoClass.getPseudoClass("extreme-small-device");

    public static final PseudoClass PSEUDO_CLASS_SM = PseudoClass.getPseudoClass("small-device");

    public static final PseudoClass PSEUDO_CLASS_MD = PseudoClass.getPseudoClass("medium-device");

    public static final PseudoClass PSEUDO_CLASS_LG = PseudoClass.getPseudoClass("large-device");

    /**
     * Returns the type that matches for the given width
     * @param width the width
     * @return the responsive type
     */
    public static ResponsiveType getForWidth(double width) {
        if(width < MAX_SIZE_XS) {
            return EXTRA_SMALL;
        } else if(width < MAX_SIZE_SM) {
            return SMALL;
        } else if(width < MAX_SIZE_MD) {
            return MEDIUM;
        } else {
            return LARGE;
        }
    }

    /**
     * Returns all supported pseudo classes
     * @return all supported pseudo classes
     */
    protected static List<PseudoClass> getAllClasses() {
        return new ArrayList<>(Arrays.asList(PSEUDO_CLASS_XS, PSEUDO_CLASS_SM, PSEUDO_CLASS_MD, PSEUDO_CLASS_LG));
    }

    /**
     * Returns all CSS pseudo classes that are inactive for this type
     * @return all inactive CSS pseudo classes
     */
    protected List<PseudoClass> getInactiveClasses() {
        List<PseudoClass> classes = getAllClasses();
        classes.removeAll(getActiveClasses());
        return classes;
    }

    /**
     * Returns all CSS pseudo classes that are active for this type
     * @return all active CSS pseudo classes
     */
    protected List<PseudoClass> getActiveClasses() {
        if(this.equals(EXTRA_SMALL)) {
            return Collections.singletonList(PSEUDO_CLASS_XS);
        } else if(this.equals(SMALL)) {
            return Collections.singletonList(PSEUDO_CLASS_SM);
        } else if(this.equals(MEDIUM)) {
            return Collections.singletonList(PSEUDO_CLASS_MD);
        } else {
            return Collections.singletonList(PSEUDO_CLASS_LG);
        }
    }

    /**
     * Returns the CSS stylesheet that will be used for the type
     * @return the stylesheet
     */
    protected String getStylesheet() {
        if(this.equals(EXTRA_SMALL)) {
            return ResponsiveType.class.getResource(STYLESHEET_XS).toExternalForm();
        } else if(this.equals(SMALL)) {
            return ResponsiveType.class.getResource(STYLESHEET_SM).toExternalForm();
        } else if(this.equals(MEDIUM)) {
            return ResponsiveType.class.getResource(STYLESHEET_MD).toExternalForm();
        } else {
            return ResponsiveType.class.getResource(STYLESHEET_LG).toExternalForm();
        }
    }
}
