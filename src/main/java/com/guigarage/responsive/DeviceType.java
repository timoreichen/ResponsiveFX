package com.guigarage.responsive;

import javafx.css.PseudoClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by hendrikebbers on 01.09.14.
 */
public enum DeviceType {
    EXTRA_SMALL, SMALL, MEDIUM, LARGE, XLARGE, NONE;

    public static final String STYLESHEET_XS = "skin-xs.css";

    public static final String STYLESHEET_SM = "skin-sm.css";

    public static final String STYLESHEET_MD = "skin-md.css";

    public static final String STYLESHEET_LG = "skin-lg.css";

    public static final String STYLESHEET_XL = "skin-xl.css";


    public static final double MAX_SIZE_XS = 400;

    public static final double MAX_SIZE_SM = 768;

    public static final double MAX_SIZE_MD = 979;

    public static final double MAX_SIZE_LG = 1366;

    public static final double MAX_SIZE_XL = 1920;


    public static final PseudoClass PSEUDO_CLASS_XS = PseudoClass.getPseudoClass("extreme-small-device");

    public static final PseudoClass PSEUDO_CLASS_SM = PseudoClass.getPseudoClass("small-device");

    public static final PseudoClass PSEUDO_CLASS_MD = PseudoClass.getPseudoClass("medium-device");

    public static final PseudoClass PSEUDO_CLASS_LG = PseudoClass.getPseudoClass("large-device");

    public static final PseudoClass PSEUDO_CLASS_XL = PseudoClass.getPseudoClass("extrem-large-device");

    public static DeviceType getForWidth(double width) {

        if(width > MAX_SIZE_XL)
            return XLARGE;
        else if(width > MAX_SIZE_LG)
            return LARGE;
        else if(width > MAX_SIZE_MD)
            return MEDIUM;
        else if(width > MAX_SIZE_SM)
            return SMALL;
        else
            return EXTRA_SMALL;
    }

    public static List<PseudoClass> getAllClasses() {
        return new ArrayList<>(Arrays.asList(PSEUDO_CLASS_XS, PSEUDO_CLASS_SM, PSEUDO_CLASS_MD, PSEUDO_CLASS_LG, PSEUDO_CLASS_XL));
    }

    public List<PseudoClass> getInactiveClasses() {
        List<PseudoClass> classes = getAllClasses();
        classes.removeAll(getActiveClasses());
        return classes;
    }

    public List<PseudoClass> getActiveClasses() {
        if(this.equals(EXTRA_SMALL)) {
            return Collections.singletonList(PSEUDO_CLASS_XS);
        } else if(this.equals(SMALL)) {
            return Collections.singletonList(PSEUDO_CLASS_SM);
        } else if(this.equals(MEDIUM)) {
            return Collections.singletonList(PSEUDO_CLASS_MD);
        } else if(this.equals(LARGE)) {
            return Collections.singletonList(PSEUDO_CLASS_LG);
        }
        else {
            return Collections.singletonList(PSEUDO_CLASS_XL);
        }
    }

    public String getStylesheet() {
        if(this.equals(EXTRA_SMALL)) {
            return DeviceType.class.getResource(STYLESHEET_XS).toExternalForm();
        } else if(this.equals(SMALL)) {
            return DeviceType.class.getResource(STYLESHEET_SM).toExternalForm();
        } else if(this.equals(MEDIUM)) {
            return DeviceType.class.getResource(STYLESHEET_MD).toExternalForm();
        } else if(this.equals(LARGE)) {
            return DeviceType.class.getResource(STYLESHEET_LG).toExternalForm();
        } else {
            return DeviceType.class.getResource(STYLESHEET_XL).toExternalForm();
        }
    }
}
