package com.hp.roam.service;

import java.util.List;

import com.hp.roam.model.Board;
import com.hp.roam.model.BoardExample;

/**
 * @author ck
 * @date 2019年3月21日 上午10:01:39
 */
public interface BoardService {
	int insert(Board record);
	List<Board> selectByExample(BoardExample example);
	void trunateBoard();
}
