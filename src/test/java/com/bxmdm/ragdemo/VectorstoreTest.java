package com.bxmdm.ragdemo;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class VectorstoreTest {

	@Autowired
	private VectorStore vectorStore;

	@Test
	public void pgvectorTest(){
		List <Document> documents = List.of(
				new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", Map.of("meta1", "meta1")),
				new Document("The World is Big and Salvation Lurks Around the Corner"),
				new Document("You walk forward facing the past and you turn back toward the future.", Map.of("meta2", "meta2")));

// Add the documents to PGVector
		vectorStore.add(documents);

		// Retrieve documents similar to a query
		List<Document> results = vectorStore.similaritySearch(SearchRequest.query("Spring").withTopK(5));

		for (Document result : results) {
			System.out.println(result);
		}
	}


}
