package net.csdcodes.controller;

import net.csdcodes.service.exception.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class AppExceptionHandler {

    Logger logger = LoggerFactory.getLogger(AppExceptionHandler.class);

    @ExceptionHandler(FileStorageException.class)
    public ModelAndView handleException(FileStorageException exception, RedirectAttributes redirectAttributes) {

        ModelAndView mav = new ModelAndView();
        mav.addObject("message", exception.getMsg());
        mav.setViewName("error");
        return mav;
    }
}
