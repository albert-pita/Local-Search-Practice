package localsearch;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;

public class LocalSearchThirdSuccessorFunction extends LocalSearchSuccessorFunction implements SuccessorFunction {
    
    @Override
    public List getSuccessors(Object pState) {
        /* This third successor function iterates through the requests and then it uses
           both of the operators.
        */
        ArrayList<Successor> retVal = new ArrayList<>();
        LocalSearchState state = (LocalSearchState) pState;
        
        for(int i = 0; i < state.getRequests().size(); i++) {
            
            for (int j = 0; j < state.getTimePerServer().length; j++) {
                LocalSearchState newState = new LocalSearchState(state);
                
                newState.move_petition(i, j);
                String s = String.valueOf(newState.getServerMaxTime());
                retVal.add(new Successor(s, newState));
            }
            
            for (int j = i + 1; j < state.getRequests().size() - 1; j++) {
                LocalSearchState newState = new LocalSearchState(state);
                
                newState.swap_petitions(i, j);
                String s = String.valueOf(newState.getServerMaxTime());
                retVal.add(new Successor(s, newState));
            }
        }
        
        return retVal;
    }
}