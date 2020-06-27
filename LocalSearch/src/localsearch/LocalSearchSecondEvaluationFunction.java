package localsearch;

import aima.search.framework.HeuristicFunction;

public class LocalSearchSecondEvaluationFunction extends LocalSearchEvaluationFunction implements HeuristicFunction {
    
    @Override
    public double getHeuristicValue(Object state) {
        
        return ((LocalSearchState) state).average_time();
    }
}
