/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterrmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author alan
 */
public interface ServidorRemoto extends Remote {

    public void postaTwitter(String twitt) throws RemoteException;
}
