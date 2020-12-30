package com.ex.searchengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class SearchEngine {

    private static SearchEngine searchEngine;
    private Map<String, Map<Integer, List<Integer>>> invertedIndex;
    private Map<Integer, Integer> documentWordCount;
    private Map<String, Map<Integer, Double>> termFrequency;
    private Map<String, Double> inverseDocumentFrequency;
    private Map<Integer, List<Double>> documentVectors;
    private Map<Integer, Double> documentsRanked;

    private ObjectMapper mapper;
    private String invertedIndexPath = "file here";
    private String termFrequencyPath = " file here";
    private String documentWordCountPath = "file here";//documentWordCount.txt

    public SearchEngine() throws URISyntaxException {
        mapper = new ObjectMapper();
        /*
        URI invertedIndexUri = ClassLoader.getSystemClassLoader().getResource("index.txt").toURI();
        URI termFrequencyUri = ClassLoader.getSystemClassLoader().getResource("termFrequency.txt").toURI();
        URI documentWordCountUri = ClassLoader.getSystemClassLoader().getResource("documentWordCount.txt").toURI();
        this.invertedIndexPath = Paths.get(invertedIndexUri).toString();
        this.termFrequencyPath = Paths.get(termFrequencyUri).toString();
        this.documentWordCountPath = Paths.get(documentWordCountUri).toString();*/

        LoadInvertedIndex();
        LoadTermFrequencyMap();
        LoadDocumentWordCount();
    }

    public static SearchEngine getInstance() throws URISyntaxException {
        if (searchEngine == null)
            searchEngine = new SearchEngine();

        return searchEngine;
    }

    public List<Integer> Query(String query){
        PhraseQuery(TokenizeText(query));
        CalculateInverseDocumentFrequency();

        documentVectors = new HashMap<>();
        PopulateDocumentsVectors();

        List<Double> queryVector = CalculateQueryVector(TokenizeText(query));

        RankDocuments(queryVector);

        List<Integer> docs = new ArrayList<>(FilterAnswers(documentsRanked.keySet()));
        Collections.sort(docs, Comparator.comparing(item -> documentsRanked.get(item)));
        Collections.reverse(docs);

        return docs;
    }

    private List<Integer> FilterAnswers(Set<Integer> docs){
        List<Integer> result = new ArrayList<>();
        for (Integer doc : docs){
            if (documentsRanked.get(doc) != 0)
                result.add(doc);
        }
        return result;
    }

    public void AddDocument(int documentId, String document){
        AddTextToIndex(documentId, document);
    }

    private void LoadInvertedIndex(){
        try {
            String json = readFileAsString(invertedIndexPath);
            invertedIndex = new HashMap<>();
            invertedIndex = mapper.readValue(json, new TypeReference<Map<String, Map<Integer, List<Integer>>>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void LoadTermFrequencyMap(){
        try {
            String json = readFileAsString(termFrequencyPath);
            termFrequency = new HashMap<>();
            termFrequency = mapper.readValue(json, new TypeReference<Map<String, Map<Integer, Double>>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void LoadDocumentWordCount(){
        try {
            String json = readFileAsString(documentWordCountPath);
            documentWordCount = new HashMap<>();
            documentWordCount = mapper.readValue(json, new TypeReference<Map<Integer, Integer>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readFileAsString(String file)throws Exception
    {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    private void AddTextToIndex(int documentId, String documentText){
        List<String> tokens = TokenizeText(documentText);
        documentWordCount.put(documentId, tokens.size());
        Map<String, List<Integer>> positions = new HashMap<>();
        for(int i = 0; i < tokens.size(); i++){
            if(!positions.containsKey(tokens.get(i)))
                positions.put(tokens.get(i), new ArrayList<>());
            positions.get(tokens.get(i)).add(i);
        }

        //Calculates the term frequency for each term in the document
        for(int i = 0; i < tokens.size(); i++){
            if(!termFrequency.containsKey(tokens.get(i)))
                termFrequency.put(tokens.get(i), new HashMap<>());
            double count = positions.get(tokens.get(i)).size();
            double tf = count / documentWordCount.get(documentId);
            termFrequency.get(tokens.get(i)).put(documentId, tf);
        }

        for(String word : positions.keySet()){
            if(!invertedIndex.containsKey(word))
                invertedIndex.put(word, new HashMap<>());
            invertedIndex.get(word).put(documentId, positions.get(word));
        }

        SaveInvertedIndex();
        SaveTermFrequency();
        SaveDocumentWordCount();
    }

    private void SaveInvertedIndex(){
        if (invertedIndex == null)
            invertedIndex = new HashMap<>();
        try {
            mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(invertedIndex);
            Files.write(Paths.get(invertedIndexPath), json.getBytes());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Set<Integer> StandardQuery(List<String> queryWords){
        Set<Integer> documents = new HashSet<>();

        for(String word : queryWords){
            if (invertedIndex.containsKey(word))
                documents.addAll(invertedIndex.get(word).keySet());
        }

        return documents;
    }

    private List<Integer> PhraseQuery(List<String> queryWords){

        //get the documents where every word occurs
        List<Set<Integer>> documents = new ArrayList<>();
        List<String> queryWord;
        for(String word : queryWords){
            queryWord = new ArrayList<>();
            queryWord.add(word);
            Set<Integer> docs = StandardQuery(queryWord);
            documents.add(docs);
        }

        //get the intersection of all the documents AKA the documents where all the documents occur at the same time
        if(documents.size() > 0) {
            Set<Integer> intersection = documents.get(0);

            for (int i = 1; i < documents.size(); i++) {
                intersection = documents.get(i).stream()
                        .distinct()
                        .filter(intersection::contains)
                        .collect(Collectors.toSet());
            }

            List<Integer> result = new ArrayList<>();
            List<List<Integer>> positions;
            for(int document : intersection){
                positions = new ArrayList<>();
                for(String word : queryWords){
                    positions.add(invertedIndex.get(word).get(document));
                }

                for(int i = 0; i < queryWords.size(); i++){
                    for(int j = 0; j < positions.get(i).size(); j++)
                        positions.get(i).set(j, positions.get(i).get(j) - i);
                }

                List<Integer> temp = new ArrayList<>(positions.get(0));
                for(int i = 1; i < positions.size(); i++){
                    temp = positions.get(i).stream()
                            .distinct()
                            .filter(temp::contains)
                            .collect(Collectors.toList());
                }

                if(temp.size() > 0)
                    result.add(document);
            }
            return result;
        }
        return null;
    }

    private void RankDocuments(List<Double> queryVector){
        documentsRanked = new HashMap<>();

        for (int documentId : documentVectors.keySet()){
            documentsRanked.put(documentId, CalculateCosineSimilarity(queryVector, documentVectors.get(documentId)));
        }
    }

    private double CalculateCosineSimilarity(List<Double> vectorA, List<Double> vectorB){
        double result = 0;
        double magnitudA = 0;
        double magnitudB = 0;

        for (int i = 0; i < vectorA.size(); i++) {
            result += vectorA.get(i) * vectorB.get(i);

            magnitudA += Math.pow(vectorA.get(i), 2);
            magnitudB += Math.pow(vectorB.get(i), 2);

        }
        return result / (Math.sqrt(magnitudA) * Math.sqrt(magnitudB));
    }

    private List<Double> CalculateQueryVector(List<String> tokenizedQuery){
        Map<String, Integer> wordFrequency = new HashMap<>();
        for (String word : tokenizedQuery){
            if (wordFrequency.containsKey(word)){
                int count = wordFrequency.get(word) + 1;
                wordFrequency.replace(word, count);
            }
            else
                wordFrequency.put(word, 1);
        }

        List<Double> vector = new ArrayList<>();
        for (String word : invertedIndex.keySet())
            vector.add(0.0);

        for (String term : tokenizedQuery) {
            int i = -1;
            for (String word : invertedIndex.keySet()) {
                i++;
                if (word.equals(term))
                    vector.set(i, (((double)wordFrequency.get(term) / tokenizedQuery.size()) * inverseDocumentFrequency.get(term)));
            }
        }

        return vector;
    }

    private void CalculateInverseDocumentFrequency(){
        inverseDocumentFrequency = new HashMap<>();
        double documentTotal = documentWordCount.size();

        for(String term : invertedIndex.keySet()){
            double frequency = 1 + Math.log(documentTotal / invertedIndex.get(term).size());
            inverseDocumentFrequency.put(term, frequency);
        }
    }

    private void PopulateDocumentsVectors(){
        for(Integer id : documentWordCount.keySet())
            PopulateDocumentVector(id);
    }

    private void PopulateDocumentVector(int documentId){
        List<Double> vector = new ArrayList<>();
        double tf = 0;
        double idf = 0;

        for(String term : invertedIndex.keySet()){
            if(termFrequency.get(term)!=null){
                if(termFrequency.get(term).containsKey(documentId))
                    tf = termFrequency.get(term).get(documentId);
                else
                    tf = 0;
                if(inverseDocumentFrequency.containsKey(term)){
                    idf = inverseDocumentFrequency.get(term);
                }
                else{
                    idf = 0;
                }
            }

            vector.add(tf * idf);
        }
        documentVectors.put(documentId, vector);
    }

    private List<String> TokenizeText(String text){
        List<String> tokens = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(text.toLowerCase());
        while(tokenizer.hasMoreTokens())
            tokens.add(tokenizer.nextToken());
        List<String> stopWords = LoadStopWords();
        tokens.removeAll(stopWords);

        return tokens;
    }

    private List<String> LoadStopWords() {
        String words = null;
        try {
            /*
            URI uri = ClassLoader.getSystemClassLoader().getResource("stopwords.txt").toURI();
            String path = Paths.get(uri).toString();
            System.out.println(path);*/
            words = new String(Files.readAllBytes(Paths.get("file here")));

        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] stopWords = words.split("\r\n");
        return Arrays.asList(stopWords);
    }

    private void SaveTermFrequency(){
        if (termFrequency == null)
            termFrequency = new HashMap<>();
        try {
            mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(termFrequency);
            Files.write(Paths.get(termFrequencyPath), json.getBytes());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void SaveDocumentWordCount(){
        if (documentWordCount == null)
            documentWordCount = new HashMap<>();
        try {
            mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(documentWordCount);
            Files.write(Paths.get(documentWordCountPath), json.getBytes());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
