package Main;

import IA.DistFS.Servers;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import localsearch.*;

import java.util.*;

public class Main {

    private static void optionsRecap(int users, int requests, int seed, int nserv, int nrep, int a, int h, int s, int algorithm) {
        System.out.println("El problema se a generado con:");
        System.out.println(users + " USUARIOS");
        System.out.println(requests + " PETICIONES");
        System.out.println("Siendo la SEMILLA " + seed);
        System.out.println(nserv + " SERVIDORES");
        System.out.println("Siendo el NUMERO MINIMO DE REPLICACIONES POR FICHERO " + nrep);
        
        String generator;
        switch (a) {
            case 0:
                generator = "GREEDY";
                break;
            case 1:
                generator = "LOW QUALITY";
                break;
            case 2:
                generator = "LESS TIME NEEDED";
                break;
            default:
                generator = "LESS TIME OCCUPIED";
                break;
        }
        System.out.println("Se ha escogido el generador " + generator);
        
        String heuristic;
        switch (h) {
            case 0:
                heuristic = "TIMES SUM";
                break;
            case 1:
                heuristic = "AVERAGE_TIME";
                break;
            case 2:
                heuristic = "VARIANCE";
                break;
            case 3:
                heuristic = "ENTROPY";
                break;
            default:
                heuristic = "SERVER MAX TIME";
                break;
        }
        System.out.println("Se ha escogido la funcion heuristica: " + heuristic);
        
        String successor;
        switch (s) {
            case 0:
                successor = "MOVE PETITION";
                break;
            case 1:
                successor = "SWAP PETITIONS";
                break;
            default:
                successor = "MOVE PETITION + SWAP PETITIONS";
                break;
        }
        System.out.println("Se ha escogido la funcion sucesora: " + successor);
        
        String alg;
        switch (algorithm) {
            case 0:
                alg = "HILL CLIMBING";
                break;
            default:
                alg = "SIMULATED ANNEALING";
                break;
        }
        System.out.println("Se ha escogido el algoritmo: " + alg);
    }
    
    private static void showOptions() {
        System.out.println("0. Generador LOW QUALITY");
        System.out.println("1. Generador LESS TIME NEEDED");
        System.out.println("2. Generador LESS TIME OCCUPIED");
    }
    
    private static void showHeuristics() {
        System.out.println("0. Heuristica TIMES SUM");
        System.out.println("1. Heuristica AVERAGE_TIME");
        System.out.println("2. Heuristica VARIANCE");
        System.out.println("3. Heuristica ENTROPY");
        System.out.println("4. Heuristica SERVER_MAX_TIME");
    }
    
    private static void showSuccessors() {
        System.out.println("0. Sucesor utilizando MOVE PETITION");
        System.out.println("1. Sucesor utilizando SWAP PETITIONS");
        System.out.println("2. Sucesor utilizando MOVE PETITION + SWAP PETITIONS");
        System.out.println("3. Sucesor utilizando MOVE PETITION (SA)");
        System.out.println("4. Sucesor utilizando SWAP PETITIONS (SA)");
        System.out.println("5. Sucesor utilizando MOVE PETITION + SWAP PETITIONS (SA)");
    }
    
    private static void showAlgorithms() {
        System.out.println("0. Algoritmo HILL CLIMBING");
        System.out.println("1. Algoritmo SIMULATED ANNEALING");
    }

