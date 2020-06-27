package localsearch;

import aima.search.framework.GoalTest;

public class LocalSearchGoalTest implements GoalTest{
    
    @Override
    public boolean isGoalState(Object state) {
        
        return ((LocalSearchState) state).is_goal();
    }
}
