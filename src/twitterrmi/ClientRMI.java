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
        String twitt = "Teste Básico da app";
        String hashtag = "#asminapira";
        String token = null;

        try {

            Registry registry = LocateRegistry.getRegistry(enderecoRegistry, porta);

            System.out.println("Lista de Objetos Remotos obtidos do registry em "
                    + enderecoRegistry);
            String[] listaRemotos = registry.list();
            for (int i = 0; i < listaRemotos.length; i++) {
                System.out.println("   [" + i + "] " + listaRemotos[i]);
            }

            ServidorRemoto servidor;
            servidor = (ServidorRemoto) registry.lookup("rmi://"+nomeServidor);
            ServidorRemoto stubRemoto = (ServidorRemoto) UnicastRemoteObject.exportObject(servidor, 0);

            token = servidor.logarApp("alan", "123456");
            System.out.println(token);
//            try{
//            //servidor.logoutApp("alan");
//            servidor.logoutApp(token);
//            }catch(RemoteException e){
//                System.out.println(e.getMessage());
//            }
            
            showTwitts(servidor.getFriendsStatus("fqfsÁjidOzsd7;d6.}78},,dGWYd7,67"));
            showTwitts(servidor.search("#android","fqfsÁjidOzsd7;d6.}78},,dGWYd7,67"));
            showTwitts(servidor.getFriendsStatus("fqfsÁjidOzsd7;d6.}78},,dGWYd7,67"));
            showTwitts(servidor.getUserStatus("fqfsÁjidOzsd7;d6.}78},,dGWYd7,67"));

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
        for(String twiit : twitts){
            System.out.println(twiit);
        }
        System.out.println("\n\n");
    }
}
