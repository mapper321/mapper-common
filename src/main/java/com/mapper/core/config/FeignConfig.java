package com.mapper.core.config;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.codec.Decoder;
import feign.codec.Encoder;
/**
 * 统一配置Feign 的Encoder和Decoder的Jackson转换方式<br>
 * 在服务通过Feign进行请求的时候，传NULL值引用类型值时，会出现类型转换异常，由于HttpMessageConverters直接把NULL转为了""
 * @author mapper
 *
 */
@Configuration
public class FeignConfig {
    
	@Bean
    public Decoder feignDecoder() {
		MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(customObjectMapper());
        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
		supportedMediaTypes.add(new MediaType("text", "plain"));
		supportedMediaTypes.add(new MediaType("application", "json"));
		jacksonConverter.setSupportedMediaTypes(supportedMediaTypes);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
    }
    @Bean
    public Encoder feignEncoder(){
    	MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(customObjectMapper());
        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
		supportedMediaTypes.add(new MediaType("text", "plain"));
		supportedMediaTypes.add(new MediaType("application", "json"));
		jacksonConverter.setSupportedMediaTypes(supportedMediaTypes);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
        return new SpringEncoder(objectFactory);
    }
 
    public ObjectMapper customObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        //Customize as much as you want
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DateFormat dateFormat = objectMapper.getDateFormat();
        objectMapper.setDateFormat(new MyDateFormat(dateFormat));
        return objectMapper;
    }
    
    public class MyDateFormat extends DateFormat {
    	 
        private DateFormat dateFormat;
     
        private SimpleDateFormat format1 = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
     
        public MyDateFormat(DateFormat dateFormat) {
            this.dateFormat = dateFormat;
        }
     
        @Override
        public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
            return dateFormat.format(date, toAppendTo, fieldPosition);
        }
     
        @Override
        public Date parse(String source, ParsePosition pos) {
     
            Date date = null;
     
            try {
     
                date = format1.parse(source, pos);
            } catch (Exception e) {
     
                date = dateFormat.parse(source, pos);
            }
     
            return date;
        }
     
        // 主要还是装饰这个方法
        @Override
        public Date parse(String source) throws ParseException {
     
            Date date = null;
     
            try {
     
                // 先按我的规则来
                date = format1.parse(source);
            } catch (Exception e) {
     
                // 不行，那就按原先的规则吧
                date = dateFormat.parse(source);
            }
     
            return date;
        }
     
        // 这里装饰clone方法的原因是因为clone方法在jackson中也有用到
        @Override
        public Object clone() {
            Object format = dateFormat.clone();
            return new MyDateFormat((DateFormat) format);
        }

    }
}
