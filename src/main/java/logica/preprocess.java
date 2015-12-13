// Archivo donde se calcula la matriz de caracteristicas

// Filas i representan personas 
// Columnas j representan caracteristicas

//Al final, features[i][j] representa el numero de palabras que pertenecen a la caracteristica j, para el usuario i

package logica;

import logica.HarvardDictionary;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.Arrays;
import java.util.HashMap;

import logica.HibernateUtil;

import org.hibernate.Query;
import org.hibernate.Session;

import java.io.PrintWriter;
import java.io.File;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;


import logica.otherCategories;
import logica.otherCategories2;

public class preprocess{

// Hash de palabras contenidas en los comentarios
// Se utiliza para no buscar mas de una vez la misma palabra en la BD

static HashMap<String,int[]> wordsMap = new HashMap<String,int[]>();
// Matriz de caracteristicas
public static int[][] features = new int[254][];
public static int numOfCategories = 207;


  public static void main(String[] args) throws IOException {
    
    long startTime;
    long endTime;
    long totalTime;

    startTime = System.currentTimeMillis();
    ReadCSV obj = new ReadCSV();
    //obj.loadOtherCategories();
    obj.run();
   
    searchFeatures();
    endTime  = System.currentTimeMillis();
    totalTime = (endTime - startTime)/1000;
    System.out.println("searchFeatures " + totalTime);

    printInFile();

    /*startTime = System.currentTimeMillis();


    searchInDB();
    endTime  = System.currentTimeMillis();
    totalTime = (endTime - startTime)/1000;
    System.out.println("searchInDB " + totalTime);


    startTime = System.currentTimeMillis();
    searchFeaturesInDB2();
    endTime  = System.currentTimeMillis();
    totalTime = (endTime - startTime)/1000;
    System.out.println("searchFeaturesInDB2 " + totalTime);
    
    startTime = System.currentTimeMillis();
    searchFeatures();
    endTime  = System.currentTimeMillis();
    totalTime = (endTime - startTime)/1000;
    System.out.println("searchFeatures " + totalTime);

    


    startTime = System.currentTimeMillis();
    searchFeaturesInDB();
    endTime  = System.currentTimeMillis();
    totalTime = (endTime - startTime)/1000;
    System.out.println("searchFeaturesInDB " + totalTime);*/
}

