package localsearch;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;

public class LocalSearchFirstSuccessorFunction extends LocalSearchSuccessorFunction implements SuccessorFunction{
    
    @Override
    public List getSuccessors(Object pState) {
        /* This first successor function just iterates through the requests and the servers 
           and it moves the different petitions. It only uses the move_petition operator.
        */
        ArrayList<Successor> retVal = new ArrayList<>();
        LocalSearchState state = (LocalSearchState) pState;
        
        for(int i = 0; i < state.getRequests().size(); i++) {
            
            for (int j = 0; j < state.getTimePerServer().length; j++) {
                LocalSearchState newState = new LocalSearchState(state);
                
                newState.move_petition(i, j);
                StringBuilder S = new StringBuilder();
                S.append("Se ha movido peticion ").append(i).append(" a servidor ").append(j).append("\n");
                retVal.add(new Successor(S.toString(), newState));
            }
        }
        
        return retVal;
    }
}
