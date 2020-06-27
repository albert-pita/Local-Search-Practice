package localsearch;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LocalSearchThirdSuccessorFunctionSA extends LocalSearchSuccessorFunction implements SuccessorFunction {
    
    @Override
    public List getSuccessors(Object pState) {
        
        ArrayList<Successor> retVal = new ArrayList<>();
        LocalSearchState state = (LocalSearchState) pState;
        Random rand = new Random();
        
        int i = rand.nextInt(state.getRequests().size());
        int j_move = rand.nextInt(state.getTimePerServer().length);
        int j_swap;
        
        do{
            j_swap = rand.nextInt(state.getRequests().size());
        }while(i == j_swap);
        
        LocalSearchState newState = new LocalSearchState(state);
                
        int decision = rand.nextInt(2);
        String s;
        
        if(decision == 0) {
            newState.move_petition(i,j_move);
            s = "Se ha movido peticion" + i + " a servidor " + j_move + "Server max time: " + newState.getServerMaxTime() + " ttt: " + newState.getTotalTransmissionTime() + "\n";
        }
        else {
            newState.swap_petitions(i, j_swap);
            s = "Se han intercambiado las peticiones " + i + " y " + j_swap + " Server max time: " + newState.getServerMaxTime() + " ttt: " + newState.getTotalTransmissionTime() + "\n";
        }
        
        //System.out.println(s);
        retVal.add(new Successor(s, newState));
        
        return retVal;
        
    }
}