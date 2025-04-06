package com.psc.demo.dto.board;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardMetaDTO {

    private long id;

    private String tags;
    private LocalDateTime regDate;  // 작성일
    private LocalDateTime updatedDate;  // 수정일
}
