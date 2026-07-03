package com.github.oolongmc.application.ZhihuCrawler;

import java.nio.file.Path;

/**
 * 本类用来处理本软件的配置信息。
 */
public class Config{
    
    public GetMethod getMethod;
    
    public String lastQuestionUrl;
    
    public boolean isOver;
    
    public String cookiePath;
    
    public String curlImpersonatePath;
    
    public static Config getDefaultConfig(){
        Config tmp = new Config();
        tmp.getMethod = GetMethod.CHROME;
        tmp.lastQuestionUrl = null;
        tmp.isOver = false;
        tmp.cookiePath = "./cookie.txt";
        tmp.curlImpersonatePath = null;
        return tmp;
    }
    
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
}