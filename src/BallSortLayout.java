import java.util.*;

public class BallSortLayout implements Ilayout, Cloneable {
    private Stack<Character>[] tubes;
    private final int height;

    @SuppressWarnings("unchecked")
    public BallSortLayout(String str) {
        String[] rows = str.trim().split("\n");
        height = rows.length;

        String[] firstRow = rows[0].trim().split(" ");

        tubes = new Stack[firstRow.length];

        for (int i = 0; i < firstRow.length; i++) {
            tubes[i] = new Stack<Character>();
        }

        for (int i = rows.length - 1; i >= 0; i--) {
            String[] cols = rows[i].trim().split(" ");

            for (int j = 0; j < firstRow.length; j++) {

                char ball = cols[j].charAt(0);

                if (ball != 'E') {
                    tubes[j].push(ball);
                }
            }
        }
    }


    @Override
    public List<Ilayout> children() {
        List<Ilayout> list = new ArrayList<>();

        for (int i = 0; i < tubes.length; i++) {
            if (tubes[i].isEmpty()) continue;
            char top = (char) tubes[i].peek();
            for (int j = 0; j < tubes.length; j++) {
                if (i == j || (!tubes[j].isEmpty() && (char) tubes[j].peek() != top) || tubes[j].size() == height)
                    continue;
                BallSortLayout child = this.clone();
                child.tubes[i].pop();
                child.tubes[j].push(top);
                list.add(child);

            }
        }


        return list;
    }

    public int hashCode() {
        return Arrays.deepHashCode(tubes);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BallSortLayout)) return false;
        if (this.tubes.length != ((BallSortLayout) o).tubes.length) return false;
        return Arrays.equals(this.tubes, ((BallSortLayout) o).tubes);
    }

    @Override
    public boolean isGoal(Ilayout l) {
        for (int i = 0; i < tubes.length; i++) {
            if (tubes[i].isEmpty()) continue;

            if (tubes[i].size() != height) return false;
            char top = tubes[i].peek();
            for (char c : tubes[i]) {
                if (c != top) return false;
            }
        }
        return true;
    }

    @Override
    public double getK() {
        return 1;
    }

    @Override
    public double getHeuristic(Ilayout l) {
        HashMap<Character,Integer> hash = new HashMap<>();

        int totalelements=0;

        for(Stack<Character> s: tubes){
            if(s.isEmpty()) continue;

            totalelements += s.size();
            char base =  s.get(0);
            hash.putIfAbsent(base,0);
            int baseSize=1;

            for(int i = 1 ; i < s.size(); i++){
                if(s.get(i) != base)break;
                baseSize++;
            }
            hash.merge(base,baseSize,Math::max);
            }
        for(int i: hash.values()){
            totalelements-=i;
        }

        return totalelements;
    }

    @Override
    @SuppressWarnings("unchecked")
    public BallSortLayout clone() {
        try {
            BallSortLayout clone = (BallSortLayout) super.clone();
            clone.tubes = new Stack[this.tubes.length];
            for (int i = 0; i < this.tubes.length; i++) {
                clone.tubes[i] = (Stack<Character>) this.tubes[i].clone();
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
