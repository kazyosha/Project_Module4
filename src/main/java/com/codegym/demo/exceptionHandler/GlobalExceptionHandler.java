package com.codegym.demo.exceptionHandler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    //Runtime
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntime(RuntimeException ex, Model model) {
        model.addAttribute("statusCode", 500);
        model.addAttribute("title", "Lỗi Runtime");
        model.addAttribute("message", ex.getMessage());
        return "errors/error";
    }
    //Database
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDatabase(DataIntegrityViolationException ex, Model model) {
        model.addAttribute("statusCode", 500);
        model.addAttribute("title", "Lỗi Dữ Liệu");
        model.addAttribute("message", ex.getRootCause().getMessage());
        return "errors/error";
    }
    //404 not found
    @ExceptionHandler(NoHandlerFoundException.class) // 404
    public String handle404(NoHandlerFoundException ex, Model model) {
        model.addAttribute("statusCode", 404);
        model.addAttribute("title", "Không tìm thấy trang");
        model.addAttribute("message", "Trang bạn yêu cầu không tồn tại.");
        return "errors/error";
    }
    //Genaral
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("statusCode", 500);
        model.addAttribute("title", "Lỗi Hệ Thống");
        model.addAttribute("message", "Có lỗi xảy ra: " + ex.getMessage());
        return "errors/error";
    }
}

