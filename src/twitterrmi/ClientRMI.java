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
import java.util.List;
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
        /**
         *  Atenção: Esses dois parametros são de grande importancia. 
         */
        
        String enderecoIPLocal = "127.0.0.1";
        String nomeServidor = "Servidor";
        int porta = 2020;
        
        /* ########################  */
        
        String enderecoRegistry = enderecoIPLocal;
        String twitt = "Após tanto quebrar a cabeça, o trabalho a sair!";
        String hashtag = "#asminapira";

        try {

            Registry registry = LocateRegistry.getRegistry(enderecoRegistry, porta);

            System.out.println("Lista de Objetos Remotos obtidos do registry em "
                    + enderecoRegistry);
            String[] listaRemotos = registry.list();
            for (int i = 0; i < listaRemotos.length; i++) {
                System.out.println("   [" + i + "] " + listaRemotos[i]);
            }

            System.out.println("----\n\n");

            ServidorRemoto servidor;
            servidor = (ServidorRemoto) registry.lookup("rmi://"+nomeServidor);
            ServidorRemoto stubRemoto = (ServidorRemoto) UnicastRemoteObject.exportObject(servidor, 0);

            List<String> list = servidor.retrieveStatus(); 
            showTwitts(list);
//            System.out.println(twitt);

        } catch (NotBoundException ex) {
            Logger.getLogger(ClientRMI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AccessException ex) {
            Logger.getLogger(ClientRMI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ClientRMI.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.exit(0);
        }
    }
    
    public static void showTwitts(List<String> twitts){
        int i = 0;
        for(String twiit : twitts){
            System.out.println(i++ +" -- "+ twiit);
        }
        System.out.println("--------------------");
    }
}
