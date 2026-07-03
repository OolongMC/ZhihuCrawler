package com.github.oolongmc.application.ZhihuCrawler;

import com.github.oolongmc.aicodes.grok.HttpUtil;
import com.github.oolongmc.aicodes.deepseek.ZhihuQuestion;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Random;
import java.util.Set;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;


/**
 * 爬虫的核心逻辑。
 */
public final class Crawler{
    /**
     * 用于curl-impersonate的参数。
     * 此命令来自于curl-impersonate项目的curl_chrome116脚本。
     */
    private static final String[] CURL_ARGS = {
        "--ciphers", "TLS_AES_128_GCM_SHA256,TLS_AES_256_GCM_SHA384,TLS_CHACHA20_POLY1305_SHA256,ECDHE-ECDSA-AES128-GCM-SHA256,ECDHE-RSA-AES128-GCM-SHA256,ECDHE-ECDSA-AES256-GCM-SHA384,ECDHE-RSA-AES256-GCM-SHA384,ECDHE-ECDSA-CHACHA20-POLY1305,ECDHE-RSA-CHACHA20-POLY1305,ECDHE-RSA-AES128-SHA,ECDHE-RSA-AES256-SHA,AES128-GCM-SHA256,AES256-GCM-SHA384,AES128-SHA,AES256-SHA", 
        "-H", "sec-ch-ua: \"Chromium\";v=\"116\", \"Not)A;Brand\";v=\"24\", \"Google Chrome\";v=\"116\"", 
        "-H", "sec-ch-ua-mobile: ?0", 
        "-H", "sec-ch-ua-platform: \"Windows\"", 
        "-H", "Upgrade-Insecure-Requests: 1", 
        "-H", "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36", 
        "-H", "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7", 
        "-H", "Sec-Fetch-Site: none", 
        "-H", "Sec-Fetch-Mode: navigate", 
        "-H", "Sec-Fetch-User: ?1", 
        "-H", "Sec-Fetch-Dest: document", 
        "-H", "Accept-Encoding: gzip, deflate, br", 
        "-H", "Accept-Language: en-US,en;q=0.9", 
        "--http2", "--http2-no-server-push", "--compressed", 
        "--tlsv1.2", "--alps", "--tls-permute-extensions", 
        "--cert-compression", "brotli",
        "-sS"
    };
    /**
     * 输出美化:
     */
    private static final AttributedStyle GREEN = AttributedStyle
        .DEFAULT
        .foreground(AttributedStyle.GREEN);
    private static final AttributedStyle RED = AttributedStyle
        .DEFAULT
        .foreground(AttributedStyle.RED);
    private static final AttributedStyle PINK = AttributedStyle
        .DEFAULT
        .foreground(255, 192, 203);
    
