# AspectJAOP
[原文地址](http://dim.red/2017/04/23/aspectj_aop/)
# 开头  
java上的AOP 框架比较出名的是AspectJ，比较可惜的是这个框架是在gradle上需要一些变换才能使用。  
在使用这个框架之前,我们来看AspectJ 命令的几个比较重要的参数：  
-classpath：class和source 的位置  
-aspectpath： 定义了切面规则的class  
-d：指定输出的目录  
-outjar：指定输出的jar上  
-inpath：需要处理的.class  
classpath 的作用是在当解析一个类的时候，当这个类是不在inpath 中，会从classpath 中寻找。  
在使用AspectJ的时候, 我们用以下几个方面来优化我们的速度。  
## 一 多线程:  
首先我们通过android plugin的Transform api ，收集所有的class文件。  
然后用AspectJ处理所有的class.输出到对应的目录下。这里我们为了保证transform灵活性，我们不将所有的class 合并成一个jar，而是每个输入对应一个输出。同时使用多线程来为这个过程加速。
在后续的使用我们发现单纯的直接用多线程来处理这个问题，会引发一个异常，异常如下：
![异常.png](http://upload-images.jianshu.io/upload_images/166866-df7277b830e682a9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
通过分析可以发现问题是出现在CompilerAdapter 类下，具体是因为CompilerAdapter 会被注入到相关类中。而注入的是同一个实例，在多线程下操作中，一并发就会导致一些bug， 我们重新编写CompilerAdapter 类。在对应的field上用ThreadLocal 包裹，使在参数实例在多线程下隔离，这样就简单的解决问题。

这里有个比较有意思的点是：AspectJ AOP 框架对自身的代码进行了AOP编程。 

## 二 指定精确的aspectpath  
为了加速aspectJ的处理过程， 我们会将aspectpath对应的class 找出来，然后在AspectJ处理的时候将参数指向给aspectpath。 我们通过用ASM 框架解析类，查找所有被Lorg/aspectj/lang/annotation/Aspect 注释的类，收集到一个单独的文件夹中。这样可以为AspectJ处理提升速度。
同时为了加快查找aspectpath 的文件。  
我们支持了对Android library 和Java Library 的aspectpath 文件进行标注。在主项目中查找aspectpath 文件的时候，可以根据这个标注更快的收集.
* Android Library 我们定义2个Transform ，一个针对Class，收集和分析class，一个针对资源，将上个Transform收集的结果写入Java Resource 中的NOTICE文件。
* Java Library 则是在jar task 后面分析生成的jar文件.同样把结果写入Java Resource 的 NOTICE文件。 


这里之所以使用NOTICE ，而不使用别的，一是为了兼容Android Plugin 版本的变迁。二是因为 Android Plugin 的Java Resource 的合并是会忽略掉几个特定的文件，而NOTICE就在这几个特定文件中。    
2.0 Android Plugin 忽略的文件：   
![Android Plugin 2.0 ](http://upload-images.jianshu.io/upload_images/166866-dd51e8f4a4147a6c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

2.3 Android Plugin 忽略的文件：   
![Android Plugin 2.3.png](http://upload-images.jianshu.io/upload_images/166866-c3dfb3269a1bc81d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  

使用NOTICE 好处就是，你用插件处理的jar 包或者aar 在应用到不相干的项目中，在生成的APK不会多余令人困户的配置文件存在。 

## 三 支持增量： 
我们几乎所有的Transform 都支持了增量编译，意思是AspectJ 并不会每次都处理所有的class， 他只会处理有变更的class 文件。 除非是你的aspectpath文件出现了变更，即AOP规则变更了, 之前的处理结果已经不可靠了，就会触发一次全量的处理。
为了支持这个特性，我们必须手动的为AspectJ 新增一个参数  `-infiles`。为了这个特性我们必须去修改aspectJ 的源码。修改以下3个类 `org.aspectj.ajdt.internal.core.builder.AjBuildManager`和`org.aspectj.ajdt.internal.core.builder.AjBuildConfig` 和`org.aspectj.ajdt.ajc.BuildArgParser`这里不展开详说， 这个特性在持续开发中对于节省时间特别有效。

因为在一些需求我们需要修改AspectJ的代码，因为AspectJ 的源码编译比较麻烦。我们这里使用修改后的java编译出的class 文件，然后直接覆盖AspectJ jar包中对应的文件上。 

# 结尾:
我们将所有的代码放置在github 上 [AspectJAOP](https://github.com/zzz40500/AspectJAOP)。
