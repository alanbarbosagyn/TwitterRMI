/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterrmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import twitter4j.TwitterException;

/**
 *
 * @author Alan
 */
public class Servidor implements ServidorRemoto {

    private TwitterImpl twitter = null;
    private final String nome = "Servidor";

    public Servidor() {
        this.twitter = new TwitterImpl("Alan");
    }

//    private static final Servidor servidor = new Servidor();
//
//    private Servidor() {
//        this.twitter = new TwitterImpl("Alan");
//    }
//
//    public static Servidor getEstancia() {
//        return new Servidor();
//    }
    
    public static void main(String[] args) {
        
        /**
         *  Atenção: Esses dois parametros são de grande importancia. 
         */
        
        String enderecoIPLocal = "127.0.0.1";
        String nomeServidor = "Servidor";
        
        /* ########################  */

        if (enderecoIPLocal != null) {
            System.setProperty("java.rmi.server.hostname", enderecoIPLocal);
        }
        System.out.println("Objeto remoto no endereco " + enderecoIPLocal);

        try {
            Servidor banco = new Servidor();

            // cria o registry para evitar problemas.
            Registry registry =  LocateRegistry.createRegistry(2020);

            ServidorRemoto stubRemoto = (ServidorRemoto) UnicastRemoteObject.exportObject(banco, 0);

            String uriBancoRemoto = "rmi://" + nomeServidor;

            registry.rebind(uriBancoRemoto, stubRemoto);
            System.out.println("Objeto remoto exportado com nome "
                    + uriBancoRemoto);
            System.out.println("\nObjeto remoto servidor do ar!");

        } catch (RemoteException e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    @Override
    public void postaTwitter(String twitt) throws RemoteException {
        try {
            this.twitter.modificaStatusOAuth(twitt);
        } catch (TwitterException ex) {
            throw new RemoteException("Problemas ao postar o Twitt!");
        }
    }
    /**
     * @return the twitter
     */
    public TwitterImpl getTwitter() {
        return twitter;
    }

    /**
     * @param twitter the twitter to set
     */
    public void setTwitter(TwitterImpl twitter) {
        this.twitter = twitter;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }
}