    // Metodo que busca y cuenta caracteristicas en la BD de harvard
    public static void searchFeaturesInDB() throws IOException {
    File file;
    PrintWriter writer;

    File file2;
    PrintWriter writer2;

    // Archivo donde estan guardadas las caracteristicas del diccionario de Harvard
    String harvardFile = "/home/susana/Miniproyecto/preprocess/src/main/java/logica/caracteristicas_harvard.txt";
    BufferedReader br = null;
    String line = "";

    // Arreglo que tiene las caracteristicas del diccionario de Harvard
    String[] harvard_features = new String[182];

    Session session = HibernateUtil.getSessionFactory().openSession();
    String hql;
    Query query;
    List<Object[]> results;
    br = new BufferedReader(new FileReader(harvardFile));
    //file = new File ("/home/susana/Miniproyecto/preprocess/src/main/java/logica/CommentsTagged/CARACTERISTICAS.txt");
    //writer = new PrintWriter(file, "UTF-8");


    // Creacion del query
    // Se lee en la variable 'line' cada una de las lineas del archivo harvardFile
    // Es decir, line tiene las caracteristicas del diccionario de Harvard
    // Se usa para construir el query
     int m = 0;
    hql = "SELECT ";
    file2 = new File ("/home/susana/Miniproyecto/preprocess/src/main/java/logica/CommentsTagged/Caracteristicas.txt");    
    writer2 = new PrintWriter(file2, "UTF-8");
    writer2.print("Usuario");  

    while ((line = br.readLine()) != null) {
        if (m == 0) {            
            hql = hql + "COALESCE(SUM(" + line + "),0)";
        }
        else {
            hql = hql + ", COALESCE(SUM(" + line + "),0)";
        }
        harvard_features[m] = line;
        writer2.print(" "  + harvard_features[m]);
        m++;  
       
    }
    writer2.print(" "  + "cEXT");
    writer2.print(" "  + "cNEU");
    writer2.print(" "  + "cAGR");
    writer2.print(" "  + "cCON");
    writer2.print(" "  + "cOPN");
    writer2.print(" "  + "sEXT");
    writer2.print(" "  + "sNEU");
    writer2.print(" "  + "sAGR");
    writer2.print(" "  + "sCON");
    writer2.print(" "  + "sOPN");
    hql = hql + "FROM HarvardDictionary WHERE Entry IN (";
    String hql2;
    boolean found = false;

    for (int i = 0; i < features.length; i++) {
        writer2.println("\n");
        writer2.print("Usuario"+i+": ");
        // Parte final del query que contiene cada comentario separado por palabras
        hql2 = hql + ReadCSV.usrCommentsSeparated[i] +")";

        // 182 es el numero de caracteristicas del diccionario de Harvard
        features[i] = new int[numOfCategories];

        // Estoy guardando en archivos distintos el conteo de caracteristicas. Un archivo por persona
        file = new File ("/home/susana/Miniproyecto/preprocess/src/main/java/logica/CommentsTagged/Caracteristicas" + i + ".txt");
        writer = new PrintWriter(file, "UTF-8");
            
            // Se hace la consulta
            query = session.createQuery(hql2);
            results = query.list();

            // Se guardan los resultados de la consulta en la matriz features
            // La consulta devuelve un arreglo de enteros
            // Cada casilla del arreglo representa el numero de palabras que pertenecen a la caracteristica correspondiente
            // En el caso del diccionario de Harvard, son 182 caracteristicas

            for(Object[] arr : results){
                for (int j = 0; j < arr.length; j++) {
                    // Suma del numero de palabras que pertenecen a la caracteristica j
                    features[i][j] = ((Number)arr[j]).intValue();

                    //Se escribe en el archivo mencionado anteriormente
                    writer.println(harvard_features[j] + " " + features[i][j]);
                    writer2.print(features[i][j] + " ");   
                }
            }

            writer2.print(ReadCSV.cBigFive[i][0] + " "); 
            writer2.print(ReadCSV.cBigFive[i][1] + " "); 
            writer2.print(ReadCSV.cBigFive[i][2] + " "); 
            writer2.print(ReadCSV.cBigFive[i][3] + " "); 
            writer2.print(ReadCSV.cBigFive[i][4] + " "); 
            writer2.print(ReadCSV.sBigFive[i][0] + " "); 
            writer2.print(ReadCSV.sBigFive[i][1] + " "); 
            writer2.print(ReadCSV.sBigFive[i][2] + " "); 
            writer2.print(ReadCSV.sBigFive[i][3] + " "); 
            writer2.print(ReadCSV.sBigFive[i][4] + " "); 
            writer.close();
        
    }
    session.close();  
    writer2.close();  
  }
  public static void searchFeatures() throws IOException {
        File file;
    PrintWriter writer;

    File file2;
    PrintWriter writer2;

    // Archivo donde estan guardadas las caracteristicas del diccionario de Harvard
    String harvardFile = "/home/susana/Miniproyecto/preprocess/src/main/java/logica/caracteristicas_harvard.txt";
    BufferedReader br = null;
    String line = "";

    // Arreglo que tiene las caracteristicas del diccionario de Harvard
    String[] harvard_features = new String[182];
    //String[] harvard_features= new String[]{"Positiv", "Negativ", "Pstv", "Affil", "Ngtv", "Hostile", "Strong", "Pow1", "Weak", "Submit", "Active", "Passive", "Pleasur", "Pain", "Feel", "Arousal", "EMOT", "Virtue", "Vice", "Ovrst", "Undrst", "Academ", "Doctrin", "Econat", "Exch", "ECON1", "Exprsv", "Legal", "Milit", "Polit", "POLIT1", "Relig", "Role", "COLL", "Work1", "Ritual", "SocRel", "Race", "Kin", "MALE", "Female", "Nonadlt", "HU", "ANI", "PLACE", "Social", "Region", "Route", "Aquatic", "Land", "Sky", "Object1", "Tool", "Food", "Vehicle", "BldgPt", "ComnObj", "NatObj", "BodyPt", "ComForm", "COM", "Say", "Need", "Goal", "Try", "Means", "Persist", "Complet", "Fail", "NatrPro", "Begin1", "Vary", "Increas", "Decreas", "Finish", "Stay", "Rise", "Exert", "Fetch1", "Travel", "Fall", "Think", "Know", "Causal", "Ought", "Perceiv", "Compare", "Eval", "EVAL1", "Solve", "Absat", "ABS1", "Quality", "Quan", "NUMB", "ORD1", "CARD", "FREQ", "DIST", "Timeat", "TIME1", "Space1", "POS", "DIM", "Rel", "COLOR", "Self", "Our", "You", "Name", "Yes", "No1", "Negate", "Intrj", "IAV", "DAV", "SV", "IPadj", "IndAdj", "PowGain", "PowLoss", "PowEnds", "PowAren", "PowCon", "PowCoop", "PowAuPt", "PowPt", "PowDoct", "PowAuth", "PowOth", "PowTot", "RcEthic", "RcRelig", "RcGain", "RcLoss", "RcEnds", "RcTot", "RspGain", "RspLoss", "RspOth", "RspTot", "AffGain", "AffLoss", "AffPt", "AffOth", "AffTot", "WltPt", "WltTran", "WltOth", "WltTot", "WlbGain", "WlbLoss", "WlbPhys", "WlbPsyc", "WlbPt", "WlbTot", "EnlGain", "EnlLoss", "EnlEnds", "EnlPt", "EnlOth", "EnlTot", "SklAsth", "SklPt", "SklOth", "SklTot", "TrnGain", "TrnLoss", "TranLw", "MeansLw", "EndsLw", "ArenaLw", "PtLw", "Nation", "Anomie", "NegAff", "PosAff", "SureLw", "If1", "NotLw", "TimeSpc", "FormLw"};

    Session session = HibernateUtil.getSessionFactory().openSession();
    String hql;
    Query query;
    List<Object[]> results;
   br = new BufferedReader(new FileReader(harvardFile));
    int m = 0;

    while ((line = br.readLine()) != null) {
        harvard_features[m] = line;
        //System.out.print("\"" + line + "\", ");
        //writer2.print(" "  + harvard_features[m]);
        m++;  
       
    }
    String hql2;
    boolean found = false;
    String[] aux;
    String[] stemmed;
    String othTags = "";
    String defined = "";

    long startTime = System.currentTimeMillis();

    for (int i = 0; i < ReadCSV.usrCommentsSeparated.length; i++) {
        //System.out.println("Nuevo usuario" + i);
        aux = (ReadCSV.usrCommentsSeparated[i].replace("\'", "")).split(", ");
        stemmed = (ReadCSV.wordsStemmed[i].replace("\'", "")).split(", ");
        features[i] = new int[numOfCategories];
        /*String comment = String.join(" ", aux);
        //System.out.println(comment);
        for (int h = 0; h < otherCategories.adverbs.length; h++) {
            if (comment.contains(otherCategories.adverbs[h])) {
                features[i][182] = features[i][182] + 1;
            }
        }
        for (int h = 0; h < otherCategories.anger.length; h++) {
            if (comment.contains(otherCategories.anger[h])) {
                features[i][183] = features[i][183] + 1;
            }
        }
        for (int h = 0; h < otherCategories.swearWords.length; h++) {
            if (comment.contains(otherCategories.swearWords[h])) {
                features[i][184] = features[i][184] + 1;
            }
        }
        for (int h = 0; h < otherCategories.sadness.length; h++) {
            if (comment.contains(otherCategories.sadness[h])) {
                features[i][185] = features[i][185] + 1;
            }
        }
        for (int h = 0; h < otherCategories.openness.length; h++) {
            if (comment.contains(otherCategories.openness[h])) {
                features[i][186] = features[i][186] + 1;
            }
        }
        for (int h = 0; h < otherCategories.conscientiousness.length; h++) {
            if (comment.contains(otherCategories.conscientiousness[h])) {
                features[i][187] = features[i][187] + 1;
            }
        }
        for (int h = 0; h < otherCategories.extraversion.length; h++) {
            if (comment.contains(otherCategories.extraversion[h])) {
                features[i][188] = features[i][188] + 1;
            }
        }
        for (int h = 0; h < otherCategories.agreableness.length; h++) {
            if (comment.contains(otherCategories.agreableness[h])) {
                features[i][189] = features[i][189] + 1;
            }
        }
        for (int h = 0; h < otherCategories.neuroticism.length; h++) {
            if (comment.contains(otherCategories.neuroticism[h])) {
                features[i][190] = features[i][190] + 1;
            }
        }*/
            for (int j = 0; j < aux.length; j++) {
                if (otherCategories.adverbs.contains(aux[j])) {
                    features[i][182] = features[i][182] + 1;
                }
                if (otherCategories.anger.contains(aux[j])) {
                    features[i][183] = features[i][183] + 1;
                }
                if (otherCategories.swearWords.contains(aux[j])) {
                    features[i][184] = features[i][184] + 1;
                }
                if (otherCategories.sadness.contains(aux[j])) {
                    features[i][185] = features[i][185] + 1;
                }
                if (otherCategories.openness.contains(aux[j])) {
                    features[i][186] = features[i][186] + 1;
                }
                if (otherCategories.conscientiousness.contains(aux[j])) {
                    features[i][187] = features[i][187] + 1;
                }
                if (otherCategories.extraversion.contains(aux[j])) {
                    features[i][188] = features[i][188] + 1;
                }
                if (otherCategories.agreableness.contains(aux[j])) {
                    features[i][189] = features[i][189] + 1;
                }
                if (otherCategories.neuroticism.contains(aux[j])) {
                    features[i][190] = features[i][190] + 1;
                }
                if (otherCategories2.beauty.contains(aux[j])) {
                    features[i][191] = features[i][191] + 1;
                }
                if (otherCategories2.behaviour.contains(aux[j])) {
                    features[i][192] = features[i][192] + 1;
                }
                if (otherCategories2.biology.contains(aux[j])) {
                    features[i][193] = features[i][193] + 1;
                }
                if (otherCategories2.science.contains(aux[j])) {
                    features[i][194] = features[i][194] + 1;
                }
                if (otherCategories2.earth.contains(aux[j])) {
                    features[i][195] = features[i][195] + 1;
                }
                if (otherCategories2.entrepreneurship.contains(aux[j])) {
                    features[i][196] = features[i][196] + 1;
                }
                if (otherCategories2.faith.contains(aux[j])) {
                    features[i][197] = features[i][197] + 1;
                }
                if (otherCategories2.fashion.contains(aux[j])) {
                    features[i][198] = features[i][198] + 1;
                }
                if (otherCategories2.friends.contains(aux[j])) {
                    features[i][199] = features[i][199] + 1;
                }
                if (otherCategories2.law.contains(aux[j])) {
                    features[i][200] = features[i][200] + 1;
                }
                if (otherCategories2.marriage.contains(aux[j])) {
                    features[i][201] = features[i][201] + 1;
                }
                if (otherCategories2.military.contains(aux[j])) {
                    features[i][202] = features[i][202] + 1;
                }
                if (otherCategories2.music.contains(aux[j])) {
                    features[i][203] = features[i][203] + 1;
                }
                if (otherCategories2.occasions.contains(aux[j])) {
                    features[i][204] = features[i][204] + 1;
                }
                if (otherCategories2.vacations.contains(aux[j])) {
                    features[i][205] = features[i][205] + 1;
                }
                if (otherCategories2.violence.contains(aux[j])) {
                    features[i][206] = features[i][206] + 1;
                }

                   /*if (wordsMap.containsKey(aux[j])) {
                    //System.out.println(aux[j]);
                    int[] values = wordsMap.get(aux[j]);
                    for (int k = 0; k < values.length; k++) {
                        //System.out.print((String)arr[0] + " " + ((Number)arr[k]).intValue());  
                        features[i][k] = features[i][k] + values[k];

                    }    
                }
                else {*/
                    if (ReadCSV.posTags[i][j].equals("CC")) {
                        othTags = "CONJ";
                        defined = "conjunction";
                    } else  if (ReadCSV.posTags[i][j].equals("CD")) {
                        othTags = "DET";
                        defined = "cardinal number";
                    } else  if (ReadCSV.posTags[i][j].equals("DT")) {
                        othTags = "DET ART";
                    } else  if (ReadCSV.posTags[i][j].equals("EX")) {
                        othTags = "PRON";
                        defined = "existential";
                    } else  if (ReadCSV.posTags[i][j].equals("IN")) {
                        othTags = "PREP";
                        defined = "prep";
                    } else  if (ReadCSV.posTags[i][j].equals("JJ")) {
                        othTags = "Modif";
                        defined = "adj";
                        //System.out.println("ENTRE " + defined);
                    } else  if (ReadCSV.posTags[i][j].equals("JJR")) {
                        othTags = "";
                        defined = "adj%comparative";
                    } else  if (ReadCSV.posTags[i][j].equals("JJS")) {
                        othTags = "";
                        defined = "adj%superlative";
                    } else  if (ReadCSV.posTags[i][j].equals("MD")) {
                        othTags = "MOD";
                        defined = "modal";
                    } else  if (ReadCSV.posTags[i][j].equals("NN")) {
                        othTags = "Noun";
                        defined = "noun";
                    } else  if (ReadCSV.posTags[i][j].equals("NNS")) {
                        othTags = "Noun S";
                        defined = "noun%plural";
                    } else  if (ReadCSV.posTags[i][j].equals("PRP")) {
                        othTags = "";
                        defined = "pronoun";
                    } else  if (ReadCSV.posTags[i][j].equals("PRP$")) {
                        othTags = "";
                        defined = "possessive";
                    } else  if (ReadCSV.posTags[i][j].equals("RB")) {
                        othTags = "";
                        defined = "adv";
                    } else  if (ReadCSV.posTags[i][j].equals("RBR")) {
                        othTags = "";
                        defined = "adv%comparative";
                    } else  if (ReadCSV.posTags[i][j].equals("RBS")) {
                        othTags = "";
                        defined = "adv%superlative";
                    } else  if (ReadCSV.posTags[i][j].equals("RP")) {
                        othTags = "";
                        defined = "particle";
                    } else  if (ReadCSV.posTags[i][j].equals("SYM")) {
                        othTags = "";
                        defined = "symbol";
                    } else  if (ReadCSV.posTags[i][j].equals("UJ")) {
                        othTags = "";
                        defined = "interj";
                    } else  if (ReadCSV.posTags[i][j].equals("VB") || ReadCSV.posTags[i][j].equals("VBD") || ReadCSV.posTags[i][j].equals("VBG") || ReadCSV.posTags[i][j].equals("VBN") ||
                                ReadCSV.posTags[i][j].equals("VBP") || ReadCSV.posTags[i][j].equals("VBZ")) {
                        othTags = "SUPV";
                        defined = "verb";
                    }


                //System.out.println("Antes");
                hql2 = "SELECT ";
                for (int k = 0; k < harvard_features.length; k++) {
                    hql2 = hql2 + harvard_features[k];
                    if (k < harvard_features.length - 1) hql2 = hql2 + ", ";
                }
                hql2 = hql2 + " FROM HarvardDictionary WHERE (Entry = '" + aux[j] + "' OR Entry = '" + stemmed[j] + "') OR ((Entry LIKE '" + aux[j] + "#%'"  + " OR Entry LIKE '" + stemmed[j] + "#%') " + "AND Defined LIKE '%" + defined + "%')";
                //System.out.println(hql2);          
                query = session.createQuery(hql2);
                
                results = query.list();
                //Object[] arr ;
                //List<Object[]> ls = results.get(0);
                //System.out.println(aux[j] + " " + results.size());

                
                if (results.size() == 0) continue;
                Object[] arr = results.get(0);
                //for(Object[] arr : results){
                    //System.out.println(arr.length);
                    //System.out.println(aux[j]);
                //int[] newValues = new int[182];
                    for (int k = 0; k < arr.length; k++) {
                        //System.out.print((String)arr[0] + " " + ((Number)arr[k]).intValue());
                        //newValues[k] = ((Number)arr[k]).intValue();
                        features[i][k] = features[i][k] + ((Number)arr[k]).intValue();

                    }
                //wordsMap.put(aux[j],newValues);
                //}
            //}
        }

            //System.out.println(features[i][0]);        
    }

   // writer2.close();  
  }


