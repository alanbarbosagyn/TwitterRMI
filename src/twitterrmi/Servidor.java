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
import org.omg.CORBA.TIMEOUT;
import twitter4j.TimeZone;
import twitter4j.TwitterException;

/**
 *
 * @author Alan
 */
public class Servidor implements ServidorRemoto {

    private static int porta;
    private TwitterImplementado twitterImp = null;
    private final String nome = "Servidor";
    private HashMap users;
    private HashMap usersOnline;

    public Servidor() {
        this.twitterImp = new TwitterImplementado("Alan");
        this.users = new HashMap();
        this.usersOnline = new HashMap();

        // carrega usuarios para autenticação
        this.carregaUsers();
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
    /**
     * *
     * user: alan - senha: 123456 user: jose - senha: antonio user: maria -
     * senha: maria user: lucas - senha: lu12ca84
     */
    private void carregaUsers() {
//        String[] usuarioSenha = null;
//        usuarioSenha[0] = "alan";
//        usuarioSenha[1] = "123456";
//        this.users.put("fqfs", usuarioSenha);
//        usuarioSenha[0] = "jose";
//        usuarioSenha[1] = "antonio";
//        this.users.put("otxj", usuarioSenha);
//        usuarioSenha[0] = "maria";
//        usuarioSenha[1] = "maria";
//        this.users.put("rfwnf", usuarioSenha);
//        usuarioSenha[0] = "lucas";
//        usuarioSenha[1] = "lu12ca84";
//        this.users.put("qzhfx", usuarioSenha);
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

    public ArrayList<String> getFriendsStatus() throws RemoteException {
        ArrayList<String> resultado = null;
        resultado = this.twitterImp.recuperaFriendsStatus();
        return resultado;
    }

    public ArrayList<String> getUserStatus() throws RemoteException {
        ArrayList<String> resultado = null;
        resultado = this.twitterImp.recuperaUserStatus();
        return resultado;
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

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    @Override
    public String logarApp(String usuario, String senha) throws RemoteException {
        if (this.users.containsKey(usuario)) {
            if (this.users.containsValue(senha)) {
                String clientToken = criptografar(usuario + new Date().toString());
                if(this.verificaOnline(clientToken)){
                    return "Usuario já está Conectado!!";
                }
                String[] userAndSenha = {usuario, senha};
                if (this.usersOnline.containsKey(clientToken)) {
                    this.usersOnline.put(clientToken, userAndSenha);
                } else{
                return "Falha ao gerar o token!";
                }
            }
            return "Senha incorreta";
        }
        return "Usuario incorreto";
    }

    @Override
    public String logoutApp(String clientToken) {
        if (verificaOnline(clientToken)) {
            this.usersOnline.remove(clientToken);
            return "Cliente efetuou logout!";
        }
        return "Cliente não está conectado!";
    }

    private boolean verificaOnline(String clientToken) {
        if (this.usersOnline.containsKey(clientToken)) {
            return true;
        }
        return false;
    }
}
