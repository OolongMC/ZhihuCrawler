package com.github.oolongmc.application.ZhihuCrawler;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
/**
 * 这是程序的入口，用来处理参数或进行初始化。
 */
public class Main{
    
    private static final String HELP = """
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
        
    private final static DEFAULT_CONFIG = new Config(Config.GetMethod.CHROME, null, false);
    
    public static void main (String[] args) throws IOException, InterruptedException{
        Config config = init();
        processingArgs(args, config);
    }
    
    private static Config init(){
        Print.basePrint(new AttributedString("欢迎使用ZhihuCrawler\n", GREEN));
        Files.createDirectories(Path.of("./Collection/.temp/"));
        Path configPath = Path.of("./Collection/config.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        Config config = null;
        
        if(Files.exists(configPath)){
            try{
                config = mapper.readValue(Files.readString(configPath), Config.class);
            }catch(JsonProcessingException | IOException e){
                config = DEFAULT_CONFIG;
                try{
                    Files.writeString(configPath, mapper.writeValueAsString(DEFAULT_CONFIG));
                }catch(IOException e){}
                Print.basePrint("配置文件无效，已重置。\n");
            }
        }else{
            config = DEFAULT_CONFIG;
            try{
                Files.writeString(configPath, mapper.writeValueAsString(DEFAULT_CONFIG));
            }catch(IOException e){}
        }
        
        return config;
    }
    
    private static void processingArgs(String args[], Config config) throws IOException, InterruptedException{
        if(args.length == 0){
            if(config.lastQuestionUrl != null && isOver == false){
                chooseCatchWayByConfig(url, config);
            }else{
                Print.basePrint(HELP);
                System.exit(0);
            }
        }
        switch(args[0]){
            case "-c":
            case "--catch":
                if(args.length == 4){
                    Crawler.ZhihuCrawler(args[1], Config.GetMethod.CHROME, Path.of(args[2]), Path.of(args[3]));
                }
                else if(args.length == 3){
                    Crawler.ZhihuCrawler(args[1], Config.GetMethod.CHROME, null, Path.of(args[2]));
                }
                else if(args.length == 2){
                    Crawler.ZhihuCrawler(args[1], Config.GetMethod.CHROME, null, null);
                }
                else{
                    Print.basePrint("不合法参数。\n");
                    Print.basePrint(HELP);
                }
                break;
            case "-r":
            case "--read":
                Print.basePrint("未来计划添加阅读功能。\n");
                break;
            default:
                Print.basePrint("不合法参数。\n");
                Print.basePrint(HELP + "\n");
        }
    }
    
        private static void chooseCatchWayByConfig(String url, Config config){
        switch(config.getMethod){
            case URL:
                
            case CHROME:
                
        }
    }
}