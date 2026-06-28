package com.github.oolongmc.application.ZhihuCrawler;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import java.io.IOException;

/**
 * 这个类用来规范整个项目里面的Print的方法
 */
public class Print{
    
    /**
     * 基础打印(打印不换行)。
     */
    public static void basePrint(String s){
        System.out.print(s);
    }
    
    public static void basePrint (AttributedString s) throws IOException{
        Terminal terminal = TerminalBuilder.builder().system(true).jansi(true).build();
        basePrint(s, terminal);
        terminal.flush();
        terminal.close();
    }
    
    
    public static void basePrint(AttributedString s, Terminal terminal){
        s.print(terminal);
        terminal.flush();
    }
}