    /*
     * 直接使用Java自带的方法获取页面，不进行任何配置。
     * 于0.3版本回归。
     */
    public static void catchByJavaBaseUrl(Config config, Terminal terminal){
        StringBuilder cookie = new StringBuilder();
        try{
            String netscapeCookie = Files.readString(Path.of(config.cookiePath));
            // 此处进行Netscape转Header处理。
            // step 1: 去除注释。
            String[] builder1 = netscapeCookie.split("\\R");
            int validLines = 0;
            for(int line = 0; line < builder1.length; line++){
                if(builder1[line].startsWith("#")||builder1[line].isEmpty()){
                    continue;
                }else{
                    builder1[validLines] = builder1[line];
                    validLines++;
                }
            }
            String[] builder2 = Arrays.copyOfRange(builder1, 0, validLines);
            // step2: 按Tab分割。
            // 不行，这里必须用List了。
            List<String> builder3 = new ArrayList<>();
            for(int i = 0;i < builder2.length;i++){
                builder3.addAll(Arrays.asList(builder2[i].split("\t")));
            }
            /* Netscape格式: 域名, 布尔值, 路径, 布尔值, 时间戳, 变量名, 值。
             * Header实际需要的是变量名和值。
             * 所以按Tab分割后的数组长度的1/7为变量数量，第n个变量乘6的位置为变量名，乘7的位置则为值。
             */
            // step3: 拼接。
            for(int i = 1;i <= builder3.size()/7; i++){
                cookie.append(builder3.get(i*7-2));
                cookie.append("=");
                cookie.append(builder3.get(i*7-1));
                if(i != builder3.size()/7){
                cookie.append("; ");
                }
            }
            
            
            Print.basePrint("加载cookie成功！\n");
        }catch(Exception e){
            Print.basePrint(new AttributedString("加载cookie.txt失败，请检查cookie.txt路径是否正确且其格式符合标准，可能无法爬取问题。\n", RED), terminal);
        }
        
        
        
        for(int i = 1;config.isOver == false ;i++){
            HttpUtil website = null;
            try{
                website = HttpUtil.get(config.lastQuestionUrl, null, cookie.toString(), null);
                switch(website.getStatusCode()){
                    case 200:break;
                    case 403:
                        Print.basePrint(new AttributedString("访问失败: 403。\n", RED), terminal);
                        Print.basePrint(new AttributedString("可能并不是爬虫问题，尝试等几天或者更换网络环境重新尝试喵。\n", PINK), terminal);
                        Print.basePrint(new AttributedString("事实上成功率百分之几，非常低。可以尝试一下使用chrome模式喵。\n", PINK), terminal);
                        System.exit(1);
                        break;
                    default:
                        Print.basePrint(new AttributedString("访问失败: " + website.getStatusCode() + "。\n", RED), terminal);
                        Print.basePrint(new AttributedString("尝试重新访问。\n", PINK), terminal);
                         continue;
                }
                
            }catch(Exception e){
                Print.basePrint(new AttributedString("get" + i + "访问失败: " + e.getMessage() + "。\n", RED), terminal);
                e.printStackTrace();
                Print.basePrint(new AttributedString("尝试重新访问。\n", PINK), terminal);
                continue;
            }
            Print.basePrint("get" + i + ": ");
            processJson(website.getResponseBody(), config, terminal);
            Random random = new Random();
            try{Thread.sleep(3000 + random.nextInt(2001));}catch(InterruptedException e){}
        }
    }
    
