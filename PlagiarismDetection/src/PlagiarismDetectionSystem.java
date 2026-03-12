import java.util.*;

/**
 * UseCase1AcademicPlagiarismDetection
 * Academic plagiarism detection system using n-gram hashing.
 */

class AcademicPlagiarismDetector {

    private HashMap<String, Set<String>> ngramIndex = new HashMap<>();
    private int n = 5; // 5-gram window

    public void addDocument(String docId, String text) {

        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - n; i++) {

            StringBuilder ngramBuilder = new StringBuilder();

            for (int j = 0; j < n; j++) {
                ngramBuilder.append(words[i + j]).append(" ");
            }

            String ngram = ngramBuilder.toString().trim();

            ngramIndex.putIfAbsent(ngram, new HashSet<>());
            ngramIndex.get(ngram).add(docId);
        }
    }

    public void analyzeDocument(String docId, String text) {

        String[] words = text.toLowerCase().split("\\s+");

        int totalNgrams = Math.max(words.length - n + 1, 0);

        HashMap<String, Integer> matches = new HashMap<>();

        for (int i = 0; i <= words.length - n; i++) {

            StringBuilder ngramBuilder = new StringBuilder();

            for (int j = 0; j < n; j++) {
                ngramBuilder.append(words[i + j]).append(" ");
            }

            String ngram = ngramBuilder.toString().trim();

            if (ngramIndex.containsKey(ngram)) {

                for (String existingDoc : ngramIndex.get(ngram)) {
                    matches.put(existingDoc,
                            matches.getOrDefault(existingDoc, 0) + 1);
                }
            }
        }

        System.out.println("Analyzing Document: " + docId);
        System.out.println("Extracted " + totalNgrams + " n-grams\n");

        for (String doc : matches.keySet()) {

            int matchCount = matches.get(doc);
            double similarity = (matchCount * 100.0) / totalNgrams;

            System.out.println("Found " + matchCount +
                    " matching n-grams with \"" + doc + "\"");

            System.out.printf("Similarity: %.2f%% ", similarity);

            if (similarity > 60) {
                System.out.println("(PLAGIARISM DETECTED)");
            } else if (similarity > 10) {
                System.out.println("(Suspicious)");
            } else {
                System.out.println("(Low similarity)");
            }

            System.out.println();
        }
    }
}

public class  PlagiarismDetectionSystem{

    public static void main(String[] args) {

        AcademicPlagiarismDetector detector =
                new AcademicPlagiarismDetector();

        String essay1 =
                "Artificial intelligence is transforming the world of technology and science";

        String essay2 =
                "Artificial intelligence is transforming the world of technology rapidly today";

        String newEssay =
                "Artificial intelligence is transforming the world of technology in modern education";

        detector.addDocument("essay_089.txt", essay1);
        detector.addDocument("essay_092.txt", essay2);

        detector.analyzeDocument("essay_123.txt", newEssay);
    }
}
