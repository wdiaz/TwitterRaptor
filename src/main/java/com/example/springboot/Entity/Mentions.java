package com.example.springboot.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * The type Mentions.
 */
@Entity
public class Mentions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String idStr;
    private String screenName;
    private String fullText;
    private String targetUrl;
    private Date dateCreated;

    /**
     * Instantiates a new Mentions.
     */
public Mentions() {
    }

    /**
     * Instantiates a new Mentions.
     *
     * @param idStr the id str
     * @param screenName the screen name
     * @param fullText the full text
     * @param targetUrl the target url
     * @param dateCreated the date created
     */
public Mentions(String idStr, String screenName, String fullText, String targetUrl, Date dateCreated) {
        this.idStr = idStr;
        this.screenName = screenName;
        this.fullText = fullText;
        this.targetUrl = targetUrl;
        this.dateCreated = dateCreated;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets id str.
     *
     * @return the id str
     */
public String getIdStr() {
        return idStr;
    }

    /**
     * Sets id str.
     *
     * @param idStr the id str
     */
public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    /**
     * Gets screen name.
     *
     * @return the screen name
     */
public String getScreenName() {
        return screenName;
    }

    /**
     * Sets screen name.
     *
     * @param screenName the screen name
     */
public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    /**
     * Gets full text.
     *
     * @return the full text
     */
public String getFullText() {
        return fullText;
    }

    /**
     * Sets full text.
     *
     * @param fullText the full text
     */
public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    /**
     * Gets target url.
     *
     * @return the target url
     */
public String getTargetUrl() {
        return targetUrl;
    }

    /**
     * Sets target url.
     *
     * @param targetUrl the target url
     */
public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    /**
     * Gets date created.
     *
     * @return the date created
     */
public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * Sets date created.
     *
     * @param dateCreated the date created
     */
public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
