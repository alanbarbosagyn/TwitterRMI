/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterrmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PAF-ECF_2
 */
public class Controlador {
    
    ServidorRemoto servidor = null;
    
    public Controlador(){
    }
    
    public void obtemReferenciaObjRemoto(){
        String enderecoRegistry = "127.0.0.1";
        int porta = 2020;
        String nomeServidor = "Servidor";
        
         Registry registry;
        try {
            registry = LocateRegistry.getRegistry(enderecoRegistry, porta);

            System.out.println("Lista de Objetos Remotos obtidos do registry em "
                    + enderecoRegistry);
            String[] listaRemotos = registry.list();
            for (int i = 0; i < listaRemotos.length; i++) {
                System.out.println("   [" + i + "] " + listaRemotos[i]);
            }

            this.servidor = (ServidorRemoto) registry.lookup("rmi://"+nomeServidor);
            ServidorRemoto stubRemoto = (ServidorRemoto) UnicastRemoteObject.exportObject(servidor, 0);
            
        } catch (RemoteException ex) {
            Logger.getLogger(FormClientTwitterRMI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(FormClientTwitterRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
