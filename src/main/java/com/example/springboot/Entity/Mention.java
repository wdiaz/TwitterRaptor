package com.example.springboot.Entity;

import javax.persistence.*;
import java.util.Date;

/**
 * The type Mentions.
 */
@Entity
@Table(name = "Mentions")
public class Mention {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String requestorTweetId;
    private String targetTweetId;
    private String requestorScreenName;
    private String targetScreenName;

    @Column(columnDefinition = "TEXT")
    private String fullText;
    private String targetUrl;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    /**
     * Instantiates a new Mentions.
     */
    public Mention() {
    }

    /**
     * Instantiates a new Mentions.
     *
     * @param requestorTweetId    the id str
     * @param requestorScreenName the screen name
     * @param fullText            the full text
     * @param targetUrl           the target url
     * @param createdAt           the date created
     */
    public Mention(String requestorTweetId, String targetTweetId, String requestorScreenName, String fullText, String targetUrl, Date createdAt) {
        this.requestorTweetId = requestorTweetId;
        this.targetTweetId = targetTweetId;
        this.requestorScreenName = requestorScreenName;
        this.fullText = fullText;
        this.targetUrl = targetUrl;
        this.createdAt = createdAt;
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
     * Gets requestor tweet id
     *
     * @return the requestor tweet id
     */
    public String getRequestorTweetId() {
        return requestorTweetId;
    }

    /**
     * Sets requestor tweet id.
     *
     * @param requestorTweetId the id str
     */
    public void setRequestorTweetId(String requestorTweetId) {
        this.requestorTweetId = requestorTweetId;
    }


    /**
     * Gets id str.
     *
     * @return the requestor tweet id
     */
    public String getTargetTweetId() {
        return targetTweetId;
    }

    /**
     * Sets id str.
     *
     * @param targetTweetId the id str
     */
    public void setTargetTweetId(String targetTweetId) {
        this.targetTweetId = targetTweetId;
    }


    /**
     * Gets screen name.
     *
     * @return the screen name
     */
    public String getRequestorScreenName() {
        return requestorScreenName;
    }

    /**
     * Sets screen name.
     *
     * @param screenName the screen name
     */
    public void setRequestorScreenName(String screenName) {
        this.requestorScreenName = screenName;
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
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets date created.
     *
     * @param dateCreated the date created
     */
    public void setCreatedAt(Date dateCreated) {
        this.createdAt = dateCreated;
    }

    public String getTargetScreenName() {
        return targetScreenName;
    }

    public void setTargetScreenName(String targetScreenName) {
        this.targetScreenName = targetScreenName;
    }
}
