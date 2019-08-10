/*
 * Copyright (c) 2012 Denis Solonenko.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */
package ru.orangesoftware.financisto.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;

public abstract class TransactionBase implements Serializable, Cloneable {

    @Column(name = "accuracy")
    public float accuracy;

    @Column(name = "attached_picture")
    public String attachedPicture;

    @Column(name = "datetime")
    public long dateTime = System.currentTimeMillis();

    @Column(name = "from_amount")
    public long fromAmount;

    @Id
    @Column(name = "_id")
    public long id = -1;

    @Column(name = "is_ccard_payment")
    public int isCCardPayment;

    @Column(name = "is_template")
    public int isTemplate;

    @Column(name = "last_recurrence")
    public long lastRecurrence;

    @Column(name = "latitude")
    public double latitude;

    @Column(name = "longitude")
    public double longitude;

    @Column(name = "note")
    public String note;

    @Column(name = "notification_options")
    public String notificationOptions;

    @Column(name = "original_from_amount")
    public long originalFromAmount;

    @Column(name = "parent_id")
    public long parentId;

    @Column(name = "provider")
    public String provider;

    @Column(name = "recurrence")
    public String recurrence;

    @Column(name = "remote_key")
    public String remoteKey;

    @Column(name = "status")
    public TransactionStatus status = TransactionStatus.UR;

    @Column(name = "template_name")
    public String templateName;

    @Column(name = "to_amount")
    public long toAmount;

    @Column(name = "updated_on")
    public long updatedOn = System.currentTimeMillis();

    public boolean isCreatedFromTemlate() {
        return !isTemplate() && templateName != null && templateName.length() > 0;
    }

    public boolean isCreditCardPayment() {
        return isCCardPayment == 1;
    }

    public boolean isNotTemplateLike() {
        return isTemplate == 0;
    }

    public boolean isScheduled() {
        return isTemplate == 2;
    }

    public boolean isSplitChild() {
        return parentId > 0;
    }

    public boolean isTemplate() {
        return isTemplate == 1;
    }

    public boolean isTemplateLike() {
        return isTemplate > 0;
    }

    public void setAsScheduled() {
        this.isTemplate = 2;
    }

    public void setAsTemplate() {
        this.isTemplate = 1;
    }

}
