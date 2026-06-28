package com.github.oolongmc.application.ZhihuCrawler;

import com.github.oolongmc.aicodes.grok.HttpUtil;
import com.github.oolongmc.aicodes.deepseek.ZhihuQuestion;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Random;
import java.util.Set;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;


public class Crawler{
    /**
     * 问题信息:
     */
    private static String questionNumber = null;
    /**
     * 捕获网页需要:
     */
    private final static String[] PARAMS = {    "include", "data[*].is_normal,admin_closed_comment,reward_info,is_collapsed,annotation_action,annotation_detail,collapse_reason,is_sticky,collapsed_by,suggest_edit,comment_count,can_comment,content,editable_content,attachment,voteup_count,reshipment_settings,comment_permission,created_time,updated_time,review_info,relevant_info,question,excerpt,is_labeled,paid_info,paid_info_content,reaction_instruction,relationship.is_authorized,is_author,voting,is_thanked,is_nothelp;data[*].author.follower_count,vip_info,kvip_info,badge[*].topics;data[*].settings.table_of_content.enabled", "limit", "10", "offset", "0", "order", "default", "ws_qiangzhisafe", "1"};
    private final static String[] HEADERS = {
        "User-Agent", "Mozilla/5.0 (Linux 5.10.43; OXF-AN10; aarch64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36",
        "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
        "Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8",
        "Accept-Encoding", "gzip, deflate, br",
        "Upgrade-Insecure-Requests", "1",
        "Sec-Fetch-Dest", "document",
        "Sec-Fetch-Mode", "navigate",
        "Sec-Fetch-Site", "none",
        "Sec-Fetch-User", "?1"
    };
    private static Path cookie = null;
    private static String url = null;
    /**
     * 用于curl-impersonate的参数。
     * 此命令来自于curl-impersonate项目的curl_chrome116脚本。
     */
    private static String[] curlArgs = {
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
    private static Terminal terminal = null;
    /**
     * 爬取方法:
     */
    public enum GetMethod{
        URL("使用JAVA的URL类"),
        CHROME("使用curl-impersonate项目");
        
        private String wayDescription;
        
        private GetMethod(String wayDescription){
            this.wayDescription = wayDescription;
        }
        
        public String getWayDescription(){
            return wayDescription;
        }
    }
    
    /**
     * 爬虫主程序。
     */
    public static void ZhihuCrawler (String url, GetMethod method, Path applicationPath, Path cookieFilePath) throws IOException, InterruptedException{
        Crawler.url = url;
        terminal = TerminalBuilder
            .builder()
            .system(true)
            .jansi(true)
            .build();
        Print.basePrint(new AttributedString("欢迎使用ZhihuCrawler\n", GREEN), terminal);
        Files.createDirectories(Paths.get("./Collection/"));
        /*
        try{
            if(cookieFilePath == null){
                cookie = Files.readString(Paths.get("cookie.txt"));
            }else{
                cookie = Files.readString(cookieFilePath);
            }
            Print.basePrint("加载cookie成功！\n");
        }catch(IOException e){
            Print.basePrint(new AttributedString("未找到cookie.txt，可能无法爬取问题。\n", RED), terminal);
        }
        */
        if(cookieFilePath == null){
            cookie = Paths.get("./cookie.txt");
        }else{
            cookie = cookieFilePath;
        }
        
        questionNumber = url.replaceAll("https://www.zhihu.com/api/v4/questions/(\\d+)/feeds.*", "$1");
        
        Print.basePrint("开始爬取问题" + questionNumber + "。\n");
        Print.basePrint("方式: " + method.getWayDescription() + "。\n");
        try{
            switch(method){
                case URL:
                    catchByJavaBaseUrl();
                    break;
                case CHROME:
                    catchByChromeCurl(applicationPath);
                    break;
                default:break;
            }
        }finally{
            terminal.close();
        }
    }
    
    /*
     * 直接使用Java自带的方法获取页面，不进行任何配置。
     * @deprecated 成功率极低。
     * 现已彻底无法使用，因为cookie改用Netscape格式。
     */
    private static void catchByJavaBaseUrl(){
        boolean is_end = false;
        for(int i = 1;is_end == false ;i++){
            HttpUtil website = null;
            try{
                String cookie = null;
                website = HttpUtil.get(url, PARAMS, cookie, HEADERS);
                // 此方法已经弃用，但由于cookie变量的修改，导致此方法无法成功编译，于是添加了String cookie = null。
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
            is_end = processJson(website.getResponseBody());
            Random random = new Random();
            try{Thread.sleep(3000 + random.nextInt(2001));}catch(InterruptedException e){}
        }
    }
    
    /**
     * 暂时待更新，未使用外部程序的方法。
     */
    private static void catchByParametricSimulationOfChromeCurl(Path customizeApplicationPath){}
    
    
    /**
     * 
     */
    private static void catchByChromeCurl(Path customizeApplicationPath) throws IOException, InterruptedException{
        String os, appName;
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
        
        if(customizeApplicationPath == null){
            Files.createDirectories(Paths.get("./Collection/.temp/"));
            applicationPath = Paths.get("./Collection/.temp/curl-impersonate-chrome");
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
            applicationPath = customizeApplicationPath;
            // Windows用户必须要手动指定自定义程序，自定义程序必须保证可执行性(包括类Unix系统)以及文件名的正确，所以不用我们负责。
        }
        
        boolean is_end = false;
        for(int i = 1;is_end == false ;i++){
            
            // 合成调用命令。
            String[] command = new String[curlArgs.length + 4];
            command[0] = applicationPath.toString();
            System.arraycopy(curlArgs, 0, command, 1, curlArgs.length);
            command[command.length - 3] = url;
            command[command.length - 2] = "-b";
            command[command.length - 1] = cookie.toString();
            
            
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
                is_end = processJson(json.toString());
                
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
    
    
    private static boolean processJson(String json){
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
            return false;// 应付编译器。
        }
        if(q.error != null){
            switch(q.error.code){
                case 40353:
                    Print.basePrint(new AttributedString("响应失败，返回内容为未登录，可能是cookie过期或格式错误\n", RED), terminal);
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
            return false;// 应付编译器。
        }else{
            try{
                Files.writeString(Paths.get("Collection/" + questionNumber + '_' + q.paging.page + ".json"), json);
            }catch(IOException e){
                Print.basePrint(new AttributedString("文件保存失败" + e.getMessage() + "\n", RED), terminal);
            }
            Print.basePrint("Done.\n");
            url = q.paging.next.replace("\\u0026", "&");
            return q.paging.is_end;
        }
    }
}


