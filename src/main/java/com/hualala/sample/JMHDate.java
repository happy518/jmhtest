package com.hualala.sample;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.joda.time.DateTime;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;

/**
 * @ClassName: JMHDate
 * @Description: 使用JMH测试date
 * @author yangpengbing
 * @date 2018/12/5-15:12
 * @version V1.0.0
 *
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JMHDate {

	static int millis = 24 * 3600 * 1000;

	@Benchmark
	@Threads(5)
	public void runCalendar() {
		Calendar cale = Calendar.getInstance();
	}

	@Benchmark
	@Threads(5)
	public void runDate(){
		Date date = new Date();
	}

	@Benchmark
	@Threads(5)
	public void runJoda() {
		DateTime time = new DateTime();
	}

	@Benchmark
	@Threads(5)
	public void runSystem(){
		long result = System.currentTimeMillis() / millis;
	}

	/**************************** 学习笔记(2018年12月5日) ******************************/
//	JMH 使用说明
//	JMH(java microbenchmark harness),是专门用于代码微基准测试的工具套件，何为microbenchmark呢？ 简单的说就是基于方法层面的基准测试，精度可以达到微秒级，当你定位到热点方法，希望进一步优化方法性能的时候，就可以使用JMH对优化的结果进行量化分析，
//	JMH最具有特色的地方就是，他是由oracle内部实现jit的那波人员开发的，对于jit和jvm所谓的"profile guided optimization"对基准测试准确性的影响可谓是心知肚明

//	JMH比较典型的应用场景有：
//	1. 想准确的知道某个方法需要执行的时间，已经执行时间和输入之间的相关性
//	2. 对比接口不同实现在给定条件下的吞吐量
//	3. 查看多少百分比的请求在多长时间内完成

//	# Run complete. Total time: 00:06:46
//
//	REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
//	why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
//	experiments, perform baseline and negative tests that provide experimental control, make sure
//	the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
//	Do not assume the numbers tell you what you want them to tell.
//
//	Benchmark            Mode  Cnt    Score    Error  Units
//	JMHDate.runCalendar  avgt    5  267.545 ± 16.742  ns/op
//	JMHDate.runDate      avgt    5   17.399 ±  0.350  ns/op
//	JMHDate.runJoda      avgt    5   39.964 ±  1.366  ns/op
//	JMHDate.runSystem    avgt    5   17.581 ±  0.461  ns/op

//	测试结果表明：
//	   runCalendar方法平均运行时间为267微秒 误差为±16.742
//	   runJoda方法的平均运行时间为39微秒 误差为±1.366
//	根据用例测试结果可以看到，Calender(非线程安全)在高并发情况下性能远不及dateTime(线程安全)，对代码性能和安全性要求比较高的可以放弃使用Calender和SimpleDateFormat(底层实现是Calender)，转而使用joda-time

//	详细说明
//
//  基本概念
//	    mode：表示JMH进行benchmark时所使用的模式，通常是测量的纬度不同，或者测量的方式不同，目前JMH共有四种模式：
//		    1. Thoughput：整体吞吐量，例如"1s内可以执行多少次调用"
//		    2. AverageTime：调用的平均时间，例如"每次调用平均耗时xxx毫秒"
//		    3. SampleTime: 随机取样。最后输出取样结果的分布，例如99%的调用在XXX毫秒内，99.99%的调用在XXX毫秒内
//		    4. SingleShotTime：以上模式都是默认一次 iteration是1s。唯有singleShotTime是只运行一次，往往同时把warmup次数设置为0，用于测试冷启动时的性能。

//	    iteration
//		    iteration是JMH进行测试的最小单位，在大部分模式下，一次iteration代表是1s，JMH会在这一秒内不断调用需要benchmark的方法，然后根据模式对其采样，计算吞吐量，计算平均时间等。

