package com.qrsynergy.ui.view.createdocument;

import java.time.LocalDate;
import java.util.Date;

/**
 * Sets draft/publish of the qr,
 * Expiration date of the qr and,
 * Public/not public of the qr.
 * Default type is publish
 * Default date is null
 * Default is not public
 */
public class AdditionalOptionsInfo {

    private boolean isPublished;

    private Date expirationDate;

    private boolean isPublic;

    /**
     * Constructor
     * Default is published, 1970 date, not public
     */
    public AdditionalOptionsInfo() {
        this.isPublished = true;
        this.expirationDate = new Date(0L);
        this.isPublic = false;
    }

    /**
     *
     * @return is document published or draft?
     */
    public boolean isPublished() {
        return isPublished;
    }

    /**
     *
     * @param published publish
     */
    public void setPublished(boolean published) {
        isPublished = published;
    }

    /**
     *
     * @return expiration date of the document
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     *
     * @param expirationDate expiration date
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     *
     * @return is document public or not?
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     *
     * @param aPublic public
     */
    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
