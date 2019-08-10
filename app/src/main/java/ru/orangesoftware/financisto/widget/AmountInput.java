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
package ru.orangesoftware.financisto.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.DimensionPixelSizeRes;
import org.androidannotations.annotations.res.DrawableRes;
import ru.orangesoftware.financisto.R;
import ru.orangesoftware.financisto.model.Currency;
import ru.orangesoftware.financisto.utils.MyPreferences;
import ru.orangesoftware.financisto.utils.Utils;

@EViewGroup(R.layout.amount_input)
public class AmountInput extends LinearLayout implements AmountListener {

    public interface OnAmountChangedListener {

        void onAmountChanged(long oldAmount, long newAmount);
    }

    private static final AtomicInteger EDIT_AMOUNT_REQUEST = new AtomicInteger(2000);

    private static final char[] acceptedChars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private static final char[] commaChars = new char[]{'.', ','};

    @ViewById(R.id.delimiter)
    protected TextView delimiter;

    @DimensionPixelSizeRes(R.dimen.select_entry_height_no_label)
    protected int minHeight;

    @ColorRes(R.color.negative_amount)
    protected int minusColor;

    @DrawableRes(R.drawable.ic_action_minus)
    protected Drawable minusDrawable;

    protected Activity owner;

    @ColorRes(R.color.positive_amount)
    protected int plusColor;

    @DrawableRes(R.drawable.ic_action_add)
    protected Drawable plusDrawable;

    @ViewById(R.id.primary)
    protected EditText primary;

    @ViewById(R.id.secondary)
    protected EditText secondary;

    @ViewById(R.id.signSwitcher)
    protected ImageSwitcher signSwitcher;

    private Currency currency;

    private int decimals;

    private boolean incomeExpenseEnabled = true;

    private boolean isExpense = true;

    private OnAmountChangedListener onAmountChangedListener;

