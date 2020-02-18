package kr.or.connect.guestbook.argumentresolver;

import java.util.HashMap;
import java.util.Map;

//아규먼트리졸버는 맵을 직접 사용할 수 없음
// -> 클래스를 만들어서 내부 맵을 선언 
public class HeaderInfo {
	private Map<String, String> map;
	
	public HeaderInfo() {
		map = new HashMap<>();
	}

	public void put(String name, String value) {
		map.put(name,  value);
	}
	
	public String get(String name) {
		return map.get(name);
	}
}
