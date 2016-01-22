package com.cee.livraria.controller.jsf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.powerlogic.jcompany.commons.PlcException;

@RequestScoped
public class FileUploadDownloadMB extends AppMB {

	private static final long serialVersionUID = 598287553306587199L;

	public void downloadAbrindoArquivo(byte[] document, String nomeArquivo, String mime) throws PlcException {
		
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext(); // Context
		HttpServletResponse response = (HttpServletResponse) context.getResponse();

		try {
			nomeArquivo = URLDecoder.decode(nomeArquivo, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		response.reset();

		if (mime == null || mime.equals("")) {
			mime = "application/octet-strStream";
			response.setContentType(mime);
		} else {
			response.setContentType(mime);
		}

		response.addHeader("Content-Disposition", "attachment;filename=\"" + nomeArquivo + "\"");
		response.addHeader("Content-Transfer-Encoding", "binary");
		response.setContentLength(document.length);
		
		OutputStream out;
		
		try {
			out = response.getOutputStream();
			out.write(document, 0, document.length);
			response.flushBuffer();
			out.flush();
			out.close();

			FacesContext.getCurrentInstance().responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
			throw new PlcException("erro.download");
		}
	}
	
}
