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
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.TwitterException;

/**
 *
 * @author Alan
 */
public class Servidor implements ServidorRemoto {

    private static int porta;
    private TwitterImplementado twitterImp = null;
    private HashMap users;
    private HashMap usersOnline;

    public Servidor() {
        this.twitterImp = new TwitterImplementado("Alan");
        this.users = new HashMap();
        this.usersOnline = new HashMap();

        // carrega usuarios para autenticação
        this.carregaUsers();
    }

    /**
     * *
     * user: alan - senha: 123456 user: jose - senha: antonio user: maria -
     * senha: maria user: lucas - senha: lu12ca84
     */
    private void carregaUsers() {
        this.users.put("alan", "123456");
        this.users.put("jose", "antonio");
        this.users.put("maria", "maria");
        this.users.put("lucas", "lu12ca84");
    }

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
    public void changingTwitterAccount() throws RemoteException {
        try {
            this.twitterImp.obtemTokenAutenticacao();
        } catch (Exception ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    @Override
    public void updateStatus(String twitt, String clientToken) throws RemoteException {
        try {
            this.verificaOnline(clientToken);
            this.twitterImp.modificaStatusOAuth(twitt);
        } catch (TwitterException ex) {
            throw new RemoteException("Problemas ao postar o Twitt!");
        } catch (NotAuthenticatedException ex) {
            throw ex;
        }
    }

    @Override
    public ArrayList<String> search(String hashtag, String clientToken) throws RemoteException {
        this.verificaOnline(clientToken);
        ArrayList<String> resultado = null;
        resultado = this.twitterImp.pesquisa(hashtag);
        return resultado;
    }

    @Override
    public ArrayList<String> getFriendsStatus(String clientToken) throws RemoteException {
        this.verificaOnline(clientToken);
        ArrayList<String> resultado = null;
        resultado = this.twitterImp.recuperaFriendsStatus();
        return resultado;
    }

    @Override
    public ArrayList<String> getUserStatus(String clientToken) throws RemoteException {
        this.verificaOnline(clientToken);
        ArrayList<String> resultado = null;
        resultado = this.twitterImp.recuperaUserStatus();
        return resultado;
    }

    @Override
    public String logarApp(String usuario, String senha) throws RemoteException {
        if (this.users.containsKey(usuario) && this.users.containsValue(senha)) {
            String clientToken = criptografar(usuario);
            String[] userAndSenha = {usuario, senha};
            if (!this.usersOnline.containsKey(clientToken)) {
                this.usersOnline.put(clientToken, userAndSenha);
                return clientToken;
            } else {
                throw new NotAuthenticatedException("Usuário já está conectado!");
            }
        }
        throw new NotAuthenticatedException("Usuário ou senha incorreto!");
    }

    @Override
    public void logoutApp(String clientToken) throws NotAuthenticatedException {
        try {
            verificaOnline(clientToken);
        } catch (NotAuthenticatedException ex) {
            throw ex;
        }
        this.usersOnline.remove(clientToken);
    }

    private void verificaOnline(String clientToken) throws NotAuthenticatedException {
        if (!this.usersOnline.containsKey(clientToken)) {
            throw new NotAuthenticatedException("Token inválido!");
        }
    }

    public static String criptografar(String texto) {

        String alfabeto = " <abcdefghijklmnopqrstuvwxyzçéáíúóãõABCDEFGHIJKLMNOPQRSTUVWXYZÇÁÉÓÍÚÃÕ1234567890.;:?,º]}§[{ª!@#$%&*()_+-=\\/|\'\">";
        char[] t = texto.toCharArray();
        String palavra = "";

        for (int i = 0; i < t.length; i++) {
            int posicao = alfabeto.indexOf(t[i]) + 5;
            if (alfabeto.length() <= posicao) {
                posicao = posicao - alfabeto.length();
            }
            //Criando um método de Criptografia 
            palavra = palavra + alfabeto.charAt(posicao);
        }
        return palavra;
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
}