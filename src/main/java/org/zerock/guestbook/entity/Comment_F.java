package org.zerock.guestbook.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "b_f_comment")
public class Comment_F {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bfcId;

    @Column(name = "bfc_bfid", nullable = false)
    private String bfcBfid;

    @Column(name = "bfc_writernum", nullable = false)
    private String bfcWriternum;

    @Column(name = "bfc_writer", nullable = false)
    private String bfcWriter;

    @Column(name = "bfc_content", nullable = false)
    private String bfcContent;

    @Column(name = "bfc_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bfcDate = new Date(); // Default value for date

    // Default constructor
    public Comment_F() {
    }

    // Getters and setters
    public Long getBfcId() {
        return bfcId;
    }

    public void setBfcId(Long bfcId) {
        this.bfcId = bfcId;
    }

    public String getBfcBfid() {
        return bfcBfid;
    }

    public void setBfcBfid(String bgcBfid) {
        this.bfcBfid = bfcBfid;
    }

    public String getBfcWriternum() {
        return bfcWriternum;
    }

    public void setBfcWriternum(String bfcWriternum) {
        this.bfcWriternum = bfcWriternum;
    }

    public String getBfcWriter() {
        return bfcWriter;
    }

    public void setBfcWriter(String bfcWriter) {
        this.bfcWriter = bfcWriter;
    }

    public String getBfcContent() {
        return bfcContent;
    }

    public void setBfcContent(String bfcContent) {
        this.bfcContent = bfcContent;
    }

    public Date getBfcDate() {
        return bfcDate;
    }

    public void setBfcDate(Date bfcDate) {
        this.bfcDate = bfcDate;
    }
}
