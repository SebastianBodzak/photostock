package pl.com.bottega.fun;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.*;

/**
 * Created by Dell on 2016-06-04.
 */
public class NonEmptyListTest {

    @Test
    public void shouldAddElements() {
        //given
        FunList<String> l = FunList.create();

        //when
        l = l.add("first").add("second").add("third").add("fourth").add("fifth");

        //then
        Assert.assertTrue(l.contains("first"));
        Assert.assertTrue(l.contains("second"));
        Assert.assertTrue(l.contains("third"));
        Assert.assertTrue(l.contains("fourth"));
        Assert.assertTrue(l.contains("fifth"));
    }

    @Test
    public void shouldReturnStringRepresentation() {
        //given
        FunList<String> l = FunList.create();

        //when
        l = l.add("first").add("second").add("third").add("fifth");

        //then
        Assert.assertEquals("first, second, third, fifth", l.toString());
    }

    @Test
    public void shouldReturnSize() {
        //given
        FunList<String> l = FunList.create();

        //when
        l = l.add("first").add("second").add("third").add("fourth").add("fifth");

        //then
        Assert.assertEquals(5, l.size());
    }

    @Test
    public void shouldGetElement() {
        //given
        FunList<String> l = FunList.create();

        //when
        l = l.add("first").add("second").add("third").add("fourth").add("fifth");

        //then
        Assert.assertEquals("first", l.get(0));
        Assert.assertEquals("third", l.get(2));
        Assert.assertNull(l.get(-1));
    }

    @Test
    public void shouldFindElement() {
        //given
        FunList<Integer> l = FunList.create();
        //when
        l = l.add(100).add(50).add(300).add(400);
        //then
//        Predicate<Integer> p = new Predicate<Integer>() {
//            @Override
//            public boolean test(Integer integer) {
//                return false;
//            }
//        };

        int number = l.find(new Predicate<Integer>() {
            @Override
            public boolean test(Integer i) {
                return i > 200;
            }
        });

        Assert.assertEquals(300, number);
        Assert.assertNull(l.find(new Predicate<Integer>() {
            @Override
            public boolean test(Integer i) {
                return i == 500;
            }
        }));

//        Predicate<Integer> p = (i) -> i >200;
        int number2 = l.find((i) -> i == 400);

        Assert.assertEquals(400, number2);
        Assert.assertEquals(50, (int) l.find((i) -> i == 50));
        Assert.assertNull(l.find((x) -> {
            System.out.println("Calling predicate");
            return x > 1000;
        }));

        Predicate<Integer> p = (i) -> {
            int x = 20;
            int y = 30;
            String s = "  400";
            s = s.trim();
            return x + y == 70;
        };
    }
}