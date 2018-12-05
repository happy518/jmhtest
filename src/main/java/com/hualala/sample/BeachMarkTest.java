package com.hualala.sample;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @ClassName: BeachMarkTest
 * @Description: JMH(https://blog.csdn.net/lxbjkben/article/details/79410740)
 * @author yangpengbing
 * @date 2018/12/5-14:30
 * @version V1.0.0
 *
 */
@Slf4j
public class BeachMarkTest {

	public static void main(String[] args) throws RunnerException {
//		firstBeachMark();
		jmhDate();
	}

	private static void jmhDate() throws RunnerException {
		Options opt = new OptionsBuilder().include(JMHDate.class.getSimpleName()).forks(1).build();
		new Runner(opt).run();
	}

	private static void firstBeachMark() throws RunnerException {
		Options opt = new OptionsBuilder().include(FirstBeachMark.class.getSimpleName()).forks(1).warmupIterations(5)
				.measurementIterations(5).build();

		new Runner(opt).run();
	}
}
