// http://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
//http://stackoverflow.com/questions/1757065/java-splitting-a-comma-separated-string-but-ignoring-commas-in-quotes


package logica;

import logica.AbbContrSlang;
import logica.AbbContrSlang2;
import logica.AbbContrSlang3;
import logica.AbbContrSlang4;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.io.Reader;
import java.io.StringReader;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.LowerCaseFilter;
//port org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.Version;

import java.io.InputStream;
import java.io.FileInputStream;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.POSModel;

import logica.otherCategories;

public class ReadCSV {


// Arreglo de 254 (numero de personas en el archivo csv) que contendra
// la concatenacion de todos los comentarios de una misma persona
// Cada posicion representa una persona distina
static String[] usrComments = new String[254];
public static String[] usrCommentsSeparated = new String[254];
static Set<String> stopWords;
//static String[] stopW = new String[]{"a", "a's", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "ain't", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "aren't", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "b", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "c", "c'mon", "c's", "came", "can", "can't", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldn't", "course", "currently", "d", "definitely", "described", "despite", "did", "didn't", "different", "do", "does", "doesn't", "doing", "don't", "done", "down", "downwards", "during", "e", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "f", "far", "few", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "g", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "h", "had", "hadn't", "happens", "hardly", "has", "hasn't", "have", "haven't", "having", "he", "he's", "hello", "help", "hence", "her", "here", "here's", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "i'd", "i'll", "i'm", "i've", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isn't", "it", "it'd", "it'll", "it's", "its", "itself", "j", "just", "k", "keep", "keeps", "kept", "know", "knows", "known", "l", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "let's", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "m", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "n", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "o", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "p", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "q", "que", "quite", "qv", "r", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "s", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldn't", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "t", "t's", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "that's", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "there's", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "they'd", "they'll", "they're", "they've", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "u", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "uucp", "v", "value", "various", "very", "via", "viz", "vs", "w", "want", "wants", "was", "wasn't", "way", "we", "we'd", "we'll", "we're", "we've", "welcome", "well", "went", "were", "weren't", "what", "what's", "whatever", "when", "whence", "whenever", "where", "where's", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "who's", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "won't", "wonder", "would", "would", "wouldn't", "x", "y", "yes", "yet", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves", "z", "zero"};
// Arreglo de etiquetas de palabras por usuario
public static String[][] posTags = new String[254][];
// "cEXT","cNEU","cAGR","cCON","cOPN"
public static String[][] cBigFive = new String[254][];
//"sEXT","sNEU","sAGR","sCON","sOPN"
public static float[][] sBigFive = new float[254][];

//COmentarios con las palabras reducidas a su raiz
public static String[] wordsStemmed = new String[254];



