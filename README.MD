# **ZhihuCrawler**
******

这是一个使用Java编写的知乎爬虫。

这是我写的第一个实用性的项目，作者是一个连Java都没学完的初三生，所以只能写成这样了。

有部分AI写的代码，都标注出来了。

# 使用方法：

1. 在[Releases](https://github.com/OolongMC/ZhihuCrawler/releases/new)页面下载最新的Jar文件。
2. 打开浏览器，使用插件(如手机Edge也可以使用的[Cookie-Editor](https://github.com/Moustachauve/cookie-editor)插件)导出Netscape格式的cookie文件，保存为`cookie.txt`并与Jar文件放在同一个目录中。
3. 复制需要捕获的知乎问题的网址，如: `https://www.zhihu.com/question/545705420/answer/2034985112941675122`，再复制`question`后面的数字(545705420)，替换`https://www.zhihu.com/api/v4/questions/XXX/feeds`中的`XXX`，如这个示例替换后为`https://www.zhihu.com/api/v4/questions/545705420/feeds`。
4. 使用命令: 
```bash
java -jar 下载的Jar文件.jar -c 刚才处理好的网址
```
5. 输出示例：
```bash
root@localhost:~/Build/ZhihuCrawler# java -jar ZhihuCrawler.jar -c https://www.zhihu.com/api/v4/questions/545705420/feeds
开始爬取问题545705420。
方式: 使用curl-impersonate-chrome项目。
系统信息:Linux_aarch64
get1: curl没有提供反回结果，尝试重新访问。
get2: Done.
get3: Done.
get4: Done.
get5: Done.
get6: curl没有提供反回结果，尝试重新访问。
get7: Done.
get8: Done.
get9: Done.
get10: Done.
get11: Done.
get12: Done.
get13: Done.
get14: Done.
root@localhost:~/Build/ZhihuCrawler#
```
6.捕获的JSON文件会保存至`Collection/`文件夹中

# 注意:
源代码被打包进了`Jar:/src/`中。

Windows暂时不支持，需要手动下载[curl-impersonate](https://github.com/lwthiker/curl-impersonate)的源代码自行编译，然后自定义curl-impersonate路径。

# 后续更新打算:
1. 添加阅读捕获文件的功能。
2. 断点续传(现在其实可以，只要输入的URL是上次存档中的next中的URL)。

使用到的开源项目：

[JLine](https://jline.org/) : 输出美化。

[Jackson](https://github.com/FasterXML/jackson) : Json解析。

[curl-impersonate](https://github.com/lwthiker/curl-impersonate) : 模拟真实浏览器请求。

𝓒𝓱𝓲𝓷𝓪_𝓞𝓸𝓵𝓸𝓷𝓰.
