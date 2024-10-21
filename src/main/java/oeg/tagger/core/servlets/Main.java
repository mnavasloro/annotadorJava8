package oeg.tagger.core.servlets;

import oeg.tagger.core.time.tictag.AnnotadorStandard;

/**
 * Test of the functionality of the servlets
 *
 * @author mnavas
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        try {

            String txt = "Eso será mañana por la mañana."; // Our test sentence
            System.out.println(txt);
            System.out.println("---------------------------------------------------------------");
            String parseLegalRef = parseAndTag(txt);  // We tag it
            System.out.println(parseLegalRef);  // We print the output of the tagger

        } catch (Exception ex) {
            System.out.print(ex.toString());
        }

    }

    /**
     * 
     * @param txt plain
     * @return String with the text tagged in BRAT format
     */
    public static String parseAndTag(String txt) {

        try {
            AnnotadorStandard annotador = new AnnotadorStandard("ES");   // We innitialize the tagger in Spanish
            String date = "2000-02-11"; // Our test anchor date
            if (date != null && !date.matches("\\d\\d\\d\\d-(1[012]|0\\d)-(3[01]|[012]\\d)")) // Is it valid?
            {
                date = null; // If not, we use no date (so anchor values will not be normalized)
            }
            Salida output = annotador.annotateBRAT(txt, date); // We annotate in BRAT format
            return output.txt + "\n\n" + output.format; // We return the javascript with the values to evaluate
        } catch (Exception ex) {
            System.err.print(ex.toString());
        }
        return "";
    }

}
