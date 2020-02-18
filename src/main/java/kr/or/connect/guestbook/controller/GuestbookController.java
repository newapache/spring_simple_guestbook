package kr.or.connect.guestbook.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.connect.guestbook.argumentresolver.HeaderInfo;
import kr.or.connect.guestbook.dto.Guestbook;
import kr.or.connect.guestbook.service.GuestbookService;

//@Controller-> ComponentScan 시 해당 빈을 찾을 수 있다 
@Controller
public class GuestbookController {
	
//컨트롤러는 서비스를 사용한다. 
	@Autowired
	GuestbookService guestbookService;

	
	//@RequestParam으로 start라는 값을 꺼내 사용한다. 이 때 값이 없다면  defaultValue를 0 
	@GetMapping(path="/list")
	public String list(@RequestParam(name="start", required=false, defaultValue="0") int start,
					   ModelMap model,
					   @CookieValue(value="count", defaultValue="1", required=true) String value,
					   HttpServletResponse response, HeaderInfo headerInfo) {
		
		
/*		String value = null;
		boolean find = false;
		Cookie[] cookies = request.getCookies();
		if(cookies != null) { //처음 요청이 들어왔을 때 에러방지 
			for(Cookie cookie : cookies) {
				if("count".equals(cookie.getName())) {
					find = true;
					value = cookie.getValue();
					break;
				}
			}
		}
		 
		if(!find) {
			value = "1"; //cookie는 String value를 가진다. 
		}else { // 쿠키를 찾았다면.
			try {
				int i = Integer.parseInt(value);
				value = Integer.toString(++i);
			}catch(Exception ex) {
				value = "1";
			}
		}*/
		
		System.out.println("-----------------------------------------------------");
		System.out.println(headerInfo.get("user-agent"));
		System.out.println("-----------------------------------------------------");
		
        // 쿠키 값을 1증가 시킨다.
		try {
			int i = Integer.parseInt(value);
			value = Integer.toString(++i);
		}catch(Exception ex){
			value = "1";
		}
		
		
   // 쿠키 기본사항 저장 ( 새 쿠키 만들기 )
		Cookie cookie = new Cookie("count", value);
		cookie.setMaxAge(60 * 60 * 24 * 365); // 1년 동안 유지.
		cookie.setPath("/"); // '/' 경로 이하에 모두 쿠키 적용. 
		response.addCookie(cookie);
		
		
		
		
		// start로 시작하는 방명록 목록 구하기
		List<Guestbook> list = guestbookService.getGuestbooks(start);
		
		// 전체 페이지수 구하기
		int count = guestbookService.getCount();
		int pageCount = count / GuestbookService.LIMIT;
		if(count % GuestbookService.LIMIT > 0)
			pageCount++;
		
		// 페이지 수만큼 start의 값을 리스트로 저장
		// 예를 들면 페이지수가 3이면
		// 0, 5, 10 이렇게 저장된다.
		// list?start=0 , list?start=5, list?start=10 으로 링크가 걸린다.
		List<Integer> pageStartList = new ArrayList<>();
		for(int i = 0; i < pageCount; i++) {
			pageStartList.add(i * GuestbookService.LIMIT);
		}
		model.addAttribute("list", list);
		model.addAttribute("count", count);
		model.addAttribute("pageStartList", pageStartList);
		model.addAttribute("cookieCount", value); // jsp에게 전달하기 위해서 쿠키 값을 model에 담아 전송한다.
		
		return "list";
	}
	
	
	
	
	@PostMapping(path="/write")
	public String write(@ModelAttribute Guestbook guestbook,
						HttpServletRequest request) {
		String clientIp = request.getRemoteAddr();
		System.out.println("clientIp : " + clientIp);
		guestbookService.addGuestbook(guestbook, clientIp);
		return "redirect:list";
	}
	
	
	@GetMapping(path="/delete")
	public String delete(@RequestParam(name="id", required=true) Long id, 
			             @SessionAttribute("isAdmin") String isAdmin,
			             HttpServletRequest request,
			             RedirectAttributes redirectAttr) {
		if(isAdmin == null || !"true".equals(isAdmin)) { // 세션값이 true가 아닐 경우
			redirectAttr.addFlashAttribute("errorMessage", "로그인을 하지 않았습니다.");
			return "redirect:loginform";
		}
		String clientIp = request.getRemoteAddr();
		guestbookService.deleteGuestbook(id, clientIp);
		return "redirect:list";		
	}
}

