package ru.orangesoftware.financisto.activity;

import static ru.orangesoftware.financisto.utils.Utils.text;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import ru.orangesoftware.financisto.R;
import ru.orangesoftware.financisto.model.Account;
import ru.orangesoftware.financisto.model.Currency;
import ru.orangesoftware.financisto.model.Transaction;
import ru.orangesoftware.financisto.utils.CurrencyCache;
import ru.orangesoftware.financisto.utils.MyPreferences;
import ru.orangesoftware.financisto.utils.Utils;

/**
 * Created by IntelliJ IDEA.
 * User: Denis Solonenko
 * Date: 4/21/11 7:17 PM
 */
public abstract class AbstractSplitActivity extends AbstractActivity {

    protected Account fromAccount;

    protected EditText noteText;

    protected Currency originalCurrency;

    protected Transaction split;

    protected TextView unsplitAmountText;

    protected Utils utils;

    private final int layoutId;

    private ProjectSelector projectSelector;

    protected AbstractSplitActivity(int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(layoutId);
        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_dialog_currency);

        fetchData();
        projectSelector = new ProjectSelector(this, db, x);
        projectSelector.fetchEntities();

        utils = new Utils(this);
        split = Transaction.fromIntentAsSplit(getIntent());
        if (split.fromAccountId > 0) {
            fromAccount = db.getAccount(split.fromAccountId);
        }
        if (split.originalCurrencyId > 0) {
            originalCurrency = CurrencyCache.getCurrency(db, split.originalCurrencyId);
        }

        LinearLayout layout = (LinearLayout) findViewById(R.id.list);

        createUI(layout);
        createCommonUI(layout);
        updateUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        projectSelector.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSelectedPos(int id, int selectedPos) {
        projectSelector.onSelectedPos(id, selectedPos);
    }

    protected abstract void createUI(LinearLayout layout);

    protected abstract void fetchData();

    protected Currency getCurrency() {
        return originalCurrency != null ? originalCurrency
                : (fromAccount != null ? fromAccount.currency : Currency.defaultCurrency());
    }

    @Override
    protected void onClick(View v, int id) {
        projectSelector.onClick(id);
    }

    protected void setUnsplitAmount(long amount) {
        Currency currency = getCurrency();
        utils.setAmountText(unsplitAmountText, currency, amount, false);
    }

    @Override
    protected boolean shouldLock() {
        return MyPreferences.isPinProtectedNewTransaction(this);
    }

    protected boolean updateFromUI() {
        split.note = text(noteText);
        split.projectId = projectSelector.getSelectedEntityId();
        return true;
    }

    protected void updateUI() {
        projectSelector.selectEntity(split.projectId);
        setNote(split.note);
    }

    private void createCommonUI(LinearLayout layout) {
        unsplitAmountText = x.addInfoNode(layout, R.id.add_split, R.string.unsplit_amount, "0");

        noteText = new EditText(this);
        x.addEditNode(layout, R.string.note, noteText);

        projectSelector.createNode(layout);

        Button bSave = (Button) findViewById(R.id.bSave);
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                saveAndFinish();
            }
        });

        Button bCancel = (Button) findViewById(R.id.bCancel);
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void saveAndFinish() {
        Intent data = new Intent();
        if (updateFromUI()) {
            split.toIntentAsSplit(data);
            setResult(Activity.RESULT_OK, data);
            finish();
        }
    }

    private void setNote(String note) {
        noteText.setText(note);
    }

}
