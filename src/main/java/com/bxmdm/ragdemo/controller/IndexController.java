package com.bxmdm.ragdemo.controller;

import com.bxmdm.ragdemo.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/vi/rag")
@Tag(name = "RAG demo")
public class IndexController {

	@Autowired
	private DocumentService documentService;


	@Operation(summary = "上传文档")
	@PostMapping("/upload")
	public ResponseEntity upload(@RequestBody MultipartFile file) {
		documentService.uploadDocument(file);
		return ResponseEntity.ok("success");
	}

	@Operation(summary = "搜索文档")
	@GetMapping("/search")
	public ResponseEntity<List<Document>> searchDoc(@RequestParam String keyword) {
		return ResponseEntity.ok(documentService.search(keyword));
	}


	@Operation(summary = "问答文档")
	@GetMapping("/chat")
	public ResponseEntity chat(@RequestParam String message) {
		return ResponseEntity.ok(documentService.chat(message));
	}


}
