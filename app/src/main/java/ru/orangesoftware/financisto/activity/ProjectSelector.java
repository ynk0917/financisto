/*
 * Copyright (c) 2012 Denis Solonenko.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package ru.orangesoftware.financisto.activity;

import android.app.Activity;
import android.widget.ListAdapter;
import java.util.List;
import ru.orangesoftware.financisto.R;
import ru.orangesoftware.financisto.db.MyEntityManager;
import ru.orangesoftware.financisto.model.Project;
import ru.orangesoftware.financisto.utils.MyPreferences;
import ru.orangesoftware.financisto.utils.TransactionUtils;

/**
 * Created by IntelliJ IDEA.
 * User: denis.solonenko
 * Date: 7/2/12 9:25 PM
 */
public class ProjectSelector extends MyEntitySelector<Project> {

    public ProjectSelector(Activity activity, MyEntityManager em, ActivityLayout x) {
        super(activity, em, x, MyPreferences.isShowProject(activity),
                R.id.project, R.id.project_add, R.string.project, R.string.no_project);
    }

    @Override
    protected ListAdapter createAdapter(Activity activity, List<Project> entities) {
        return TransactionUtils.createProjectAdapter(activity, entities);
    }

    @Override
    protected List<Project> fetchEntities(MyEntityManager em) {
        return em.getActiveProjectsList(true);
    }

    @Override
    protected Class getEditActivityClass() {
        return ProjectActivity.class;
    }

}
