package kr.or.connect.guestbook.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileController {

    // get방식으로 요청이 올 경우 업로드 폼을 보여줍니다.
	@GetMapping("/uploadform")
	public String uploadform() {
		return "uploadform";
	}
	
	@PostMapping("/upload")
	public String upload(@RequestParam("file") MultipartFile file) {
		
		System.out.println("파일 이름 : " + file.getOriginalFilename());
		System.out.println("파일 크기 : " + file.getSize());
		
        try(
                // 맥일 경우  //파일 이름뒤에 중복을거르기위한 flag등을 삽입한다. 
                FileOutputStream fos = new FileOutputStream("/tmp/" + file.getOriginalFilename());
                // 윈도우일 경우
                //FileOutputStream fos = new FileOutputStream("c:/tmp/" + file.getOriginalFilename());
                InputStream is = file.getInputStream();
        ){
        	    int readCount = 0;
        	    byte[] buffer = new byte[1024];
            while((readCount = is.read(buffer)) != -1){
                fos.write(buffer,0,readCount);
            }
        }catch(Exception ex){
            throw new RuntimeException("file Save Error");
        }
		
		
		return "uploadok";
	}
	
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public @ResponseBody byte[] viewImage(@RequestParam("id") String id, HttpServletResponse response) throws Exception {
		
		System.out.println(id);
		String fileName = "2020-02-14_22:36:33_p.jpeg";
		String saveFileName = "/tmp/2020-02-14_22:36:33_p.jpeg"; 
		String contentType = "image/jpeg";
		int fileLength = 1116303;
		
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Type", contentType);
        response.setHeader("Content-Length", "" + fileLength);
        response.setHeader("Pragma", "no-cache;");
        response.setHeader("Expires", "-1;");
        InputStream fis = new FileInputStream(saveFileName);
        return IOUtils.toByteArray(fis);
	}

	@GetMapping("/download")
	public void download(HttpServletResponse response) {

        // 직접 파일 정보를 변수에 저장해 놨지만, 이 부분이 db에서 읽어왔다고 가정한다.
		String fileName = "2020-02-14_22:36:33_p.jpeg";
		String saveFileName = "/tmp/2020-02-14_22:36:33_p.jpeg"; // 맥일 경우 "/tmp/connect.png" 로 수정
		String contentType = "image/jpeg";
		int fileLength = 1116303;
		
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Content-Type", contentType);
        response.setHeader("Content-Length", "" + fileLength);
        response.setHeader("Pragma", "no-cache;");
        response.setHeader("Expires", "-1;");
        
        try(
        		InputStream fis = new FileInputStream(saveFileName);
                OutputStream out = response.getOutputStream();
        ){
        	    int readCount = 0;
        	    byte[] buffer = new byte[1024];
            while((readCount = fis.read(buffer)) != -1){
            		out.write(buffer,0,readCount);
            }
        }catch(Exception ex){
            throw new RuntimeException("file Save Error");
        }
	}
}
