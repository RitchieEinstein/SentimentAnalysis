package com.ritchieeinstein.sentimentAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class SentimentAnalysis {
	
	public static final int NOTHING = 0;
	public static final int INCREMENTER = 1;
	public static final int DECREMENTER = 2;
	public static final int INVERTER = 3;
	public static final int POSITIVE = 4;
	public static final int NEGATIVE = 5;
	public static final int TRAINDATAPERCENT = 100;
	
	public static List<String>positiveWordList;
	public static List<String>negativeWordList;
	public static List<String>incrementerWordList;
	public static List<String>decrementerWordList;
	public static List<String>inverterWordList;

	public static void main(String[] args) throws IOException {
		File folder = new File("/Users/accolite-ios-1/Desktop/sentimentAnalysis/testData");
		File positiveDictFile = new File("/Users/accolite-ios-1/Desktop/sentimentAnalysis/dictionary/positive-words.txt");
		File negativeDictFile = new File("/Users/accolite-ios-1/Desktop/sentimentAnalysis/dictionary/negative-words.txt");
		File incrementorFile = new File("/Users/accolite-ios-1/Desktop/sentimentAnalysis/dictionary/incrementer-words.txt");
		File decrementorFile = new File("/Users/accolite-ios-1/Desktop/sentimentAnalysis/dictionary/decrementer-words.txt");
		File invertorFile = new File("/Users/accolite-ios-1/Desktop/sentimentAnalysis/dictionary/inverter-words.txt");
		File NBTestFolder = new File("/Users/accolite-ios-1/Desktop/sentimentAnalysis/NBTestData");
		List<Map<String,Object>>commentTuple = new ArrayList<>();
		Map<String,Map<String,Integer>>wordTuple = new HashMap<>();
		List<String>commentList = new ArrayList<>();
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        System.out.println(file.getName());
		        String contents = new String(Files.readAllBytes(file.toPath()));
		        commentList.add(contents.toLowerCase());
		    }
		}
		List<String>NBTestCommentList = new ArrayList<>();
		listOfFiles = NBTestFolder.listFiles();
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        System.out.println(file.getName());
		        String contents = new String(Files.readAllBytes(file.toPath()));
		        NBTestCommentList.add(contents.toLowerCase());
		    }
		}
		positiveWordList = getListFromStringDictionary(positiveDictFile);
		negativeWordList = getListFromStringDictionary(negativeDictFile);
		incrementerWordList = getListFromStringDictionary(incrementorFile);
		decrementerWordList = getListFromStringDictionary(decrementorFile);
		inverterWordList = getListFromStringDictionary(invertorFile);
		for(String comment:commentList) {
			List<String>sentenceList = new ArrayList<String>();
			for(String sentence:comment.split("[.?!;]")) {
				//System.out.println(sentence);
				sentenceList.add(sentence);
			}
			int sentenceScore = 0;
			for(String sentence:sentenceList) {
				List<String>wordList = new ArrayList<String>();
				for(String word:sentence.split(" ")) {
					word = cleanseWord(word);
					wordList.add(word);
				}
				double positiveCount = 0;
				double negativeCount = 0;
				int currentWord = NOTHING;
				int prevWord = NOTHING;
				for(String word:wordList) {
					currentWord = getWordType(word);
					if(currentWord == INCREMENTER || currentWord == DECREMENTER || currentWord == INVERTER) {
						prevWord = currentWord;
						continue;
					}
					else {
						double adder = 1;
						if(prevWord == INCREMENTER)
							adder = 2;
						if(prevWord == DECREMENTER)
							adder = 0.5f;
						if(currentWord == POSITIVE) {
							if(prevWord == INVERTER)
								negativeCount += adder;
							else
								positiveCount += adder;
						}
						if(currentWord == NEGATIVE) {
							if(prevWord == INVERTER)
								positiveCount += adder;
							else
								negativeCount += adder;
						}
						prevWord = NOTHING;
					}
					
				}
				double totalScore = positiveCount+(-negativeCount);
				if(totalScore > 0) {
					sentenceScore++;
				}
				else if(totalScore < 0){
					sentenceScore--;
				}
			}
			Map<String,Object>commentMap = new HashMap<>();
			commentMap.put("comment", comment);
			commentMap.put("score", sentenceScore);
			if(sentenceScore > 0) {
//				System.out.println(comment);
				commentMap.put("type", POSITIVE);
//				System.out.println(" Postitive " + sentenceScore);
			}
			else if(sentenceScore < 0){
//				System.out.println(comment);
				commentMap.put("type", NEGATIVE);
//				System.out.println(" Negative " + sentenceScore);
			}
			else {
				commentMap.put("type", NOTHING);
//				System.out.println("Neutral or Undistinguised");
			}
			commentTuple.add(commentMap);
		}
		int posCount=0;
		int negCount=0;
		for(Map<String,Object>map:commentTuple) {
			System.out.println(map.get("comment"));
			System.out.println(map.get("score") + " " + getStringForType((int) map.get("type")));
			if((int)map.get("type")==POSITIVE) {
				posCount++;
			}
			else {
				negCount++;
			}
		}
		System.out.println("Positive : " + posCount);
		System.out.println("Negative : " + negCount);
		
		Set<String>allowedPOS = new HashSet<>();
		allowedPOS.add("NN");
		allowedPOS.add("NNS");
		allowedPOS.add("NNP");
		allowedPOS.add("NNPS");
		allowedPOS.add("RB");
		allowedPOS.add("JJ");
		allowedPOS.add("VBD");
		
		Set<String>POSWords = new HashSet<>();
		
		// The NB starts Here
		
		MaxentTagger tagger = new MaxentTagger("/Users/accolite-ios-1/Desktop/sentimentAnalysis/tagger/english-bidirectional-distsim.tagger");
		
		listOfFiles = NBTestFolder.listFiles();
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    		List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new BufferedReader(new FileReader(file)));
		    		System.out.println(sentences);
		    		for (List<HasWord> sentence : sentences) {
		    		      List<TaggedWord> tSentence = tagger.tagSentence(sentence);
		    		      for(TaggedWord t:tSentence) {
		    		    	  	if(allowedPOS.contains(t.tag())) {
		    		    	  		POSWords.add(t.word());
		    		    	  	}
		    		    	  	//System.out.println(t.tag());
		    		      }
		    		      System.out.println(SentenceUtils.listToString(tSentence, false));
		    		}
		    }
		}
		
		int commentSize = commentTuple.size();
		int trainingSetSize = (commentSize * TRAINDATAPERCENT)/100;
		
		int posWordCount = 0;
		int negWordCount = 0;
		
		for(int i=0;i<trainingSetSize;i++) {
			Map<String,Object> commentMap = commentTuple.get(i);
			String comment = (String) commentMap.get("comment");
			for(String word:comment.split("[ .?!;]")) {
				word = cleanseWord(word);
				if(!POSWords.contains(word)) {
					continue;
				}
				System.out.println("NB Train Word : " + word);
				Map<String,Integer>wordMap;
				if(wordTuple.containsKey(word)) {
					wordMap = wordTuple.get(word);
					if((int)commentMap.get("type")==POSITIVE) {
						wordMap.put("positive", wordMap.get("positive") + 1);
						posWordCount++;
					}
					if((int)commentMap.get("type")==NEGATIVE) {
						wordMap.put("negative",wordMap.get("negative") + 1);
						negWordCount++;
					}
				}
				else {
					wordMap = new HashMap<>();
					wordMap.put("positive", 0);
					wordMap.put("negative", 0);
					if((int)commentMap.get("type")==POSITIVE) {
						wordMap.put("positive", 1);
						posWordCount++;
					}
					if((int)commentMap.get("type")==NEGATIVE) {
						wordMap.put("negative",1);
						negWordCount++;
					}
				}
				wordTuple.put(word.toLowerCase(), wordMap);
			}
		}
		
		System.out.println(wordTuple.toString());
		int nbpc=0;
		int nbnc=0;
		
		listOfFiles = NBTestFolder.listFiles();
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    		List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new BufferedReader(new FileReader(file)));
		    		System.out.println(sentences);
		    		for (List<HasWord> sentence : sentences) {
		    		      List<TaggedWord> tSentence = tagger.tagSentence(sentence);
		    		      for(TaggedWord t:tSentence) {
		    		    	  	
		    		    	  	System.out.println(t.tag());
		    		      }
		    		      System.out.println(SentenceUtils.listToString(tSentence, false));
		    		}
		    }
		}
		
		
		for(String comment:NBTestCommentList) {
			
			List<String>wordList = new ArrayList<>();
			for(String word:comment.split("[ ?!;.]")) {
				wordList.add(word.toLowerCase());
			}
			
			double positiveProb = (double)posCount / (double)commentList.size();
			double negativeProb = (double)negCount / (double)commentList.size();
			//System.out.println("Started --- PosProb : " + positiveProb + " NegProb : " + negativeProb);
			for(String word:wordList) {
				if(wordTuple.containsKey(word)) {
					Map<String,Integer>wordMap = wordTuple.get(word);
					//System.out.println(word + " " + (double)wordMap.get("positive") + " " + (double)wordMap.get("negative"));
					positiveProb = positiveProb * ((double)(wordMap.get("positive") + 1) / (double)(posCount + wordTuple.size())) * 10;
					//System.out.println((double)wordMap.get("negative") + " " + (double)(negCount + wordTuple.size()) + " " + ((double)(wordMap.get("negative") + 1) / (double)(negCount + wordTuple.size())));
					negativeProb = negativeProb * ((double)(wordMap.get("negative") + 1) / (double)(negCount + wordTuple.size())) * 10;
					//System.out.println("PosProb : " + positiveProb + " NegProb : " + negativeProb);
				}
				else {
					positiveProb = positiveProb * (1.0f/ (double)(posCount + wordTuple.size()));
					negativeProb = negativeProb * (1.0f/ (double)(negCount + wordTuple.size()));
				}
			}
			String res = "";
			if(positiveProb > negativeProb) {
				res = "POSITIVE";
				nbpc++;
			}
			else {
				res = "NEGATIVE";
				nbnc++;
			}
			System.out.println(comment);
			System.out.println(positiveProb + " " + negativeProb + " " + res);
		}
		System.out.println("Positive : " + nbpc);
		System.out.println("Negative : " + nbnc);
		
		if(nbpc > nbnc) {
			System.out.println("The Post is considered to be Positive");
		}
		else if(nbpc < nbnc) {
			System.out.println("The Post is considered to be Negative");
		}
		else {
			System.out.println("The Post is considered to be Neutral or Undistinguisable");
		}
		
	}
	
	public static List<String> getListFromStringDictionary(File file) throws IOException {
		List<String> stringList = new ArrayList<>();
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		StringBuffer stringBuffer = new StringBuffer();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			if(line.startsWith(";") || line.isEmpty())
				continue;
			line = line.trim();
			stringList.add(line);
		}
		return stringList;
	}
	
	public static String cleanseWord(String word) {
		Pattern pt = Pattern.compile("[^a-zA-Z0-9']");
        Matcher match= pt.matcher(word);
        while(match.find())
        {
            String s= match.group();
            word=word.replaceAll("\\"+s, "");
        }
        System.out.println("Cleansed Word : " + word);
        return word.toLowerCase();
	}
	
	public static int getWordType(String word) {
		if(inverterWordList.contains(word.toLowerCase()))
			return INVERTER;
		if(incrementerWordList.contains(word.toLowerCase()))
			return INCREMENTER;
		if(decrementerWordList.contains(word.toLowerCase()))
			return DECREMENTER;
		if(positiveWordList.contains(word.toLowerCase()))
			return POSITIVE;
		if(negativeWordList.contains(word.toLowerCase()))
			return NEGATIVE;
		
		return NOTHING;
	}
	
	public static String getStringForType(int type) {
		if(type == INVERTER)
			return "INVERTER";
		if(type == INCREMENTER)
			return "INCREMENTER";
		if(type == DECREMENTER)
			return "DECREMENTER";
		if(type == POSITIVE)
			return "POSITIVE";
		if(type == NEGATIVE)
			return "NEGATIVE";
		return "";
	}
}
