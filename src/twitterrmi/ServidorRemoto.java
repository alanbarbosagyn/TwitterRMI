/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterrmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author alan
 */
public interface ServidorRemoto extends Remote {

    public void updateStatus(String twitt, String clientToken) throws RemoteException;

    public ArrayList<String> search(String hashtag, String clientToken) throws RemoteException;

    public ArrayList<String> getFriendsStatus(String clientToken) throws RemoteException;
    
    public ArrayList<String> getUserStatus(String clientToken) throws RemoteException;
    
    public void changingTwitterAccount() throws RemoteException;
    
    public String logarApp(String usuario, String senha) throws RemoteException;
    
    public void logoutApp(String token) throws RemoteException;
}
