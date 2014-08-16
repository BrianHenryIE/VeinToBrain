package ie.brianhenry.veintobrain.server.core;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Test;

public class ExcelImportTest {

	// Excel code for dummy hospital number:
	// =CHAR(RANDBETWEEN(65,90))&CHAR(RANDBETWEEN(65,90))&RANDBETWEEN(100,10000000000)

	@Test
	public void testImport() throws IOException {

//		System.out.println("Working Directory = " +
//	              System.getProperty("user.dir"));
		
		String testFile = "./src/test/resources/B12.xls";

		ExcelImport e = new ExcelImport("B12", testFile);

		// Our test file has the third column as the "Result" column
		
		//assertEquals(1, e.getResultColumns().size());
		//assertEquals(3, (int) e.getResultColumns().get(0));

		// It has 65535 rows of data
		
		assertEquals(65535, e.getDataRows().size());
		
	}

	// If there are 65536 rows, the user should be notified.

}
