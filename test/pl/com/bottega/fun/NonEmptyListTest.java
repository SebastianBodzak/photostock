package pl.com.bottega.fun;

import org.junit.Assert;
import org.junit.Test;
import pl.com.bottega.photostock.sales.model.Money;
import pl.com.bottega.photostock.sales.model.Product;
import pl.com.bottega.photostock.sales.model.products.Picture;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Function;
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

    @Test
    public void shouldMapElements() {
        //given
        FunList<String> l = FunList.create();
        l = l.add("1").add("2").add("3").add("4");

        //when
        FunList<Integer> mapped = l.map(s -> Integer.valueOf(s));
        //method reference
        FunList<Integer> mapped2 = l.map(Integer::valueOf);
        FunList<Integer> mappedAnotherWay = l.map(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return Integer.valueOf(s);
            }
        });

        //then
        Assert.assertTrue(mapped.contains(1));
        Assert.assertTrue(mapped.contains(2));
        Assert.assertTrue(mapped.contains(3));
        Assert.assertTrue(mappedAnotherWay.contains(4));

        FunList<Integer> expected = FunList.create();
        expected = expected.add(1).add(2).add(3).add(4);
        Assert.assertEquals(expected, mapped);
        Assert.assertEquals(expected, mappedAnotherWay);
    }

    @Test
    public void shouldReduceElements() {
        //when
        Picture p1 = new Picture("nr1", "title1", new Money(200, Money.CurrencyValues.PLN), true, Arrays.asList("tag1", "tag2", "tag3"));
        Picture p2 = new Picture("nr2", "title2", new Money(100, Money.CurrencyValues.PLN), false, Arrays.asList("tag1", "tag4"));
        Picture p3 = new Picture("nr3", "title3", new Money(50, Money.CurrencyValues.PLN), true, Arrays.asList("tag2"));
        FunList<Picture> l = FunList.create();
        l = l.add(p1).add(p2).add(p3);

        //when
        int availableCount = l.reduce(0, (accumulator, product) -> accumulator + (product.isAvailable() ? 1 : 0));
        int availableCount2 = l.reduce(0, (accumulator, product) -> {
            if (product.isAvailable())
                return accumulator + 1;
            else
                return accumulator;
        });
        Money total = l.reduce(new Money(0), (accumulator, product) -> accumulator.add(product.calculatePrice()));
        FunList<String> tags = l.reduce(FunList.create(), (accumlator, product) -> {
            for (String tag : product.getTags())
                if (!accumlator.contains(tag))
                    accumlator = accumlator.add(tag);
            return accumlator;
        });

        //then
        Assert.assertEquals(availableCount, 2);
        Assert.assertEquals(availableCount2, 2);
        Assert.assertEquals(new Money(350, Money.CurrencyValues.PLN), total);

        FunList<String> expectedTags = FunList.create();
        expectedTags = expectedTags.add("tag1").add("tag2").add("tag3").add("tag4");
        Assert.assertEquals(expectedTags, tags);
    }
}