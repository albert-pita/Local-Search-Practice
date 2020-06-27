package localsearch;

import aima.search.framework.HeuristicFunction;

public class LocalSearchFifthEvaluationFunction extends LocalSearchEvaluationFunction implements HeuristicFunction {
    
    @Override
    public double getHeuristicValue(Object state) {
        
        return ((LocalSearchState) state).server_max_time();
    }
}
