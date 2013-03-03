/**
 * SrmClientSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package lbnl.legacy;

public class SrmClientSoapBindingSkeleton implements lbnl.legacy.SrmClient, org.apache.axis.wsdl.Skeleton {
	/*
    private lbnl.legacy.SrmClient impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

   //
    // Returns List of OperationDesc objects with this name
    //
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    //
    // Returns Collection of OperationDescs
    //
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("srmPing", _params, new javax.xml.namespace.QName("", "srmPingReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("lbnl", "srmPing"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("srmPing") == null) {
            _myOperations.put("srmPing", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("srmPing")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("srmCopy", _params, new javax.xml.namespace.QName("", "srmCopyReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("lbnl", "srmCopy"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("srmCopy") == null) {
            _myOperations.put("srmCopy", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("srmCopy")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "in1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("srmGet", _params, new javax.xml.namespace.QName("", "srmGetReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("lbnl", "srmGet"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("srmGet") == null) {
            _myOperations.put("srmGet", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("srmGet")).add(_oper);
    }

    public SrmClientSoapBindingSkeleton() {
        this.impl = new lbnl.legacy.SrmClientSoapBindingImpl();
    }

    public SrmClientSoapBindingSkeleton(lbnl.legacy.SrmClient impl) {
        this.impl = impl;
    }
    public java.lang.String srmPing(java.lang.String in0) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.srmPing(in0);
        return ret;
    }

    public java.lang.String srmCopy(java.lang.String in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.srmCopy(in0, in1, in2);
        return ret;
    }

    public java.lang.String srmGet(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.srmGet(in0, in1);
        return ret;
    }
*/
}
