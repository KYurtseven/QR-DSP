package com.qrsynergy.ui.view.sharedocument;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Using Apache POI API read Microsoft Excel (.xls) file and convert into CSV file with Java API.
 * Using Java API read Comma Separated Values(.csv) file and convert into XLS file with Apache POI API.    
 * <p>
 * Microsoft Excel file is converted to CSV for all type of columns data.
 * </p>
 * <pre>
 * DataConvertionUtil.csvToEXCEL(csvFileName,excelFileName);
 * DataConvertionUtil.excelToCSV(excelFileName,csvFileName);
 * </pre>
 * @version 1.0, 13/July/2012
 * @author Stephen Babu.P
 *
 * See http://stephenbabu-p.blogspot.com/2012/07/convert-excel-file-to-csv-using-apache.html
 */
public class DataConvertionUtil
{
    /***
     * Date format used to convert excel cell date value
     */
    private static final String OUTPUT_DATE_FORMAT= "yyyy-MM-dd";
    /**
     * Comma separated characters
     */
    private static final String CVS_SEPERATOR_CHAR=",";
    /**
     * New line character for CSV file
     */
    private static final String NEW_LINE_CHARACTER="\r\n";

    /**
     * Convert CSV file to Excel file
     * @param csvFileName
     * @param excelFileName
     * @throws Exception
     */
    public static void csvToEXCEL(String csvFileName,String excelFileName) throws Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileName)));
        XSSFWorkbook myWorkBook = new XSSFWorkbook();
        FileOutputStream writer = new FileOutputStream(new File(excelFileName) );
        XSSFSheet mySheet = myWorkBook.createSheet();
        String line= "";
        int rowNo=0;
        while ( (line=reader.readLine()) != null ){
            String[] columns = line.split(CVS_SEPERATOR_CHAR);
            XSSFRow myRow =mySheet.createRow(rowNo);
            for (int i=0;i<columns.length;i++){
                XSSFCell myCell = myRow.createCell(i);
                myCell.setCellValue(columns[i]);
            }
            rowNo++;
        }
        myWorkBook.write(writer);
        writer.close();
    }

    /**
     * Convert the Excel file data into CSV file
     * @param excelFileName
     * @param csvFileName
     * @throws Exception
     */
    public static void excelToCSV(String excelFileName,String csvFileName) throws Exception{
        //XSSFWorkbook myWorkBook = new XSSFWorkbook(new POIFSFileSystem(new FileInputStream(excelFileName)));
        XSSFWorkbook myWorkBook = new XSSFWorkbook(excelFileName);
        XSSFSheet mySheet = myWorkBook.getSheetAt(0);
        Iterator  rowIter =  mySheet.rowIterator();
        String csvData="";
        while (rowIter.hasNext()) {
            XSSFRow myRow = (XSSFRow) rowIter.next();
            for ( int i=0;i<myRow.getLastCellNum();i++){
                csvData += getCellData(myRow.getCell(i));
            }
            csvData+=NEW_LINE_CHARACTER;
        }
        writeCSV(csvFileName, csvData);
    }
    /**
     * Write the string into a text file
     * @param csvFileName
     * @param csvData
     * @throws Exception
     */
    private static void writeCSV(String csvFileName,String csvData) throws Exception{
        FileOutputStream writer = new FileOutputStream(csvFileName);
        writer.write(csvData.getBytes());
        writer.close();
    }
    /**
     * Get cell value based on the excel column data type
     * @param myCell
     * @return
     */
    private static String getCellData( XSSFCell myCell) throws Exception{
        String cellData="";
        if ( myCell== null){
            cellData += CVS_SEPERATOR_CHAR;;
        }else{
            switch(myCell.getCellType() ){
                case  XSSFCell.CELL_TYPE_STRING  :
                case  XSSFCell.CELL_TYPE_BOOLEAN  :
                    cellData +=  myCell.getRichStringCellValue ()+CVS_SEPERATOR_CHAR;
                    break;
                case XSSFCell.CELL_TYPE_NUMERIC :
                    cellData += getNumericValue(myCell);
                    break;
                case  XSSFCell.CELL_TYPE_FORMULA :
                    cellData +=  getFormulaValue(myCell);
                default:
                    cellData += CVS_SEPERATOR_CHAR;;
            }
        }
        return cellData;
    }
    /**
     * Get the formula value from a cell
     * @param myCell
     * @return
     * @throws Exception
     */
    private static String getFormulaValue(XSSFCell myCell) throws Exception{
        String cellData="";
        if ( myCell.getCachedFormulaResultType() == XSSFCell.CELL_TYPE_STRING  || myCell.getCellType () ==XSSFCell.CELL_TYPE_BOOLEAN) {
            cellData +=  myCell.getRichStringCellValue ()+CVS_SEPERATOR_CHAR;
        }else  if ( myCell.getCachedFormulaResultType() == XSSFCell.CELL_TYPE_NUMERIC ) {
            cellData += getNumericValue(myCell)+CVS_SEPERATOR_CHAR;
        }
        return cellData;
    }
    /**
     * Get the date or number value from a cell
     * @param myCell
     * @return
     * @throws Exception
     */
    private static String getNumericValue(XSSFCell myCell) throws Exception {
        String cellData="";
        if ( HSSFDateUtil.isCellDateFormatted(myCell) ){
            cellData += new SimpleDateFormat(OUTPUT_DATE_FORMAT).format(myCell.getDateCellValue()) +CVS_SEPERATOR_CHAR;
        }else{
            cellData += new BigDecimal(myCell.getNumericCellValue()).toString()+CVS_SEPERATOR_CHAR ;
        }
        return cellData;
    }
}