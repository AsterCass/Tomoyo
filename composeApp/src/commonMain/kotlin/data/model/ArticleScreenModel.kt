package data.model

import api.BaseApi
import cafe.adriel.voyager.core.model.ScreenModel
import data.ArticleSimpleModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ArticleScreenModel : ScreenModel {

    private val _articleDataKey = MutableStateFlow("")
    val articleDataKey = _articleDataKey.asStateFlow()
    private val _articleIsLoadAll = MutableStateFlow(false)
    val articleIsLoadAll = _articleIsLoadAll.asStateFlow()

    private val _articleDataList = MutableStateFlow(emptyList<ArticleSimpleModel>())
    val articleDataList = _articleDataList.asStateFlow()
    suspend fun updateArticleList() {
        val newData = BaseApi().getArticleList(
            offset = _articleDataList.value.size,
            keyword = _articleDataKey.value
        )
        _articleIsLoadAll.value = newData.isEmpty()
        _articleDataList.value += newData
    }

    fun clearResetKeyword(keyword: String = "") {
        _articleDataKey.value = keyword
        _articleDataList.value = emptyList()
    }

    private val _readingArticleId = MutableStateFlow("")
    private val _readingArticleData = MutableStateFlow("")
    val readingArticleData = _readingArticleData.asStateFlow()
    suspend fun updateReadingArticleData(articleId: String, token: String) {
        if (_readingArticleId.value == articleId) {
            return
        }
        val content = BaseApi().getArticleDetail(articleId, token)
        if (content.isNotBlank()) {
            _readingArticleData.value = content
            _readingArticleId.value = articleId
        } else {
            _readingArticleId.value = articleId
            _readingArticleData.value = """
                ## 前言

                在前文[SpringReactive下的数据库交互](https://www.astercasc.com/article/detail?articleId=AT1667047868999159)中，我们简单介绍了在使用`Spring Reactive`时对于数据库的调用方式，以及对于`MySQL`数据库的第三方解决方案。本文内容我们详细了解一下`Reactive`。这里内容大致分为三节，上篇说明`Reactive`的基本原理以及对比`Servlet`，中篇在常用情景下展示`WebFlux`架构的具体相关代码以及测试方法，下篇介绍`Reactive`的一些进阶用法

                ## 基础

                ### 为什么需要反应式

                随着人们对于网络的依赖程度越来越多，互联网的活动也更加频繁，各种商家/企业的活动亦是层出不穷，从而导致我们很容易遇到一个接口/服务需要在短时间内处理大量请求的情景， 作为传统的`Servlet`架构，此时我们的一般处理方案是，来一个请求分配一个线程，请求结束，将线程分配给新的请求往复循环，这样的处理首先对于线程总量的要求比较高，其次线程间的频繁切换也是非常消耗`CPU`资源。如果需要提高用户体验，那么一般的做法是做负载均衡，将请求分散的到多个服务减少单个服务压力，同时应用熔断降级等方案保证整个系统的稳定性。如果希望更多保证请求成功率，可以选择使用令牌桶算法，在一定程度在延长请求波峰的总体请求时长，保证波峰时更多的请求可以被处理，相关内容可以参考本站的[使用Gateway作为SpringCloud网关](https://www.astercasc.com/article/detail?articleId=AT1641670665541263)，这里使用网关（也就是反应式架构）实现的，但是核心思路一样，使用`Servlet`架构一样可以达到类似的效果

                无论是哪种优化方案，对于硬件的要求都是存在的。如果在大量的请求下，没有良好硬件的支持，这种优化只能说是聊胜于无，可以让原来100分的硬件条件发挥200分的效果，但是对于400，500分的场景处理起来会相对非常艰难

                ### 为什么是反应式

                首先我们都了解，一台计算器，一个`CPU`的频率是一定的，就算是超频能超的也是有限的，内存也是一样，我们能做的也就虚拟内存这种利用空间/时间局部性原理来尽可能让内存利用的效率更高，但是这个效率是有物理极限的

                所以如果我们知道传统`Servlet`在哪里会造成资源的浪费，就可以针对性地大幅提高程序运行效率了。我们会发现，在基本运算方面基本没有获取大提升的可能性，但是在服务和外部交互（比如`Http`请求，数据库查询）时候，此时的线程是处于阻塞状态的。那么也就是如果你的某接口的请求含有大量这种外部交互，比如有三分之一的时间用于`Http`请求，三分之一用于数据库查询/入库，那么理论上你的接口请求容纳量是可以提升接近三倍的

                那么应该如何实现呢，我们想到了异步执行。但是这里的异步执行不同于我们正常手写的异步执行，当我们在手写异步执行时，我们仍然是阻塞的，因为如果我们的基本运算位于外部交互之后，或者是我们的基本运算时间远小于外部交互时间，我们仍然需要等待外部交互结束后才能继续计算或者返回请求。其次以`Http`请求为例，我们正常在手写异步执行时，不会添加观察者模式（即发布-订阅模式），也就是说此时我们的子线程仍然是阻塞等待的，对于整体硬件利用效率并没有提升

                所以我们的核心诉求就是：让我们在进行外部交互时使用发布订阅设计模式释放线程资源，同时该释放请求线程，等待外部交互执行完成，重新获取资源继续执行，在此之前线程可以提供给其他请求，从而保证可以处理大批量的请求

                同时我们还关心：流的组合性和可维护性，订阅的必需性，订阅者的反馈调节能力

                那么是反应式呢，

                > The term, “reactive,” refers to programming models that are built around reacting to change — network components reacting to I/O events, UI controllers reacting to mouse events, and others. In that sense, non-blocking is reactive, because, instead of being blocked, we are now in the mode of reacting to notifications as operations complete or data becomes available.
                >
                > There is also another important mechanism that we on the Spring team associate with “reactive” and that is non-blocking back pressure. In synchronous, imperative code, blocking calls serve as a natural form of back pressure that forces the caller to wait. In non-blocking code, it becomes important to control the rate of events so that a fast producer does not overwhelm its destination.

                这里的`non-blocking back pressure`就是我们说的订阅者的反馈调节，而反应性就是我们实现非阻塞的编程模型，结果会因为前提的变化而变化，我认为可以理解为：要才给，要什么给什么，要多少给多少。

                * 要才给：订阅的必需性
                * 要什么给什么：流的组合性和可维护性
                * 要多少给多少：订阅者的反馈调节能力

                ### 反应式的实现

                以`SpringWebFlux`为例，默认使用的是`Reactor-Netty`的底层库，熟悉`Netty`的小伙伴们就比较了解了，这个就是利用了`Netty`的事件循环机制来实现的反应式库，就和`Nodejs`的`Event Loop`一样

                <img src="https://astercasc-web-admin-1256368017.cos.ap-shanghai.myqcloud.com/admin-web-article-img/2024/%E5%8F%8D%E5%BA%94%E5%BC%8FWeb%E6%9E%B6%E6%9E%84SpringWebFlux%E8%AF%A6%E8%A7%A3_1.jpg" width=80%>

                在事件循环机制中，线程和请求没有绑定，代码中所有计算/请求/读写变成具有发布者和订阅者的处理流程，单个/少量线程可以在，获取流程发布者处理事件 -> 注册订阅者回调 ->（接受回调）-> （处理订阅者回调执行事件） -> 获取流程发布者处理事件，中不循环，从而保证高效利用硬件资源，不会因为线程阻塞而浪费，从内存上来看，就是事件循环的线程不断从`Java`堆栈中不断获取对象处理再放回的过程

                这也就是解释了为什么订阅的是必须的，当没有订阅的时候，注册订阅者回调都不能完成，流程到这里就停止了。同样，背压的实现就是通过根据需求抑制订阅者注册从而抑制计算机对于超出能力范围内数据的处理，从而避免服务崩溃，同时将机器资源分配到更加关键的流程当中。而流的组合性和可维护性则体现在对于发布者和订阅者的组合上，这种机制会比较方便地可以支持一个对象受多个结果的回调控制，以及支持一个结果回调影响多个对象

                ### 命令式和反应式的选择

                虽然反应式有种种优点，比如对于背压的实现，可以在少量机器资源的情况下应对高并发的场景，对于流媒体平台的支持更好等等，但是缺点也是显而易见的。目前大部分的外部库都是阻塞的，使用反应式框架在很多时候没办法方便地集成，对于开发、调试以及维护也相较于传统命令式编程学习成本更高。不仅如此，虽然我们说反应式编程对于硬件的使用率高，这通常意味有更高的业务效率，但这也并不是一定的，熟悉`CPU`工作流程的小伙伴会注意到，反应式的工作流程比命令式的工作流程`CPU`会更频繁地切换上下文，当线程数足够应对请求以及请求较少涉及外部交互时，甚至很有可能命令式编程的效率会更胜一筹

                所以当你的产品涉及大量高并发场景或者流媒体平台，或者在后期对于实时性数据有需求，那么在开发人员有足够时间适应反应式开发、调试以及维护的情况下，使用反应式搭建服务是一个非常值得考虑的选择。但是其他的情况下我觉得可能还是传统命令式更合适

                ## 实验

                这里只展示`WebFlux`的简单示例，更多详细示例和说明在将在下一篇展示

                ```java
                @RestController
                @RequestMapping("/demo")
                public class DemoController {

                    @GetMapping("/booksTime")
                    public Flux<Object> getTimeAll() {
                        WebClient webClient = WebClient.create("http://localhost:8002/tmp/books");
                        System.out.printf("%s %s%n",
                                Thread.currentThread().getId(), Thread.currentThread().getName());
                        return webClient.get().exchangeToFlux(
                                response -> {
                                    if (response.statusCode().equals(HttpStatus.OK)) {
                                        return response.bodyToFlux(Book.class);
                                    } else {
                                        return response.createError().flux();
                                    }
                                }
                        );
                    }

                    @Data
                    @AllArgsConstructor
                    @NoArgsConstructor
                    private static class Book {
                        public static Book Builder(String name, String author) {
                            return new Book(name, author, null);
                        }
                        private String name;
                        private String author;
                        private String date;
                    }

                }
                ```

                此时我们可以获得类似以下数据

                ```json
                [
                  {
                    "name": "母猪的产后护理",
                    "author": "张三",
                    "date": null
                  },
                  {
                    "name": "翻跟头从入门到精通",
                    "author": "汤姆",
                    "date": null
                  },
                  {
                    "name": "跟头猫的自我修养",
                    "author": "汤姆",
                    "date": null
                  },
                  {
                    "name": "论耗子药的营养价值",
                    "author": "杰瑞",
                    "date": null
                  }
                ]
                ```

                这里不足以看出反应式的对于资源利用更充分的特点，这里我们在`http://localhost:8002/tmp/books`的服务接口内打断点，模拟一个耗时的外部交互，并且不断调用本服务的`/demo/booksTime`接口，此时可以观察到控制台输出

                ```txt
                114 http-nio-8005-exec-3
                118 http-nio-8005-exec-7
                119 http-nio-8005-exec-8
                120 http-nio-8005-exec-9
                121 http-nio-8005-exec-10
                112 http-nio-8005-exec-1
                113 http-nio-8005-exec-2
                115 http-nio-8005-exec-4
                117 http-nio-8005-exec-6
                116 http-nio-8005-exec-5
                114 http-nio-8005-exec-3
                118 http-nio-8005-exec-7
                119 http-nio-8005-exec-8
                120 http-nio-8005-exec-9
                121 http-nio-8005-exec-10
                112 http-nio-8005-exec-1
                113 http-nio-8005-exec-2
                ```

                并且在超过设置/默认的超时时间后，会报出`Operator called default onErrorDropped`提醒该流程已经被丢弃，线程将放弃在事件循环中对于该状态的检查监听，但是如果我们使用的是传统方式实现的话，类似代码如下

                ```java
                    @GetMapping("/booksTime")
                    public Object getTimeAll() {
                        System.out.printf("%s %s%n",
                                Thread.currentThread().getId(), Thread.currentThread().getName());
                        HttpResponse resp = HttpRequest.get("http://localhost:8002/tmp/books").execute();
                        System.out.println(resp.body());
                        return resp.body();
                    }
                ```

                和上例一样处理，模拟耗时的外部交互，此时控制台输出为

                ```txt
                186 http-nio-8003-exec-6
                187 http-nio-8003-exec-7
                188 http-nio-8003-exec-8
                189 http-nio-8003-exec-9
                190 http-nio-8003-exec-10
                204 http-nio-8003-exec-11
                205 http-nio-8003-exec-12
                206 http-nio-8003-exec-13
                207 http-nio-8003-exec-14
                208 http-nio-8003-exec-15
                209 http-nio-8003-exec-16
                210 http-nio-8003-exec-17
                211 http-nio-8003-exec-18
                212 http-nio-8003-exec-19
                213 http-nio-8003-exec-20
                214 http-nio-8003-exec-21
                215 http-nio-8003-exec-22
                216 http-nio-8003-exec-23
                ```

                即当请求没有完成时，该请求的线程不会释放，会一直处于等待状态，新的请求只能重开线程，在高并发的场景下的表现相较反应式会逊色很多

                关于数据库的外部交互以及相关包的引入可以参考前文[SpringReactive下的数据库交互](https://www.astercasc.com/article/detail?articleId=AT1667047868999159)

                ## 参考资料

                [Servlet or Reactive Stacks: The Choice is Yours. Oh No... The Choice is Mine!](https://www.youtube.com/watch?v=Dp_aJh-akkU)

                [Spring WebFlux Overview](https://docs.spring.io/spring-framework/reference/web/webflux/new-framework.html)

                [Threading model of Spring WebFlux and Reactor](https://stackoverflow.com/questions/45019486/threading-model-of-spring-webflux-and-reactor)

                [Spring Data Relational R2DBC](https://docs.spring.io/spring-data/relational/reference/r2dbc/getting-started.html)

                [Reactor Reference](https://projectreactor.io/docs/core/release/reference)

                [Concurrency in Spring WebFlux](https://www.baeldung.com/spring-webflux-concurrency)

                [Backpressure Mechanism in Spring WebFlux](https://www.baeldung.com/spring-webflux-backpressure)

                [RxJava Wiki](https://github.com/ReactiveX/RxJava/wiki)

                [Spring WebFlux/Reactive • Frequently Asked Questions](https://www.kapresoft.com/java/2023/10/17/spring-webflux-reactive-faqs.html)
            """.trimIndent()
        }

    }


}