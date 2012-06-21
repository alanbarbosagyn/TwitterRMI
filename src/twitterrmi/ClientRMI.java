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
        String ipLocal = "194.168.1.109";
        String enderecoRegistry = ipLocal;
        String twitt = "Client Twitter work again!";
        String nomeServidor = "Servidor";

        try {

            Registry registry = LocateRegistry.getRegistry(enderecoRegistry);

            System.out.println("Lista de Objetos Remotos obtidos do registry em "
                    + enderecoRegistry);
            String[] listaRemotos = registry.list();
            for (int i = 0; i < listaRemotos.length; i++) {
                System.out.println("   [" + i + "] " + listaRemotos[i]);
            }

            System.out.println("----\n\n");

            ServidorRemoto servidor;
            servidor = (ServidorRemoto) registry.lookup(nomeServidor);
            ServidorRemoto stubRemoto = (ServidorRemoto) UnicastRemoteObject.exportObject(servidor, 0);

            servidor.postaTwitter(twitt);
            System.out.println(twitt);

        } catch (NotBoundException ex) {
            Logger.getLogger(ClientRMI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AccessException ex) {
            Logger.getLogger(ClientRMI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ClientRMI.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            System.exit(0);
        }
    }
}
