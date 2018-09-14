package com.sunchangpeng.zoo.ui;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class TranslationCommands {
    @ShellMethod("Translate text from one language to another.")
    public String translate(@ShellOption String text) {
        return "hello";
    }
}
