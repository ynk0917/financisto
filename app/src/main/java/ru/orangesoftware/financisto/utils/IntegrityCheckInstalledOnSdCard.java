package ru.orangesoftware.financisto.utils;

import static ru.orangesoftware.financisto.utils.AndroidUtils.isInstalledOnSdCard;

import android.content.Context;
import ru.orangesoftware.financisto.R;

public class IntegrityCheckInstalledOnSdCard implements IntegrityCheck {

    private final Context context;

    public IntegrityCheckInstalledOnSdCard(Context context) {
        this.context = context;
    }

    @Override
    public Result check() {
        if (isInstalledOnSdCard(context)) {
            return new Result(Level.WARN, context.getString(R.string.installed_on_sd_card_warning));
        }
        return Result.OK;
    }

}
