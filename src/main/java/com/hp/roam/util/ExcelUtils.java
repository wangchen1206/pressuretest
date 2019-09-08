package com.hp.roam.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.util.StringUtils;

import com.hp.roam.model.Board;
import com.hp.roam.model.OnlineRate;

/**
 * @author ck
 * @date 2019年3月19日 下午2:35:51
 */
public class ExcelUtils {

	public static int maxRowNum = 1000000;

	public static OutputStream exportBoardsExcel(OutputStream outputStream,
			List<Board> boards, String aesPassword) {
		Workbook workbook = new SXSSFWorkbook();

		if (boards.size() >= maxRowNum) {
			int sheetNum = boards.size() / maxRowNum;
			if (boards.size() % maxRowNum != 0) {
				sheetNum = sheetNum + 1;
			}
			for (int i = 0; i < sheetNum; i++) {
				Sheet sheet = workbook.createSheet("Board" + i);
				sheet.setDefaultColumnWidth(40);
				// 设置字体加粗
				CellStyle cellStyle = workbook.createCellStyle();
				Font font = workbook.createFont();
				font.setBold(true);
				cellStyle.setFont(font);

				Row row = sheet.createRow(0);
				Cell snCell = row.createCell(0);
				Cell usernameCell = row.createCell(1);
				Cell passwordCell = row.createCell(2);
				Cell suidCell = row.createCell(3);
				snCell.setCellStyle(cellStyle);
				usernameCell.setCellStyle(cellStyle);
				passwordCell.setCellStyle(cellStyle);
				suidCell.setCellStyle(cellStyle);
				snCell.setCellValue("SN");
				usernameCell.setCellValue("Username");
				passwordCell.setCellValue("Password");
				suidCell.setCellValue("Suid");

				// 开始分割boards
				List<Board> listBoard = boards.subList(maxRowNum * i,
						maxRowNum * (i + 1));

				if (!StringUtils.isEmpty(aesPassword)) {
					for (int j = 0; j < listBoard.size(); j++) {
						Row boardRow = sheet.createRow(j + 1);
						Cell snCell1 = boardRow.createCell(0);
						Cell usernameCell1 = boardRow.createCell(1);
						Cell passwordCell1 = boardRow.createCell(2);
						snCell1.setCellValue(AESUtil.encrypt(
								listBoard.get(j).getSn(), aesPassword));
						usernameCell1.setCellValue(AESUtil.encrypt(
								listBoard.get(j).getUsername(), aesPassword));
						passwordCell1.setCellValue(AESUtil.encrypt(
								listBoard.get(j).getPassword(), aesPassword));
					}
				} else {
					for (int j = 0; j < listBoard.size(); j++) {
						Row boardRow = sheet.createRow(j + 1);
						Cell snCell1 = boardRow.createCell(0);
						Cell usernameCell1 = boardRow.createCell(1);
						Cell passwordCell1 = boardRow.createCell(2);
						snCell1.setCellValue(listBoard.get(j).getSn());
						usernameCell1
								.setCellValue(listBoard.get(j).getUsername());
						passwordCell1
								.setCellValue(listBoard.get(j).getPassword());
					}
				}

			}

		} else {
			// 生成一个表格
			Sheet sheet = workbook.createSheet("Board");
			sheet.setDefaultColumnWidth(40);
			// 设置字体加粗
			CellStyle cellStyle = workbook.createCellStyle();
			Font font = workbook.createFont();
			font.setBold(true);
			cellStyle.setFont(font);

			Row row = sheet.createRow(0);
			Cell snCell = row.createCell(0);
			Cell usernameCell = row.createCell(1);
			Cell passwordCell = row.createCell(2);
			Cell suidCell = row.createCell(3);
			snCell.setCellStyle(cellStyle);
			usernameCell.setCellStyle(cellStyle);
			passwordCell.setCellStyle(cellStyle);
			suidCell.setCellStyle(cellStyle);
			snCell.setCellValue("SN");
			usernameCell.setCellValue("Username");
			passwordCell.setCellValue("Password");
			suidCell.setCellValue("Suid");

			if (!StringUtils.isEmpty(aesPassword)) {
				// 开始导入
				for (int i = 0; i < boards.size(); i++) {
					Row boardRow = sheet.createRow(i + 1);
					Cell snCell1 = boardRow.createCell(0);
					Cell usernameCell1 = boardRow.createCell(1);
					Cell passwordCell1 = boardRow.createCell(2);
					snCell1.setCellValue(AESUtil.encrypt(boards.get(i).getSn(),
							aesPassword));
					usernameCell1.setCellValue(AESUtil
							.encrypt(boards.get(i).getUsername(), aesPassword));
					passwordCell1.setCellValue(AESUtil
							.encrypt(boards.get(i).getPassword(), aesPassword));
				}
			} else {
				// 开始导入
				for (int i = 0; i < boards.size(); i++) {
					Row boardRow = sheet.createRow(i + 1);
					Cell snCell1 = boardRow.createCell(0);
					Cell usernameCell1 = boardRow.createCell(1);
					Cell passwordCell1 = boardRow.createCell(2);
					snCell1.setCellValue(boards.get(i).getSn());
					usernameCell1.setCellValue(boards.get(i).getUsername());
					passwordCell1.setCellValue(boards.get(i).getPassword());
				}
			}

		}

		// 输出
		try {
			workbook.write(outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return outputStream;
	}

	/**
	 * 将数据导入excel,并将excel文件放入本地,url后边的路径。
	 * @param path
	 * @param onlineRates
	 * @return
	 */
	public static String exportOnlineRateExcel(String path,
			List<OnlineRate> onlineRates) {
		@SuppressWarnings("resource")
		Workbook workbook = new SXSSFWorkbook();
		Sheet sheet = workbook.createSheet("Online Rate");
		sheet.setDefaultColumnWidth(40);
		// 设置字体加粗
		CellStyle cellStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		cellStyle.setFont(font);

		Row row = sheet.createRow(0);
		Cell deviceIdCell = row.createCell(0);
		Cell onlineRateCell = row.createCell(1);
		Cell offlineRateCell = row.createCell(2);
		Cell dateCell = row.createCell(3);
		deviceIdCell.setCellStyle(cellStyle);
		onlineRateCell.setCellStyle(cellStyle);
		offlineRateCell.setCellStyle(cellStyle);
		dateCell.setCellStyle(cellStyle);
		deviceIdCell.setCellValue("DeviceId");
		onlineRateCell.setCellValue("OnlineRate");
		offlineRateCell.setCellValue("OfflineRate");
		dateCell.setCellValue("Date");
		
		//开始填充数据
		for (int i = 0; i < onlineRates.size(); i++) {
			Row onlineRateRow = sheet.createRow(i+1);
			Cell deviceId = onlineRateRow.createCell(0);
			Cell onlineRate = onlineRateRow.createCell(1);
			Cell offlineRate = onlineRateRow.createCell(2);
			Cell date = onlineRateRow.createCell(3);
			deviceId.setCellValue(onlineRates.get(i).getDeviceId());
			//四舍五入，保留两位小数
			BigDecimal bg1 = new BigDecimal(onlineRates.get(i).getOnlineRate()*100).setScale(2, RoundingMode.HALF_UP);
			onlineRate.setCellValue(bg1.doubleValue()+"%");
			//四舍五入，保留两位小数
			BigDecimal bg = new BigDecimal(100-bg1.doubleValue()).setScale(2, RoundingMode.HALF_UP);
			offlineRate.setCellValue(bg.doubleValue()+"%");
			date.setCellValue(onlineRates.get(i).getCreateDate().minusDays(1).toLocalDate().toString());
		}
		
		//创建上传目录
		String dateString = ZonedDateTime.now()
				.withZoneSameInstant(ZoneId.of("Asia/Shanghai")).minusDays(1)
				.toLocalDateTime().toLocalDate().toString();
		String fileDir = dateString;
		String filePath = path+File.separator+fileDir;
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		//设置文件名
		String fileName = "OnlineRate"+dateString+".xlsx";
		String savePath = filePath + "/" + fileName;
		HttpUtils.logger.info("the file path is "+savePath);
		try {
			OutputStream fileOut = new FileOutputStream(savePath);
			workbook.write(fileOut);
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileDir+"/"+fileName;
	}

}
