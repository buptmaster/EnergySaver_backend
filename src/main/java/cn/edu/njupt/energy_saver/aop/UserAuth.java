package cn.edu.njupt.energy_saver.aop;

import cn.edu.njupt.energy_saver.dataobject.UserControl;
import cn.edu.njupt.energy_saver.exception.CustomError;
import cn.edu.njupt.energy_saver.exception.LocalRuntimeException;
import cn.edu.njupt.energy_saver.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class UserAuth {

    @Autowired
    UserService userService;

    @Pointcut("execution(* cn.edu.njupt.energy_saver.controller.StrategyController.*(..)) " +
            "&& execution(* cn.edu.njupt.energy_saver.controller.DeviceController.*(..))")
    public void userAuth() {
    }

    @Pointcut("execution(* cn.edu.njupt.energy_saver.controller.UserController.getAll(..))" +
            "&& execution(* cn.edu.njupt.energy_saver.controller.UserController.deleteUser(..))" +
            "&& execution(* cn.edu.njupt.energy_saver.controller.UserController.addUser(..))")
    public void adminAuth(){
    }

    @Around("userAuth()")
    public Object doAuth(ProceedingJoinPoint point) throws Throwable {
        for (Object arg : point.getArgs()) {
            if (arg instanceof UserControl) {
                UserControl u = ((UserControl) arg);
                if (!u.getRole().equals("ADMIN") && !u.getRole().equals("NORMAL")) {
                    throw new LocalRuntimeException(CustomError.UNAUTHORIZED);
                }
            }
        }
        return point.proceed();
    }

    @Around("adminAuth()")
    public Object doAdminAuth(ProceedingJoinPoint point) throws Throwable {
        for (Object arg : point.getArgs()) {
            if (arg instanceof UserControl) {
                UserControl u = ((UserControl) arg);
                if (!u.getRole().equals("ADMIN") ) {
                    throw new LocalRuntimeException(CustomError.UNAUTHORIZED);
                }
            }
        }
        return point.proceed();
    }



}
