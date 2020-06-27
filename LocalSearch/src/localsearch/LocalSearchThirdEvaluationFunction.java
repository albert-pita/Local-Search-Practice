package localsearch;

import aima.search.framework.HeuristicFunction;

public class LocalSearchThirdEvaluationFunction extends LocalSearchEvaluationFunction implements HeuristicFunction {
    
    @Override
    public double getHeuristicValue(Object state) {
        
        return ((LocalSearchState) state).variance();
    }
    
}
