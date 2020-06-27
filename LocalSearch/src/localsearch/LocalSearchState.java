package localsearch;

import IA.DistFS.Requests;
import IA.DistFS.Servers;
import java.util.*;

public class LocalSearchState {
    
    private static Requests c_requests;
    private static Servers c_servers;
    
    // Number that indicates the max time of all the set of servers
    private int server_max_time;
    
    // Number that indicates the total transmission time
    private int total_transmision_time;
    
    // Transmission time accumulated per server
    private int[] time_per_server;
    
    // Server assigned on request
    private int[] petition_server;
    
    public LocalSearchState(int users, int requests, int seed, int nserv, int nrep, int a) throws Servers.WrongParametersException {
        //Generates initial states with different algorithms
        
        c_requests = new Requests(users, requests, seed);
        c_servers = new Servers(nserv, nrep, seed);
        
        server_max_time = 0;
        total_transmision_time = 0;
        time_per_server = new int[nserv];
        petition_server = new int[c_requests.size()];
        
        switch (a) {
            case 0:
                low_quality_generator(c_requests, c_servers);
                break;
            case 1:
                less_time_needed_generator(c_requests, c_servers);
                break;
            default:
                less_time_occupied_generator(c_requests, c_servers);
                break;
        }
    }
    
    public LocalSearchState(LocalSearchState state) {
        c_requests = state.getRequests();
        c_servers = state.getServers();
        
        server_max_time = state.server_max_time;
        total_transmision_time = state.total_transmision_time;
        
        time_per_server = state.time_per_server.clone();
        petition_server = state.petition_server.clone();
    }

    private void low_quality_generator(Requests c_requests, Servers c_servers){
        /*
            Generates an initial state that consists on assigning each
            petition to the first server that contains its file sorted
            with the random seed 1234
        */
        
        for (int i = 0; i < c_requests.size(); i++) {
            int[] request = c_requests.getRequest(i);
            
            Set<Integer> servers_f = c_servers.fileLocations(request[1]);
            Iterator<Integer> iterator = servers_f.iterator();
            
            // We get the first server that has the file
            Integer s = iterator.next();
            
            // Assign that server to the petition
            petition_server[i] = s;
            
            int time = c_servers.tranmissionTime(s, request[0]);
            
            // Update the total transmision time
            total_transmision_time += time;
            
            // Update the transmision time of the server
            time_per_server[s] += time;
        }
        server_max_time = recalculate_server_max_time();
    }

    private void less_time_needed_generator(Requests c_requests, Servers c_servers){
        /*
            Generates an initial state assigning the petitions to the server
            that can satisfy them in the lesser time
         */
        
        for (int i = 0; i < c_requests.size(); i++) {
            int[] request = c_requests.getRequest(i);
            
            Set<Integer> servers_f = c_servers.fileLocations(request[1]);
            Iterator<Integer> iterator = servers_f.iterator();
            
            // We get the first server that have the file and pick its transmission Time
            Integer choosen_s = iterator.next();
            int choosen_ttime = c_servers.tranmissionTime(choosen_s, request[0]);
            
            // We iterate through all the servers that have the file, checking if its transmission time is lowest that our first guess
            while(iterator.hasNext()) {
                Integer opp_s = iterator.next();
                
                int opp_ttime = c_servers.tranmissionTime(opp_s, request[0]);
                if (opp_ttime < choosen_ttime) {
                    choosen_s = opp_s;
                    choosen_ttime = opp_ttime;
                }
            }
            
            // Assign the choosen server to the petition
            petition_server[i] = choosen_s;
            
            // Update the total transmision time
            total_transmision_time += choosen_ttime;
            
            // Update the transmision time of the choosen server
            time_per_server[choosen_s] += choosen_ttime;
        }
        server_max_time = recalculate_server_max_time();
    }

    private void less_time_occupied_generator(Requests c_requests, Servers c_servers){
        /*
            Generates an initial state assigning the petitions ordered with
            the random seed 1234 to the server that can satisfy them and has
            the minimum time sum
         */
        
        for (int i = 0; i < c_requests.size(); i++) {
            int[] request = c_requests.getRequest(i);
            
            Set<Integer> servers_f = c_servers.fileLocations(request[1]);
            Iterator<Integer> iterator = servers_f.iterator();
            
            // We get the first server that have the file and pick its total transmission time
            Integer choosen_s = iterator.next();
            int choosen_time = time_per_server[choosen_s];
            
            // We iterate through all the servers that have the file, checking if its total transmission time is lowest that our first guess
            while(iterator.hasNext()) {
                Integer opp_s = iterator.next();
                
                int opp_time = time_per_server[opp_s];
                if (opp_time < choosen_time) {
                    choosen_s = opp_s;
                    choosen_time = opp_time;
                }
            }
            
            // Assign the choosen server to the petition
            petition_server[i] = choosen_s;
            
            // Update the total transmision time
            total_transmision_time += c_servers.tranmissionTime(choosen_s, request[0]);
            
            // Update the transmision time of the choosen server
            time_per_server[choosen_s] += c_servers.tranmissionTime(choosen_s, request[0]);
        }
        server_max_time = recalculate_server_max_time();
    }
    