//		warmup
//			warmup是指在实际进行benchmark前先进行预热行为，为什么需要预热，因为jvm的jit机制的存在，如果某个函数被调用多次之后，jvm会尝试将其编译成机器码从而提高执行速度，为了让benchmark的结果更加接近真实情况就需要预热。

//	主键与选项
//		常用注解说明
//			1. @BenchmarkMode 对应mode选项，可用于类或者方法上，需要注意的是，这个注解的value是一个数组，可以把几种mode集合在一起执行，还可以设置为Mode.All，即全部执行一遍
//			2. @State 类注解，JMH测试类必须使用@State注解，State定义了一个类实例的生命周期，可以类比Spring bean的Scope。由于JMH允许多线程同时执行测试，不同的选项含义如下：
//				1）. Scope.Thread: ，默认的State,每个测试线程分配一个实例
//				2）. Scope.Benchmark: 所有测试线程共享一个实例，用于测试有状态实例在多线程共享下的性能
//				3）. Scope.Group：每一个线程组共享一个实例
//			3. @OutputTimeUnit benchmark结果所使用的时间单位，可用于类和方法注解，使用java.util.concurrent.TimeUnit的标准时间单位
//			4. @Benchmark 方法注解，表示该方法是需要进行benchmark的对象
//			5. @Setup  方法注解，会在执行benchmark之前执行，正如其名，主要用于初始化
//			6. @TearDown 方法注解，与@Setup相对的，会在所有benchmark执行结束以后执行，主要用于资源的回收等。
//			7. @Param 成员注解，可以用来指定某项参数的多种情况，特别适合用来测试一个函数在不同的参数输入的情况下的性能，@Param注解接受一个String数组，在@Setup方法执行前转换为对应的数据类型，多个@Param注解的成员之间是乘积关系，譬如有两个用@Param注解的字段，第一个有5个值。第二个有2个值，那么每个测试方法会跑5*2次

//		常用选项说明
//			include
//			benchmark 所在的类的名字，这里可以使用正则表达式对所有类进行匹配。
//
//			fork
//			JVM因为使用了profile-guided optimization而“臭名昭著”，这对于微基准测试来说十分不友好，因为不同测试方法的profile混杂在一起，“互相伤害”彼此的测试结果。对于每个@Benchmark方法使用一个独立的进程可以解决这个问题，这也是JMH的默认选项。注意不要设置为0，设置为n则会启动n个进程执行测试（似乎也没有太大意义）。fork选项也可以通过方法注解以及启动参数来设置。
//
//			warmupIterations
//			预热的迭代次数，默认1秒。
//
//			measurementIterations
//			实际测量的迭代次数，默认1秒。
//
//			CompilerControl
//			可以在@Benchmark注解中指定编译器行为。
//
//			CompilerControl.Mode.DONT_INLINE：This method should not be inlined. Useful to measure the method call cost and to evaluate if it worth to increase the inline threshold for the JVM.
//			CompilerControl.Mode.INLINE：Ask the compiler to inline this method. Usually should be used in conjunction with Mode.DONT_INLINE to check pros and cons of inlining.
//			CompilerControl.Mode.EXCLUDE：Do not compile this method – interpret it instead. Useful in holy wars as an argument how good is the JIT.
//
//			Group
//			方法注解，可以把多个 benchmark 定义为同一个 group，则它们会被同时执行，譬如用来模拟生产者－消费者读写速度不一致情况下的表现。可以参考如下例子：
//			CounterBenchmark.java
//
//					Level
//			用于控制 @Setup，@TearDown
//			的调用时机，默认是 Level.Trial。
//
//			Trial：每个benchmark方法前后；
//
//			Iteration：每个benchmark方法每次迭代前后；
//
//			Invocation：每个benchmark方法每次调用前后，谨慎使用，需留意javadoc注释；
//
//			Threads
//			每个fork进程使用多少条线程去执行你的测试方法，默认值是Runtime.getRuntime().availableProcessors()。
}
