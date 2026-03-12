import java.util.*;

/**
 * UseCase3DocumentDeduplication
 * Detects duplicate or highly similar documents using n-gram hashing.
 */

class DocumentDeduplicator {

    private HashMap<String, Set<String>> ngramIndex = new HashMap<>();
    private int n = 5; // 5-gram window

    public void addDocument(String docId, String text) {

        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - n; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < n; j++) {
                gram.append(words[i + j]).append(" ");
            }

            String ngram = gram.toString().trim();

            ngramIndex.putIfAbsent(ngram, new HashSet<>());
            ngramIndex.get(ngram).add(docId);
        }
    }

    public void checkDuplicate(String docId, String text) {

        String[] words = text.toLowerCase().split("\\s+");

        int totalNgrams = Math.max(words.length - n + 1, 0);

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (int i = 0; i <= words.length - n; i++) {

            StringBuilder gram = new StringBuilder();

            for (int j = 0; j < n; j++) {
                gram.append(words[i + j]).append(" ");
            }

            String ngram = gram.toString().trim();

            if (ngramIndex.containsKey(ngram)) {

                for (String doc : ngramIndex.get(ngram)) {
                    matchCount.put(doc,
                            matchCount.getOrDefault(doc, 0) + 1);
                }
            }
        }

        System.out.println("Checking Document: " + docId);
        System.out.println("Extracted " + totalNgrams + " n-grams\n");

        for (String doc : matchCount.keySet()) {

            int matches = matchCount.get(doc);
            double similarity = (matches * 100.0) / totalNgrams;

            System.out.println("Matched with \"" + doc + "\"");

            System.out.printf("Similarity: %.2f%% ", similarity);

            if (similarity > 70) {
                System.out.println("(DUPLICATE DOCUMENT)");
            } else if (similarity > 20) {
                System.out.println("(Similar Content)");
            } else {
                System.out.println("(Unique Document)");
            }

            System.out.println();
        }
    }
}

public class PlagiarismDetectionSystem {

    public static void main(String[] args) {

        DocumentDeduplicator deduplicator = new DocumentDeduplicator();

        String doc1 =
                "Machine learning is transforming modern data analysis and predictive systems";

        String doc2 =
                "Machine learning is transforming modern data analysis and prediction tools";

        String newDoc =
                "Machine learning is transforming modern data analysis and predictive systems";

        deduplicator.addDocument("doc_101.txt", doc1);
        deduplicator.addDocument("doc_102.txt", doc2);

        deduplicator.checkDuplicate("doc_200.txt", newDoc);
    }
}
