package com.michelle.sentimentAnalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class TaggerDemo  {

  /** A logger for this class */

  private TaggerDemo() {}

  public static void main(String[] args) throws Exception {
//    if (args.length != 2) {
//      System.out.println("usage: java TaggerDemo modelFile fileToTag");
//      return;
//    }
    MaxentTagger tagger = new MaxentTagger("/Users/accolite-ios-1/Desktop/sentimentAnalysis/tagger/english-bidirectional-distsim.tagger");
    List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new BufferedReader(new FileReader("/Users/accolite-ios-1/Desktop/sentimentAnalysis/testData/sarcastic_4.txt")));
    for (List<HasWord> sentence : sentences) {
      List<TaggedWord> tSentence = tagger.tagSentence(sentence);
      for(TaggedWord t:tSentence) {
    	  	
    	  	System.out.println(t.tag());
      }
      System.out.println(SentenceUtils.listToString(tSentence, false));
    }
  }

}
