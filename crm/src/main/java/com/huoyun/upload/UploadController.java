package com.huoyun.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.huoyun.exception.BusinessException;

@Controller
@RequestMapping
public class UploadController {

	@Autowired
	private UploadService uploadService;

	@RequestMapping(value = "/upload/{boNamespace}/{boName}/{boId}/{propertyName}", method = RequestMethod.POST)
	@ResponseBody
	public void uploadFile(@PathVariable("boNamespace") String boNamespace, @PathVariable("boName") String boName,
			@PathVariable("boId") Long boId, @PathVariable("propertyName") String propertyName,
			@RequestParam("file") MultipartFile file) throws BusinessException {
		this.uploadService.upload(boNamespace, boName, boId, propertyName, file);
	}

	@RequestMapping(value = "/upload/{boNamespace}/{boName}/{boId}/{propertyName}/imagelist", method = RequestMethod.POST)
	@ResponseBody
	public void uploadFileForImageList(@PathVariable("boNamespace") String boNamespace,
			@PathVariable("boName") String boName, @PathVariable("boId") Long boId,
			@PathVariable("propertyName") String propertyName, @RequestParam("file") MultipartFile file)
			throws BusinessException {
		this.uploadService.uploadFileForImageList(boNamespace, boName, boId, propertyName, file);
	}

	@RequestMapping(value = "/upload/{boNamespace}/{boName}/{boId}/{propertyName}", method = RequestMethod.GET)
	public String getFileUrl(@PathVariable("boNamespace") String boNamespace, @PathVariable("boName") String boName,
			@PathVariable("boId") Long boId, @PathVariable("propertyName") String propertyName)
			throws BusinessException {
		String url = this.uploadService.getFilePath(boNamespace, boName, boId, propertyName);
		return "redirect:" + url;
	}
}
