#介绍
这是一个一键帮你从设备中的数据库和SharePreference 的文件获取到本地,查看,修改,然后再push 到设备中的intellij & as 插件.




#原理:
这个类似 adb pull 和 adb push 的可视化界面.内部使用adb 命令去pull 和 push 文件

#不足:
因为这个只是一个简单的pull 和 push 操作,所以数据库的查看需要开发人员自行安装专业的数据库软件.而 SharePreference 可以直接用intellij 或者 as 直接打开.后期也不会对数据库查看做更多的支持.


#使用:
首先你的设备需要root  要root  root  t   

Find Actions 快捷键  
Mac OSX: Ctrl+Shift+A  
Windows/Linux: Ctrl+Alt+Shift+A  


Pull File
使用  "[Find Actions](http://www.jetbrains.com/idea/webhelp/navigating-to-action.html)"快捷键.
pull SharePreference 搜索 "pull preference from device "
pull 数据库 搜索 "pull database from device"



![Paste_Image.png](http://upload-images.jianshu.io/upload_images/166866-9220814a19eec7b2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

 



Push File
在你要push 的文件鼠标右键  


![Paste_Image.png](http://upload-images.jianshu.io/upload_images/166866-4a220cc7e26d1b41.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

快捷键 alt(option)+A  

![Paste_Image.png](http://upload-images.jianshu.io/upload_images/166866-8d9db07b9264c734.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

FAQ   
Q:genymotion 使用失败?   
A:通过修改genymotion 尝试设置adb 位置.  

![genymotion设置.png](http://upload-images.jianshu.io/upload_images/166866-e7702aad8a030864.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)








#Feature:
1. 本地日志记录.
* 优化选择文件体验.   




#特别鸣谢:
[adb-idea](https://github.com/pbreault/adb-idea)  
[intellij-idea-plugins](https://github.com/luonanqin/intellij-idea-plugins)