   public static void searchInDB() throws IOException {
    File file;
    PrintWriter writer;

    File file2;
    PrintWriter writer2;

    // Archivo donde estan guardadas las caracteristicas del diccionario de Harvard
    String harvardFile = "/home/susana/Miniproyecto/preprocess/src/main/java/logica/caracteristicas_harvard.txt";
    BufferedReader br = null;
    String line = "";

    // Arreglo que tiene las caracteristicas del diccionario de Harvard
    String[] harvard_features = new String[182];

    Session session = HibernateUtil.getSessionFactory().openSession();
    String hql;
    Query query;
    List<Object[]> results;
    List<Object[]> results2;
    br = new BufferedReader(new FileReader(harvardFile));
    //file = new File ("/home/susana/Miniproyecto/preprocess/src/main/java/logica/CommentsTagged/CARACTERISTICAS.txt");
    //writer = new PrintWriter(file, "UTF-8");


    // Creacion del query
    // Se lee en la variable 'line' cada una de las lineas del archivo harvardFile
    // Es decir, line tiene las caracteristicas del diccionario de Harvard
    // Se usa para construir el query
     int m = 0;

    String hql2;
    boolean found = false;
    String[] aux;

    String[] stemmed;
            hql = "SELECT Entry";

        while ((line = br.readLine()) != null) {
            /*if (m == 0) {            
                hql = hql + "COALESCE(SUM(" + line + "),0)";
            }
            else {
                hql = hql + ", COALESCE(SUM(" + line + "),0)";
            }*/
            hql = hql + ", " +line;
            harvard_features[m] = line;
            //writer2.print(" "  + harvard_features[m]);
            m++;  
           
        }
        
        hql = hql + " FROM HarvardDictionary WHERE";
    for (int i = 0; i < features.length; i++) {

        //System.out.println("Nuevo usuario: " + i);
        aux = (ReadCSV.usrCommentsSeparated[i].replace("\'", "")).split(", ");
        stemmed = (ReadCSV.wordsStemmed[i].replace("\'", "")).split(", ");
        String defined[] = new String[aux.length];
        String othTags[] = new String[aux.length];
        for (int j = 0; j < aux.length; j++) {
            if (ReadCSV.posTags[i][j].equals("CC")) {
                othTags[j] = "CONJ";
                defined[j] = "conjunction";
            } else if (ReadCSV.posTags[i][j].equals("CD")) {
                othTags[j] = "DET";
                defined[j] = "cardinal number";
            } else if (ReadCSV.posTags[i][j].equals("DT")) {
                othTags[j] = "DET ART";
            } else if (ReadCSV.posTags[i][j].equals("EX")) {
                othTags[j] = "PRON";
                defined[j] = "existential";
            } else if (ReadCSV.posTags[i][j].equals("IN")) {
                othTags[j] = "PREP";
                defined[j] = "prep";
            } else if (ReadCSV.posTags[i][j].equals("JJ")) {
                othTags[j] = "Modif";
                defined[j] = "adj";
                    //System.out.println("ENTRE " + defined);
            } else if (ReadCSV.posTags[i][j].equals("JJR")) {
                othTags[j] = "";
                defined[j] = "adj%comparative";
            } else if (ReadCSV.posTags[i][j].equals("JJS")) {
                othTags[j] = "";
                defined[j] = "adj%superlative";
            } else if (ReadCSV.posTags[i][j].equals("MD")) {
                othTags[j] = "MOD";
                defined[j] = "modal";
            } else if (ReadCSV.posTags[i][j].equals("NN")) {
                othTags[j] = "Noun";
                defined[j] = "noun";
            } else if (ReadCSV.posTags[i][j].equals("NNS")) {
                othTags[j] = "Noun S";
                defined[j] = "noun%plural";
            } else if (ReadCSV.posTags[i][j].equals("PRP")) {
                othTags[j] = "";
                defined[j] = "pronoun";
            } else if (ReadCSV.posTags[i][j].equals("PRP$")) {
                othTags[j] = "";
                defined[j] = "possessive";
            } else if (ReadCSV.posTags[i][j].equals("RB")) {
                othTags[j] = "";
                defined[j] = "adv";
            } else if (ReadCSV.posTags[i][j].equals("RBR")) {
                othTags[j] = "";
                defined[j] = "adv%comparative";
            } else if (ReadCSV.posTags[i][j].equals("RBS")) {
                othTags[j] = "";
                defined[j] = "adv%superlative";
            } else if (ReadCSV.posTags[i][j].equals("RP")) {
                othTags[j] = "";
                defined[j] = "particle";
            } else if (ReadCSV.posTags[i][j].equals("SYM")) {
                othTags[j] = "";
                defined[j] = "symbol";
            } else if (ReadCSV.posTags[i][j].equals("UJ")) {
                othTags[j] = "";
                defined[j] = "interj";
            } else if (ReadCSV.posTags[i][j].equals("VB") || ReadCSV.posTags[i][j].equals("VBD") || ReadCSV.posTags[i][j].equals("VBG") || ReadCSV.posTags[i][j].equals("VBN") ||
                    ReadCSV.posTags[i][j].equals("VBP") || ReadCSV.posTags[i][j].equals("VBZ")) {
                othTags[j] = "SUPV";
                defined[j] = "verb";
            }
        }

        // Parte final del query que contiene cada comentario separado por palabras
        hql2 = hql + " Entry IN (" +ReadCSV.usrCommentsSeparated[i] +")";
        //System.out.println(hql2);

        // 182 es el numero de caracteristicas del diccionario de Harvard
        features[i] = new int[182];

            
            // Se hace la consulta
            query = session.createQuery(hql2);
            results = query.list();

            boolean wordFound;
            //MODIFICARLO PARA BUSCAR CON CONTAISN
            for (int p = 0; p < aux.length; p++) {
                wordFound = false;
                for (int k = 0; k < results.size(); k++) {
                    Object[] arr = results.get(k);
                    //System.out.println((String)arr[0]);
                    //System.out.println((((String)arr[0]).toLowerCase()) + " " + aux[p]);
                    if (aux[p].equals(((String)arr[0]).toLowerCase())) {
                        for (int j = 0; j < arr.length - 1; j++) {
                        // Suma del numero de palabras que pertenecen a la caracteristica j
                        features[i][j] += ((Number)arr[j+1]).intValue(); 
                        }
                        //System.out.println("Encontre "+aux[p]);
                        wordFound = true;
                        break;
                    }
                }
                if (!wordFound) {
                    //if (wordsMap.containsKey(aux[p])) {
                    //System.out.println(aux[j]);
                    //    int[] values = wordsMap.get(aux[p]);
                    //    for (int k = 0; k < values.length; k++) {
                            //System.out.print((String)arr[0] + " " + ((Number)arr[k]).intValue());  
                    //        features[i][k] = features[i][k] + values[k];
                    //    }    
                    //}
                    //else {
                        //System.out.println("No he encontrado " + aux[p]);
                        hql2 = hql + " Entry = '" + stemmed[p] + "' OR ((Entry LIKE '" + aux[p] + "#%'"  + " OR Entry LIKE '" + stemmed[p] + "#%') " + "AND Defined LIKE '%" + defined[p] + "%')";
                        //System.out.println(hql2);
                        query = session.createQuery(hql2);
                        
                        results2 = query.list();
                        if (results2.size() == 0) continue;
                        //System.out.println("Ya encontre  " + aux[p]);
                        
                        wordFound = true;

                        int[] newValues = new int[182];
                        Object[] arr = results2.get(0);

                        for (int k = 0; k < arr.length - 1; k++) {
                            //System.out.println("Encontre" + k);
                            //newValues[k] = ((Number)arr[k+1]).intValue();
                            features[i][k] = features[i][k] + ((Number)arr[k+1]).intValue();                        
                        }
                        //System.out.println("Sali");
                        //wordsMap.put(aux[p],newValues);
                    //}

                    //System.out.println("Sali");
                }
                
            }
            //System.out.println(features[i][0]);

        
    }
    session.close();   
  }

