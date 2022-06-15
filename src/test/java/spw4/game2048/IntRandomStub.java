package spw4.game2048;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class IntRandomStub extends Random {

    private Iterator<Integer> iterator;

    public IntRandomStub(Iterable<Integer> values) {
        iterator = values.iterator();
    }

    public IntRandomStub(List<Cell> cells) {
        List<Integer> ints = new ArrayList<>();
        for(Cell c: cells){
            ints.add(c.y);
            ints.add(c.x);
            ints.add(c.randomValue);
        }
        iterator = ints.listIterator();
    }

    @Override
    public int nextInt() {
        return iterator.next();
    }

    @Override
    public int nextInt(int ignored) {
        return iterator.next();
    }
}
