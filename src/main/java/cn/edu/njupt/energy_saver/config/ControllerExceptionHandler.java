package cn.edu.njupt.energy_saver.config;

import cn.edu.njupt.energy_saver.exception.LocalRuntimeException;
import cn.edu.njupt.energy_saver.util.HttpResponse;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


//@ControllerAdvice
//@Controller
public class ControllerExceptionHandler implements ErrorController {


    @ResponseStatus(code = HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    @RequestMapping("/error")
    @ResponseBody
    public HttpResponse<Void> handleException(Exception uncaught) throws Exception {

        if (uncaught == null)
            return HttpResponse.failure("No Exception Found", null);

        if(uncaught instanceof LocalRuntimeException){
            LocalRuntimeException localRuntimeException = (LocalRuntimeException) uncaught;
            return HttpResponse.failure(localRuntimeException.getMessage(), localRuntimeException.getError().getCode());
        }else
            return HttpResponse.failure(uncaught.getMessage(), null);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
