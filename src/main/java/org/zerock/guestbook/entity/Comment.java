package org.zerock.guestbook.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "b_g_comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bgcId;

    @Column(name = "bgc_bgid", nullable = false)
    private String bgcBgid;

    @Column(name = "bgc_writernum", nullable = false)
    private String bgcWriternum;

    @Column(name = "bgc_writer", nullable = false)
    private String bgcWriter;

    @Column(name = "bgc_content", nullable = false)
    private String bgcContent;

    @Column(name = "bgc_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bgcDate = new Date(); // Default value for date

    // Default constructor
    public Comment() {
    }

    // Getters and setters
    public Long getBgcId() {
        return bgcId;
    }

    public void setBgcId(Long bgcId) {
        this.bgcId = bgcId;
    }

    public String getBgcBgid() {
        return bgcBgid;
    }

    public void setBgcBgid(String bgcBgid) {
        this.bgcBgid = bgcBgid;
    }

    public String getBgcWriternum() {
        return bgcWriternum;
    }

    public void setBgcWriternum(String bgcWriternum) {
        this.bgcWriternum = bgcWriternum;
    }

    public String getBgcWriter() {
        return bgcWriter;
    }

    public void setBgcWriter(String bgcWriter) {
        this.bgcWriter = bgcWriter;
    }

    public String getBgcContent() {
        return bgcContent;
    }

    public void setBgcContent(String bgcContent) {
        this.bgcContent = bgcContent;
    }

    public Date getBgcDate() {
        return bgcDate;
    }

    public void setBgcDate(Date bgcDate) {
        this.bgcDate = bgcDate;
    }
}
