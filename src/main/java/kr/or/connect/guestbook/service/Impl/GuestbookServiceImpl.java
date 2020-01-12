package kr.or.connect.guestbook.service.Impl;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.connect.guestbook.dao.GuestbookDao;
import kr.or.connect.guestbook.dao.LogDao;
import kr.or.connect.guestbook.dto.Guestbook;
import kr.or.connect.guestbook.dto.Log;
import kr.or.connect.guestbook.service.GuestbookService;

// dao -> @Repositoty
// 서비스 -> @Service
@Service
public class GuestbookServiceImpl implements GuestbookService{
	//서비스 내부에서는 GuestbookDao 사용할 것 -> 빈을 자동으로 등록하는 어노테이션 
	//GuestbookDao를 guestbookDao로 사용할 것이다 -> 자동으로 생성 후 주입해줌 
	@Autowired
	GuestbookDao guestbookDao;
	
	@Autowired
	LogDao logDao;

	//방명록 목록 가져오기 
	@Override
	@Transactional //readOnly connection 적용 
	public List<Guestbook> getGuestbooks(Integer start) {
		List<Guestbook> list = guestbookDao.selectAll(start, GuestbookService.LIMIT);
		return list;
	}
 
	@Override
	@Transactional(readOnly=false)
	public int deleteGuestbook(Long id, String ip) {
		int deleteCount = guestbookDao.deleteById(id);
		//삭제 시 로그 남기기 
		Log log = new Log();
		log.setIp(ip);
		log.setMethod("delete");
		log.setRegdate(new Date()); //로그에 남길 정보들 
		logDao.insert(log);
		return deleteCount;
	}

	@Override
	@Transactional(readOnly=false)
	public Guestbook addGuestbook(Guestbook guestbook, String ip) {
		guestbook.setRegdate(new Date());
		Long id = guestbookDao.insert(guestbook);
		guestbook.setId(id);
		
		//상단까지는 실행됐지만, 하단에 의도적으로 삽입된 예외로 인해 실행 x 
//		if(1 == 1)
//			throw new RuntimeException("test exception");
		
		Log log = new Log();
		log.setIp(ip);
		log.setMethod("insert");
		log.setRegdate(new Date());
		logDao.insert(log);
		
		
		return guestbook;
	}

	@Override
	public int getCount() {
		return guestbookDao.selectCount();
	}
	
	
}