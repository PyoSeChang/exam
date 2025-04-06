package com.psc.demo.repository.board;


import com.psc.demo.domain.board.BoardMeta;
import com.psc.demo.dto.board.BoardMetaDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardMetaRepository extends JpaRepository<BoardMeta, Long> {



}
