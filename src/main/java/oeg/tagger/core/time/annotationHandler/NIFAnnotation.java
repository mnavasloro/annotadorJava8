package oeg.tagger.core.time.annotationHandler;

/**
 * Class that stores and outputs as String the information needed for a NIF
 * annotation
 *
 * @author mnavas
 */
public class NIFAnnotation {

    public String header = "";
    public String a = "";
    public String beginIndex = "";
    public String endIndex = "";
    public String isString = "";
    public String referenceContext = "";
    public String annotationUnit = "";

    /**
     *
     * @return String with the NIF format needed for the
     * annotation
     */
    public String toString() {
        return header + a + referenceContext + isString + beginIndex + endIndex + annotationUnit + "\n\n";
    }

}
