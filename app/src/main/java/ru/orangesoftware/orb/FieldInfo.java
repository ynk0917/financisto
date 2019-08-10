/*******************************************************************************
 * Copyright (c) 2010 Denis Solonenko.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors:
 *     Denis Solonenko - initial API and implementation
 ******************************************************************************/
package ru.orangesoftware.orb;

import java.lang.reflect.Field;

class FieldInfo {

    final String columnName;

    final Field field;

    final int index;

    final boolean required;

    final FieldType type;

    public static FieldInfo entity(int index, Field field, String columnName, boolean required) {
        return new FieldInfo(index, field, columnName, new FieldType.ENTITY(field.getType()), required);
    }

    public static FieldInfo primitive(Field field, String columnName) {
        return new FieldInfo(0, field, columnName, FieldType.getType(field), false);
    }

    private FieldInfo(int index, Field field, String columnName, FieldType type, boolean required) {
        this.index = index;
        this.field = field;
        this.columnName = columnName;
        this.type = type;
        this.required = required;
    }

    @Override
    public String toString() {
        return "[" + index + ":" + columnName + "," + type + "]";
    }

}
