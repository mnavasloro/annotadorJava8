package oeg.tagger.core.time.tictag;


/**
 * Interface of Annotador, implemented for each of the domains
 * 
 * @author mnavas
 */
public interface Annotador {
    
    /**
     * 
     * @param input String text to annotate
     * @param anchorDate String date of reference
     * 
     * @return String text tagged
     */
    public String annotate(String input, String anchorDate);
    
    /**
     * 
     * @param input String text to annotate
     * @param anchorDate String date of reference
     * @param reference String NIF URI reference
     * @param lang String language code label
     * 
     * @return String text tagged as NIF
     */
    public String annotateNIF(String input, String anchorDate, String reference, String lang);
    
    /**
     * 
     * @param input String text to annotate
     * @param anchorDate String date of reference
     * 
     * @return String text tagged in JSON format
     */
    public String annotateJSON(String input, String anchorDate);
    
    
    
}