    /**
     * 暂时待更新，未使用外部程序的方法。
     */
    public static void catchByParametricSimulationOfChromeCurl(Path customizeApplicationPath){}
    
    
    /**
     * 
     */
    public static void catchByChromeCurl(Config config, Terminal terminal) throws IOException, InterruptedException{
        String os = null;
        String arch = System.getProperty("os.arch");
        if(System.getProperty("os.name").startsWith("Windows")){
            os = "Windows";
        }else if(System.getProperty("os.name").startsWith("MacOS")){
            os = "MacOS";
        }else{
            os = "Linux";
        }
        // 此处考量: 一般其他输出都是基于Linux的系统吧，应该喵。
        
        Path applicationPath;
        
        Print.basePrint("系统信息:" + os + "_" + arch + "\n");
        
        if(config.curlImpersonatePath == null){
            applicationPath = Path.of("./Collection/.temp/curl-impersonate-chrome");
            try(InputStream app = Crawler.class.getResourceAsStream("/assets/" + os + "/" + arch + "/curl-impersonate-chrome")){
                if(app != null){
                    Files.copy(app, applicationPath, StandardCopyOption.REPLACE_EXISTING);
                    Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-xr-x");
                    Files.setPosixFilePermissions(applicationPath, perms);
            // tip: Windows官方没有提供构建，所以本程序也不进行Windows构建的解压工作(只区分系统判断文件名是否有.exe)。
                }else{
                    Print.basePrint(new AttributedString("不受支持的系统。请手动前往https://github.com/lwthiker/curl-impersonate中下载或者自己编译程序，并手动指定程序位置。\n", RED), terminal);
                    System.exit(1);
                }
            }catch(IOException e){
                Print.basePrint(new AttributedString("拷贝可执行文件失败：" + e.getMessage() + "\n", RED), terminal);
            }
        }else{
            applicationPath = Path.of(config.curlImpersonatePath);
            // Windows用户必须要手动指定自定义程序，自定义程序必须保证可执行性(包括类Unix系统)以及文件名的正确，所以不用我们负责。
        }
        
        for(int i = 1;config.isOver == false ;i++){
            
            // 合成调用命令。
            String[] command = new String[CURL_ARGS.length + 4];
            command[0] = applicationPath.toString();
            System.arraycopy(CURL_ARGS, 0, command, 1, CURL_ARGS.length);
            command[command.length - 3] = config.lastQuestionUrl;
            command[command.length - 2] = "-b";
            command[command.length - 1] = config.cookiePath;
            
            
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process getJson = pb.start();
            
            StringBuilder json = new StringBuilder();
            Thread readerThread = new Thread(() -> {// Lambda表达式
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(getJson.getInputStream()))){
                    String line;
                    while ((line = reader.readLine()) != null) {
                        json.append(line).append("\n");
                    }
                }catch(IOException e){}
            });
            readerThread.start();
            int exitCode = getJson.waitFor();
            if(exitCode == 0&&!json.toString().isEmpty()){
                Print.basePrint("get" + i + ": ");
                processJson(json.toString(), config, terminal);
                
            }
            else if(exitCode == 0){
                Print.basePrint(new AttributedString("get" + i + ": " + "curl没有提供反回结果，", RED), terminal);
                Print.basePrint(new AttributedString("尝试重新访问。\n", PINK), terminal);
            }
            else{
                Print.basePrint(new AttributedString("get" + i + ": " + "访问失败: " + json.toString() + "，", RED), terminal);
                Print.basePrint(new AttributedString("尝试重新访问。\n", PINK), terminal);
                continue;
            }
            Random random = new Random();
            try{Thread.sleep(3000 + random.nextInt(2001));}catch(InterruptedException e){}
        }
    }
    
    
    private static void processJson(String json, Config config, Terminal terminal){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        ZhihuQuestion q = null;
        try{
            q = mapper.readValue(json, ZhihuQuestion.class);
        }catch(JsonProcessingException e){
            Print.basePrint(new AttributedString("处理JSON失败，请报告Bug。\n", RED), terminal);
            Print.basePrint(new AttributedString("原因：" + e.getMessage() + "\n", PINK), terminal);
            e.printStackTrace();
            Print.basePrint(new AttributedString("响应内容:\n", PINK), terminal);
            Print.basePrint(json + "\n");
            
            System.exit(1);
        }
        if(q.error != null){
            switch(q.error.code){
                case 40353:
                    Print.basePrint(new AttributedString("响应失败，返回内容为未登录，可能是cookie过期或格式错误。\n", RED), terminal);
                    System.exit(0);
                    break;
                case 10003:
                    Print.basePrint(new AttributedString("响应失败，返回内容为客户端版本过低，这是知乎抽风的表现，不必理会，过一段时间再试试喵。\n", PINK), terminal);
                    System.exit(0);
                    break;
                default:
                    Print.basePrint(new AttributedString("响应失败" + q.error.message + "\n", RED), terminal);
                    System.exit(0);
                    break;
            }
        }else{
            try{
                String questionNumber = config.lastQuestionUrl.replaceAll("https://www.zhihu.com/api/v4/questions/(\\d+)/feeds.*", "$1");
                Files.writeString(Path.of("Collection/Save/" + questionNumber + "/" + questionNumber + '_' + q.paging.page + ".json"), json);
                Files.writeString(Path.of("./Collection/config.json"), mapper.writeValueAsString(config));
            }catch(IOException e){
                Print.basePrint(new AttributedString("文件保存失败" + e.getMessage() + "\n", RED), terminal);
            }
            Print.basePrint("Done.\n");
            config.lastQuestionUrl = q.paging.next.replace("\\u0026", "&");
            config.isOver = q.paging.is_end;
        }
    }
}


