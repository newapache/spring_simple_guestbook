package kr.or.connect.guestbook.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import kr.or.connect.guestbook.argumentresolver.HeaderMapArgumentResolver;
import kr.or.connect.guestbook.interceptor.LogInterceptor;


@Configuration
@EnableWebMvc //기본설정을 자동으로 해주는 
@ComponentScan(basePackages = { "kr.or.connect.guestbook.controller" }) //컨트롤러를 알아서 읽어옴
public class WebMvcContextConfiguration extends WebMvcConfigurerAdapter{
	
	//WebMvcConfigurerAdapter : 필요한 정보들을 설정하기 위해 상속받은 후 오버라이딩된 메소드들 정의 
	
	//하단의 설정들은 dispatcher servlet이 읽어낼 수 있도록 작성한다. 
	
	
    @Override	//url요청이 /css/**와 같이 들어오는 경우 /css/ 폴더에서 읽도록 설정 
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(31556926);
        registry.addResourceHandler("/img/**").addResourceLocations("/img/").setCachePeriod(31556926);
        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(31556926);
    }
  
    // default servlet handler를 사용하게 합니다. -> 매핑정보가 없는 요청이 들어왔을 때 
    // 매핑정보가 없는 요청은 원래 spring의 defaultServletHttpRequestHandler가 처리하도록 해준다.
    // was의 defaultServlet이 static한 자원을 읽어서 보여줄 수 있게끔 한다. 
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
   
    // 특정 url에 대한 처리를 컨트롤러 클래스를 작성하지 않고 매핑할 수 있도록 해줌 
    //  '/'라고 요청이 들어오면 index라는 이름의 뷰로 보여주세요 -> 뷰 리졸버가 찾아냄 
    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
    		System.out.println("addViewControllers가 호출됩니다. ");
        registry.addViewController("/").setViewName("index");
    }
    
    // index -> /WEB-INF/views/ 라는 디렉터리 밑의  index.jsp를 보여주세요 
    @Bean
    public InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
    
    @Override
	public void addInterceptors(InterceptorRegistry registry) {
    		registry.addInterceptor(new LogInterceptor());
	}
    
   
  
    @Bean
    public MultipartResolver multipartResolver() {
        org.springframework.web.multipart.commons.CommonsMultipartResolver multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(10485760); // 1024 * 1024 * 10
        return multipartResolver;
    }
    
    @Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    		System.out.println("아규먼트 리졸버 등록..");
		argumentResolvers.add(new HeaderMapArgumentResolver());
	}
    
}