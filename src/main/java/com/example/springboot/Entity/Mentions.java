package com.example.springboot.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

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

    public Mentions() {
    }

    public Mentions(String idStr, String screenName, String fullText, String targetUrl, Date dateCreated) {
        this.idStr = idStr;
        this.screenName = screenName;
        this.fullText = fullText;
        this.targetUrl = targetUrl;
        this.dateCreated = dateCreated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
