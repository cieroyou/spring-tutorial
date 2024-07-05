package com.sera.tutorial.spring.resource;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sera.tutorial.spring.resource.service.TusService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.desair.tus.server.TusFileUploadService;
import me.desair.tus.server.exception.TusException;
import me.desair.tus.server.upload.UploadInfo;

@RequiredArgsConstructor
@RestController
@Slf4j
public class TusUploadController {

	private final TusFileUploadService tusFileUploadService;
	private final TusService tusService;

	@RequestMapping(value = {"/tus/upload", "tus/upload/**"}, method = {RequestMethod.POST, RequestMethod.PATCH,
		RequestMethod.HEAD,
		RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.GET})
	public String tusUpload(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			tusFileUploadService.process(request, response);
			UploadInfo uploadInfo = tusFileUploadService.getUploadInfo(request.getRequestURI());
		} catch (TusException e) {
			throw new RuntimeException(e);
		}
		return "success";
	}

}
