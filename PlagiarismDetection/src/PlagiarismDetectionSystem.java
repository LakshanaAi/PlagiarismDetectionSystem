import java.util.*;

/**
 * UseCase2CodeSimilarityDetection
 * Detects similarity between source codes using n-gram hashing.
 */

class CodeSimilarityDetector {

    private HashMap<String, Set<String>> ngramIndex = new HashMap<>();
    private int n = 4; // 4 token window for code

    public void addCode(String codeId, String code) {

        String[] tokens = code.replaceAll("[^a-zA-Z0-9]", " ")
                .toLowerCase()
                .split("\\s+");

        for (int i = 0; i <= tokens.length - n; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < n; j++) {
                gram.append(tokens[i + j]).append(" ");
            }

            String ngram = gram.toString().trim();

            ngramIndex.putIfAbsent(ngram, new HashSet<>());
            ngramIndex.get(ngram).add(codeId);
        }
    }

    public void analyzeCode(String codeId, String code) {

        String[] tokens = code.replaceAll("[^a-zA-Z0-9]", " ")
                .toLowerCase()
                .split("\\s+");

        int totalNgrams = Math.max(tokens.length - n + 1, 0);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (int i = 0; i <= tokens.length - n; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < n; j++) {
                gram.append(tokens[i + j]).append(" ");
            }

            String ngram = gram.toString().trim();

            if (ngramIndex.containsKey(ngram)) {

                for (String doc : ngramIndex.get(ngram)) {
                    matchCount.put(doc,
                            matchCount.getOrDefault(doc, 0) + 1);
                }
            }
        }

        System.out.println("Analyzing Code: " + codeId);
        System.out.println("Extracted " + totalNgrams + " token-grams\n");

        for (String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);

            double similarity = (matches * 100.0) / totalNgrams;

            System.out.println("Found " + matches +
                    " matching token-grams with \"" + doc + "\"");

            System.out.printf("Similarity: %.2f%% ", similarity);

            if (similarity > 60) {
                System.out.println("(CODE COPYING DETECTED)");
            } else if (similarity > 15) {
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

        CodeSimilarityDetector detector = new CodeSimilarityDetector();

        String code1 =
                "public int add(int a int b) { return a + b; }";

        String code2 =
                "public int sum(int x int y) { return x + y; }";

        String newCode =
                "public int addNumbers(int a int b) { return a + b; }";

        detector.addCode("code_001.java", code1);
        detector.addCode("code_002.java", code2);

        detector.analyzeCode("submission_123.java", newCode);
    }
}
