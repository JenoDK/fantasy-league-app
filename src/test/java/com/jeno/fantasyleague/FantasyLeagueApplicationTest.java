package com.jeno.fantasyleague;

import java.util.Map;

import com.google.common.collect.Maps;
import org.junit.Test;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class FantasyLeagueApplicationTest {

	@Test
	public void test() {
		System.err.println("Hello World");
		int sum = 2 + 2;
		System.err.println("Sum is " + sum);
	}

	@Test
	public void map() {
		Map<String, Object> map = Maps.newHashMap();
		map.put("key", "value");
		map.put("id", 4);
		System.err.println(map);
	}

	public static boolean isPrime(int x) {
		for (int i = 2; i <= Math.sqrt(x); i++) {
			if (x % i == 0) {
				return false;
			}
		}
		return true;
	}

	@Test
	public void primeTest() {
		int currentNumber = 2;
		int primesFound = 0;

		while (primesFound < 10000000) {
			if (isPrime(currentNumber)) {
				primesFound++;

				System.out.print (currentNumber + " ");
				if (primesFound % 10 == 0) {
					System.out.println();
				}
			}

			currentNumber++;
		}
	}

}