    // HEURISTICS FUNCTIONS
    
    public double times_sum(){
        //Sum of all the servers' transmission times
        return this.total_transmision_time;
    }
    
    public double server_max_time() {
        //Returns the time of the server with maximum transmission time
        return this.server_max_time;
    }
   
    public double average_time(){
        //Average transmission time for all servers
        return this.total_transmision_time / this.time_per_server.length;
    }
    
    public double variance(){
        //Variance of the servers' transmission times
        double average_time = average_time(), sum = 0;
        for(int i = 0; i < this.time_per_server.length; i++){
            sum = sum + Math.pow(this.time_per_server[i] - average_time,2.0);
        }
        sum = sum / this.time_per_server.length;
        return sum * average_time;
    }
       
    public double entropy(){
        //Entropy of the servers' transmission times
        double sum = 0, log;
        for(int i = 0; i < this.time_per_server.length; i++){
            double tsi = this.time_per_server[i];
            double expression = (tsi/Math.abs((this.total_transmision_time/this.time_per_server.length)-tsi));
            log = Math.log(expression);
            sum = sum + (expression * log);
        }
        return sum * average_time();
    }
    
    // GOAL TEST FUNCTIONS

     /* Goal test */
     public boolean is_goal(){
         return false;
     }
     
     // OPERATORS
     
    public void move_petition(int p, int s){
        // Reassigns the Petition p to the Server s and modifies the original and new server times
        // We get the previous server
        int previous_server = petition_server[p];
        // We get the request from the set of requests
        int[] request = c_requests.getRequest(p);
        // We get the transmission time of the old server and we substract it from its total time
        int old_server_ttime = c_servers.tranmissionTime(previous_server, request[0]);
        int new_server_ttime = c_servers.tranmissionTime(s, request[0]);
        
        time_per_server[previous_server] = time_per_server[previous_server] - old_server_ttime;
        
        // We get the transmission time of the new server and we add it to its total time
        time_per_server[s] += new_server_ttime;

        // Update the total transmision time
        total_transmision_time = total_transmision_time - old_server_ttime + new_server_ttime;;

        // We assign the new sever to the petition
        petition_server[p] = s;
            
        server_max_time = recalculate_server_max_time();
    }
     
    public void swap_petitions(int p0, int p1){
        //Swaps the servers assigned to the petitions p1 and p2 and modifies their times
        
        int[] requestP0 = c_requests.getRequest(p0);
        int[] requestP1 = c_requests.getRequest(p1);
        
        int serverP0 = petition_server[p0];
        int serverP1 = petition_server[p1];
        
        int P0_server_time = c_servers.tranmissionTime(serverP0, requestP0[0]);
        int P1_server_time = c_servers.tranmissionTime(serverP1, requestP1[0]);
        
        int new_P0_server_time = c_servers.tranmissionTime(serverP1, requestP0[0]);
        int new_P1_server_time = c_servers.tranmissionTime(serverP0, requestP1[0]);
   
        
        time_per_server[serverP0] = time_per_server[serverP0] - P0_server_time + new_P1_server_time;
        time_per_server[serverP1] = time_per_server[serverP1] - P1_server_time + new_P0_server_time;

        total_transmision_time = total_transmision_time - P0_server_time - P1_server_time + new_P0_server_time + new_P1_server_time;

        petition_server[p0] = serverP1;
        petition_server[p1] = serverP0;
            
        server_max_time = recalculate_server_max_time();
    }
    
    // EXTRA FUNCTIONS
    
    public Requests getRequests(){
        return c_requests;
    }
    
    public Servers getServers() {
        return c_servers;
    }
    
    public int getServerMaxTime() {
        return this.server_max_time;
    }
    
    public int recalculate_server_max_time() {
        //Returns the time of the server with maximum transmission time
        int max = 0;
        for(int i = 0; i < this.time_per_server.length; i++){
            if(max < this.time_per_server[i]) max = this.time_per_server[i];
        }
        return max;
    }
    public int getTotalTransmissionTime() {
        return this.total_transmision_time;
    }
    
    public int[] getPetitionServer() {
        return this.petition_server;
    }
    
    public int[] getTimePerServer() {
        return this.time_per_server;
    }
    
    public void showStateInfo() {
        
        System.out.println("El tiempo total de transmision es: " + total_transmision_time);
        
        System.out.println("El tiempo del servidor con más tiempo de transmisión es:" + server_max_time);
        
        System.out.println("Asignacion peticion a servidor ");
        for (int i = 0; i < petition_server.length; i++) {
            if ((i % 10 == 0 && i != 0) || i == petition_server.length - 1) System.out.println("P: " + i + " S: " + petition_server[i]);
            else System.out.print("P: " + i + " S: " + petition_server[i] + " ");
        }
        
        System.out.println("Tiempo de transmision acumulador por servidor");
        for (int i = 0; i < time_per_server.length; i++) {
            if ((i % 10 == 0 && i != 0) || i == time_per_server.length - 1) System.out.println("S: " + i + " Stt: " + time_per_server[i]);
            else System.out.print("S: " + i + " Stt: " + time_per_server[i] + " ");
        }
        
    }
}
