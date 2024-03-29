// This file is part of TagSoup and is Copyright 2002-2008 by John Cowan.
//
// TagSoup is licensed under the Apache License,
// Version 2.0.  You may obtain a copy of this license at
// http://www.apache.org/licenses/LICENSE-2.0 .  You may also have
// additional legal rights not granted by this license.
//
// TagSoup is distributed in the hope that it will be useful, but
// unless required by applicable law or agreed to in writing, TagSoup
// is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
// OF ANY KIND, either express or implied; not even the implied warranty
// of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
// 
// 
// Model of document

package com.example.evermemo.TESTEDITOR.rteditor.converter.tagsoup;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

/**
 * Abstract class representing a TSSL schema. Actual TSSL schemas are compiled
 * into concrete subclasses of this class.
 */
public abstract class Schema {

    public static final int M_ANY = 0xFFFFFFFF;
    public static final int M_EMPTY = 0;
    public static final int M_PCDATA = 1 << 30;
    public static final int M_ROOT = 1 << 31;

    public static final int F_RESTART = 1;
    public static final int F_CDATA = 2;
    public static final int F_NOFORCE = 4;

    private final HashMap<String, Integer> theEntities = new HashMap<String, Integer>();
    private final HashMap<String, ElementType> theElementTypes = new HashMap<String, ElementType>();

    private String theURI = "";
    private String thePrefix = "";
    @Nullable
    private ElementType theRoot = null;

    /**
     * Add or replace an element type for this schema.
     *
     * @param name     Name (Qname) of the element
     * @param model    Models of the element's content as a vector of bits
     * @param memberOf Models the element is a member of as a vector of bits
     * @param flags    Flags for the element
     */

    @SuppressLint("DefaultLocale")
    public void elementType(@NonNull String name, int model, int memberOf, int flags) {
        ElementType e = new ElementType(name, model, memberOf, flags, this);
        theElementTypes.put(name.toLowerCase(), e);
        if (memberOf == M_ROOT)
            theRoot = e;
    }

    /**
     * Get the root element of this schema
     */

    @Nullable
    public ElementType rootElementType() {
        return theRoot;
    }

    /**
     * Add or replace a default attribute for an element type in this schema.
     *
     * @param elemName Name (Qname) of the element type
     * @param attrName Name (Qname) of the attribute
     * @param type     Type of the attribute
     * @param value    Default value of the attribute; null if no default
     */

    public void attribute(@NonNull String elemName, String attrName, String type,
                          String value) {
        ElementType e = getElementType(elemName);
        if (e == null) {
            throw new Error("Attribute " + attrName
                    + " specified for unknown element type " + elemName);
        }
        e.setAttribute(attrName, type, value);
    }

    /**
     * Specify natural parent of an element in this schema.
     *
     * @param name       Name of the child element
     * @param parentName Name of the parent element
     */

    public void parent(@NonNull String name, @NonNull String parentName) {
        ElementType child = getElementType(name);
        ElementType parent = getElementType(parentName);
        if (child == null) {
            throw new Error("No child " + name + " for parent " + parentName);
        }
        if (parent == null) {
            throw new Error("No parent " + parentName + " for child " + name);
        }
        child.setParent(parent);
    }

    /**
     * Add to or replace a character entity in this schema.
     *
     * @param name  Name of the entity
     * @param value Value of the entity
     */

    public void entity(String name, int value) {
        theEntities.put(name, value);
    }

    /**
     * Get an ElementType by name.
     *
     * @param name Name (Qname) of the element type
     * @return The corresponding ElementType
     */

    @Nullable
    @SuppressLint("DefaultLocale")
    public ElementType getElementType(@NonNull String name) {
        return theElementTypes.get(name.toLowerCase());
    }

    /**
     * Get an entity value by name.
     *
     * @param name Name of the entity
     * @return The corresponding character, or 0 if none
     */

    public int getEntity(String name) {
        // System.err.println("%% Looking up entity " + name);
        Integer ch = theEntities.get(name);
        if (ch == null)
            return 0;
        return ch.intValue();
    }

    /**
     * Return the URI (namespace name) of this schema.
     */

    public String getURI() {
        return theURI;
    }

    /**
     * Change the URI (namespace name) of this schema.
     */

    public void setURI(String uri) {
        theURI = uri;
    }

    /**
     * Return the prefix of this schema.
     */

    public String getPrefix() {
        return thePrefix;
    }

    /**
     * Change the prefix of this schema.
     */

    public void setPrefix(String prefix) {
        thePrefix = prefix;
    }

}