  public void run() throws IOException  {


	this.readFile();
	File file;
	// Para etiquetar palabras
	/*String[] aux;
  	InputStream modelIn = null;
  	POSModel model = null;
	try {
	  modelIn = new FileInputStream("/home/susana/Miniproyecto/preprocess/src/main/java/logica/en-pos-maxent.bin");
	  model = new POSModel(modelIn);
	}
	catch (IOException e) {
	  // Model loading failed, handle the error
	  e.printStackTrace();
	}
	finally {
	  if (modelIn != null) {
	    try {
	      modelIn.close();
	    }
	    catch (IOException e) {
	    }

	  }
	}

	POSTaggerME tagger = new POSTaggerME(model);
	*/
	// 
	for (int i = 0; i < 254; i++) {
		String separatedInWordsComment = removeStopWordsAndStem(usrComments[i]);
		usrCommentsSeparated[i] = separatedInWordsComment;
		wordsStemmed[i] = stemWords(usrCommentsSeparated[i]);

		//Etiquetado de palabras
		//aux = (usrCommentsSeparated[i].replace("\'", "")).split(",");
  		//posTags[i] = tagger.tag(aux);  	
		// 254 archivos de comentarios (un archivo por persona)
		// Contiene los comentarios sin stopwords y con el algoritmo porterstem aplicado
		/*file = new File ("/home/susana/Miniproyecto/preprocess/src/main/java/logica/CommentsTagged/removeStopWordsAndStem"+i+".txt");
	
		try {
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			String separatedInWordsComment = removeStopWordsAndStem(usrComments[i]);
			usrCommentsSeparated[i] = separatedInWordsComment;
			wordsStemmed[i] = stemWords(usrCommentsSeparated[i]);
			writer.println(usrCommentsSeparated[i]);	
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	// Etiquetar palabras
	tagWords();

	//System.out.println(usrComments[0]);
	//System.out.println(removeStopWordsAndStem(usrComments[0]));

  }

  // Metodo que calcula las etiquetas de las palabras de los comentarios segun el algoritmo POS tag
  public static void tagWords(){

  	String[] aux;
  	InputStream modelIn = null;
  	POSModel model = null;
	try {
	  modelIn = new FileInputStream("/home/susana/Miniproyecto/preprocess/src/main/java/logica/en-pos-maxent.bin");
	  model = new POSModel(modelIn);
	}
	catch (IOException e) {
	  // Model loading failed, handle the error
	  e.printStackTrace();
	}
	finally {
	  if (modelIn != null) {
	    try {
	      modelIn.close();
	    }
	    catch (IOException e) {
	    }

	  }
	}

	POSTaggerME tagger = new POSTaggerME(model);

  	for (int i = 0; i < 254; i++) {
  		aux = (usrCommentsSeparated[i].replace("\'", "")).split(",");
  		posTags[i] = tagger.tag(aux);  	
  	}
  }
  //Metodo que agrega las stopwords de archivo "/home/susana/Miniproyecto/Logica/stopwords.txt"
  //al hashset stopWords

  public static void addStopwordsToSet(){
  	String stopwordsFile = "/home/susana/Miniproyecto/preprocess/src/main/java/logica/stopwords.txt";
  	BufferedReader br = null;
	String line = "";
  	stopWords= new HashSet<String>();
  	//for(int i = 0; i < stopW.length; i++) {
  	//	stopWords.add(stopW[i]);
  	//}
  	try {
		br = new BufferedReader(new FileReader(stopwordsFile));
		//line = br.readLine();
		while ((line = br.readLine()) != null) {
			stopWords.add(line);
			/*stopWords.add(line.concat("#1"));
			stopWords.add(line.concat("#2"));
			stopWords.add(line.concat("#3"));
			stopWords.add(line.concat("#4"));
			stopWords.add(line.concat("#5"));
			stopWords.add(line.concat("#6"));
			stopWords.add(line.concat("#7"));
			stopWords.add(line.concat("#8"));
			stopWords.add(line.concat("#9"));
			stopWords.add(line.concat("#10"));
			stopWords.add(line.concat("#11"));
			stopWords.add(line.concat("#12"));
			stopWords.add(line.concat("#13"));
			stopWords.add(line.concat("#_10"));
			stopWords.add(line.concat("#_11"));
			stopWords.add(line.concat("#_12"));
			stopWords.add(line.concat("#_13"));			
			stopWords.add(line.concat("#_14"));
			stopWords.add(line.concat("#_15"));
			stopWords.add(line.concat("#_16"));
			stopWords.add(line.concat("#_17"));
			stopWords.add(line.concat("#_18"));
			stopWords.add(line.concat("#_19"));
			stopWords.add(line.concat("#_20"));
			stopWords.add(line.concat("#_21"));
			stopWords.add(line.concat("#_22"));
			stopWords.add(line.concat("#_23"));*/
			//System.out.print("\"" + line+ "\", ");
		}
	} 
	catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//stopWords.forEach(System.out::println);

  }


  // Metodo para separar en tokens el string input.
  // Hace conversiones del estilo zup -> what's up
  // Es solo para eliminar slang words, aun no elimina contracciones
  // El metodo tokenizeContractions es el que elimina las contracciones
    public static String tokenizeSlangWords(String input) throws IOException {

    TokenStream tokenStream = new StandardTokenizer(
            Version.LUCENE_30, new StringReader(input));
    tokenStream = new LowerCaseFilter(tokenStream);
    StringBuilder sb = new StringBuilder();

    TermAttribute termAttr = tokenStream.getAttribute(TermAttribute.class);
    int index;
    StringBuilder contraction;;
    StringBuilder contraction2;
    boolean isContraction = false;

    while (tokenStream.incrementToken()) {
    	contraction = new StringBuilder();
		contraction2 = new StringBuilder();
    	//Eliminacion de contracciones y abreviaciones
    	contraction.append(termAttr.term());
    	index = Arrays.asList(AbbContrSlang2.slanglistA).indexOf(contraction.toString());
    	if (index != -1) {
    		contraction2.append(AbbContrSlang2.slanglistA1[index]);
    		isContraction = true;
    	} else {
    		index = Arrays.asList(AbbContrSlang3.slanglistB).indexOf(contraction.toString());
	    	if (index != -1) {
	    		contraction2.append(AbbContrSlang3.slanglistB1[index]);
	    		isContraction = true;
	    	} else {
	    		index = Arrays.asList(AbbContrSlang4.slanglistC).indexOf(contraction.toString());
		    	if (index != -1) {
		    		contraction2.append(AbbContrSlang4.slanglistC1[index]);
		    		isContraction = true;
		    	}
    		}
    	}

        if (sb.length() > 0) {
            sb.append(" ");
        }
        if (isContraction){
        	sb.append(contraction2.toString());
        	isContraction = false;
        } else {
        	sb.append(contraction.toString());
        }
    }
    
    return sb.toString();
  }
  	
  // Metodo para separar en tokens el string input.
  // Hace conversiones del estilo i'm -> i am
  public static String tokenizeContractions(String input) throws IOException {

    TokenStream tokenStream = new StandardTokenizer(
            Version.LUCENE_30, new StringReader(tokenizeSlangWords(input)));
    tokenStream = new LowerCaseFilter(tokenStream);
    StringBuilder sb = new StringBuilder();

    TermAttribute termAttr = tokenStream.getAttribute(TermAttribute.class);
    int index;
    StringBuilder contraction;;
    StringBuilder contraction2;
    boolean isContraction = false;

    while (tokenStream.incrementToken()) {
    	contraction = new StringBuilder();
		contraction2 = new StringBuilder();
    	//Eliminacion de contracciones y abreviaciones
    	contraction.append(termAttr.term());
    	index = Arrays.asList(AbbContrSlang.NegationWordsList).indexOf(contraction.toString());
    	if (index != -1) {
    		contraction2.append(AbbContrSlang.NegationWordsList1[index]);
    		isContraction = true;
    	} else {
    		index = Arrays.asList(AbbContrSlang.ContractionListA).indexOf(contraction.toString());
    		if (index != -1) {
    			contraction2.append(AbbContrSlang.ContractionListA1[index]);
    			isContraction = true;
    		} else {
    			index = Arrays.asList(AbbContrSlang.ContractionListB).indexOf(contraction.toString());
	    		if (index != -1) {
	    			contraction2.append(AbbContrSlang.ContractionListB1[index]);
	    			isContraction = true;
	    		} else {
    				index = Arrays.asList(AbbContrSlang.ContractionListC).indexOf(contraction.toString());
	    			if (index != -1) {
	    				contraction2.append(AbbContrSlang.ContractionListC1[index]);
	    				isContraction = true;
	    			} else {
	    				index = Arrays.asList(AbbContrSlang.ContractionListD).indexOf(contraction.toString());
			    		if (index != -1) {
			    			contraction2.append(AbbContrSlang.ContractionListD1[index]);
			    			isContraction = true;
			    		} else {
			    			index = Arrays.asList(AbbContrSlang.ContractionListD).indexOf(contraction.toString());
				    		if (index != -1) {
				    			contraction2.append(AbbContrSlang.ContractionListD1[index]);
				    			isContraction = true;
				    		}
			    		}
	    			}
    			}
    		}
    	}

        if (sb.length() > 0) {
            sb.append(" ");
        }
        if (isContraction){
        	sb.append(contraction2.toString().replaceAll("\'", ""));
        	isContraction = false;
        } else {
        	sb.append(contraction.toString().replaceAll("\'", ""));
        }
    }
    
    return sb.toString();
  }

  // Metodo que elimina stopwords del string 'input'
  public static String removeStopWordsAndStem(String input) throws IOException {

  	addStopwordsToSet();

    TokenStream tokenStream = new StandardTokenizer(
            Version.LUCENE_30, new StringReader(tokenizeContractions(input)));
    tokenStream = new LowerCaseFilter(tokenStream);
    tokenStream = new StopFilter(true, tokenStream, stopWords);
    //tokenStream = new PorterStemFilter(tokenStream);

    StringBuilder sb = new StringBuilder();
    StringBuilder sb2;
    TermAttribute termAttr = tokenStream.getAttribute(TermAttribute.class);
    int index;
    StringBuilder contraction;;
    StringBuilder contraction2;
    boolean isContraction = false;
    
    
    while (tokenStream.incrementToken()) {
    	sb2 = new StringBuilder();

        if (sb.length() > 0) {
            sb.append(", ");
        }
        sb2.append("\'");
        sb2.append(termAttr.term());
       	sb2.append("\'");
        sb.append(sb2);
        //sb.append(", ");
        /*sb2 = new StringBuilder();
	    sb2.append("|");
	    sb2.append(termAttr.term());
	    sb2.append("#");
	    sb.append(sb2);*/
    }
    
    return sb.toString();
	}

	public static String stemWords(String input) throws IOException {

  	//addStopwordsToSet();

    TokenStream tokenStream = new StandardTokenizer(
            Version.LUCENE_30, new StringReader(tokenizeContractions(input)));
    //tokenStream = new LowerCaseFilter(tokenStream);
    //tokenStream = new StopFilter(true, tokenStream, stopWords);
    tokenStream = new PorterStemFilter(tokenStream);

    StringBuilder sb = new StringBuilder();
    StringBuilder sb2;
    TermAttribute termAttr = tokenStream.getAttribute(TermAttribute.class);
    int index;
    StringBuilder contraction;;
    StringBuilder contraction2;
    boolean isContraction = false;
    
    
    while (tokenStream.incrementToken()) {
    	sb2 = new StringBuilder();

        if (sb.length() > 0) {
            sb.append(", ");
        }
        sb2.append("\'");
        sb2.append(termAttr.term());
       	sb2.append("\'");
        sb.append(sb2);
    }
    
    return sb.toString();
	}

  public void readFile() {
  	// Archivo csv que se va a leer
	String csvFile = "/home/susana/Miniproyecto/preprocess/src/main/java/logica/DataSetPersonality.csv";
	BufferedReader br = null;
	String line = "";
	//String cvsSplitBy = ",";
	// Archivo donde se imprimiran los comentarios por usuario
	//File file = new File ("/home/susana/Miniproyecto/preprocess/src/main/java/logica/comments.txt");


	try {
		//PrintWriter writer = new PrintWriter(file, "UTF-8");
		br = new BufferedReader(new FileReader(csvFile));
		line = br.readLine();
		String usr = "";
		//int usrCount= 0;


		int i = -1;
		// Procesamiento del archivo csv
		// str[1] contiene el id del autor
		// str[2] contiene los comentarios
		while ((line = br.readLine()) != null) {

		        // use comma as separator
			//split on the comma only if that comma has zero, or an even number of quotes ahead of it.
			String otherThanQuote = " [^\"] ";
        	String quotedString = String.format(" \" %s* \" ", otherThanQuote);
        	String regex = String.format("(?x) "+ // enable comments, ignore white spaces
                ",                         "+ // match a comma
                "(?=                       "+ // start positive look ahead
                "  (                       "+ //   start group 1
                "    %s*                   "+ //     match 'otherThanQuote' zero or more times
                "    %s                    "+ //     match 'quotedString'
                "  )*                      "+ //   end group 1 and repeat it zero or more times
                "  %s*                     "+ //   match 'otherThanQuote'
                "  $                       "+ // match the end of the string
                ")                         ", // stop positive look ahead
                otherThanQuote, quotedString, otherThanQuote);

			String[] str = line.split(regex, -1);
			// Son 254 usuarios. Cada posicion del arreglo tene una concatenacion de sus comentarios.
			// Si entra en el if es porque es una nueva persona
			if (!usr.equals(str[1])){
				// Esto es solo para imprimir en el archivo
				if (i!=-1){
					//writer.println(usrComments[i]);
				}
				i++;
				// Se inicializa la casilla de esa persona				
				usrComments[i] = "";
				usr = str[1];
				cBigFive[i] = new String[5];
				sBigFive[i] = new float[5];
				for (int j = 0; j < 5; j++) {
					cBigFive[i][j] = str[8+j].replace("\"", "");
					sBigFive[i][j] = Float.parseFloat(str[3+j].replace("\"", ""));
				}

				//usrCount++;
				// Para imprimir en el archivo
				//writer.println("User: " + usr + "\n");
			}
			// Concatenacion del nuevo comentario a los anteriores
			usrComments[i] = usrComments[i].concat(" ");
			//str[2] = str[2].replace("\'", "");
			usrComments[i] = usrComments[i].concat(str[2].replace("\"", ""));

			

			//System.out.println("User: " + str[1] 
            //                     + " , comment=" + str[2] + "]");

		}
		//writer.println(usrComments[i]);
		//writer.close();

	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//System.out.println(usrCount);
	//System.out.println("Done");
  }

  public void loadOtherCategories() throws IOException {
  	String csvFileO = "/home/susana/Miniproyecto/preprocess/src/main/java/logica/0-Openness.csv";
  	String csvFileC = "/home/susana/Miniproyecto/preprocess/src/main/java/logica/0-Conscientiousnesss.csv";
  	String csvFileE = "/home/susana/Miniproyecto/preprocess/src/main/java/logica/0-Extraversion.csv";
  	String csvFileA = "/home/susana/Miniproyecto/preprocess/src/main/java/logica/0-Agreeableness.csv";
  	String csvFileN = "/home/susana/Miniproyecto/preprocess/src/main/java/logica/0-Neuroticism.csv";
  	String fileAdv = "/home/susana/Miniproyecto/preprocess/src/main/java/logica/violence.txt";
  	String adv = "";
	BufferedReader br = null;
	String line = "";
	String agr = "";
	try {
		//PrintWriter writer = new PrintWriter(file, "UTF-8");
		br = new BufferedReader(new FileReader(csvFileN));
		while ((line = br.readLine()) != null) {
			String[] words = line.split(",");
			agr = agr + ", " + "\"" + words[0] + "\"";
		}
		//System.out.println(agr);

	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	try {
		//PrintWriter writer = new PrintWriter(file, "UTF-8");
		br = new BufferedReader(new FileReader(fileAdv));
		while ((line = br.readLine()) != null) {
			//adv = adv + ", " + "\"" + line.toLowerCase() + "\"" ;
			adv = adv + line.toLowerCase();
		}
		System.out.println(adv);

	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	}
}
