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
package ru.orangesoftware.financisto.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import ru.orangesoftware.financisto.R;
import ru.orangesoftware.financisto.adapter.TemplateListAdapter;

public class SelectTemplateActivity extends TemplatesListActivity {

    public static final String TEMPATE_ID = "template_id";

    public static final String MULTIPLIER = "multiplier";

    public static final String EDIT_AFTER_CREATION = "edit_after_creation";

    private int multiplier = 1;

    private TextView multiplierText;

    public SelectTemplateActivity() {
        super(R.layout.templates);
    }

    @Override
    public void editItem(View v, int position, long id) {
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        // do nothing
    }

    @Override
    public void registerForContextMenu(View view) {
    }

    @Override
    protected ListAdapter createAdapter(Cursor cursor) {
        return new TemplateListAdapter(this, db, cursor);
    }

    protected void decrementMultiplier() {
        --multiplier;
        if (multiplier < 1) {
            multiplier = 1;
        }
        multiplierText.setText("x" + multiplier);
    }

    protected void incrementMultiplier() {
        ++multiplier;
        multiplierText.setText("x" + multiplier);
    }

    @Override
    protected void internalOnCreate(Bundle savedInstanceState) {
        internalOnCreateTemplates();

        getListView().setOnItemLongClickListener((parent, view, position, id) -> {
            returnResult(id, true);
            return true;
        });

        Button b = findViewById(R.id.bEditTemplates);
        b.setOnClickListener(arg0 -> {
            setResult(RESULT_CANCELED);
            finish();
            Intent intent = new Intent(SelectTemplateActivity.this, TemplatesListActivity.class);
            startActivity(intent);
        });
        b = findViewById(R.id.bCancel);
        b.setOnClickListener(arg0 -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        multiplierText = findViewById(R.id.multiplier);
        ImageButton ib = findViewById(R.id.bPlus);
        ib.setOnClickListener(arg0 -> incrementMultiplier());
        ib = findViewById(R.id.bMinus);
        ib.setOnClickListener(arg0 -> decrementMultiplier());
    }

    @Override
    protected void onItemClick(View v, int position, long id) {
        returnResult(id, false);
    }

    @Override
    protected void viewItem(View v, int position, long id) {
        returnResult(id, false);
    }

    void returnResult(long id, boolean edit) {
        Intent intent = new Intent();
        intent.putExtra(TEMPATE_ID, id);
        intent.putExtra(MULTIPLIER, multiplier);
        if (edit) {
            intent.putExtra(EDIT_AFTER_CREATION, true);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

}
