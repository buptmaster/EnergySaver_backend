package cn.edu.njupt.energy_saver.util;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;

@Component
@ControllerAdvice
public class JsonWrapper extends MappingJackson2HttpMessageConverter implements ResponseBodyAdvice {

    private static final String CLASS_NAME = "cn.edu.njupt.energy_saver.util.HttpResponse";


    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        if (!(object instanceof HttpResponse)) {
            object = HttpResponse.success(object);
        }
        super.writeInternal(object, type, outputMessage);
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return !CLASS_NAME.equals(returnType.getParameterName());
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        return body == null ? HttpResponse.success(null) : body;
    }
}
