package kr.or.connect.guestbook.controller;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.connect.guestbook.dto.Guestbook;
import kr.or.connect.guestbook.service.GuestbookService;


@RestController
//해당 api클래스 안에서 같은 매핑으로 여러 작업을 수행 -> @RequestMapping을 선언 
@RequestMapping(path="/guestbooks")
public class GuestbookApiController {
	@Autowired
	GuestbookService guestbookService; //이 클래스에서도 서비스를 사용할 것 
	
	//dispatcherServlet은 jsonMessageConvert를 내부적으로 사용, Map객체를 json으로 변환해 전송 
	@GetMapping //-> path는 따로 없다. get방식으로 요청이 들어오면 list 메소드를 실행할 것 
	public Map<String, Object> list(@RequestParam(name="start", required=false, defaultValue="0") int start) {
		
		List<Guestbook> list = guestbookService.getGuestbooks(start);
		
		int count = guestbookService.getCount();
		int pageCount = count / GuestbookService.LIMIT;
		if(count % GuestbookService.LIMIT > 0)
			pageCount++;
		
		List<Integer> pageStartList = new ArrayList<>();
		for(int i = 0; i < pageCount; i++) {
			pageStartList.add(i * GuestbookService.LIMIT);
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("count", count);
		map.put("pageStartList", pageStartList);
		
		return map;
	}
	
	@PostMapping // 클라이언트에는 Json으로 변환되어 전송될 것 
	public Guestbook write(@RequestBody Guestbook guestbook,
						HttpServletRequest request) {
		String clientIp = request.getRemoteAddr();
		// id가 입력된 guestbook이 반환된다.
		Guestbook resultGuestbook = guestbookService.addGuestbook(guestbook, clientIp);
		return resultGuestbook;
	}
	
	@DeleteMapping("/{id}") //delete는 path정보가 있다. 
	//{id}와 같은 정보 ->  pathvariable
	public Map<String, String> delete(@PathVariable(name="id") Long id,
			HttpServletRequest request) {
		String clientIp = request.getRemoteAddr();
		
		int deleteCount = guestbookService.deleteGuestbook(id, clientIp);
		return Collections.singletonMap("success", deleteCount > 0 ? "true" : "false");
	}
}