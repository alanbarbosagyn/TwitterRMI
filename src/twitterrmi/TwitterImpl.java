/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterrmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;



/**
 *
 * @author alan
 */
public class TwitterImpl implements TwitterRemoto {

    private String nome;

    public TwitterImpl(String nome) {
        this.nome = nome;
    }
    
    
    @Override
    public String teste() {
        return "Olá mundo!!!";
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public static void main(String[] args){
        String ipLocal = "localhost";
        String enderecoRegistry = ipLocal;
        String nomeDoObjeto = "Alan";
        String enderecoIPLocal = ipLocal;
        

        try {
            TwitterImpl twitter = new TwitterImpl(nomeDoObjeto);

            TwitterRemoto stubRemoto = (TwitterRemoto) UnicastRemoteObject.exportObject(twitter, 0);

            Registry registry = LocateRegistry.getRegistry(enderecoRegistry);

            String uriBancoRemoto = "rmi://" + nomeDoObjeto;

            registry.rebind(uriBancoRemoto, stubRemoto);
            System.out.println("Objeto remoto exportado com nome "
                    + uriBancoRemoto);
            System.out.println("\nObjeto remoto servidor do ar!!!!");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String testeNovo() throws RemoteException {
        return "Novo Teste!!!!";
    }
}
