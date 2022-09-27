package com.example.demo.controller;

import com.example.demo.exception.client.CAccessDeniedException;
import com.example.demo.exception.client.CAuthenticationEntryPointException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/exception")
public class ExceptionController {

    @GetMapping(value = "/entrypoint")
    public void entryPointException(){
        throw new CAuthenticationEntryPointException();
    }
    
    @GetMapping(value = "/accessdenied")
    public void accessDeniedException(){
        throw new CAccessDeniedException();
    }

}
