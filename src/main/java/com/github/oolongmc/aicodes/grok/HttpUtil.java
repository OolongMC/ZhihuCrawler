package com.github.oolongmc.aicodes.grok;

/**
 * As a human，要诚实，AI就AI，不丢人，只要自己审了，自己改了就行。
 * 𝓒𝓱𝓲𝓷𝓪_𝓞𝓸𝓵𝓸𝓷𝓰.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * 这是一个类，用来快速获取网页内容和进行基础操作。
 * 使用方法：
 * 首先使用{@link #get(String, String[], String, String[])}获取页面内容示例。
 * 之后可以使用成员方法{@link #saveResponseToFile(String)}保存资源文件。
 * 使用{@link #saveHarToFile(String)}保存Har响应文件。
 * 也可以使用{@link #getResponseBody()}获取String的页面内容或是用{@link #getStatusCode()}获得响应的状态代码。
 * 
 * 本类还提供了一个静态方法{@link #mergeHarFiles(String, String...)}来合并Har文件。
 * 
 * 当获取页面内容失败时有2种情况：
 * 1. 请求失败，会直接抛出异常。
 * 2. 请求成功，但是返回502等需要调用者自己检查。
 * 
 * @deprecated 成功率极低。
 * 现已彻底无法使用，因为cookie改用Netscape格式。
 */