   public static void searchFeaturesInDB2() throws IOException {
        File file;
    PrintWriter writer;

    File file2;
    PrintWriter writer2;

    // Archivo donde estan guardadas las caracteristicas del diccionario de Harvard
    String harvardFile = "/home/susana/Miniproyecto/preprocess/src/main/java/logica/caracteristicas_harvard.txt";
    BufferedReader br = null;
    String line = "";

    // Arreglo que tiene las caracteristicas del diccionario de Harvard
    String[] harvard_features = new String[182];

    Session session = HibernateUtil.getSessionFactory().openSession();
    String hql;
    String hql3="";
    String hql4="";
    Query query;
    List<Object[]> results;
    br = new BufferedReader(new FileReader(harvardFile));
     int m = 0;
    hql = "SELECT ";
    hql4 = "SELECT Entry, "; 

    while ((line = br.readLine()) != null) {
        if (m == 0) {            
            hql = hql + "COALESCE(SUM(" + line + "),0)";
            hql3 = hql3 + line + " = 1";
            hql4 = hql4 + line;
        }
        else {
            hql = hql + ", COALESCE(SUM(" + line + "),0)";
            hql3 = hql3 + " OR " + line + " = 1";
            hql4 = hql4 + ", " + line;
        }

        harvard_features[m] = line;
        m++;  
       
    }
    hql4 = hql4 + " FROM HarvardDictionary WHERE";
    hql = hql + ", GROUP_CONCAT(Entry) FROM HarvardDictionary WHERE Entry IN(";
    String hql2;
    boolean found = false;
    String[] aux;
    String stemmed[];

    for (int i = 0; i < features.length; i++) {
        //System.out.println("Usuario " + i);
        aux = (ReadCSV.usrCommentsSeparated[i].replace("\'", "")).split(", ");
        stemmed = (ReadCSV.wordsStemmed[i].replace("\'", "")).split(", ");
        String defined[] = new String[aux.length];
        String othTags[] = new String[aux.length];
        for (int j = 0; j < aux.length; j++) {
            if (ReadCSV.posTags[i][j].equals("CC")) {
                        othTags[j] = "CONJ";
                        defined[j] = "conjunction";
                    } else  if (ReadCSV.posTags[i][j].equals("CD")) {
                        othTags[j] = "DET";
                        defined[j] = "cardinal number";
                    } else  if (ReadCSV.posTags[i][j].equals("DT")) {
                        othTags[j] = "DET ART";
                    } else  if (ReadCSV.posTags[i][j].equals("EX")) {
                        othTags[j] = "PRON";
                        defined[j] = "existential";
                    } else  if (ReadCSV.posTags[i][j].equals("IN")) {
                        othTags[j] = "PREP";
                        defined[j] = "prep";
                    } else  if (ReadCSV.posTags[i][j].equals("JJ")) {
                        othTags[j] = "Modif";
                        defined[j] = "adj";
                        //System.out.println("ENTRE " + defined);
                    } else  if (ReadCSV.posTags[i][j].equals("JJR")) {
                        othTags[j] = "";
                        defined[j] = "adj%comparative";
                    } else  if (ReadCSV.posTags[i][j].equals("JJS")) {
                        othTags[j] = "";
                        defined[j] = "adj%superlative";
                    } else  if (ReadCSV.posTags[i][j].equals("MD")) {
                        othTags[j] = "MOD";
                        defined[j] = "modal";
                    } else  if (ReadCSV.posTags[i][j].equals("NN")) {
                        othTags[j] = "Noun";
                        defined[j] = "noun";
                    } else  if (ReadCSV.posTags[i][j].equals("NNS")) {
                        othTags[j] = "Noun S";
                        defined[j] = "noun%plural";
                    } else  if (ReadCSV.posTags[i][j].equals("PRP")) {
                        othTags[j] = "";
                        defined[j] = "pronoun";
                    } else  if (ReadCSV.posTags[i][j].equals("PRP$")) {
                        othTags[j] = "";
                        defined[j] = "possessive";
                    } else  if (ReadCSV.posTags[i][j].equals("RB")) {
                        othTags[j] = "";
                        defined[j] = "adv";
                    } else  if (ReadCSV.posTags[i][j].equals("RBR")) {
                        othTags[j] = "";
                        defined[j] = "adv%comparative";
                    } else  if (ReadCSV.posTags[i][j].equals("RBS")) {
                        othTags[j] = "";
                        defined[j] = "adv%superlative";
                    } else  if (ReadCSV.posTags[i][j].equals("RP")) {
                        othTags[j] = "";
                        defined[j] = "particle";
                    } else  if (ReadCSV.posTags[i][j].equals("SYM")) {
                        othTags[j] = "";
                        defined[j] = "symbol";
                    } else  if (ReadCSV.posTags[i][j].equals("UJ")) {
                        othTags[j] = "";
                        defined[j] = "interj";
                    } else  if (ReadCSV.posTags[i][j].equals("VB") || ReadCSV.posTags[i][j].equals("VBD") || ReadCSV.posTags[i][j].equals("VBG") || ReadCSV.posTags[i][j].equals("VBN") ||
                                ReadCSV.posTags[i][j].equals("VBP") || ReadCSV.posTags[i][j].equals("VBZ")) {
                        othTags[j] = "SUPV";
                        defined[j] = "verb";
                    }
        }

            hql2 = hql + ReadCSV.usrCommentsSeparated[i] +")";
            hql2 = hql2 + " AND (" + hql3 + ")";
            //if (i == 83) {
            //    System.out.println(hql2);
            //}
            //System.out.println("Usuario " + i);


            features[i] = new int[182];

            
            //String[] wordsFound = new String[1];
            String wordsFound = "";
            boolean wordFound;
            query = session.createQuery(hql2);
            //System.out.println(hql2);
            results = query.list();
            /*if (i == 83) {
                    System.out.println("Entre " + results.size());
            }
            if (i == 83) {
                    System.out.println("Entre2 " + results.get(0)[results.get(0).length -1]);
            }*/
            for(Object[] arr : results){
                //System.out.println("Entre3");
                //wordsFound = (((String)arr[arr.length - 1]).toLowerCase()).split(",");
                wordsFound = ((String)arr[arr.length - 1]);
                
                //System.out.println(((String)arr[arr.length - 1]));
                for (int s = 0; s < arr.length - 1; s++) {
                    features[i][s] = ((Number)arr[s]).intValue();

                }
            }
                            //System.out.println(wordsFound.length);

            //System.out.println(ReadCSV.usrCommentsSeparated[i]);
            
            for (int p = 0; p < aux.length; p++) {
                //if (i == 83) {
                //    System.out.println("Entre4 " + p + " length " + aux.length);
                    /*System.out.println(wordsFound);
                    System.out.println(wordsFound.toLowerCase());
                    System.out.println(aux[p]);
                    System.out.println(wordsFound.toLowerCase().contains(aux[p]));*/
                //}
                /*wordFound = false;
                //System.out.println(wordsFound.length);
                for (int k = 0; k < wordsFound.length; k++) {
                    if (aux[p].equals(wordsFound[k])) {
                        //System.out.println("First Found " + aux[p]);
                        wordFound = true;
                        break;
                    }
                }*/
                if (wordsFound == null || !wordsFound.toLowerCase().contains(aux[p])) {
                    
                    //System.out.println("Not Found " + aux[p]);
                    hql2 = hql4 + " Entry = '" + stemmed[p] + "' OR ((Entry LIKE '" + aux[p] + "#%'"  + " OR Entry LIKE '" + stemmed[p] + "#%') " + "AND Defined LIKE '%" + defined[p] + "%')";
                        //System.out.println(hql2);
                        query = session.createQuery(hql2);
                                    //if (i == 83) {
                                    //    System.out.println(hql2);
                                    //}
                        
                        results = query.list();
                        if (results.size() == 0) {
                            //System.out.println("Not Found " + aux[p] + " Stem " + stemmed[p] + " Tag " + defined[p] + "Tag ");
                            continue;
                        }
                        //System.out.println("Found " + aux[p] + results.get(0)[0]);
                        
                        wordFound = true;

                        Object[] arr = results.get(0);

                        for (int k = 0; k < arr.length - 1; k++) {
                            features[i][k] = features[i][k] + ((Number)arr[k+1]).intValue();                        
                        }
                }
                else {
                    //System.out.println("Entre6");
                    continue;
                }
            }
        //System.out.println(features[i][0]);

    }
    session.close();  
  }

