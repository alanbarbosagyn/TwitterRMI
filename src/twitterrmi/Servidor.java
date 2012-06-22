/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterrmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import twitter4j.TwitterException;

/**
 *
 * @author Alan
 */
public class Servidor implements ServidorRemoto {
    private static int porta;

    private TwitterImplementado twitterImp = null;
    private final String nome = "Servidor";

    public Servidor() {
        this.twitterImp = new TwitterImplementado("Alan");
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
         * Atenção: Esses dois parametros são de grande importancia.
         */
        String enderecoIPLocal = "127.0.0.1";
        String nomeServidor = "Servidor";
        int porta = 2020;
        

        /*
         * ########################
         */

        if (enderecoIPLocal != null) {
            System.setProperty("java.rmi.server.hostname", enderecoIPLocal);
        }
        System.out.println("Objeto remoto no endereco " + enderecoIPLocal);

        try {
            Servidor banco = new Servidor();

            // cria o registry para evitar problemas.
            Registry registry = LocateRegistry.createRegistry(porta);

            ServidorRemoto stubRemoto = (ServidorRemoto) UnicastRemoteObject.exportObject(banco, 0);

            String uriServidorRemoto = "rmi://" + nomeServidor;

            registry.rebind(uriServidorRemoto, stubRemoto);
            System.out.println("Objeto remoto exportado com nome "
                    + uriServidorRemoto);
            System.out.println("\nObjeto remoto servidor do ar!");

        } catch (RemoteException e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    @Override
    public void updateStatus(String twitt) throws RemoteException {
        try {
            this.twitterImp.modificaStatusOAuth(twitt);
        } catch (TwitterException ex) {
            throw new RemoteException("Problemas ao postar o Twitt!");
        }
    }

    @Override
    public ArrayList<String> search(String hashtag) throws RemoteException {
        ArrayList<String> resultado = null;
        resultado = this.twitterImp.pesquisa(hashtag);
        return resultado;
    }

    public ArrayList<String> retrieveStatus() throws RemoteException {
        ArrayList<String> resultado = null;
        resultado = this.twitterImp.recuperaStatus();
        return resultado;
    }

    /**
     * @return the twitter
     */
    public TwitterImplementado getTwitter() {
        return twitterImp;
    }

    /**
     * @param twitter the twitter to set
     */
    public void setTwitter(TwitterImplementado twitter) {
        this.twitterImp = twitter;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }
}
