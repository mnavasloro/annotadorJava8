/**
 * Class that manages Soco files
 *  
 * See also
 * @ref FileSoco
 * 
 */
package oeg.tagger.core.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager of the corpus Soco
 *
 * @author mnavas
 */
public class ManagerSoco {

    File cleanDocs = new File("C:/Users/mnavas/DATA/CORPUS SOCO/temporal/test/"); // Path to the folder with the input (clean) files
    File testDocs = new File("C:/Users/mnavas/DATA/CORPUS SOCO/temporal/checked/"); // Path to the folder with the test files
    File outDocs = new File("C:/Users/mnavas/DATA/CORPUS SOCO/temporal/output/"); // Path to the folder to store the output files

    public List<FileSoco> lista = new ArrayList<FileSoco>();

    /**
     * Constructor of ManagerSoco
     * 
     * @param fInput Path of the plain text files folder
     * @param fTest Path of the test folder
     * @param fOut Path of the output folder
     *
     */
    public ManagerSoco(String fInput, String fTest, String fOut) {
        cleanDocs = new File(fInput);
        testDocs = new File(fTest);
        outDocs = new File(fOut);

        for (File f : cleanDocs.listFiles()) {
            FileSoco ft3 = new FileSoco(f, testDocs, outDocs);
            lista.add(ft3);
        }
    }

    /**
     * Constructor of ManagerSoco
     *
     */
    public ManagerSoco() {

        for (File f : cleanDocs.listFiles()) {
            FileSoco ft3 = new FileSoco(f, testDocs, outDocs);
            lista.add(ft3);
        }
    }

}
