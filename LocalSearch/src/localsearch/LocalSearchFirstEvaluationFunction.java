package localsearch;

import aima.search.framework.HeuristicFunction;

public class LocalSearchFirstEvaluationFunction extends LocalSearchEvaluationFunction implements HeuristicFunction {
    
    @Override
    public double getHeuristicValue(Object state) {
        
        return ((LocalSearchState) state).times_sum();
    }
}