    private final NumberKeyListener keyListener = new NumberKeyListener() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (end - start == 1) {
                char c = source.charAt(0);
                if (c == '.' || c == ',') {
                    onDotOrComma();
                    return "";
                }
                if (isIncomeExpenseEnabled()) {
                    if (c == '-') {
                        setExpense();
                        return "";
                    }
                    if (c == '+') {
                        setIncome();
                        return "";
                    }
                }
            }
            return super.filter(source, start, end, dest, dstart, dend);
        }

        @Override
        public int getInputType() {
            return InputType.TYPE_CLASS_PHONE;
        }

        @Override
        public boolean onKeyDown(View view, Editable content, int keyCode, KeyEvent event) {
            char c = event.getMatch(commaChars);
            if (c == '.' || c == ',') {
                onDotOrComma();
                return true;
            }
            return super.onKeyDown(view, content, keyCode, event);
        }

        @Override
        protected char[] getAcceptedChars() {
            return acceptedChars;
        }
    };

    private int requestId;

    private final View.OnFocusChangeListener selectAllOnFocusListener = (v, hasFocus) -> {
        EditText t = (EditText) v;
        if (hasFocus) {
            t.selectAll();
        }
    };

    private final TextWatcher textWatcher = new TextWatcher() {
        private long oldAmount;

        @Override
        public void afterTextChanged(Editable s) {
            if (onAmountChangedListener != null) {
                long amount = getAmount();
                onAmountChangedListener.onAmountChanged(oldAmount, amount);
                oldAmount = amount;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                int after) {
            oldAmount = getAmount();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                int count) {
        }
    };

    protected AmountInput(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected AmountInput(Context context) {
        super(context);
    }

    public void disableIncomeExpenseButton() {
        incomeExpenseEnabled = false;
        signSwitcher.setEnabled(false);
    }

    public long getAmount() {
        String p = primary.getText().toString();
        String s = secondary.getText().toString();
        long x = 100 * toLong(p);
        long y = toLong(s);
        long amount = x + (s.length() == 1 ? 10 * y : y);
        return isExpense() ? -amount : amount;
    }

    public void setAmount(long amount) {
        long absAmount = Math.abs(amount);
        long x = absAmount / 100;
        primary.setText(String.valueOf(x));

        if (MyPreferences.isEnterCurrencyDecimalPlaces(getContext())) {
            long y = absAmount - 100 * x;
            secondary.setText(String.format("%02d", y));
        }

        if (isIncomeExpenseEnabled() && amount != 0) {
            if (amount > 0) {
                setIncome();
            } else {
                setExpense();
            }
        }
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getDecimals() {
        return decimals;
    }

    public boolean isExpense() {
        return isExpense;
    }

    public boolean isIncomeExpenseEnabled() {
        return incomeExpenseEnabled;
    }

    @Override
    public void onAmountChanged(String amount) {
        try {
            long oldAmount = getAmount();
            BigDecimal d = new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
            boolean wasExpense = isExpense();
            setAmount(d.unscaledValue().longValue());
            if (wasExpense) {
                setExpense();
            }
            if (onAmountChangedListener != null) {
                onAmountChangedListener.onAmountChanged(oldAmount, getAmount());
            }
        } catch (NumberFormatException ignored) {
        }
    }

    public void openCalculator() {
        CalculatorInput input = CalculatorInput_.builder().amount(getAbsAmountString()).build();
        input.setListener(this);
        input.show(owner.getFragmentManager(), "calculator");
    }

    public void setColor(int color) {
        primary.setTextColor(color);
        secondary.setTextColor(color);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        Utils.setEnabled(this, enabled);
        if (!incomeExpenseEnabled) {
            disableIncomeExpenseButton();
        }
    }

    public void setExpense() {
        if (!isExpense) {
            onClickSignSwitcher();
        }
    }

    public void setIncome() {
        if (isExpense) {
            onClickSignSwitcher();
        }
    }

    public void setOnAmountChangedListener(
            OnAmountChangedListener onAmountChangedListener) {
        this.onAmountChangedListener = onAmountChangedListener;
    }

    public void setOwner(Activity owner) {
        this.owner = owner;
    }

    @AfterViews
    protected void initialize() {
        setMinimumHeight(minHeight);
        plusDrawable.mutate().setColorFilter(plusColor, PorterDuff.Mode.SRC_ATOP);
        minusDrawable.mutate().setColorFilter(minusColor, PorterDuff.Mode.SRC_ATOP);
        requestId = EDIT_AMOUNT_REQUEST.incrementAndGet();
        signSwitcher.setFactory(() -> {
            ImageView v = new ImageView(getContext());
            v.setScaleType(ImageView.ScaleType.FIT_CENTER);
            v.setLayoutParams(new ImageSwitcher.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            return v;
        });
        signSwitcher.setImageDrawable(minusDrawable);
        primary.setKeyListener(keyListener);
        primary.addTextChangedListener(textWatcher);
        primary.setOnFocusChangeListener(selectAllOnFocusListener);
        secondary.setKeyListener(new DigitsKeyListener(false, false) {

            @Override
            public int getInputType() {
                return InputType.TYPE_CLASS_PHONE;
            }

            @Override
            public boolean onKeyDown(View view, Editable content, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (content.length() == 0) {
                        primary.requestFocus();
                        int pos = primary.getText().length();
                        primary.setSelection(pos, pos);
                        return true;
                    }
                }
                return super.onKeyDown(view, content, keyCode, event);
            }

        });
        secondary.addTextChangedListener(textWatcher);
        secondary.setOnFocusChangeListener(selectAllOnFocusListener);

        if (!MyPreferences.isEnterCurrencyDecimalPlaces(getContext())) {
            secondary.setVisibility(GONE);
            delimiter.setVisibility(GONE);
        }
    }

    @Click(R.id.calculator)
    protected void onClickCalculator() {
        openCalculator();
    }

    @Click(R.id.signSwitcher)
    protected void onClickSignSwitcher() {
        if (isExpense) {
            isExpense = false;
            signSwitcher.setImageDrawable(plusDrawable);
            notifyAmountChangedListener();
        } else {
            isExpense = true;
            signSwitcher.setImageDrawable(minusDrawable);
            notifyAmountChangedListener();
        }
    }

    @Click(R.id.amount_input)
    protected void onClickUpDown() {
        openQuickInput();
    }

    protected void onDotOrComma() {
        secondary.requestFocus();
    }

    private String getAbsAmountString() {
        String p = primary.getText().toString().trim();
        String s = secondary.getText().toString().trim();
        return (Utils.isNotEmpty(p) ? p : "0") + "."
                + (Utils.isNotEmpty(s) ? s : "0");
    }

    private void notifyAmountChangedListener() {
        if (onAmountChangedListener != null) {
            long amount = getAmount();
            onAmountChangedListener.onAmountChanged(-amount, amount);
        }
    }

    private void openQuickInput() {
        QuickAmountInput input = QuickAmountInput_.builder().amount(getAmount()).build();
        input.setListener(this);
        input.show(owner.getFragmentManager(), "quick");
    }

    private long toLong(String s) {
        return s == null || s.length() == 0 ? 0 : Long.parseLong(s);
    }

}
