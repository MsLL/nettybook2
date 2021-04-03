# nettybook2

该项目是李林峰老师编写的netty权威指南（第二版）对应的源码。
源码原始地址是：http://vdisk.weibo.com/s/C9LV9iVqAFvqu

因为原始项目是用ant构建，而且还要下载导入。所以本人将其项目进行简单的maven转换，并且提交到github上。这样同志们就可以直接import -> git查看。（当然一些有关更ant的操作，本人没有修改，大神勿喷）
下一步我想将书本中对源码的注解搬到项目中来，这样可以在调试代码的过程中，直接通过注解解决调试中的疑惑。
当然这些工作得到的李林峰老师的同意和认可，不信，你看：http://weibo.com/1725503810/Ci8nIa5IQ?type=comment

有关该书的更多信息可以关注李林峰老师的微博 @Nettying 以及查看其在ifeve网站上的文章：http://ifeve.com/author/linfeng/

# 项目结构及运行说明及代码分析

## chapter10

### file demo project

package path：com.phei.netty.protocol.chapter10.http.file

#### 测试运行

没有包装成http的东西，FileServer启起来之后，用nc工具连` nc -v localhost 8080`。

测试输入目标文件路径(相对于程序pwd目录，在idea下面跑，就是该项目根目录)：

连上去之后，测试输入一个不存在的文件`xxx`，响应`File not found: xxx`；测试输入一个存在的文件`pom.xml`，响应是整个文件的内容。