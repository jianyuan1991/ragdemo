package com.bxmdm.ragdemo;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

/**
 * @Description: 先运行 ollama run qwen:7b
 * @Author: jay
 * @Date: 2024/3/11 15:37
 * @Version: 1.0
 */
@SpringBootTest
class ETLPipelineTest {

	@Value("classpath:bikes.json")
	private Resource jsonResource;

	@Value("classpath:text_source.txt")
	private Resource textResource;

	@Value("classpath:sample1.pdf")
	private Resource pdfResource;

	@Value("classpath:word-sample.docx")
	private Resource wordResource;

	@Test
	void jsonReaderTest() {
		JsonReader jsonReader = new JsonReader(jsonResource, "description");
		List<Document> docs = jsonReader.get();
		//打印docs


		for (Document doc : docs) {
			System.out.println(doc.toString());
		}

	}

	@Test
	void textReaderTest() {
		TextReader textReader = new TextReader(textResource);
		textReader.getCustomMetadata().put("filename", "text-source.txt");
		List<Document> docs = textReader.get();
		//打印docs
		for (Document doc : docs) {
			System.out.println(doc.toString());
		}
	}

	@Test
	void pdfReaderTest() {
		PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(pdfResource,
				PdfDocumentReaderConfig.builder()
						.withPageTopMargin(0)
						.withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
								.withNumberOfTopTextLinesToDelete(0)
								.build())
						.withPagesPerDocument(1)
						.build());

		List<Document> docs = pdfReader.get();
		//打印docs
		for (Document doc : docs) {
			System.out.println(doc.toString());
		}
	}

	@Test
	void tikReaderTest() {
		TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(wordResource);
		List<Document> docs = tikaDocumentReader.get();
		//打印docs
		for (Document doc : docs) {
			System.out.println(doc.toString());
		}
	}

}
