package com.qrsynergy.ui.view.sharedocument.infos;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Sets draft/publish of the qr,
 * Expiration date of the qr and,
 * Public/not public of the qr.
 * Default type is publish
 * Default date is today + 30 days
 * Default is not public
 */
public class AdditionalOptionsInfo {

    private boolean isPublished;

    private Date expirationDate;

    private boolean isPublic;

    /**
     * Constructor
     * Default is published, current date + 30 days, not public
     */
    public AdditionalOptionsInfo() {
        this.isPublished = true;
        this.expirationDate = Date.from(LocalDate.now().plusDays(30)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
        this.isPublic = false;
    }

    /**
     *
     * @return local date of the object
     */
    public LocalDate getLocalDate(){
        return this.expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
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
     * Sets expiration date from local date
     * @param localDate local date
     */
    public void setExpirationDate(LocalDate localDate){
        this.expirationDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
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
