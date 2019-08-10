/*
 * Copyright (c) 2012 Denis Solonenko.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package ru.orangesoftware.financisto.activity;

import static ru.orangesoftware.financisto.activity.AbstractActivity.setVisibility;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.util.List;
import ru.orangesoftware.financisto.db.DatabaseHelper;
import ru.orangesoftware.financisto.db.MyEntityManager;
import ru.orangesoftware.financisto.model.MyEntity;

/**
 * Created by IntelliJ IDEA.
 * User: denis.solonenko
 * Date: 7/2/12 9:25 PM
 */
public abstract class MyEntitySelector<T extends MyEntity> {

    private final Activity activity;

    private ListAdapter adapter;

    private final int defaultValueResId;

    private final MyEntityManager em;

    private List<T> entities;

    private final boolean isShow;

    private final int labelResId;

    private final int layoutId;

    private final int layoutPlusId;

    private View node;

    private long selectedEntityId = 0;

    private TextView text;

    private final ActivityLayout x;

    public MyEntitySelector(Activity activity, MyEntityManager em, ActivityLayout x, boolean isShow,
            int layoutId, int layoutPlusId, int labelResId, int defaultValueResId) {
        this.activity = activity;
        this.em = em;
        this.x = x;
        this.isShow = isShow;
        this.layoutId = layoutId;
        this.layoutPlusId = layoutPlusId;
        this.labelResId = labelResId;
        this.defaultValueResId = defaultValueResId;
    }

    public void createNode(LinearLayout layout) {
        if (isShow) {
            text = x.addListNodePlus(layout, layoutId, layoutPlusId, labelResId, defaultValueResId);
            node = (View) text.getTag();
        }
    }

    public void fetchEntities() {
        entities = fetchEntities(em);
        adapter = createAdapter(activity, entities);
    }

    public long getSelectedEntityId() {
        return node == null || node.getVisibility() == View.GONE ? 0 : selectedEntityId;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == layoutPlusId) {
            onNewEntity(data);
        }
    }

    public void onClick(int id) {
        if (id == layoutId) {
            pickEntity();
        } else if (id == layoutPlusId) {
            Intent intent = new Intent(activity, getEditActivityClass());
            activity.startActivityForResult(intent, layoutPlusId);
        }
    }

    public void onSelectedPos(int id, int selectedPos) {
        if (id == layoutId) {
            onEntitySelected(selectedPos);
        }
    }

    public void selectEntity(long entityId) {
        if (isShow) {
            T e = MyEntity.find(entities, entityId);
            selectEntity(e);
        }
    }

    public void setNodeVisible(boolean visible) {
        if (isShow) {
            setVisibility(node, visible ? View.VISIBLE : View.GONE);
        }
    }

    protected abstract ListAdapter createAdapter(Activity activity, List<T> entities);

    protected abstract List<T> fetchEntities(MyEntityManager em);

    protected abstract Class getEditActivityClass();

    private void onEntitySelected(int selectedPos) {
        T e = entities.get(selectedPos);
        selectEntity(e);
    }

    private void onNewEntity(Intent data) {
        fetchEntities();
        long entityId = data.getLongExtra(DatabaseHelper.EntityColumns.ID, -1);
        if (entityId != -1) {
            selectEntity(entityId);
        }
    }

    private void pickEntity() {
        int selectedEntityPos = MyEntity.indexOf(entities, selectedEntityId);
        x.selectPosition(activity, layoutId, labelResId, adapter, selectedEntityPos);
    }

    private void selectEntity(T e) {
        if (isShow && e != null) {
            text.setText(e.title);
            selectedEntityId = e.id;
        }
    }

}
