package oeg.tagger.core.time.annotationHandler;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.*;

/**
 * Class that converts a TIMEX annotation into a JSON annotation
 *
 * @author mnavas
 */
public class TIMEX2JSON {

    private static final Logger LOGGER = Logger.getLogger(TIMEX2JSON.class.getName());

    /**
     * Initializes a instance of the converter
     *
     * @return an instance of the converter
     */
    public TIMEX2JSON() {
        init();
    }

    public void init() {

    }

    /**
     * Converts a sentence @intput in TIMEX format into JSON
     *
     * @param input String in TIMEX format
     * @return String in JSON
     */
    public String translateSentence(String input) {
        try {
            String inp2 = input;
            
            JSONObject json = new JSONObject();

            JSONArray array = new JSONArray();
            String pattern = "<TIMEX3 tid=\"([^\"]+)\" type=\"([^\"]+)\" value=\"([^\"]+)\"[^>]*>([^<]*)<\\/TIMEX3>";
                Pattern p = Pattern.compile(pattern);
                
            while (!inp2.isEmpty()) {
                Matcher m = p.matcher(inp2);
                StringBuffer sb = new StringBuffer(inp2.length());
                if (m.find()) {
                    JSONObject item = new JSONObject();
                    int end = (m.start() + m.group(4).length());
                    item.put("beginIndex", m.start());
                    item.put("endIndex", end);
                    item.put("anchorOf", m.group(4) );
                    item.put("tid", m.group(1) );
                    item.put("type", m.group(2) );
                    item.put("value", m.group(3) );
                    
                    array.add(item);
                                       
                    m.appendReplacement(sb, m.group(4));
                    m.appendTail(sb);
                    inp2 = sb.toString();
                } 
                else {
                    break;
                }
                
                
            }
//            String message = array.toString();            
            json.put("text", inp2);
            json.put("annotations", array);
            return json.toString();

        } catch (Exception ex) {
            Logger.getLogger(TIMEX2JSON.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
