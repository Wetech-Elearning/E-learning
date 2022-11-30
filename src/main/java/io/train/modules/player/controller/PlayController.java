package io.train.modules.player.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.train.common.io.PropertiesUtils;
import io.train.modules.player.handle.NonStaticResourceHttpRequestHandler;


@RestController
@RequestMapping("/sys/video")
public class PlayController {
	
	@Autowired
	private NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;

	@RequestMapping(value = "/play")
	public ResponseEntity<FileSystemResource> getFile(@RequestParam(name="path") String filePath,HttpServletRequest request,HttpServletResponse response) {
		try {
			String path = PropertiesUtils.getConfig("file.upload.path");
//			File file = new File(path + filePath);
//			if (file.exists()) {
//				return export(file);
//			}
			play(request,response,path+filePath);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void play(HttpServletRequest request,HttpServletResponse response,String filepath) throws IOException{
		response.reset();
		String rangeString = request.getHeader("Range");
		OutputStream outputStream = response.getOutputStream();
		File file = new File(filepath);
		if(file.exists()){
			RandomAccessFile targetFile = new RandomAccessFile(file, "r");
			long fileLength = targetFile.length();
			if(targetFile != null){
				long range = Long.valueOf(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
				 response.setHeader("Content-Type", "video/mp4");
                 //设置此次相应返回的数据长度
                 response.setHeader("Content-Length", String.valueOf(fileLength - range));
                 //设置此次相应返回的数据范围
                 response.setHeader("Content-Range", "bytes "+range+"-"+(fileLength-1)+"/"+fileLength);
                 //返回码需要为206，而不是200
                 response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                 //设定文件读取开始位置（以字节为单位）
                 targetFile.seek(range);
			} else {
				
				response.setHeader("Content-Disposition", "attachment; filename=test.mp4");
	            //设置文件长度
	            response.setHeader("Content-Length", String.valueOf("test.mp4"));
	            //解决编码问题
	            response.setHeader("Content-Type","application/octet-stream");
			}
			byte[] cache = new byte[1024 * 300];
            int flag;
            while ((flag = targetFile.read(cache))!=-1){
                outputStream.write(cache, 0, flag);
            }
		} else {
			String message = "file:"+"test.mp4"+" not exists";
            response.setHeader("Content-Type","application/json");
            outputStream.write(message.getBytes(StandardCharsets.UTF_8));
		}
		outputStream.flush();
        outputStream.close();
	}

	public ResponseEntity<FileSystemResource> export(File file) {
		if (file == null) {
			return null;
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Content-Disposition", "attachment; filename=" + file.getName());
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		headers.add("Last-Modified", new Date().toString());
		headers.add("ETag", String.valueOf(System.currentTimeMillis()));
		return ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType.parseMediaType("application/octet-stream")).body(new FileSystemResource(file));
//		.contentType(MediaType.parseMediaType("application/octet-stream")).body(new FileSystemResource(file));
	}
	
	@GetMapping("/playvideo")
	public void videoPreview(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String realPath = "G:\\third\\io\\video\\2021\\202107\\20210725\\bfc2bb40-a1bf-4825-949c-813932025d1e.mp4";
		long startbit = new Long(request.getHeader("Range").toString());
		Path filePath = Paths.get(realPath );
		if(Files.exists(filePath)){
			String mimeType = Files.probeContentType(filePath);
			if (!StringUtils.isEmpty(mimeType)) {
                response.setContentType(mimeType);
            }
			File file = new File(realPath);
			FileInputStream in = new FileInputStream(file);
			in.skip(startbit);
			InputStream sbs = new BufferedInputStream(in);
			request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
			nonStaticResourceHttpRequestHandler.handleRequest(request, response);
		}
	}
	
	
	@RequestMapping(value = "/playvideos",method = RequestMethod.GET)
	public void videoPreviews(HttpServletRequest request, HttpServletResponse response) {
		try {
			String realPath = "G:\\third\\io\\video\\2021\\202107\\20210725\\bfc2bb40-a1bf-4825-949c-813932025d1e.mp4";
			response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
			response.setHeader("Content-Disposition", "attachment; filename=test.mp4");
			File file = new File(realPath);
			InputStream iStream = new FileInputStream(file);
			long startbit = new Long(request.getHeader("Range").toString().replace("bytes=", "").replace("-", ""));
			iStream.skip(startbit);
			byte[] iobytes = new byte[1024*1024*10];
			iStream.read(iobytes);
			response.getOutputStream().write(iobytes);
			response.getOutputStream().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
