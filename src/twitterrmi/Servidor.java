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
    
    
    public Servidor(){
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
        String ipLocal = "194.168.1.109";
        String enderecoRegistry = ipLocal;
        String enderecoIPLocal = ipLocal;
        String nomeDoBanco = "Alan";

        if (enderecoIPLocal != null) {
            System.setProperty("java.rmi.server.hostname", enderecoIPLocal);
        }
        System.out.println("Objeto remoto no endereco " + enderecoIPLocal);

        try {
            Servidor banco = new Servidor();
            
            // cria o registry para evitar problemas.
            LocateRegistry.createRegistry(1099);

            ServidorRemoto stubRemoto = (ServidorRemoto) UnicastRemoteObject.exportObject(banco, 0);

            Registry registry = LocateRegistry.getRegistry(enderecoRegistry);

            String uriBancoRemoto = "rmi://" + nomeDoBanco;

            registry.rebind(uriBancoRemoto, stubRemoto);
            System.out.println("Objeto remoto exportado com nome "
                    + uriBancoRemoto);
            System.out.println("\nObjeto remoto servidor do ar!");

        } catch (RemoteException e) {
            e.printStackTrace();
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
