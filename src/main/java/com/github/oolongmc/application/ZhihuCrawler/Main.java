package com.github.oolongmc.application.ZhihuCrawler;

import java.io.IOException;
import java.nio.file.Path;

public class Main{
    
    private static String help = """
        欢迎使用ZhihuCrawler。
        这是我的第一个具有实用性的项目，作者连Java都没学完，所以写的可能不是太好，请见谅。
        命令:
        -c | --catch  <URL>	抓取知乎页面，默认使用./cookie.txt作为Cookie。
        -c | --catch  <URL>  <COOKIE_PATH>	抓取知乎页面，指定Cookie文件。
        -c | --catch  <URL>  <CURL_PATH>  <COOKIE_PATH>	抓取知乎页面，指定Cookie文件和curl-impersonate的可执行文件的路径。
        -r | --read  <JSON_FILE>	阅读已抓取页面。
        
        注意:
        知乎页面都是如\033[1;33m"https://www.zhihu.com/question/XXX/"\033[0m，要将问题编号复制下来放到\033[1;33m"https://www.zhihu.com/api/v4/questions/XXX/feeds"\033[0m中，然后再使用本程序。
        curl-impersonate的核心是curl-impersonate-chrome(.exe)文件，其余在压缩包内的都是执行它的脚本，脚本内容已在本程序中内置，所以只需要指定这个本体的路径。
        𝓒𝓱𝓲𝓷𝓪_𝓞𝓸𝓵𝓸𝓷𝓰.
        """;
    
    public static void main (String[] args) throws IOException, InterruptedException{
        if(args.length == 0){
            System.out.println(help);
            System.exit(0);
        }
        switch(args[0]){
            case "-c":
            case "--catch":
                if(args.length == 4){
                    Crawler.ZhihuCrawler(args[1], Crawler.GetMethod.CHROME, Path.of(args[2]), Path.of(args[3]));
                }
                else if(args.length == 3){
                    Crawler.ZhihuCrawler(args[1], Crawler.GetMethod.CHROME, null, Path.of(args[2]));
                }
                else if(args.length == 2){
                    Crawler.ZhihuCrawler(args[1], Crawler.GetMethod.CHROME, null, null);
                }
                else{
                    System.out.println("不合法参数。");
                    System.out.println(help);
                }
                break;
            case "-r":
            case "--read":
                System.out.println("未来计划添加阅读功能。");
                break;
            default:
                System.out.println("不合法参数。");
                System.out.println(help);
        }
    }
}