/**
 * SrmClientServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package lbnl;

public class SrmClientServiceLocator extends org.apache.axis.client.Service implements lbnl.SrmClientService {

    public SrmClientServiceLocator() {
    }


    public SrmClientServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SrmClientServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SrmClient
    private java.lang.String SrmClient_address = "http://localhost/axis/services/SrmClient";

    public java.lang.String getSrmClientAddress() {
        return SrmClient_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SrmClientWSDDServiceName = "SrmClient";

    public java.lang.String getSrmClientWSDDServiceName() {
        return SrmClientWSDDServiceName;
    }

    public void setSrmClientWSDDServiceName(java.lang.String name) {
        SrmClientWSDDServiceName = name;
    }

    public lbnl.SrmClient getSrmClient() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SrmClient_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSrmClient(endpoint);
    }

    public lbnl.SrmClient getSrmClient(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            lbnl.SrmClientSoapBindingStub _stub = new lbnl.SrmClientSoapBindingStub(portAddress, this);
            _stub.setPortName(getSrmClientWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSrmClientEndpointAddress(java.lang.String address) {
        SrmClient_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (lbnl.SrmClient.class.isAssignableFrom(serviceEndpointInterface)) {
                lbnl.SrmClientSoapBindingStub _stub = new lbnl.SrmClientSoapBindingStub(new java.net.URL(SrmClient_address), this);
                _stub.setPortName(getSrmClientWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("SrmClient".equals(inputPortName)) {
            return getSrmClient();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("lbnl", "SrmClientService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("lbnl", "SrmClient"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SrmClient".equals(portName)) {
            setSrmClientEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
