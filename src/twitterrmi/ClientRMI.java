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
import java.util.Scanner;
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
    public static void main(String[] args) throws RemoteException, NotBoundException {
        /**
         * Atenção: Esses dois parametros são de grande importancia.
         */
        String enderecoIPLocal = "127.0.0.1";
        String nomeServidor = "Servidor";
        int porta = 2020;

        /*
         * ########################
         */

        String enderecoRegistry = enderecoIPLocal;
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
            servidor = (ServidorRemoto) registry.lookup("rmi://" + nomeServidor);
            ServidorRemoto stubRemoto = (ServidorRemoto) UnicastRemoteObject.exportObject(servidor, 0);

            while (true) {
                if (token == null) {
                    
                    token = autenticarUsuario(servidor);
                    
                } else {

                    Scanner in = new Scanner(System.in);
                    int opcao;
                    
                    
                    /****
                     *  #### Menu principal.
                     * */
                    System.out.println("Digite sua opcao: \n");
                    System.out.println(" "
                            + "1 - updateStatus \n"
                            + " 2 - search \n"
                            + " 3 - getFriendsStatus \n"
                            + " 4 - getUserStatus \n"
                            + " 5 - changingTwitterAccount \n"
                            + " 6 - logoutApp \n"
                            + " 7 - Sair \n");
                    
                    System.out.println("Opcao: ");
                    opcao = in.nextInt();
                    
                    /**
                     *  Faz as chamadas ao objeto servente.
                     * */
                    switch (opcao) {

                        case 1:
                            System.out.println(" =============== FAZER ATUALIZACAO DO STATUS =============== ");

                            in = new Scanner(System.in);
                            System.out.println(" Digite twitt para publicacao:");
                            String twitt = in.nextLine();
                            servidor.updateStatus(twitt, token);
                            System.out.println(" TWITTE PUBLICADO COM SUCESSO ! ");
                            break;

                        case 2:
                            System.out.println(" =============== FAZER PESQUISA TWITTE =============== ");

                            in = new Scanner(System.in);
                            System.out.println(" Digite hashtag da pesquisa:");
                            String hashtag = in.nextLine();
                            showTwitts(servidor.search(hashtag, token));
                            
                            break;

                        case 3:
                            System.out.println(" =============== STATUS DOS AMIGOS =============== ");
                            showTwitts(servidor.getFriendsStatus(token));
                           
                            break;

                        case 4:
                            System.out.println(" =============== MEU STATUS =============== ");
                            showTwitts(servidor.getUserStatus(token));
                            
                            break;

                        case 5:
                            System.out.println(" =============== CONTA TWITTE =============== ");
                            servidor.changingTwitterAccount();
                            break;

                        case 6:
                            System.out.println(" =============== FAZER LOGOUT =============== ");
                            servidor.logoutApp(token);
                            System.out.println(" LOGOUT ENCERRADO COM SUCESSO ! ");
                            token = null;
                            
                            break;

                        case 7:
                            System.out.println(" ===============  FINALIZAR APLICACAO  =============== ");
                            System.out.println(" APLICACAO ENCERRADO COM SUCESSO ! ");
                            System.exit(1);
                           
                            break;
                    }
                }
            }
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

    public static void showTwitts(List<String> twitts) {
        for (String twiit : twitts) {
            System.out.println(twiit);
        }
        System.out.println("\n\n");
    }

    public static String autenticarUsuario(ServidorRemoto servidor) throws RemoteException, NotBoundException {


        System.out.println("\n ===========  PROJETO TWITTER ===========\n ");
        System.out.println(" ###AUTENTICAR USUARIO### ");
        System.out.println(" Digite seu login e senha para entrar no sistema: ");
 
      
        
        Scanner login = new Scanner(System.in);
        String token = null;
        
        String usuario = login.nextLine();
        
        
        System.out.println(" USUARIO: " + usuario);
        String senha = login.nextLine();
        System.out.println(" SENHA: " + senha);
        try {
            token = servidor.logarApp(usuario, senha);
        } catch (RemoteException ex) {
            System.err.println(" USUARIO OU SENHA INCORRETOS. DIGITE NOVAMENTE !");
        }
        return token;
    }
}