public class HttpUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    // 实例持有的数据（随对象回收而释放）
    private final byte[] rawResponseBytes;
    private final String responseBody;
    private final ObjectNode harData;
    private final String originalUrl;
    private final int statusCode;
    private final Map<String, List<String>> responseHeaders;

    /**
     * 私有构造器，通过静态工厂方法创建
     */
    private HttpUtil(String url, String[] appendParams, String jsonCookie, String[] headers) throws Exception {
        this.originalUrl = url;
        
        HttpRequest request = buildRequest(url, appendParams, jsonCookie, headers);
        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

        this.rawResponseBytes = processResponseBody(response);
        this.statusCode = response.statusCode();
        this.responseHeaders = response.headers().map();
        
        String charset = getCharsetFromContentType(response.headers().firstValue("Content-Type").orElse(""));
        this.responseBody = new String(this.rawResponseBytes, charset);
        
        this.harData = buildHarData(url, request, response, this.rawResponseBytes, 
                                   appendParams, jsonCookie, headers);
    }

    /**
     * 静态工厂方法 - 最常用：只有URL
     */
    public static HttpUtil get(String url) throws Exception {
        return new HttpUtil(url, null, null, null);
    }

    /**
     * 静态工厂方法 - URL + 参数
     */
    public static HttpUtil get(String url, String[] appendParams) throws Exception {
        return new HttpUtil(url, appendParams, null, null);
    }

    /**
     * 静态工厂方法 - URL + Cookie
     */
    public static HttpUtil get(String url, String jsonCookie) throws Exception {
        return new HttpUtil(url, null, jsonCookie, null);
    }

    /**
     * 完整参数的静态工厂方法
     * @param url 请求的目标 URL
     * @param appendParams 需要拼接到URL后的查询参数数组（key,value 成对）
     * @param jsonCookie JSON 格式的 Cookie 字符串
     * @param headers 自定义请求头数组（key,value 成对）
     * @return 返回一个配置好的{@link HttpUtil}实例
     */
    public static HttpUtil get(String url, String[] appendParams, String jsonCookie, String[] headers) throws Exception {
        return new HttpUtil(url, appendParams, jsonCookie, headers);
    }

    /**
     * 构建HTTP请求
     */
    private static HttpRequest buildRequest(String url, String[] appendParams, 
                                           String jsonCookie, String[] headers) throws Exception {
        String fullUrl = buildUrlWithParams(url, appendParams);
        
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .timeout(Duration.ofSeconds(30))
                .GET();

        builder.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
        builder.header("Accept-Encoding", "gzip, deflate");

        if (jsonCookie != null && !jsonCookie.trim().isEmpty()) {
            addCookies(builder, jsonCookie);
        }

        if (headers != null && headers.length > 0) {
            addHeaders(builder, headers);
        }

        return builder.build();
    }

    /**
     * 处理响应体（支持GZIP解压）
     */
    private byte[] processResponseBody(HttpResponse<byte[]> response) throws IOException {
        byte[] body = response.body();
        
        Optional<String> contentEncoding = response.headers().firstValue("Content-Encoding");
        if (contentEncoding.isPresent() && contentEncoding.get().contains("gzip")) {
            body = decompressGzip(body);
        }
        return body;
    }

    // ==================== 成员方法 ====================

    /**
     * 将响应内容保存到指定文件
     * @param filePath 文件保存路径
     */
    public void saveResponseToFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        Files.write(path, rawResponseBytes);
    }

    /**
     * 将响应内容保存到指定文件（File重载）
     */
    public void saveResponseToFile(File file) throws IOException {
        saveResponseToFile(file.getAbsolutePath());
    }

    /**
     * 将HAR数据保存到指定文件
     * @param filePath HAR文件保存路径
     */
    public void saveHarToFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), harData);
    }

    /**
     * 将HAR数据保存到指定文件（File重载）
     */
    public void saveHarToFile(File file) throws IOException {
        saveHarToFile(file.getAbsolutePath());
    }

    /**
     * 获取响应字符串
     */
    public String getResponseBody() {
        return responseBody;
    }

    /**
     * 获取原始响应字节数组（返回副本）
     */
    public byte[] getRawResponseBytes() {
        return rawResponseBytes.clone();
    }

    /**
     * 获取HAR数据
     */
    public ObjectNode getHarData() {
        return harData.deepCopy();
    }

    /**
     * 获取HTTP状态码
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * 获取响应头（不可修改视图）
     */
    public Map<String, List<String>> getResponseHeaders() {
        return Collections.unmodifiableMap(responseHeaders);
    }

    // ==================== 静态工具方法 ====================

    /**
     * 合并多个HAR文件为一个
     * @param outputPath 输出HAR文件路径
     * @param harFiles 输入的HAR文件路径列表
     */
    public static void mergeHarFiles(String outputPath, String... harFiles) throws IOException {
        if (harFiles == null || harFiles.length == 0) {
            throw new IllegalArgumentException("至少需要一个HAR文件");
        }

        ObjectNode merged = objectMapper.createObjectNode();
        merged.put("version", "1.2");

        ObjectNode creator = merged.putObject("creator");
        creator.put("name", "HttpUtil Merger");
        creator.put("version", "1.0");

        ObjectNode log = merged.putObject("log");
        ArrayNode entries = log.putArray("entries");
        ArrayNode pages = log.putArray("pages");

        int pageIndex = 1;
        for (String harPath : harFiles) {
            ObjectNode har = (ObjectNode) objectMapper.readTree(Paths.get(harPath).toFile());
            
            if (har.has("log")) {
                ObjectNode harLog = (ObjectNode) har.get("log");
                
                if (harLog.has("entries")) {
                    ArrayNode harEntries = (ArrayNode) harLog.get("entries");
                    for (JsonNode entry : harEntries) {
                        entries.add(entry.deepCopy());
                    }
                }
                
                if (harLog.has("pages")) {
                    ArrayNode harPages = (ArrayNode) harLog.get("pages");
                    for (JsonNode page : harPages) {
                        ObjectNode pageCopy = page.deepCopy();
                        pageCopy.put("id", "page_" + pageIndex++);
                        pages.add(pageCopy);
                    }
                }
            }
        }

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputPath), merged);
    }

    // ==================== 私有方法 ====================

    private static String buildUrlWithParams(String url, String[] appendParams) {
        if (appendParams == null || appendParams.length == 0) {
            return url;
        }
        
        if (appendParams.length % 2 != 0) {
            throw new IllegalArgumentException("参数数组长度必须为偶数");
        }
        
        StringJoiner joiner = new StringJoiner("&");
        for (int i = 0; i < appendParams.length; i += 2) {
            String key = appendParams[i];
            String value = appendParams[i + 1];
            if (key == null || key.isEmpty()) {
                continue;
            }
            
            String encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8);
            String encodedValue = value != null ? 
                URLEncoder.encode(value, StandardCharsets.UTF_8) : "";
            joiner.add(encodedKey + "=" + encodedValue);
        }
        
        String queryString = joiner.toString();
        if (queryString.isEmpty()) {
            return url;
        }
        
        String separator = url.contains("?") ? "&" : "?";
        return url + separator + queryString;
    }

    private static void addCookies(HttpRequest.Builder builder, String jsonCookie) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonCookie);
            StringJoiner cookieJoiner = new StringJoiner("; ");
            
            if (rootNode.isArray()) {
                for (JsonNode cookieNode : rootNode) {
                    JsonNode nameNode = cookieNode.get("name");
                    JsonNode valueNode = cookieNode.get("value");
                    if (nameNode != null && valueNode != null) {
                        String name = nameNode.asText();
                        String value = valueNode.asText();
                        if (!name.isEmpty() && !value.isEmpty()) {
                            cookieJoiner.add(name + "=" + value);
                        }
                    }
                }
            } else if (rootNode.isObject()) {
                rootNode.fields().forEachRemaining(entry -> {
                    String value = entry.getValue().asText();
                    if (value != null && !value.isEmpty()) {
                        cookieJoiner.add(entry.getKey() + "=" + value);
                    }
                });
            } else {
                builder.header("Cookie", jsonCookie.trim());
                return;
            }
            
            if (cookieJoiner.length() > 0) {
                builder.header("Cookie", cookieJoiner.toString());
            }
        } catch (Exception e) {
            builder.header("Cookie", jsonCookie.trim());
        }
    }

    private static void addHeaders(HttpRequest.Builder builder, String[] headers) {
        if (headers == null || headers.length == 0) {
            return;
        }
        
        if (headers.length % 2 != 0) {
            throw new IllegalArgumentException("请求头数组长度必须为偶数");
        }
        
        for (int i = 0; i < headers.length; i += 2) {
            String key = headers[i];
            String value = headers[i + 1];
            if (key != null && !key.isEmpty() && value != null && !value.isEmpty()) {
                builder.header(key, value);
            }
        }
    }

    private static byte[] decompressGzip(byte[] compressed) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
             GZIPInputStream gis = new GZIPInputStream(bis);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        }
    }

    private static String getCharsetFromContentType(String contentType) {
        if (contentType == null || contentType.isEmpty()) {
            return StandardCharsets.UTF_8.name();
        }
        String[] parts = contentType.split(";");
        for (String part : parts) {
            String trimmed = part.trim().toLowerCase();
            if (trimmed.startsWith("charset=")) {
                return trimmed.substring("charset=".length()).trim();
            }
        }
        return StandardCharsets.UTF_8.name();
    }

    private static ObjectNode buildHarData(String url, HttpRequest request, HttpResponse<byte[]> response,
                                           byte[] responseBodyBytes, String[] appendParams, 
                                           String jsonCookie, String[] headers) {
        ObjectNode har = objectMapper.createObjectNode();
        har.put("version", "1.2");

        ObjectNode creator = har.putObject("creator");
        creator.put("name", "HttpUtil");
        creator.put("version", "1.0");

        ObjectNode log = har.putObject("log");

        ObjectNode browser = log.putObject("browser");
        browser.put("name", "HttpUtil");
        browser.put("version", "1.0");

        ArrayNode pages = log.putArray("pages");
        ObjectNode page = pages.addObject();
        page.put("startedDateTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "+08:00");
        page.put("id", "page_1");
        page.put("title", url);
        page.put("pageTimings", objectMapper.createObjectNode());

        ArrayNode entries = log.putArray("entries");
        ObjectNode entry = entries.addObject();
        entry.put("startedDateTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "+08:00");
        entry.put("time", 0); // 可后续优化为真实耗时

        // Request
        ObjectNode requestNode = entry.putObject("request");
        requestNode.put("method", request.method());
        requestNode.put("url", url);
        requestNode.put("httpVersion", "HTTP/1.1");

        ArrayNode requestHeaders = requestNode.putArray("headers");
        request.headers().map().forEach((key, values) -> {
            values.forEach(value -> {
                ObjectNode header = requestHeaders.addObject();
                header.put("name", key);
                header.put("value", value);
            });
        });

        // Query String
        ArrayNode queryString = requestNode.putArray("queryString");
        if (appendParams != null) {
            for (int i = 0; i < appendParams.length; i += 2) {
                if (i + 1 < appendParams.length) {
                    ObjectNode param = queryString.addObject();
                    param.put("name", appendParams[i]);
                    param.put("value", appendParams[i + 1]);
                }
            }
        }

        requestNode.putArray("cookies"); // 简化处理，可后续增强
        requestNode.put("headersSize", -1);
        requestNode.put("bodySize", 0);

        // Response
        ObjectNode responseNode = entry.putObject("response");
        responseNode.put("status", response.statusCode());
        responseNode.put("statusText", getStatusText(response.statusCode()));
        responseNode.put("httpVersion", "HTTP/1.1");

        ArrayNode responseHeadersNode = responseNode.putArray("headers");
        response.headers().map().forEach((key, values) -> {
            values.forEach(value -> {
                ObjectNode header = responseHeadersNode.addObject();
                header.put("name", key);
                header.put("value", value);
            });
        });

        ObjectNode content = responseNode.putObject("content");
        content.put("size", responseBodyBytes.length);
        
        String contentType = response.headers().firstValue("Content-Type").orElse("");
        content.put("mimeType", contentType.split(";")[0]);

        String charset = getCharsetFromContentType(contentType);
        boolean isText = isTextContent(contentType);

        if (isText) {
            try {
                String textContent = new String(responseBodyBytes, charset);
                content.put("text", textContent);
            } catch (Exception e) {
                String base64Content = Base64.getEncoder().encodeToString(responseBodyBytes);
                content.put("text", base64Content);
                content.put("encoding", "base64");
            }
        } else {
            String base64Content = Base64.getEncoder().encodeToString(responseBodyBytes);
            content.put("text", base64Content);
            content.put("encoding", "base64");
        }

        responseNode.put("redirectURL", "");
        responseNode.put("headersSize", -1);
        responseNode.put("bodySize", responseBodyBytes.length);

        entry.set("cache", objectMapper.createObjectNode());

        ObjectNode timings = entry.putObject("timings");
        timings.put("blocked", 0);
        timings.put("dns", -1);
        timings.put("connect", -1);
        timings.put("send", 0);
        timings.put("wait", 0);
        timings.put("receive", 0);
        timings.put("ssl", -1);

        entry.put("serverIPAddress", "");
        entry.put("connection", "");

        return har;
    }

    private static boolean isTextContent(String contentType) {
        if (contentType == null) return false;
        String mime = contentType.split(";")[0].toLowerCase();
        return mime.startsWith("text/") || 
               mime.contains("json") || 
               mime.contains("xml") || 
               mime.contains("javascript") || 
               mime.contains("css") || 
               mime.contains("html");
    }

    private static String getStatusText(int statusCode) {
        switch (statusCode) {
            case 200: return "OK";
            case 201: return "Created";
            case 204: return "No Content";
            case 301: return "Moved Permanently";
            case 302: return "Found";
            case 304: return "Not Modified";
            case 400: return "Bad Request";
            case 401: return "Unauthorized";
            case 403: return "Forbidden";
            case 404: return "Not Found";
            case 500: return "Internal Server Error";
            default: return "";
        }
    }
}