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
package ru.orangesoftware.financisto.model;

import javax.persistence.Column;
import javax.persistence.Transient;

public class CategoryEntity<T extends CategoryEntity<T>> extends MyEntity {

    public static final int TYPE_EXPENSE = 0;

    public static final int TYPE_INCOME = 1;

    @Transient
    public CategoryTree<T> children;

    @Column(name = "left")
    public int left = 1;

    @Transient
    public T parent;

    @Column(name = "right")
    public int right = 2;

    @Column(name = "type")
    public int type = TYPE_EXPENSE;

    @SuppressWarnings("unchecked")
    public void addChild(T category) {
        if (children == null) {
            children = new CategoryTree<T>();
        }
        category.parent = (T) this;
        category.type = this.type;
        children.add(category);
    }

    public long getParentId() {
        return parent != null ? parent.id : 0;
    }

    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    public boolean isExpense() {
        return type == TYPE_EXPENSE;
    }

    public boolean isIncome() {
        return type == TYPE_INCOME;
    }

    public void makeThisCategoryExpense() {
        this.type = TYPE_EXPENSE;
    }

    public void makeThisCategoryIncome() {
        this.type = TYPE_INCOME;
    }

    public void removeChild(T category) {
        if (children != null) {
            children.remove(category);
        }
    }

}
