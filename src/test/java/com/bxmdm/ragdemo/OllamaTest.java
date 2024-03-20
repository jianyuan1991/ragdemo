package com.bxmdm.ragdemo;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Description: 先运行 ollama run qwen:7b
 * @Author: jay
 * @Date: 2024/3/11 15:37
 * @Version: 1.0
 */
@SpringBootTest
class OllamaTest {

	@Autowired
	private OllamaChatClient ollamaChatClient;

	@Autowired
	private EmbeddingClient embeddingClient;

	@Test
	void embeddingDimensionsTest() {
		System.out.println(embeddingClient.dimensions());
	}

	@Test
	void ollamaChatClientTest() {
		String message = """
					给我讲一个java程序员的笑话吧
				""";
		System.out.println(ollamaChatClient.call(message));
	}

	@Test
	void embeddingClientTest1() {
		EmbeddingResponse embeddingResponse = embeddingClient.call(
				new EmbeddingRequest(List.of("Hello World", "World is big and salvation is near"),
						OllamaOptions.create()
								.withModel("qwen:7b")));
		for (Embedding result : embeddingResponse.getResults()) {
			System.out.println(Arrays.toString(result.getOutput().toArray()));
		}
	}

	@Test
	void embeddingClientTest2() {
		EmbeddingResponse embeddingResponse = embeddingClient.embedForResponse(List.of("Hello World", "World is big and salvation is near"));
		for (Embedding result : embeddingResponse.getResults()) {
			System.out.println(Arrays.toString(result.getOutput().toArray()));
		}
	}
}
