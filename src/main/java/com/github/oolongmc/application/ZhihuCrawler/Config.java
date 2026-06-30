package com.github.oolongmc.application.ZhihuCrawler;

/**
 * 本类用来处理本软件的配置信息。
 */
public class Config{
    
    public GetMethod getMethod;
    
    public String lastQuestionUrl;
    
    public boolean isOver;
    
    public String cookiePath;
    
    public String curlImpersonatePath;
    
    public Config(GetMethod getMethod, String lastQuestionUrl, boolean isOver) {
        this.getMethod = getMethod;
        this.lastQuestionUrl = lastQuestionUrl;
        this.isOver = isOver;
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