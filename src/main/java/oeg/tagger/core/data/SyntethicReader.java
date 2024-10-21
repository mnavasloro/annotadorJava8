/**
 * Class that reads the Synthetic part of the Hourglass corpus
 *  
 * 
 */
package oeg.tagger.core.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Reader the Synthetic part of the Hourglass corpus
 * 
 * @author Maria
 */
public class SyntethicReader {

    LinkedHashMap<String, String> files = new LinkedHashMap<String, String>();

    /**
     * 
     * @param filename excel file to read
     * @param outputFolder folder to output the documents read
     * @return a map with the path to the files and their content
     * @throws IOException 
     */
    public LinkedHashMap<String, String> read(String filename, String outputFolder) throws IOException {
        File excelFile = new File(filename);
        FileInputStream fis = new FileInputStream(excelFile);

        // we create an XSSF Workbook object for our XLSX Excel File
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        // we get first sheet
        XSSFSheet sheet = workbook.getSheetAt(0);

        // we iterate on rows
        Iterator<Row> rowIt = sheet.iterator();
        rowIt.next();
        while (rowIt.hasNext()) {
            int i = 0;
            Row row = rowIt.next();
            Cell cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);

            // Name
            String ev = ".txt";
            if (cell != null) {
                ev = cell.toString().replaceAll("\\.0", "") + ev;
            }
            i++;
           
            
            // Content
            cell = row.getCell(i);
            String content = "";
            if (cell != null) {
                content = cell.toString() + "\n";
            }
            
            String filePath = outputFolder + "\\" + ev;
            
            if(!content.isEmpty() && !content.startsWith(".")){
                files.put(filePath, content);
            }

            System.out.println(ev.toString() + "done! \n------------");

        }

        workbook.close();
        fis.close();
        return files;
    }

    private static final String INPUTEXCEL = "C:\\Users\\mnavas\\CODE\\Annotador\\annotador-core\\src\\main\\resources\\rules\\TEST-ES2.xlsx";
    private static final String OUTPUTFOLDER = "C:\\Users\\mnavas\\CODE\\Annotador\\annotador-core\\src\\main\\resources\\rules\\test_input\\";

    /**
     * 
     * @param args no args used, just an example on how to use the function read
     */
    public static void main(String[] args){
        System.out.println("Test of the ExcelReader:");
        LinkedHashMap<String, String> outp = new LinkedHashMap<String, String>();
        SyntethicReader er = new SyntethicReader();
        try {
            outp = er.read(INPUTEXCEL,OUTPUTFOLDER);
        } catch (IOException ex) {
            Logger.getLogger(SyntethicReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        Set<String> oupt2 = outp.keySet();
        for(String o : oupt2){
            FileOutputStream fos = null;
            try {
                File foutput = new File(o);
                fos = new FileOutputStream(foutput.getAbsolutePath());
                OutputStreamWriter w = new OutputStreamWriter(fos, "UTF-8");
                BufferedWriter bw = new BufferedWriter(w);
                bw.write(outp.get(o));
                bw.flush();
                bw.close();
            } catch (Exception ex) {
                Logger.getLogger(SyntethicReader.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fos.close();
                } catch (IOException ex) {
                    Logger.getLogger(SyntethicReader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