  public static void printInFile() throws IOException{

    File file;
    PrintWriter writer;

    // Archivo donde estan guardadas las caracteristicas del diccionario de Harvard
    String harvardFile = "/home/susana/Miniproyecto/preprocess/src/main/java/logica/caracteristicas_harvard.txt";
    System.out.println("AQUI");
    BufferedReader br = null;
    br = new BufferedReader(new FileReader(harvardFile));
    String line = "";

    file = new File ("/home/susana/Miniproyecto/preprocess/src/main/java/logica/Caracteristicas2c.csv");    
    writer = new PrintWriter(file, "UTF-8");
    writer.print("User");  

    while ((line = br.readLine()) != null) {
        writer.print(", "  + line);    
    }
    writer.print(", adverbs, anger, swearWords, sadness, openness, conscientiousness, extraversion, agreableness, neuroticism, beauty, behaviour, biology, science, earth, entrepreneurship, faith, fashion, fiends, law, marriage, military, music, occasions, vacations, violence, sEXT, sNEU, sAGR, sCON, sOPN, cEXT, cNEU, cAGR, cCON, cOPN");

    for (int i = 0; i < ReadCSV.usrCommentsSeparated.length; i++) { 
        writer.println("");
        writer.print("User" + i + ", ");
        for (int j = 0; j < features[i].length; j++) {
            writer.print(features[i][j] + ", ");
        }

            writer.print(ReadCSV.sBigFive[i][0] + ", "); 
            writer.print(ReadCSV.sBigFive[i][1] + ", "); 
            writer.print(ReadCSV.sBigFive[i][2] + ", "); 
            writer.print(ReadCSV.sBigFive[i][3] + ", "); 
            writer.print(ReadCSV.sBigFive[i][4] + ", ");
            writer.print(ReadCSV.cBigFive[i][0] + ", "); 
            writer.print(ReadCSV.cBigFive[i][1] + ", "); 
            writer.print(ReadCSV.cBigFive[i][2] + ", "); 
            writer.print(ReadCSV.cBigFive[i][3] + ", "); 
            writer.print(ReadCSV.cBigFive[i][4]);  
    }
    writer.close();
    }
}



