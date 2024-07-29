package org.zerock.guestbook.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "board_game")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "B_G_IDX")
    private Long id;

    @Column(name = "B_G_WRITER", nullable = false)
    private String writer;

    @Column(name = "B_G_WRITERNUM", nullable = false)
    private Integer writerNum;

    @Column(name = "B_G_TITLE", nullable = false)
    private String title;

    @Column(name = "B_G_CONTENT", columnDefinition = "TEXT")
    private String content;

    @Lob
    @Column(name = "B_G_THUMBNAIL")
    private byte[] thumbnail;

    @Column(name = "B_G_SCORESUM", precision = 5, scale = 2)
    private Double scoreSum;

    @Column(name = "B_G_SCORECOUNT")
    private Integer scoreCount;

    @Column(name = "B_G_DATE")
    private LocalDateTime date;

    @Column(name = "B_G_TAG")
    private String tag;

    @Column(name = "B_G_GAMETITLE")
    private String gameTitle;

    @Transient
    private String thumbnailBase64; // Transient field for Base64 encoded thumbnail

    // Getter for thumbnailBase64
    public String getThumbnailBase64() {
        return thumbnailBase64;
    }

    // Setter for thumbnailBase64
    public void setThumbnailBase64(String thumbnailBase64) {
        this.thumbnailBase64 = thumbnailBase64;
    }
}
