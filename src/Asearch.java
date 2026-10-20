import java.util.*;

public class Asearch {
    protected Queue<State> abertos;
    private Map<State, Double> melhorCusto;
    private State actual;
    private Ilayout objective;

    static class State {
        private Ilayout layout;
        private State father;
        private Ilayout goal;
        private double g;
        private double f;

        public State(Ilayout l, State n, Ilayout goal) {
            layout = l;
            father = n;
            this.goal = goal;
            if (father != null)
                g = father.g + l.getK();
            else g = 0.0;
            f = g + l.getHeuristic(goal);
        }


        public String toString() {
            return layout.toString();
        }

        public double getG() {
            return g;
        }

        public double getF() {
            return f;
        }

        public int hashCode() {
            return toString().hashCode();
        }

        public boolean equals(Object o) {
            if (o == null) return false;
            if (this.getClass() != o.getClass()) return false;
            State n = (State) o;
            return this.layout.equals(n.layout);
        }
    }

    final private List<State> sucessores(State n) {
        List<State> sucs = new ArrayList<>();
        List<Ilayout> children = n.layout.children();

        for (Ilayout e : children) {
            if (n.father == null || !e.equals(n.father.layout)) {
                State nn = new State(e, n,objective);
                sucs.add(nn);
            }
        }
        return sucs;
    }

    final public Iterator<State> solve(Ilayout s, Ilayout goal) {
        objective = goal;
        abertos = new PriorityQueue<>(10,
                (s1, s2) -> (int) Math.signum(s1.getF() - s2.getF()));
        melhorCusto = new HashMap<>();
        abertos.add(new State(s, null,goal));
        List<State> sucs;

        while (!abertos.isEmpty()) {

            actual = abertos.poll();

            if (actual.layout.isGoal(this.objective)) {
                List<State> path = new ArrayList<>();
                State temp = actual;
                while (temp != null) {
                    path.add(temp);
                    temp = temp.father;
                }
                Collections.reverse(path);
                return path.iterator();
            }

            if (actual.g > melhorCusto.getOrDefault(actual, Double.MAX_VALUE)) {
                continue;
            }

                melhorCusto.put(actual, actual.g);
                sucs = this.sucessores(actual);
                for (State child : sucs) {
                    if(child.g <  melhorCusto.getOrDefault(child, Double.MAX_VALUE)) {
                        melhorCusto.put(child, child.g);
                        abertos.add(child);
                    }
                }
        }
        return null;
    }
}

