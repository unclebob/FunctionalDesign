package sieve;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class SieveTest {
  @Test
  public void sieveTest() throws Exception {
    assertEquals(List.of(), Sieve.primesUpTo(0));
    assertEquals(List.of(), Sieve.primesUpTo(1));
    assertEquals(List.of(2), Sieve.primesUpTo(2));
    assertEquals(List.of(2, 3), Sieve.primesUpTo(3));
    assertEquals(List.of(2, 3), Sieve.primesUpTo(4));
    assertEquals(List.of(2, 3, 5), Sieve.primesUpTo(5));
    assertEquals(List.of(2, 3, 5), Sieve.primesUpTo(6));
    assertEquals(List.of(2, 3, 5, 7), Sieve.primesUpTo(7));
    assertEquals(List.of(2, 3, 5, 7), Sieve.primesUpTo(8));
    assertEquals(List.of(2, 3, 5, 7), Sieve.primesUpTo(9));
    assertEquals(List.of(2, 3, 5, 7), Sieve.primesUpTo(10));
    assertEquals(List.of(2, 3, 5, 7, 11), Sieve.primesUpTo(11));
    assertEquals(List.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29), Sieve.primesUpTo(30));







  }
}
