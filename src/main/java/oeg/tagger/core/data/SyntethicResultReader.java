/**
 * Class that reads the annotated version Synthetic part of the Hourglass corpus
 *  
 * 
 */
package oeg.tagger.core.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Class that reads the annotated version Synthetic part of the Hourglass corpus
 *
 * @author Maria
 */
public class SyntethicResultReader {

    static LinkedHashMap<String, String> files = new LinkedHashMap<String, String>();
    static LinkedHashMap<String, String> filesXML = new LinkedHashMap<String, String>();
    static LinkedHashMap<String, String> filesResult = new LinkedHashMap<String, String>();

    /**
     * 
     * @param filename excel file to read
     * @param outputFolder folder to output the documents read in txt
     * @param outputFolderXML folder to output the documents read in xml
     * @param outputFolderResult folder to output the annotated documents read in xml
     * @return a map with the path to the files and their content
     * @throws IOException 
     */
    public LinkedHashMap<String, String> read(String filename, String outputFolder, String outputFolderXML, String outputFolderResult) throws IOException {
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
            String ev = "";
            if (cell != null) {
                ev = cell.toString().replaceAll("\\.0", "");
            }
            i++;
           
            
            // Content
            cell = row.getCell(i);
            String content = "";
            if (cell != null) {
                content = cell.toString() + "\n";
            }
            
            i++;
            
            
            // Prov
            cell = row.getCell(i);
            String prov = "";
            if (cell != null) {
                prov = cell.toString() + "\n";
            }
            
            
            i++;
            
            // Tag
            cell = row.getCell(i);
            String tag = "";
            if (cell != null) {
                tag = cell.toString() + "\n";
            }
            
            
            i++;
            
            // Registry
            cell = row.getCell(i);
            String reg = "";
            if (cell != null) {
                reg = cell.toString() + "\n";
            }
            
            
            i++;
            
            // Test
            cell = row.getCell(i);
            String result = "";
            if (cell != null) {
                result = cell.toString() + "\n";
            }
            
            String filePath = outputFolder + "\\" + ev + ".txt";
            String filePathXML = outputFolderXML + "\\" + ev + ".tml";
            String filePathResult = outputFolderResult + "\\" + ev + ".tml";
            
            if(!content.isEmpty() && !content.startsWith(".")){
                files.put(filePath, content);
            }
            
            if(!content.isEmpty() && !content.startsWith(".")){
                filesXML.put(filePathXML, content);
            }
            
            if(!result.isEmpty() && !result.startsWith(".")){
                filesResult.put(filePathResult, result);
            }

            System.out.println(ev.toString() + "done! \n------------");

        }

        workbook.close();
        fis.close();
        return files;
    }

    private static final String INPUTEXCEL = "C:\\Users\\mnavas\\CODE\\Annotador\\annotador-core\\src\\main\\resources\\rules\\FINAL-HOURGLASS.xlsx";
    private static final String OUTPUTFOLDER = "C:\\Users\\mnavas\\CODE\\Annotador\\annotador-core\\src\\main\\resources\\rules\\HOURGLASS_RESULTS\\test_input_plain\\";
    private static final String OUTPUTFOLDERXML = "C:\\Users\\mnavas\\CODE\\Annotador\\annotador-core\\src\\main\\resources\\rules\\HOURGLASS_RESULTS\\test_input_XML\\";
    private static final String OUTPUTFOLDERRESULT = "C:\\Users\\mnavas\\CODE\\Annotador\\annotador-core\\src\\main\\resources\\rules\\HOURGLASS_RESULTS\\test_Result\\";

    /**
     * 
     * @param args no args used, just an example on how to use the function read
     */
    public static void main(String[] args){
        System.out.println("Test of the ExcelReader:");
        LinkedHashMap<String, String> outp = new LinkedHashMap<String, String>();
        SyntethicResultReader er = new SyntethicResultReader();
        try {
            outp = er.read(INPUTEXCEL,OUTPUTFOLDER,OUTPUTFOLDERXML,OUTPUTFOLDERRESULT);
        } catch (IOException ex) {
            Logger.getLogger(SyntethicResultReader.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(SyntethicResultReader.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        
        Set<String> xmlout = filesXML.keySet();
        for(String o : xmlout){
            FileOutputStream fos = null;
            try {
                File foutput = new File(o);
                fos = new FileOutputStream(foutput.getAbsolutePath());
                OutputStreamWriter w = new OutputStreamWriter(fos, "UTF-8");
                BufferedWriter bw = new BufferedWriter(w);
                String txt = "<?xml version=\"1.0\" ?>\n" +
"<TimeML xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://timeml.org/timeMLdocs/TimeML_1.2.1.xsd\">\n" +
"\n" +
                                                "\n" +
"\n" +
"<TEXT>" + filesXML.get(o) + "</TEXT>\n" +
"\n" +
"</TimeML>";
                
                bw.write(txt);
                bw.flush();
                bw.close();
            } catch (Exception ex) {
                Logger.getLogger(SyntethicResultReader.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        
        
                Set<String> resout = filesResult.keySet();
        for(String o : resout){
            FileOutputStream fos = null;
            try {
                File foutput = new File(o);
                fos = new FileOutputStream(foutput.getAbsolutePath());
                OutputStreamWriter w = new OutputStreamWriter(fos, "UTF-8");
                BufferedWriter bw = new BufferedWriter(w);
                String txt = "<?xml version=\"1.0\" ?>\n" +
"<TimeML xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://timeml.org/timeMLdocs/TimeML_1.2.1.xsd\">\n" +
"\n" +
                                                "\n" +
"\n" +
"<TEXT>" + filesResult.get(o) + "</TEXT>\n" +
"\n" +
"</TimeML>";
                
                bw.write(txt);
                bw.flush();
                bw.close();
            } catch (Exception ex) {
                Logger.getLogger(SyntethicResultReader.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
}
