package localsearch;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;

public class LocalSearchSecondSuccessorFunction extends LocalSearchSuccessorFunction implements SuccessorFunction {
    
    @Override
    public List getSuccessors(Object pState) {
        /* This second successor function just iterates through the requests
           and it swaps the different petitions. It only uses the swap_petitions operator.
        */
        ArrayList<Successor> retVal = new ArrayList<>();
        LocalSearchState state = (LocalSearchState) pState;
        
        for(int i = 0; i < state.getRequests().size(); i++) {
            
            for (int j = i + 1; j < state.getRequests().size() - 1; j++) {
                LocalSearchState newState = new LocalSearchState(state);
                
                newState.swap_petitions(i, j);
                StringBuilder S = new StringBuilder();
                S.append("Se han intercanviados las peticiones ").append(i).append(" y ").append(j).append("\n");
                retVal.add(new Successor(S.toString(), newState));
            }
        }
        
        return retVal;
    }
}