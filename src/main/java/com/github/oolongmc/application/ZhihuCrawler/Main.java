package com.github.oolongmc.application.ZhihuCrawler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * 这是程序的入口，用来处理参数或进行初始化。
 * Terminal 的参数是非必要的，只是为了美观。
 */
public class Main{
    
    public static final String ¥告示¥ = """
    这个软件的核心是知乎的API的URL，来自[【可用】2025年最新python爬取知乎某个问题下面的回答_qiangzhisafe 知乎-CSDN博客](https://blog.csdn.net/m0_54132386/article/details/145031465)。爬取逻辑是自己写的(因为没学PYTHON)。
    """;
    
    private static final String HELP = """
        欢迎使用ZhihuCrawler。
        支持:知乎问题号码、知乎API URL、知乎问题URL。
        这是我的第一个具有实用性的项目，作者连Java都没学完，所以写的可能不是太好，请见谅。
        命令:
        无参	初始化程序(首次使用必要) | 继续上一次捕获进度。
        catch  <URL>	抓取知乎页面。
        config	<OPTION>  <VALUE>	配置ZhihuCrawler(也可以直接修改config.json)。
        read  <JSON_FILE>	阅读已抓取页面。
        
        注意:
        curl-impersonate的核心是curl-impersonate-chrome(.exe)文件，其余在压缩包内的都是执行它的脚本，脚本内容已在本程序中内置，所以只需要指定这个本体的路径。
        𝓒𝓱𝓲𝓷𝓪_𝓞𝓸𝓵𝓸𝓷𝓰.
        """;
        
        private static final AttributedStyle GREEN = AttributedStyle
        .DEFAULT
        .foreground(AttributedStyle.GREEN);
    
    public static void main (String[] args) throws IOException, InterruptedException{
        Terminal terminal = TerminalBuilder
            .builder()
            .system(true)
            .jansi(true)
            .build();
        Config config = init(terminal);
        
        processingArgs(args, config, terminal);
    }
    
    private static Config init(Terminal terminal) throws IOException{
        Print.basePrint(new AttributedString("欢迎使用ZhihuCrawler\n", GREEN), terminal);
        Files.createDirectories(Path.of("./Collection/.temp/"));
        Files.createDirectories(Path.of("./Collection/Save/"));
        Path configPath = Path.of("./Collection/config.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        Config config = null;
        
        if(Files.exists(configPath)){
            try{
                config = mapper.readValue(Files.readString(configPath), Config.class);
            }catch(/*JsonProcessingException | */IOException e){
                config = Config.getDefaultConfig();
                try{
                    Files.writeString(configPath, mapper.writeValueAsString(Config.getDefaultConfig()));
                }catch(IOException err){}
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
    
    private static void processingArgs(String args[], Config config, Terminal terminal) throws IOException, InterruptedException{
        if(args.length == 0){
            if(config.lastQuestionUrl != null && !config.isOver){
                chooseCatchWayByConfig(config.lastQuestionUrl, config, terminal);
                return;
            }else{
                Print.basePrint(HELP);
                return;
            }
        }
        switch(args[0]){
            case "catch":
                if(args.length == 2){
                    args[1] = getApiUrl(args[1]);
                    chooseCatchWayByConfig(args[1], config, terminal);
                }
                else{
                    Print.basePrint("不合法参数。\n");
                    Print.basePrint(HELP);
                }
                break;
            case "config":
                Print.basePrint("未来计划添加，因为按AI说的这里要反射，我还没学。\n");
                break;
            case "read":
                Print.basePrint("未来计划添加阅读功能。\n");
                //Reader.render(null, terminal);
                break;
            default:
                Print.basePrint("不合法参数。\n");
                Print.basePrint(HELP + "\n");
        }
    }
    
    private static String getApiUrl(String url){
        Matcher api = Pattern.compile(".*www\\.zhihu\\.com/api/v4/questions/\\d+/feeds.*").matcher(url);
        Matcher front = Pattern.compile(".*www\\.zhihu\\.com/question/(\\d+).*").matcher(url);
        Matcher number = Pattern.compile("^\\d+$").matcher(url);
        
        if(api.find()){
            return url;
        }
        else if(front.find()){
            return "https://www.zhihu.com/api/v4/questions/" + front.group(1) + "/feeds";
        }
        else if(number.find()){
            return "https://www.zhihu.com/api/v4/questions/" + url + "/feeds";
        }
        else{
            Print.basePrint("不合法参数。\n");
            System.exit(0);
            return null;
        }
    }
    
    private static void chooseCatchWayByConfig(String url, Config config, Terminal terminal) throws IOException, InterruptedException{
        String questionNumber = url.replaceAll("https://www.zhihu.com/api/v4/questions/(\\d+)/feeds.*", "$1");
        Files.createDirectories(Path.of("./Collection/Save/" + questionNumber + "/"));
        config.lastQuestionUrl = url;
        config.isOver = false;
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        
        Files.writeString(Path.of("./Collection/config.json"), mapper.writeValueAsString(config));
        
        Print.basePrint("开始爬取问题" + questionNumber + "。\n");
        Print.basePrint("方式: " + config.getMethod.getWayDescription() + "。\n");
        try{
            switch(config.getMethod){
                case URL:
                    Crawler.catchByJavaBaseUrl(config, terminal);
                case CHROME:
                    Crawler.catchByChromeCurl(config, terminal);
            }
        }finally{
            terminal.close();
        }
    }
    
    private static void setConfig(){
        
    }
}