/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitterrmi;

import java.rmi.RemoteException;

/**
 *
 * @author Alan
 */
public class NotAuthenticatedException extends RemoteException {
    public NotAuthenticatedException(String msg){
        super(msg);
    }
    
    public NotAuthenticatedException(){
        super();
    }
}
