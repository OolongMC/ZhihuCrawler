package com.github.oolongmc.application.ZhihuCrawler;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
/**
 * 这是程序的入口，用来处理参数或进行初始化。
 * Terminal 的参数是非必要的，只是为了美观。
 */
public class Main{
    
    private static final String HELP = """
        欢迎使用ZhihuCrawler。
        这是我的第一个具有实用性的项目，作者连Java都没学完，所以写的可能不是太好，请见谅。
        命令:
        无参	初始化程序(首次使用必要) | 继续上一次捕获进度。
        catch  <URL>	抓取知乎页面。
        config	<OPTION>  <VALUE>	配置ZhihuCrawler(也可以直接修改config.json)。
        read  <JSON_FILE>	阅读已抓取页面。
        
        注意:
        知乎页面都是如\033[1;33m"https://www.zhihu.com/question/XXX/"\033[0m，要将问题编号复制下来放到\033[1;33m"https://www.zhihu.com/api/v4/questions/XXX/feeds"\033[0m中，然后再使用本程序。
        curl-impersonate的核心是curl-impersonate-chrome(.exe)文件，其余在压缩包内的都是执行它的脚本，脚本内容已在本程序中内置，所以只需要指定这个本体的路径。
        𝓒𝓱𝓲𝓷𝓪_𝓞𝓸𝓵𝓸𝓷𝓰.
        """;
    
    public static void main (String[] args) throws IOException, InterruptedException{
        terminal = TerminalBuilder
            .builder()
            .system(true)
            .jansi(true)
            .build();
        Config config = init(terminal);
        
        processingArgs(args, config, terminal);
    }
    
    private static Config init(Terminal terminal){
        Print.basePrint(new AttributedString("欢迎使用ZhihuCrawler\n", GREEN), terminal);
        Files.createDirectories(Path.of("./Collection/.temp/"));
        Path configPath = Path.of("./Collection/config.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        Config config = null;
        
        if(Files.exists(configPath)){
            try{
                config = mapper.readValue(Files.readString(configPath), Config.class);
            }catch(JsonProcessingException | IOException e){
                config = Config.getDefaultConfig();
                try{
                    Files.writeString(configPath, mapper.writeValueAsString(DEFAULT_CONFIG));
                }catch(IOException e){}
                Print.basePrint("配置文件无效，已重置。\n");
            }
        }else{
            config = Config.getDefaultConfig();
            try{
                Files.writeString(configPath, mapper.writeValueAsString(config));
            }catch(IOException e){}
        }
        
        return config;
    }
    
    private static void processingArgs(String args[], Config config) throws IOException, InterruptedException{
        if(args.length == 0){
            if(config.lastQuestionUrl != null && isOver == false){
                chooseCatchWayByConfig(config.lastQuestionUrl, config);
            }else{
                Print.basePrint(HELP);
                System.exit(0);
            }
        }
        switch(args[0]){
            case "catch":
                if(args.length == 2){
                    chooseCatchWayByConfig(args[1], config);
                }
                else{
                    Print.basePrint("不合法参数。\n");
                    Print.basePrint(HELP);
                }
                break;
            case "config":
                Print.basePrint("未来计划添加，因为按AI说的这里要反射，我还没学。");
                break;
            case "read":
                Print.basePrint("未来计划添加阅读功能。\n");
                break;
            default:
                Print.basePrint("不合法参数。\n");
                Print.basePrint(HELP + "\n");
        }
    }
    
    private static void chooseCatchWayByConfig(String url, Config config, Terminal terminal){
        questionNumber = url.replaceAll("https://www.zhihu.com/api/v4/questions/(\\d+)/feeds.*", "$1");
            
        Print.basePrint("开始爬取问题" + questionNumber + "。\n");
        Print.basePrint("方式: " + config.getMethod.getWayDescription() + "。\n");
        try{
            switch(config.getMethod){
                case URL:
                    Crawler.
                case CHROME:
                    
            }
        }finally{
            terminal.close();
        }
    }
    
    private static void setConfig(){
        
    }
}