package com.hp.roam.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hp.roam.exception.MyException;
import com.hp.roam.model.Board;
import com.hp.roam.model.BoardExample;
import com.hp.roam.model.BoardExample.Criteria;
import com.hp.roam.model.MakeBoard;
import com.hp.roam.service.BoardService;
import com.hp.roam.util.ExcelUtils;
import com.hp.roam.util.MD5Util;



/**
 * @author ck
 * @date 2019年3月19日 下午2:31:58
 */
@Controller
public class BoardController {
	
	
	@Autowired
	private BoardService boardService;

	/**
	 * 生成序列号，由序列号生成用户名密码，存入数据库。 序列号暂时设为8位。ex.00000001. 用户名16位。密码20位 存入数据库
	 * 
	 * @param response
	 * @param lengthOfSn
	 *            序列号的位数 default 8
	 * @param startSn
	 *            序列号的起始 default 0
	 * @param numOfSn
	 *            default 100
	 * @param lengthOfUsername
	 *            default 16
	 * @param lengthOfPassword
	 *            default 16
	 * @throws MyException 
	 * @throws ServletException 
	 */
	@PostMapping("getBoards")
	public void makeBoards(HttpServletResponse response, MakeBoard makeBoard) throws Exception {
		String fileName = "Boards" + System.currentTimeMillis() + ".xlsx";
		// 开始制作 username/password
		System.out.println(makeBoard.getLengthOfPassword());
		List<Board> boards = new ArrayList<>();
		Random random = new Random(10000000);
		for (int i = 0; i < Integer.valueOf(makeBoard.getNumOfSn()); i++) {
			int snInt = Integer.valueOf(makeBoard.getStartSn().trim()) + i;
			Board board = createBoard(random,snInt, Integer.valueOf(makeBoard.getLengthOfSn().trim()),
					Integer.valueOf(makeBoard.getLengthOfUsername().trim()),
					Integer.valueOf(makeBoard.getLengthOfPassword().trim()));
			BoardExample boardExample = new BoardExample();
			Criteria criteria = boardExample.createCriteria();
			criteria.andSnEqualTo(board.getSn());
			List<Board> boards2 = boardService.selectByExample(boardExample);
			if(boards2!=null && boards2.size()!=0){
				throw new MyException("400", "the Sn is existed !");
			}
			boardService.insert(board);
			boards.add(board);
		}
		// 生成Board.xlsx
		OutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename="
					+ new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
			out = response.getOutputStream();
			ExcelUtils.exportBoardsExcel(out, boards,makeBoard.getAesPassword());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用户名加密，(时间戳+sn).md5 密码加密：
	 * 
	 * @param snInt
	 *            数值sn
	 * @param sizeOfSn
	 * @param sizeOfUsername
	 * @param sizeOfPassword
	 * @return
	 */
	public Board createBoard(Random random,int snInt, int lengthOfSn, int lengthOfUsername,
			int lengthOfPassword) {
		String sn = snInt + "";
		int length = (snInt + "").length();
		if (length < lengthOfSn) {
			for (int j = 0; j < (lengthOfSn - length); j++) {
				sn = "0" + sn;
			}
		}
		
		String username = (random.nextInt(100000000)+MD5Util
				.md5Encode(sn + System.currentTimeMillis(), null))
				.substring(0, lengthOfUsername);
		String password = null;
		if (lengthOfPassword > 32 && lengthOfPassword <= 64) {
			password = (MD5Util.md5Encode(username, null)
					+ MD5Util.md5Encode(System.currentTimeMillis() + "", null))
							.substring(0, lengthOfPassword);
		} else if (lengthOfPassword < 32) {
			password = MD5Util.md5Encode(username, null).substring(0, lengthOfPassword);
		}
		return new Board(sn, username, password);
	}
	
	@RequestMapping("clearAll")
	public String clearAll(ModelMap modelMap){
		boardService.trunateBoard();
		modelMap.addAttribute("msg", "clear all boards successfully !");
		return "error";
	}
}
