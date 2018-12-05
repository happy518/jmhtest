package com.hualala.sample;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@Slf4j
@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class FirstBeachMark {

	@Benchmark
	public String strConcat() {
		String a = "a";
		String b = "b";
		String c = "c";

		String s = a + b + c;
		log.debug(s);

		return s;
	}
}
