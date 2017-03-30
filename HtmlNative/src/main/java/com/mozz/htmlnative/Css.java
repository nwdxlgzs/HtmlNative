package com.mozz.htmlnative;

import android.support.annotation.NonNull;

import com.mozz.htmlnative.css.ClassSelector;
import com.mozz.htmlnative.css.CssSelector;
import com.mozz.htmlnative.css.IdSelector;
import com.mozz.htmlnative.css.TypeSelector;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Yang Tao, 17/3/27.
 */

final class Css {
    AttrsSet mCssSet;

    private SelectorHolder mClassSelectors;
    private SelectorHolder mIdSelectors;
    private SelectorHolder mTypeSelectors;

    public Css() {
        mCssSet = new AttrsSet();

        mClassSelectors = new SelectorHolder();
        mIdSelectors = new SelectorHolder();
        mTypeSelectors = new SelectorHolder();
    }

    public void newAttr(@NonNull AttrsOwner tree) {
        mCssSet.newAttr(tree);
    }

    public void putAttr(@NonNull AttrsOwner tree, String paramsKey, @NonNull Object value) {
        mCssSet.put(tree, paramsKey, value);
    }

    public void putSelector(CssSelector cssSelector) {
        if (cssSelector.getClass().equals(ClassSelector.class)) {
            ClassSelector classSelector = (ClassSelector) cssSelector;
            mClassSelectors.put(classSelector.getName(), classSelector);
        } else if (cssSelector.getClass().equals(IdSelector.class)) {
            IdSelector idSelector = (IdSelector) cssSelector;
            mIdSelectors.put(idSelector.getName(), idSelector);
        } else if (cssSelector.getClass().equals(TypeSelector.class)) {
            TypeSelector typeSelector = (TypeSelector) cssSelector;
            mTypeSelectors.put(typeSelector.getName(), typeSelector);
        }
    }

    public Set<CssSelector> matchedSelector(String type, String id, String clazz,
                                            Set<CssSelector> parentSelector) {
        
        Set<CssSelector> matchedSelector = new HashSet<>();
        mClassSelectors.select(clazz, matchedSelector);
        mIdSelectors.select(id, matchedSelector);
        mTypeSelectors.select(type, matchedSelector);

        if (parentSelector != null && !parentSelector.isEmpty()) {
            for (CssSelector selector : parentSelector) {
                CssSelector next = selector.next();

                if (next != null && next.matchThis(type, id, clazz)) {
                    matchedSelector.add(next);
                }
            }
        }
        return matchedSelector;
    }

    @Override
    public String toString() {
        return "AttrSet=" + mCssSet.toString() + ", class=" + mClassSelectors + ", id=" +
                mIdSelectors + ", type=" + mTypeSelectors;
    }
}
