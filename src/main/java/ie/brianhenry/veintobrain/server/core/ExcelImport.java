package ie.brianhenry.veintobrain.server.core;

import ie.brianhenry.veintobrain.shared.representations.AnalyteResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.LocalDate;

public class ExcelImport {

	HSSFWorkbook workbook;

	List<AnalyteResult> data = new ArrayList<AnalyteResult>();

	// TODO catch instead of throw
	public ExcelImport(String analyteType, File file) throws IOException {

		FileInputStream fileIS = new FileInputStream(file);

		// Get the workbook instance for XLS file
		workbook = new HSSFWorkbook(fileIS);

		// Get first sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);

		int dateCol = 0;
		int resultCol = 0;

		// Get iterator to all cells of current row
		Iterator<Cell> cellIterator = sheet.getRow(0).cellIterator();
		while (cellIterator.hasNext()) {
			Cell c = cellIterator.next();

			if (c.getCellType() == Cell.CELL_TYPE_STRING && c.getStringCellValue().toLowerCase().equals("result"))
				resultCol = c.getColumnIndex();
			if (c.getCellType() == Cell.CELL_TYPE_STRING && c.getStringCellValue().toLowerCase().contains("receive date"))
				dateCol = c.getColumnIndex();
		}

		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();

		if (dateCol > 0 && resultCol > 0) {
			rowIterator.next(); // Skip the first row
			while (rowIterator.hasNext()) {
				Row r = rowIterator.next();
				if (!r.getCell(resultCol).getStringCellValue().equals(""))
					data.add(new AnalyteResult(analyteType, new LocalDate(r.getCell(dateCol).getDateCellValue()), r.getCell(resultCol)
							.getStringCellValue()));
			}
		} else {
			// TODO Need to inform user that there's been a problem
		}

	}

	public List<AnalyteResult> getDataRows() {
		return data;
	}

}