    public static void main(String[] args) throws Servers.WrongParametersException, Exception {
        int users, requests, seed, nserv, nrep, iniState, h, s, algorithm;
        
        Scanner in = new Scanner(System.in);
        
        System.out.println("Introduce NUMERO DE USUARIOS");
        users = in.nextInt();

        System.out.println("Introduce NUMERO DE PETICIONES");
        requests = in.nextInt();

        System.out.println("Introduce SEMILLA");
        seed = in.nextInt();

        System.out.println("Introduce NUMERO DE SERVIDORES");
        nserv = in.nextInt();

        System.out.println("Introduce NUMERO MINIMO DE REPLICACIONES POR FICHERO");
        nrep = in.nextInt();

        System.out.println("Introduce ALGORITMO PARA GENERAR EL ESTADO INICIAL");
        showOptions();
        iniState = in.nextInt();

        System.out.println("Introduce FUNCION HEURISTICA");
        showHeuristics();
        h = in.nextInt();

        System.out.println("Introduce FUNCION SUCESORA");
        showSuccessors();
        s = in.nextInt();

        System.out.println("Introduce ALGORITMO");
        showAlgorithms();
        algorithm = in.nextInt();
        
        optionsRecap(users, requests, seed, nserv, nrep, iniState, h, s ,algorithm);
        
        LocalSearchState state = new LocalSearchState(users, requests, seed, nserv, nrep, iniState);
        
        // System.out.println("Escribe el estado inicial: ");
        // state.showStateInfo();
        
        LocalSearchEvaluationFunction lsef;
        
        switch (h) {
            case 0:
                lsef = new LocalSearchFirstEvaluationFunction();
                break;
            case 1:
                lsef = new LocalSearchSecondEvaluationFunction();
                break;
            case 2:
                lsef = new LocalSearchThirdEvaluationFunction();
                break;
            case 3:
                lsef = new LocalSearchFourthEvaluationFunction();
                break;
            default:
                lsef = new LocalSearchFifthEvaluationFunction();
                break;
        }
        
        LocalSearchSuccessorFunction lssf;
        
        switch (s) {
            case 0:
                lssf = new LocalSearchFirstSuccessorFunction();
                break;
            case 1:
                lssf = new LocalSearchSecondSuccessorFunction();
                break;
            case 2:
                lssf = new LocalSearchThirdSuccessorFunction();
                break;
            case 3:
                lssf = new LocalSearchFirstSuccessorFunctionSA();
                break;
            case 4:
                lssf = new LocalSearchSecondSuccessorFunctionSA();
                break;
            default:
                lssf = new LocalSearchThirdSuccessorFunctionSA();
                break;
        }
        
        Problem p = new Problem(state,
                                lssf,
                                new LocalSearchGoalTest(),
                                lsef);
        
        SearchAgent agent;
        
        long init_time = 0;
        long end_time = 0;
        
        if (algorithm == 0) { 
            Search search = new HillClimbingSearch();
            
            init_time = System.currentTimeMillis();
            agent = new SearchAgent(p, search);
            end_time = System.currentTimeMillis();
            
            System.out.println("Escribe el estado final: ");
            
            LocalSearchState final_state = (LocalSearchState)search.getGoalState();
            
            final_state.showStateInfo();
            
            System.out.println("Elapsed time: " + (double)(end_time-init_time)/1000);
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        }
        else {
            System.out.println("Introduce MAX NUM ITERACIONES");
            int num_i = in.nextInt();
            
            System.out.println("Introduce ITERACIONES/PASO DE TEMPERATURA");
            int i_x_temp = in.nextInt();
            
            System.out.println("Introduce K");
            int k = in.nextInt();
            
            System.out.println("Introduce LAMBDA");
            double lambda = in.nextDouble();
            
            SimulatedAnnealingSearch search = new SimulatedAnnealingSearch(num_i, i_x_temp, k, lambda);
            
            init_time = System.currentTimeMillis();
            agent = new SearchAgent(p, search);
            end_time = System.currentTimeMillis();
            
            //System.out.println("Escribe el estado final: ");
            //LocalSearchState final_state = (LocalSearchState)search.getGoalState();
            //final_state.showStateInfo();
            
            System.out.println("Elapsed time: " + (double)(end_time - init_time)/1000);
            //printActions(agent.getActions());
            //printInstrumentation(agent.getInstrumentation());
        }   
    }
    
    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }
    }

    private static void printActions(List actions) {
        for (int i = 0; i < actions.size();i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }
}