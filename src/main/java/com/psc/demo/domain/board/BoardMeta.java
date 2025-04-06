package com.psc.demo.domain.board;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 메타 ID (PK)

    @OneToOne
    @MapsId
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Board board;  // 게시글 정보와 연결

    private LocalDateTime regDate;  // 작성일
    private LocalDateTime updatedDate;  // 수정일
    private String tags;  // 게시글 태그 (콤마로 구분)

    @PrePersist
    protected void onCreate() {
        this.regDate = LocalDateTime.now();  // 작성일 자동 설정
    }

    // 게시글 수정 시 자동으로 수정일을 갱신하도록 설정
    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();  // 수정일 자동 갱신
    }
}
