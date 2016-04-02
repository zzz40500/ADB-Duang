# ADB-Duang
Plugin for one key pull file ( database,preference ) from device and  push file( database,preference ) to device  
#介绍
这是一个一键帮你从设备中的数据库和SharePreference 的文件获取到本地,查看,修改,然后再push 到设备中的intellij & as 插件.
初衷是.
开发中查看数据库的方式:
1. 使用facebook 开源的框架stetho.
* 使用[SQLScout](http://www.idescout.com/)
* 使用手机re管理器查看修改.
* 使用adb命令读取数据库.

第一种方式要在集成在代码中,在web端查看.
第二种付费.
第三种要切换视角(从电脑到手机).
第四种方式比较麻烦,
我是对这几个方式并不感冒.

#原理:
这个类似 adb pull 和 adb push 的可视化界面.内部使用adb 命令去pull 和 push 数据库.

#不足:
因为这个只是一个简单的pull 和 push 操作,所以数据库的查看需要开发人员自行安装专业的数据库软件.而 SharePreference 可以直接用intellij 或者 as 直接打开.后期也不会对数据库查看做更多的支持.
毕竟我的支持相对于第三方数据库软件来说弱爆了.


#使用:
首先你的设备需要root.这个对开发人员来说不难吧.

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


![Paste_Image.png](http://upload-images.jianshu.io/upload_images/166866-9ea86717c24bc99c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)




#Feature:
1. 本地日志记录.
* 更多的快捷方式.
* 更正确的权限判断.



#特别鸣谢:
[adb-idea](https://github.com/pbreault/adb-idea)  
[intellij-idea-plugins](https://github.com/luonanqin/intellij-idea-plugins)
