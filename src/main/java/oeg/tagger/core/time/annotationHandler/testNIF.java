package oeg.tagger.core.time.annotationHandler;

import oeg.tagger.core.time.tictag.AnnotadorStandard;

/**
 * Main Class to test the NIF conversion
 * 
 * @author mnavas
 */
public class testNIF {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
               String examplePlain = "hoy es 3 de octubre";                
               String exampleNIF = "@prefix nif: <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\n" +
"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
"@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .\n" +
"@prefix lkg: <http://lkg.lynx-project.eu/def/>\n" +
"\n" +
"<URL>\n" +
"        a             lkg:LynxDocument ;\n" +
"        nif:isString    \"hoy es 3 de octubre\" .\n" +
"\n" +
"\n" +
"<URL#offset_0_3>\n" +
"        a                     nif:Annotation ;\n" +
"        nif:referenceContext   <URL> ;\n" +
"        nif:anchorOf    \"hoy\" .\n" +
"        nif:beginIndex  0 ;\n" +
"        nif:endIndex    3 ;\n" +
"        nif:annotationUnit [\n" +
"        a lkg:LynxDocument, nif:AnnotationUnit, nif:OffsetBasedString ;\n" +
"        itsrdf:taClassRef <http://www.w3.org/2006/time#TemporalEntity> ;\n" +
"        itsrdf:taConfidence 1.0 ;\n" +
"        itsrdf:taIdentRef lkg:DATE\n" +
"        rdf:value \"2020-01-23\" ;\n" +
"        ] ;";
    
    /* TEST I : NIF2NIF */
                NIFReader nr = new NIFReader();
                NIFText nf = nr.readNIF(exampleNIF);
                String txtinput = nf.text;
                String reference = nf.reference;
            

            // ADDING replace for \r\n
            String input = txtinput;
            txtinput = txtinput.replaceAll("\\r\\n", "\\\\n");

            String txt = txtinput;

            /* TEXT HANDLING */
            
            /* ANNOTATION */
            
            // We annotate the text with asdad (EN,ES) or Heideltime (NL,IT,GE)
            
            AnnotadorStandard upm_timex = new AnnotadorStandard("es");   // We initialize the tagger in Spanish
            String res = upm_timex.annotate(txt, "2020-02-22");
            System.out.println("TEST I:\nTIMEX RESULT:\n" + res);
            
            TIMEX2NIF toNIF = new TIMEX2NIF();
            res = toNIF.insert2ExistingNIF(res, reference, exampleNIF);
            
            
            System.out.println("res TEST I:\n" + res);
            


    /* TEST II : Plain2NIF */
//                TIMEX2NIF toNIF = new TIMEX2NIF();
//                res = toNIF.translateSentence(res, reference);
    
    }
}
