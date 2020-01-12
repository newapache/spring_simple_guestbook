package kr.or.connect.guestbook.service;

import java.util.List;

import kr.or.connect.guestbook.dto.Guestbook;

//서비스 인터페이스 정의 
public interface GuestbookService {
	//상수로 지정 -> 나중에 변경이 용이 
	public static final Integer LIMIT = 5;
	public List<Guestbook> getGuestbooks(Integer start);
	public int deleteGuestbook(Long id, String ip);
	public Guestbook addGuestbook(Guestbook guestbook, String ip);
	public int getCount();
}


//방명록 정보를 페이지 별로 읽어오기 
//페이징처리를 위한 전체 건 수 읽어오기
//아이디에 해당하는 방명록 가져오기 
