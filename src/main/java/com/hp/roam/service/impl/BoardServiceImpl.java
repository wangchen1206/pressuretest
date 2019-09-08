package com.hp.roam.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hp.roam.dao.BoardMapper;
import com.hp.roam.model.Board;
import com.hp.roam.model.BoardExample;
import com.hp.roam.service.BoardService;

/**
 * @author ck
 * @date 2019年3月21日 上午10:04:54
 */
@Service
@Transactional
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private BoardMapper boardMapper;

	/* (non-Javadoc)
	 * @see com.hp.roam.service.BoardService#insert(com.hp.roam.model.Board)
	 */
	@Override
	public int insert(Board record) {
		return boardMapper.insert(record);
	}

	/* (non-Javadoc)
	 * @see com.hp.roam.service.BoardService#selectByExample(com.hp.roam.model.BoardExample)
	 */
	@Override
	public List<Board> selectByExample(BoardExample example) {
		return boardMapper.selectByExample(example);
	}

	/* (non-Javadoc)
	 * @see com.hp.roam.service.BoardService#trunateBoard()
	 */
	@Override
	public void trunateBoard() {
		boardMapper.trunateBoard();
	}

}
