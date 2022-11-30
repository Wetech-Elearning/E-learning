package io.train.modules.player.handle;

import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Component
public class NonStaticResourceHttpRequestHandler extends ResourceHttpRequestHandler {

	public final static String ATTR_FILE = "NON-STATIC-FILE";

	@Override
	protected Resource getResource(HttpServletRequest request) {
		final Path filePath = (Path) request.getAttribute(ATTR_FILE);
		return new FileSystemResource(filePath);
	}

}
