package localsearch;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LocalSearchFirstSuccessorFunctionSA extends LocalSearchSuccessorFunction implements SuccessorFunction{
    
    @Override
    public List getSuccessors(Object pState) {
        
        ArrayList<Successor> retVal = new ArrayList<>();
        LocalSearchState state = (LocalSearchState) pState;
        Random rand = new Random();
        
        int i = rand.nextInt(state.getRequests().size());
        int j = rand.nextInt(state.getTimePerServer().length);
        
        LocalSearchState newState = new LocalSearchState(state);
        
        newState.move_petition(i,j);
        StringBuilder S = new StringBuilder();
        S.append("Se ha movido peticion ").append(i).append(" a servidor " ).append(j).append("\n");
        retVal.add(new Successor(S.toString(), newState));
        
        return retVal;
        
    }
}