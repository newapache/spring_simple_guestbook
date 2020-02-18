package kr.or.connect.guestbook.argumentresolver;

import java.util.Iterator;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class HeaderMapArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		// TODO Auto-generated method stub
		HeaderInfo headerInfo = new HeaderInfo();
		
		Iterator<String> headerNames = webRequest.getHeaderNames();
		while(headerNames.hasNext()) {
			String headerName = headerNames.next();
			String headerValue = webRequest.getHeader(headerName);
			System.out.println(headerName + " , " + headerValue);
			headerInfo.put(headerName, headerValue);
		}
		
		return headerInfo;

	}

	@Override //인자의 정보를 파라미터로 반환 . 유뮤 구분 / true리턴 시 호출 
	public boolean supportsParameter(MethodParameter parameter) {
		// TODO Auto-generated method stub
		return parameter.getParameterType() == HeaderInfo.class;
	}

}
