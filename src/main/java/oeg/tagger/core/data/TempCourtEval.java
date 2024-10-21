/**
 * Class that evaluates Annotador on the TempCourt corpus
 *  
 * 
 */
package oeg.tagger.core.data;

import java.io.File;
import oeg.tagger.core.time.tictag.AnnotadorLegal;
import oeg.tagger.core.time.tictag.AnnotadorStandard;

/**
 *  Main Class that evaluates Annotador on the TempCourt corpus
 *
 * @author mnavas
 */
public class TempCourtEval {
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    /**
     * @param args no need of command line arguments
     */
    public static void main(String[] args) {

        String[] taggers = {"ANNOTADOR"};
        String[] corpora = {"ECHR", "ECJ", "USC"};

        AnnotadorLegal ann = new AnnotadorLegal("EN");
        for (String corpus : corpora) {
            System.out.println("***************************************************************");
            System.out.println("STARTING CORPUS " + corpus);
            System.out.println("***************************************************************");
            for (String tagger : taggers) {
                System.out.println("_____________________________________________");
                System.out.println("STARTING TAGGER " + tagger + " for corpus" + corpus);
                System.out.println("_____________________________________________");
//        String tagger = "CLEARTK";
//        String corpus = "ECHR";
                File folder = new File("C:\\Users\\mnavas\\DATA\\TempCourt\\TIMEML\\PlainTimemL\\" + corpus);
//                if(tagger.equals("TARSQI")){
//                    cleanDocs(folder);
//                }
                File[] listFiles = folder.listFiles();


                File folderTagger = new File("C:\\Users\\mnavas\\DATA\\TempCourt\\TempCourt_output_corpus\\" + corpus + "\\" + tagger);
                folderTagger.mkdir();
                
                int i = 0;
                for (File f : listFiles) {
                    i = i + 1;
                    System.out.println(corpus + "|" + tagger + ": " + "Processing file " + i + "...");
                    File foutput = new File("C:\\Users\\mnavas\\DATA\\TempCourt\\TempCourt_output_corpus\\" + corpus + "\\" + tagger + "\\" + f.getName().replaceFirst("tml", "xml"));
                    FileTempCourt filetc = new FileTempCourt(f, f, foutput);
                    try{
                        String outp = ann.annotate(filetc.getTextInput(), filetc.getDCT(corpus));
                        if(filetc.writeOutputFile(outp)){                            
                            System.out.println(corpus + "|" + tagger + ": " + "File " + i + " done!");
                        } else{
                            System.out.println("ERROR WHILE WRITING");
                        }
                    }catch(Exception ex){
                    System.err.println(corpus + "|" + tagger + ": " + "Error while processing file " + i);
                    System.err.println(ex.toString());
                }
                    
                        System.out.println("------------------------");
                    
                }
            }
        }
        
                    
                    
    }
    
   
}
