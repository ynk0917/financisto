package ru.orangesoftware.financisto.export;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import ru.orangesoftware.financisto.backup.DatabaseExport;
import ru.orangesoftware.financisto.db.DatabaseAdapter;

public class BackupExportTask extends ImportExportAsyncTask {

    public volatile String backupFileName;

    public final boolean uploadOnline;

    public BackupExportTask(Activity context, ProgressDialog dialog, boolean uploadOnline) {
        super(context, dialog);
        this.uploadOnline = uploadOnline;
    }

    @Override
    protected String getSuccessMessage(Object result) {
        return String.valueOf(result);
    }

    @Override
    protected Object work(Context context, DatabaseAdapter db, String... params) throws Exception {
        DatabaseExport export = new DatabaseExport(context, db.db(), true);
        backupFileName = export.export();
        if (uploadOnline) {
            doUploadToDropbox(context, backupFileName);
            doUploadToGoogleDrive(context, backupFileName);
        }
        return backupFileName;
    }

}