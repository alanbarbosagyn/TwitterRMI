/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterrmi;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alan
 */
public class ClientRMI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String enderecoRegistry = "localhost";
        
        try {
            
            Registry registry = LocateRegistry.getRegistry(enderecoRegistry);

            System.out.println("Lista de Objetos Remotos obtidos do registry em "
                    + enderecoRegistry);
            String[] listaRemotos = registry.list();
            String nomeRemoto = listaRemotos[0];
//            for (int i = 0; i < listaRemotos.length; i++) {
//                System.out.println("   [" + i + "] " + listaRemotos[i]);
//            }
//            String nomeRemoto = listaRemotos[(new Random()).nextInt(listaRemotos.length)];

            System.out.println("----\nEscolhendo aleatoriamente nome remoto "
                    + nomeRemoto + "");
            System.out.println("----\n\n\n\n");
            
            TwitterRemoto twitter;
            twitter = (TwitterRemoto) registry.lookup(nomeRemoto);
            TwitterRemoto stubRemoto = (TwitterRemoto) UnicastRemoteObject.exportObject(twitter, 0);
            
            System.out.println(twitter.teste());
                    
        } catch (NotBoundException ex) {
            Logger.getLogger(ClientRMI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AccessException ex) {
            Logger.getLogger(ClientRMI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ClientRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
