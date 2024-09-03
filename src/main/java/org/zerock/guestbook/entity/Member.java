package org.zerock.guestbook.entity;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "M_NUM")
    private String id; // MEMBER table's primary key

    @Column(name = "M_ID", unique = true, nullable = false)
    private String username; // M_ID column

    @Column(name = "M_PASSWORD", nullable = false)
    private String password; // M_PASSWORD column

    @Column(name = "M_NICKNAME", nullable = false)
    private String nickname; // M_NICKNAME column

    @Column(name = "M_EMAIL", unique = true, nullable = false)
    private String email; // M_EMAIL column

    @Column(name = "M_ISADMIN", nullable = false)
    private boolean isAdmin; // M_ISADMIN column

    @Column(name = "mute", nullable = false)
    private boolean  mute; // 정지여부

}
