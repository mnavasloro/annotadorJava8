/**
 * Class that manages TimeBank files
 *  
 * See also
 * @ref FileTimeBank
 * 
 */
package oeg.tagger.core.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 * Manager of the corpus TimeBank
 *
 * @author mnavas
 */
public class ManagerTimeBank {

    String rootData = "C:/Users/mnavas/CODE/OLD_CODE/data";
    File cleanDocs = new File(rootData + "/datasets/timeEval/timebank/test-clean/"); // Path to the folder with the input (clean) files
    File testDocs = new File(rootData + "/datasets/timeEval/timebank/test/"); // Path to the folder with the test files
    File outDocs = new File(rootData + "/datasets/timeEval/timebank/output/"); // Path to the folder to store the output files

    public List<FileTimeBank> lista = new ArrayList<FileTimeBank>();

    /**
     * Constructor of ManagerTimeBank
     * 
     * @param fInput Path of the plain text files folder
     * @param fTest Path of the test folder
     * @param fOut Path of the output folder
     *
     */
    public ManagerTimeBank(String fInput, String fTest, String fOut) {
        cleanDocs = new File(fInput);
        testDocs = new File(fTest);
        outDocs = new File(fOut);

        if (cleanDocs.length() == 0) {
            cleanDocs();
        }

        for (File f : cleanDocs.listFiles()) {
            FileTimeBank ft3 = new FileTimeBank(f, testDocs, outDocs);
            lista.add(ft3);
        }
    }

    /**
     * Constructor of ManagerTimeBank
     *
     */
    public ManagerTimeBank() {
        File[] a = cleanDocs.listFiles();
        if (cleanDocs.listFiles().length != testDocs.listFiles().length) {
            cleanDocs();
        }

        for (File f : cleanDocs.listFiles()) {
            FileTimeBank ft3 = new FileTimeBank(f, testDocs, outDocs);
            lista.add(ft3);
        }
    }

    /**
     * Cleans the docs of TimeML tags
     *
     * @return boolean TRUE (ended correctly) or FALSE (Exception)
     */
    public boolean cleanDocs() {
        for (File f : testDocs.listFiles()) {
            try {
                String input = FileUtils.readFileToString(f, "UTF-8");
                String body = FileTimeBank.getBody(input);
                String text = FileTimeBank.getText(body);
                String text2 = text;
                text2 = text2.replaceAll("<[^>]*>", "");
                String output = body.replace(text, text2);
                File file2 = new File(cleanDocs.getCanonicalFile() + "/" + f.getName());
                if (!writeOutputFile(output, file2)) {
                    return false;
                }
                System.out.println("File: " + f.getName() + " cleaned");

            } catch (IOException ex) {
                Logger.getLogger(FileTimeBank.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }

    /**
     * Writes an output file
     *
     * @param tagged String with the content to write down
     * @param f File to write the content
     * @return boolean TRUE (ended correctly) or FALSE (Exception)
     */
    public boolean writeOutputFile(String tagged, File f) {
        try {
            FileOutputStream fos = new FileOutputStream(f.getAbsolutePath());
            OutputStreamWriter w = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(w);
            bw.write(tagged);
            bw.flush();
            bw.close();
            System.out.println("File: " + f.getName() + " done. \n-------------------------");
            return true;
        } catch (Exception ex) {
            Logger.getLogger(ManagerTimeBank.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
