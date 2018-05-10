package com.example.websoket;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class TT {

    public static void main(String[] args) throws IOException {
        System.out.println(new ClassPathResource("").getFile());
    }
}
