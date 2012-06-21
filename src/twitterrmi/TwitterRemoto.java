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
public interface TwitterRemoto extends Remote{
    
    public String teste() throws RemoteException;
    
    public String testeNovo() throws RemoteException;
       
}
