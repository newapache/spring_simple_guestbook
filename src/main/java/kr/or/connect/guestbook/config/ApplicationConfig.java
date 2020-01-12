 package kr.or.connect.guestbook.config;

 
 import org.springframework.context.annotation.ComponentScan;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.context.annotation.Import;

 @Configuration
 @ComponentScan(basePackages = { "kr.or.connect.guestbook.dao",  "kr.or.connect.guestbook.service"})
 @Import({ DBConfig.class })
 public class ApplicationConfig {

 }
 
////dao나 service에 구현된 컴포넌트들을 필요로하는 config -> 해당 패키지들을 지정함 
//@ComponentScan(basePackages = { "kr.or.connect.guestbook.dao",  "kr.or.connect.guestbook.service"})



// //수행될때 내부적으로 dbconfig에 사용되고 있는 것들을 사용하기 위함 
//@Import({ DBConfig.class })
