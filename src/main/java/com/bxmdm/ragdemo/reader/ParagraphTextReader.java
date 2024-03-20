package com.bxmdm.ragdemo.reader;

import cn.hutool.core.collection.ListUtil;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

/**
 * @Description: text文本段落读取
 * @Author: jay
 * @Date: 2024/3/19 9:05
 * @Version: 1.0
 */
public class ParagraphTextReader implements DocumentReader {

	public static final String CHARSET_METADATA = "charset";

	public static final String SOURCE_METADATA = "source";

	/**
	 * Input resource to load the text from.
	 */
	private final Resource resource;

	/**
	 * @return Character set to be used when loading data from the
	 */
	private Charset charset = StandardCharsets.UTF_8;

	/**
	 * 默认窗口大小，为1
	 */
	private static final int DEFAULT_WINDOW_SIZE = 1;

	/**
	 * 窗口大小，为段落的数量，用于滚动读取
	 */
	private int windowSize = DEFAULT_WINDOW_SIZE;

	public static final String START_PARAGRAPH_NUMBER = "startParagraphNumber";
	public static final String END_PARAGRAPH_NUMBER = "endParagraphNumber";

	private final Map<String, Object> customMetadata = new HashMap<>();

	public ParagraphTextReader(String resourceUrl) {
		this(new DefaultResourceLoader().getResource(resourceUrl));
	}

	public ParagraphTextReader(Resource resource) {
		Objects.requireNonNull(resource, "The Spring Resource must not be null");
		this.resource = resource;
	}

	public ParagraphTextReader(String resourceUrl, int windowSize) {
		this(new DefaultResourceLoader().getResource(resourceUrl), windowSize);
	}

	public ParagraphTextReader(Resource resource, int windowSize) {
		Objects.requireNonNull(resource, "The Spring Resource must not be null");
		this.resource = resource;
		this.windowSize = windowSize;
	}

	public void setCharset(Charset charset) {
		Objects.requireNonNull(charset, "The charset must not be null");
		this.charset = charset;
	}

	public Charset getCharset() {
		return this.charset;
	}

	/**
	 * Metadata associated with all documents created by the loader.
	 *
	 * @return Metadata to be assigned to the output Documents.
	 */
	public Map<String, Object> getCustomMetadata() {
		return this.customMetadata;
	}

	/**
	 * 读取文本内容,并根据换行进行分段,采用窗口模式,窗口为段落的数量
	 *
	 * @return 文档信息列表
	 */
	@Override
	public List<Document> get() {
		try {

			List<Document> readDocuments = new ArrayList();
			String document = StreamUtils.copyToString(this.resource.getInputStream(), this.charset);

			// Inject source information as a metadata.
			this.customMetadata.put(CHARSET_METADATA, this.charset.name());
			this.customMetadata.put(SOURCE_METADATA, this.resource.getFilename());

			List<String> paragraphs = Arrays.asList(document.split("\n"));

			//采用窗口滑动读取
			int startIndex = 0;
			int endIndex = startIndex + this.windowSize;
			if (endIndex > paragraphs.size()) {
				readDocuments.add(this.toDocument(paragraphs, startIndex + 1, paragraphs.size()));
			} else {
				for (; endIndex <= paragraphs.size(); startIndex++, endIndex++) {
					readDocuments.add(this.toDocument(ListUtil.sub(paragraphs, startIndex, endIndex), startIndex + 1, endIndex));
				}
			}
			return readDocuments;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 封装段落成文档
	 *
	 * @param paragraphList     段落内容列表
	 * @param startParagraphNum 开始段落编码
	 * @param endParagraphNum   结束段落编码
	 * @return 文档信息
	 */
	private Document toDocument(List<String> paragraphList, int startParagraphNum, int endParagraphNum) {
		Document doc = new Document(String.join("\n", paragraphList));
		doc.getMetadata().putAll(this.customMetadata);
		doc.getMetadata().put(START_PARAGRAPH_NUMBER, startParagraphNum);
		doc.getMetadata().put(END_PARAGRAPH_NUMBER, endParagraphNum);
		return doc;
	}

}
