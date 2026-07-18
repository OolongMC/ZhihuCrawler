# 2026.6.14

发布0.1版本后开始记日志。

把`ZhihuQuestion`类移动到了`com.github.oolongmc.aicodes.deepseek`中。

把`app`子项目放入了根项目中。

修复了`Print.basePrint(...)`两个重载打印之后没有刷新的问题。

# 2026.6.28

初中毕业啦！

成功添加了依赖`curl-impersonate`模拟真实浏览器的调用方法。

改JSON格式的cookie为Netscape。

模块化`com.github.oolongmc.application.ZhihuCrawler.Crawler`

由于改用Netscape格式的cookie，所以移除了cookie加载。

发布0.2版本。

# 2026.6.29

移动`com.github.oolongmc.application.ZhihuCrawler.Crawler.GetMethod`至`com.github.oolongmc.application.ZhihuCrawler.Config.GetMethod`，为下次更新作准备。

考虑改变版本号规则。

添加`com.github.oolongmc.application.ZhihuCrawler.Config`类，打算添加配置JSON文件来实现断点续传等。

# 2026.6.30

一点点东西，感觉又要重新写软件的使用逻辑了，斯，我也不知道该怎么描述这次写的内容。虽然现在还编译不了，但我还是先git commit -m吧。

# 2026.7.1

重写了`com.github.oolongmc.application.Main`的逻辑。

# 2026.7.2

维护了`com.github.oolongmc.application.ZhihuCrawler.catchByJavaBaseUrl(Config config, Terminal terminal)`。

# 2026.7.3

0.3版本更新内容：

1. 祝贺`com.github.oolongmc.application.ZhihuCrawler.catchByJavaBaseUrl(Config config, Terminal terminal)`方法的回归！
2. 更新内容: 
   - 添加了配置文件，以及断点续传功能。
   - 调整了命令行参数。

# 2026.7.18

放弃使用JLine制作TUI的阅读器。

0.4发布(尽管是小更新)。
