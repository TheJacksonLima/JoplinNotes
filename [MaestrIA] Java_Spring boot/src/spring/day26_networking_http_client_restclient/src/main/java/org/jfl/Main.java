package org.jfl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.*;
import java.util.List;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws IOException {
        var context = SpringApplication.run(Main.class, args);

    }

}