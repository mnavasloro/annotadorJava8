/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oeg.tagger.main;


import edu.stanford.nlp.util.logging.RedwoodConfiguration;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import oeg.tagger.core.time.tictag.Annotador;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import oeg.tagger.core.time.tictag.AnnotadorLegal;
import oeg.tagger.core.time.tictag.AnnotadorStandard;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

/**
 * Main class of the jar.
 *
 * @author vroddon
 * @author mnavas
 */
public class MainNoArguments {

    static final Logger logger = Logger.getLogger(Main.class.getName());
    static boolean logs = false;
    static String lang = "es";
    static String domain = "standard";
    static String date = "";
    static String format = "timeml";
    static String outpfilename = null;
    static String text1 = "Hoy es 4 de octubre";

    public static void main(String[] args) {

        // We do this to avoid the Ixa-Pipes debugging messages...
        PrintStream systemerr = System.err;

        init(args);
        
        String res = parsear(args);
       if (!res.isEmpty()) {
                System.out.println(res);
        }
        
        System.setErr(systemerr);
    }

    public static void init(String[] args) {
        logs = Arrays.asList(args).contains("-logs");
        initLogger(logs);
        
        
        //Welcome message
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(new FileReader("pom.xml"));
            String welcome = model.getArtifactId() + " " + model.getVersion() + "\n-----------------------------------------------------\n";
            logger.info(welcome);
        } catch (Exception e) {
        }

    }

    public static String parsear(String[] args) {
        ///Response
        StringBuilder res = new StringBuilder();
        CommandLineParser parser = null;
        CommandLine cmd = null;
        try {

            Options options = new Options();
            options.addOption("nologs", false, "OPTION to disables logs");
            options.addOption("logs", false, "OPTION to enable logs");
            options.addOption("lang", true, "OPTION to change language [ES, EN] (Spanish by default)");
            options.addOption("domain", true, "OPTION to change domain [standard, legal] (Standard by default)");
            options.addOption("date", true, "OPTION to add an anchor date in the format yyyy-mm-dd (today by default)");
            options.addOption("text", true, "COMMAND to parse a text");
            options.addOption("f", true, "COMMAND to parse a file");
            options.addOption("outf", true, "COMMAND to save the output to a file"); 
            options.addOption("format", true, "COMMAND to choose the output format [timeml,json,nif] (TimeML by default)"); 
            options.addOption("help", false, "COMMAND to show help (Help)");
            parser = new BasicParser();
            cmd = parser.parse(options, args);
            String outp = "";
            
            if (cmd.hasOption("help") || args.length == 0) {
                new HelpFormatter().printHelp(Main.class.getCanonicalName(), options);
            }
            if (cmd.hasOption("lang")) {
                lang = cmd.getOptionValue("lang");
            }
            if (cmd.hasOption("domain")) {
                domain = cmd.getOptionValue("domain");
            }
//            if (!cmd.hasOption("logs")) {
//                initLoggerDisabled();
//            }
            if (cmd.hasOption("date")) {
                date = cmd.getOptionValue("date");
            }
            if (cmd.hasOption("format")) {
                format = cmd.getOptionValue("format");
            }
            if (cmd.hasOption("f")) {
                String filename = cmd.getOptionValue("f");
                logger.info("Trying to parse the file " + filename);
                outp = parse(filename);
            }
            
            String text = text1;
            logger.info("Trying to process the text " + text);
            outp = parseText(text);
            
            if(cmd.hasOption("outf")){
                outpfilename = cmd.getOptionValue("outf");
                if(!writeFile(outp, outpfilename)){
                    logger.warning("Error while writing."); // ERROR
                } else{
                    logger.info("Output correctly written to " + outpfilename);
                }
            }
            if(outp != null){                
                if(logs){
                    System.out.println("\n----------------\n");
                }
                System.out.println(outp);
                if(logs){
                    System.out.println("\n----------------\n");
                }
            }

        } catch (Exception e) {
System.out.println(e.toString());
        }

        return res.toString();
    }

    public static String parse(String filename) {   
        String res = "";
        try {
            File f = new File(filename);
            logger.info("parsing the folder " + filename); // DEBUG
            String input = FileUtils.readFileToString(f, "UTF-8");
            res = parseText(input);
                
        } catch (Exception e) {
            logger.warning("error opening file"); // ERROR
            return "";
        }
        logger.info("Parsing correct\n\n");
        return res;
    }
    
    public static String parseText(String txt) {
        String res = "";
        Date dct = null;
        try{
            if (!date.matches("\\d\\d\\d\\d-(1[012]|0\\d)-(3[01]|[012]\\d)")) // Is it valid?
        {
            logger.info("No correct date provided, ");
            dct = Calendar.getInstance().getTime();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            date = df.format(dct);
        }
        
        Annotador annotador;
        
        if(domain.equalsIgnoreCase("standard")){
            if(lang.equalsIgnoreCase("ES")){
                   // We innitialize the tagger in Spanish        
                   annotador = new AnnotadorStandard("es");
            }
            else if(lang.equalsIgnoreCase("EN")){
                annotador = new AnnotadorStandard("en");
            }
            else{
                logger.warning("error in language; for now, just available ES and EN"); // ERROR
                return res;
            }
        } else if(domain.equalsIgnoreCase("legal")){
            if(lang.equalsIgnoreCase("ES")){
                   // We innitialize the tagger in Spanish        
                   annotador = new AnnotadorLegal("es");
            }
            else if(lang.equalsIgnoreCase("EN")){
                annotador = new AnnotadorLegal("en");
            }
            else{
                logger.warning("error in language; for now, just available ES and EN"); // ERROR
                return res;
            }
        } else{
            logger.warning("error in domain; for now, just available standard and legal"); // ERROR
                return res;
        }
        
        if(format.equalsIgnoreCase("timeml")){
            res = annotador.annotate(txt, date);
        } else if(format.equalsIgnoreCase("nif")){
            res = annotador.annotateNIF(txt, date, "ref", lang);
        } else if(format.equalsIgnoreCase("json")){
            res = annotador.annotateJSON(txt, date);
        } else{
            logger.warning("Incorrect format; TimeML will be used."); // ERROR
            res = annotador.annotate(txt, date);
        }
        
        } catch (Exception e) {
            logger.warning("error processing text:\n" + res); // ERROR
            return "";
        }
       logger.info("Text processing correct:\n" + res);

        return res;
    }

    public static void initLogger(boolean logs) {
        if (logs) {
            initLoggerDebug();
        } else {
            initLoggerDisabled();
        }

    }

    /**
     * Shuts up all the loggers. 
     * Also the logs from third parties.
     */
    private static void initLoggerDisabled() {
        Logger.getLogger("").setLevel(Level.FINEST);
//
//        List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
//        loggers.add(LogManager.getRootLogger());
//        for (Logger log : loggers) {
//            log.setLevel(Level.OFF);
//        }
//        Logger.getRootLogger().setLevel(Level.OFF);
        
        // We do this to void IxaPipes messages...
        PrintStream falseerr = new PrintStream(new OutputStream(){
            public void write(int b) {
            }
        });
        System.setErr(falseerr);
        
        // We turn off CoreNLP logger
        RedwoodConfiguration.current().clear().apply();        
        
        // We turn off some inner IxaPipes loggers
//        ch.qos.logback.classic.Logger logger1 = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(SpanishReadabilityModel.class);
//        logger1.setLevel(ch.qos.logback.classic.Level.OFF);           
//        ch.qos.logback.classic.Logger logger2 = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Hyphenator.class);
//        logger2.setLevel(ch.qos.logback.classic.Level.OFF);
//        ch.qos.logback.classic.Logger logger3 = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(BasicAnnotator.class);
//        logger3.setLevel(ch.qos.logback.classic.Level.OFF);
//        
//        logger.setLevel(Level.OFF);

//        Logger.getRootLogger().removeAllAppenders();
//Logger.getRootLogger().addAppender(new NullAppender());
    }

    /**
     * Si se desean logs, lo que se hace es: - INFO en consola - DEBUG en
     * archivo de logs logs.txt
     */
    private static void initLoggerDebug() {

//        Enumeration currentLoggers = LogManager.getCurrentLoggers();
//        List<Logger> loggers = Collections.<Logger>list(currentLoggers);
//        loggers.add(LogManager.getRootLogger());
//        for (Logger log : loggers) {
//            log.setLevel(Level.OFF);
//        }
//
//        Logger root = Logger.getRootLogger();
//        root.setLevel((Level) Level.DEBUG);
//
//        //APPENDER DE CONSOLA (INFO)%d{ABSOLUTE} 
//        PatternLayout layout = new PatternLayout("%d{HH:mm:ss} [%5p] %13.13C{1}:%-4L %m%n");
//        ConsoleAppender appenderconsole = new ConsoleAppender(); //create appender
//        appenderconsole.setLayout(layout);
//        appenderconsole.setThreshold(Level.INFO);
//        appenderconsole.activateOptions();
//        appenderconsole.setName("console");
//        root.addAppender(appenderconsole);
//
//        //APPENDER DE ARCHIVO (DEBUG)
//        PatternLayout layout2 = new PatternLayout("%d{ISO8601} [%5p] %13.13C{1}:%-4L %m%n");
//        FileAppender appenderfile = null;
//        String filename = "./logs/logs.txt";
//        try {
//            MavenXpp3Reader reader = new MavenXpp3Reader();
//            Model model = reader.read(new FileReader("pom.xml"));
//            filename = "./logs/" + model.getArtifactId() + ".txt";
//        } catch (Exception e) {
//        }
//        try {
//            appenderfile = new FileAppender(layout2, filename, false);
//            appenderfile.setName("file");
//            appenderfile.setThreshold(Level.DEBUG);
//            appenderfile.activateOptions();
//        } catch (Exception e) {
//        }
//
//        root.addAppender(appenderfile);
//
//
//        logger = Logger.getLogger(Main.class.getName());
    }
    public static boolean writeFile(String input, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            OutputStreamWriter w = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(w);
            bw.write(input);
            bw.flush();
            bw.close();
            return true;
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Annotador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return false;
    }

}

