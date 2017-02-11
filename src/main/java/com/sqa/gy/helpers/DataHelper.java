package com.sqa.gy.helpers;

import java.io.*;
import java.sql.*;
import java.util.*;

import org.apache.log4j.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import com.sqa.gy.helpers.exceptions.*;

public class DataHelper {

	private static Logger logger = Logger.getLogger(DataHelper.class);

	public static String displayData(Object[][] data) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data.length; j++) {
				sb.append(data[i][j] + "\t");
			}
		}
		return sb.toString();
	}

	public static void displayData(Object[][] data, Logger logger) {
		// Display the data Object[][] to console, does not use the logger.
	}

	public static void displayData2(Object[][] data) {
		// Display the data Object[][] to console using the logger.
		for (int i = 0; i < data.length; i++) {
			String dataLine = "";
			for (int j = 0; j < data[i].length; j++) {
				dataLine += data[i][j];
				dataLine += "  \t";
			}
			logger.info(dataLine.trim());
		}
	}

	public static Object[][] evalDatabaseTable(String driverClassString, String databaseStringUrl, String username,
			String password, String tableName) throws ClassNotFoundException, SQLException {
		return evalDatabaseTable(driverClassString, databaseStringUrl, username, password, tableName, 0, 0);
	}

	// I think this gets the data from each row of the table and turns it into a
	// Object [][] for a data provider
	public static Object[][] evalDatabaseTable(String driverClassString, String databaseStringUrl, String username,
			String password, String tableName, int rowOffset, int colOffset)
			throws ClassNotFoundException, SQLException {
		Object[][] myData;
		ArrayList<Object> myArrayData = new ArrayList<Object>();
		Class.forName(driverClassString);
		Connection dbconn = DriverManager.getConnection(databaseStringUrl, username, password);
		Statement stmt = dbconn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from " + tableName);
		int numOfColumns = rs.getMetaData().getColumnCount();
		int curRow = 1;
		while (rs.next()) {
			if (curRow > rowOffset) {
				Object[] rowData = new Object[numOfColumns - colOffset];
				for (int i = 0, j = colOffset; i < rowData.length; i++) {
					rowData[i] = rs.getString(i + colOffset + 1);
				}
				myArrayData.add(rowData);
			}
			curRow++;
		}
		myData = new Object[myArrayData.size()][];
		for (int i = 0; i < myData.length; i++) {
			myData[i] = (Object[]) myArrayData.get(i);
		}
		rs.close();
		stmt.close();
		dbconn.close();
		return myData;
	}

	public static Object[][] getExcelFileData(String fileLocation, String fileName, Boolean hasLabels)
			throws InvalidExcelExtensionException {
		Object[][] resultsObject;
		String[] fileNameParts = fileName.split("[.]");
		String extension = fileNameParts[fileNameParts.length - 1];
		ArrayList<Object> results = null;
		if (extension.equalsIgnoreCase("xlsx")) {
			results = getNewExcelFileResults(fileLocation, fileName, hasLabels);
		} else if (extension.equalsIgnoreCase("xls")) {
			results = getOldExcelFileResults(fileLocation, fileName, hasLabels);
		} else {
			throw new InvalidExcelExtensionException();
		}
		resultsObject = new Object[results.size()][];
		results.toArray(resultsObject);
		return resultsObject;
	}

	@SuppressWarnings("deprecation")
	private static ArrayList<Object> getNewExcelFileResults(String fileLocation, String fileName, Boolean hasLabels) {
		ArrayList<Object> results = new ArrayList<Object>();
		try {
			String fullFilePath = fileLocation + fileName;
			InputStream newExcelFormatFile = new FileInputStream(new File(fullFilePath));
			XSSFWorkbook workbook = new XSSFWorkbook(newExcelFormatFile);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			if (hasLabels) {
				rowIterator.next();
			}
			while (rowIterator.hasNext()) {
				ArrayList<Object> rowData = new ArrayList<Object>();
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_BOOLEAN:
						System.out.print(cell.getBooleanCellValue() + "\t\t\t");
						rowData.add(cell.getBooleanCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						System.out.print(cell.getNumericCellValue() + "\t\t\t");
						rowData.add(cell.getNumericCellValue());
						break;
					case Cell.CELL_TYPE_STRING:
						System.out.print(cell.getStringCellValue() + "\t\t\t");
						rowData.add(cell.getStringCellValue());
						break;
					}
				}
				Object[] rowDataObject = new Object[rowData.size()];
				rowData.toArray(rowDataObject);
				results.add(rowDataObject);
				System.out.println("");
			}
			newExcelFormatFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}

	private static ArrayList<Object> getOldExcelFileResults(String fileLocation, String fileName, Boolean hasLabels) {
		ArrayList<Object> results = new ArrayList<Object>();
		try {
			String fullFilePath = fileLocation + fileName;
			InputStream newExcelFormatFile = new FileInputStream(new File(fullFilePath));
			HSSFWorkbook workbook = new HSSFWorkbook(newExcelFormatFile);
			HSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			if (hasLabels) {
				rowIterator.next();
			}
			while (rowIterator.hasNext()) {
				ArrayList<Object> rowData = new ArrayList<Object>();
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_BOOLEAN:
						System.out.print(cell.getBooleanCellValue() + "\t\t\t");
						rowData.add(cell.getBooleanCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						System.out.print(cell.getNumericCellValue() + "\t\t\t");
						rowData.add(cell.getNumericCellValue());
						break;
					case Cell.CELL_TYPE_STRING:
						System.out.print(cell.getStringCellValue() + "\t\t\t");
						rowData.add(cell.getStringCellValue());
						break;
					}
				}
				Object[] rowDataObject = new Object[rowData.size()];
				rowData.toArray(rowDataObject);
				results.add(rowDataObject);
				System.out.println("");
			}
			newExcelFormatFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}

	private String dataString;

	// Constructor
	public DataHelper(Object[][] data) {
		this.dataString = displayData(data);
	}

	// Instance method (will need constructor that takes the Object[][])
	public String displayData() {
		return this.dataString;
	}

}