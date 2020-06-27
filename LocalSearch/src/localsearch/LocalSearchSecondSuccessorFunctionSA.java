package localsearch;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LocalSearchSecondSuccessorFunctionSA extends LocalSearchSuccessorFunction implements SuccessorFunction {
    
    @Override
    public List getSuccessors(Object pState) {

        ArrayList<Successor> retVal = new ArrayList<>();
        LocalSearchState state = (LocalSearchState) pState;
        Random rand = new Random();
        
        int i = rand.nextInt(state.getRequests().size());
        int j;
        do{
            j = rand.nextInt(state.getRequests().size());
        }while(i == j);
        
        LocalSearchState newState = new LocalSearchState(state);
        
        newState.swap_petitions(i, j);
        StringBuilder S = new StringBuilder();
        S.append("Se han intercambiado las peticiones ").append(i).append(" y ").append(j).append("\n");
        
        retVal.add(new Successor(S.toString(), newState));
        
        return retVal;
    }
}