/**
 * SrmClientService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package lbnl;

public interface SrmClientService extends javax.xml.rpc.Service {
    public java.lang.String getSrmClientAddress();

    public lbnl.SrmClient getSrmClient() throws javax.xml.rpc.ServiceException;

    public lbnl.SrmClient getSrmClient(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
