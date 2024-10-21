package oeg.tagger.core.time.annotationHandler;

/**
 * Class that stores and outputs as String the information needed for a BRAT
 * annotation
 *
 * @author mnavas
 */
public class BRATAnnotation {

    /* Fields */
    public String beginIndex = "";
    public String endIndex = "";
    public String type = "";
    public String id = "";
    public String value = "";

    /* Color of the BRAT annotations */
    public String colDate = "#7fa2ff";
    public String colTime = "#ffbb99";
    public String colSet = "#ccb3ff";
    public String colDur = "#99ffeb";

    /**
     *
     * @return String with the javadoc JSON of the BRAT annotation
     */
    @Override
    public String toString() {
//        return "[\n" +
//"            'T" + id + "',\n" +
//"            '" + type + "',\n" +
//"            [ [ " + beginIndex + ", " + endIndex + " ] ]\n" +
//"        ],\n" +
        return "[\n"
                + "            'TT" + id + "',\n"
                + "            '" + value + "',\n"
                + "            [ [ " + beginIndex + ", " + endIndex + " ] ]\n"
                + "        ],\n";
    }

    /**
     *
     * @return String with the javadoc JSON of the BRAT format needed for the
     * annotation
     */
    public String formatToString() {
        String outp = "{\n"
                + "            type   : '" + value + "',\n"
                + "            labels : ['" + value + "'],\n";

        if (type.equalsIgnoreCase("DATE")) {
            outp = outp + "bgColor: '" + colDate + "',\n";
        } else if (type.equalsIgnoreCase("TIME")) {
            outp = outp + "bgColor: '" + colTime + "',\n";
        } else if (type.equalsIgnoreCase("SET")) {
            outp = outp + "bgColor: '" + colSet + "',\n";
        } else if (type.equalsIgnoreCase("DURATION")) {
            outp = outp + "bgColor: '" + colDur + "',\n";
        }

        return outp
                + "            borderColor: 'darken'\n"
                + "    },";
    }

}
