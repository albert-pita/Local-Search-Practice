package localsearch;

import aima.search.framework.HeuristicFunction;

public class LocalSearchFourthEvaluationFunction extends LocalSearchEvaluationFunction implements HeuristicFunction {
    
    @Override
    public double getHeuristicValue(Object state) {
        
        return ((LocalSearchState) state).entropy();
    }
}
