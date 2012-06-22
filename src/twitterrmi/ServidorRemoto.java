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

    public void updateStatus(String twitt) throws RemoteException;

    public ArrayList<String> search(String hashtag) throws RemoteException;

    public ArrayList<String> retrieveStatus() throws RemoteException;
}
