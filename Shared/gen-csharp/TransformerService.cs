/**
 * Autogenerated by Thrift Compiler (0.13.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using System.IO;
using Thrift;
using Thrift.Collections;
using System.Runtime.Serialization;
using Thrift.Protocol;
using Thrift.Transport;

public partial class TransformerService {
  public interface ISync {
    string login(string username, string password, string host, int port);
    List<MeciDTO> findAllMeciWithTickets();
    List<MeciDTO> findAllMeci();
    void ticketsSold(MeciDTO meci, ClientDTO loggedInClient);
  }

  public interface Iface : ISync {
    #if SILVERLIGHT
    IAsyncResult Begin_login(AsyncCallback callback, object state, string username, string password, string host, int port);
    string End_login(IAsyncResult asyncResult);
    #endif
    #if SILVERLIGHT
    IAsyncResult Begin_findAllMeciWithTickets(AsyncCallback callback, object state);
    List<MeciDTO> End_findAllMeciWithTickets(IAsyncResult asyncResult);
    #endif
    #if SILVERLIGHT
    IAsyncResult Begin_findAllMeci(AsyncCallback callback, object state);
    List<MeciDTO> End_findAllMeci(IAsyncResult asyncResult);
    #endif
    #if SILVERLIGHT
    IAsyncResult Begin_ticketsSold(AsyncCallback callback, object state, MeciDTO meci, ClientDTO loggedInClient);
    void End_ticketsSold(IAsyncResult asyncResult);
    #endif
  }

  public class Client : IDisposable, Iface {
    public Client(TProtocol prot) : this(prot, prot)
    {
    }

    public Client(TProtocol iprot, TProtocol oprot)
    {
      iprot_ = iprot;
      oprot_ = oprot;
    }

    protected TProtocol iprot_;
    protected TProtocol oprot_;
    protected int seqid_;

    public TProtocol InputProtocol
    {
      get { return iprot_; }
    }
    public TProtocol OutputProtocol
    {
      get { return oprot_; }
    }


    #region " IDisposable Support "
    private bool _IsDisposed;

    // IDisposable
    public void Dispose()
    {
      Dispose(true);
    }
    

    protected virtual void Dispose(bool disposing)
    {
      if (!_IsDisposed)
      {
        if (disposing)
        {
          if (iprot_ != null)
          {
            ((IDisposable)iprot_).Dispose();
          }
          if (oprot_ != null)
          {
            ((IDisposable)oprot_).Dispose();
          }
        }
      }
      _IsDisposed = true;
    }
    #endregion


    
    #if SILVERLIGHT
    
    public IAsyncResult Begin_login(AsyncCallback callback, object state, string username, string password, string host, int port)
    {
      return send_login(callback, state, username, password, host, port);
    }

    public string End_login(IAsyncResult asyncResult)
    {
      oprot_.Transport.EndFlush(asyncResult);
      return recv_login();
    }

    #endif

    public string login(string username, string password, string host, int port)
    {
      #if SILVERLIGHT
      var asyncResult = Begin_login(null, null, username, password, host, port);
      return End_login(asyncResult);

      #else
      send_login(username, password, host, port);
      return recv_login();

      #endif
    }
    #if SILVERLIGHT
    public IAsyncResult send_login(AsyncCallback callback, object state, string username, string password, string host, int port)
    {
      oprot_.WriteMessageBegin(new TMessage("login", TMessageType.Call, seqid_));
      login_args args = new login_args();
      args.Username = username;
      args.Password = password;
      args.Host = host;
      args.Port = port;
      args.Write(oprot_);
      oprot_.WriteMessageEnd();
      return oprot_.Transport.BeginFlush(callback, state);
    }

    #else

    public void send_login(string username, string password, string host, int port)
    {
      oprot_.WriteMessageBegin(new TMessage("login", TMessageType.Call, seqid_));
      login_args args = new login_args();
      args.Username = username;
      args.Password = password;
      args.Host = host;
      args.Port = port;
      args.Write(oprot_);
      oprot_.WriteMessageEnd();
      oprot_.Transport.Flush();
    }
    #endif

    public string recv_login()
    {
      TMessage msg = iprot_.ReadMessageBegin();
      if (msg.Type == TMessageType.Exception) {
        TApplicationException x = TApplicationException.Read(iprot_);
        iprot_.ReadMessageEnd();
        throw x;
      }
      login_result result = new login_result();
      result.Read(iprot_);
      iprot_.ReadMessageEnd();
      if (result.__isset.success) {
        return result.Success;
      }
      throw new TApplicationException(TApplicationException.ExceptionType.MissingResult, "login failed: unknown result");
    }

    
    #if SILVERLIGHT
    
    public IAsyncResult Begin_findAllMeciWithTickets(AsyncCallback callback, object state)
    {
      return send_findAllMeciWithTickets(callback, state);
    }

    public List<MeciDTO> End_findAllMeciWithTickets(IAsyncResult asyncResult)
    {
      oprot_.Transport.EndFlush(asyncResult);
      return recv_findAllMeciWithTickets();
    }

    #endif

    public List<MeciDTO> findAllMeciWithTickets()
    {
      #if SILVERLIGHT
      var asyncResult = Begin_findAllMeciWithTickets(null, null);
      return End_findAllMeciWithTickets(asyncResult);

      #else
      send_findAllMeciWithTickets();
      return recv_findAllMeciWithTickets();

      #endif
    }
    #if SILVERLIGHT
    public IAsyncResult send_findAllMeciWithTickets(AsyncCallback callback, object state)
    {
      oprot_.WriteMessageBegin(new TMessage("findAllMeciWithTickets", TMessageType.Call, seqid_));
      findAllMeciWithTickets_args args = new findAllMeciWithTickets_args();
      args.Write(oprot_);
      oprot_.WriteMessageEnd();
      return oprot_.Transport.BeginFlush(callback, state);
    }

    #else

    public void send_findAllMeciWithTickets()
    {
      oprot_.WriteMessageBegin(new TMessage("findAllMeciWithTickets", TMessageType.Call, seqid_));
      findAllMeciWithTickets_args args = new findAllMeciWithTickets_args();
      args.Write(oprot_);
      oprot_.WriteMessageEnd();
      oprot_.Transport.Flush();
    }
    #endif

    public List<MeciDTO> recv_findAllMeciWithTickets()
    {
      TMessage msg = iprot_.ReadMessageBegin();
      if (msg.Type == TMessageType.Exception) {
        TApplicationException x = TApplicationException.Read(iprot_);
        iprot_.ReadMessageEnd();
        throw x;
      }
      findAllMeciWithTickets_result result = new findAllMeciWithTickets_result();
      result.Read(iprot_);
      iprot_.ReadMessageEnd();
      if (result.__isset.success) {
        return result.Success;
      }
      throw new TApplicationException(TApplicationException.ExceptionType.MissingResult, "findAllMeciWithTickets failed: unknown result");
    }

    
    #if SILVERLIGHT
    
    public IAsyncResult Begin_findAllMeci(AsyncCallback callback, object state)
    {
      return send_findAllMeci(callback, state);
    }

    public List<MeciDTO> End_findAllMeci(IAsyncResult asyncResult)
    {
      oprot_.Transport.EndFlush(asyncResult);
      return recv_findAllMeci();
    }

    #endif

    public List<MeciDTO> findAllMeci()
    {
      #if SILVERLIGHT
      var asyncResult = Begin_findAllMeci(null, null);
      return End_findAllMeci(asyncResult);

      #else
      send_findAllMeci();
      return recv_findAllMeci();

      #endif
    }
    #if SILVERLIGHT
    public IAsyncResult send_findAllMeci(AsyncCallback callback, object state)
    {
      oprot_.WriteMessageBegin(new TMessage("findAllMeci", TMessageType.Call, seqid_));
      findAllMeci_args args = new findAllMeci_args();
      args.Write(oprot_);
      oprot_.WriteMessageEnd();
      return oprot_.Transport.BeginFlush(callback, state);
    }

    #else

    public void send_findAllMeci()
    {
      oprot_.WriteMessageBegin(new TMessage("findAllMeci", TMessageType.Call, seqid_));
      findAllMeci_args args = new findAllMeci_args();
      args.Write(oprot_);
      oprot_.WriteMessageEnd();
      oprot_.Transport.Flush();
    }
    #endif

    public List<MeciDTO> recv_findAllMeci()
    {
      TMessage msg = iprot_.ReadMessageBegin();
      if (msg.Type == TMessageType.Exception) {
        TApplicationException x = TApplicationException.Read(iprot_);
        iprot_.ReadMessageEnd();
        throw x;
      }
      findAllMeci_result result = new findAllMeci_result();
      result.Read(iprot_);
      iprot_.ReadMessageEnd();
      if (result.__isset.success) {
        return result.Success;
      }
      throw new TApplicationException(TApplicationException.ExceptionType.MissingResult, "findAllMeci failed: unknown result");
    }

    
    #if SILVERLIGHT
    
    public IAsyncResult Begin_ticketsSold(AsyncCallback callback, object state, MeciDTO meci, ClientDTO loggedInClient)
    {
      return send_ticketsSold(callback, state, meci, loggedInClient);
    }

    public void End_ticketsSold(IAsyncResult asyncResult)
    {
      oprot_.Transport.EndFlush(asyncResult);
      recv_ticketsSold();
    }

    #endif

    public void ticketsSold(MeciDTO meci, ClientDTO loggedInClient)
    {
      #if SILVERLIGHT
      var asyncResult = Begin_ticketsSold(null, null, meci, loggedInClient);
      End_ticketsSold(asyncResult);

      #else
      send_ticketsSold(meci, loggedInClient);
      recv_ticketsSold();

      #endif
    }
    #if SILVERLIGHT
    public IAsyncResult send_ticketsSold(AsyncCallback callback, object state, MeciDTO meci, ClientDTO loggedInClient)
    {
      oprot_.WriteMessageBegin(new TMessage("ticketsSold", TMessageType.Call, seqid_));
      ticketsSold_args args = new ticketsSold_args();
      args.Meci = meci;
      args.LoggedInClient = loggedInClient;
      args.Write(oprot_);
      oprot_.WriteMessageEnd();
      return oprot_.Transport.BeginFlush(callback, state);
    }

    #else

    public void send_ticketsSold(MeciDTO meci, ClientDTO loggedInClient)
    {
      oprot_.WriteMessageBegin(new TMessage("ticketsSold", TMessageType.Call, seqid_));
      ticketsSold_args args = new ticketsSold_args();
      args.Meci = meci;
      args.LoggedInClient = loggedInClient;
      args.Write(oprot_);
      oprot_.WriteMessageEnd();
      oprot_.Transport.Flush();
    }
    #endif

    public void recv_ticketsSold()
    {
      TMessage msg = iprot_.ReadMessageBegin();
      if (msg.Type == TMessageType.Exception) {
        TApplicationException x = TApplicationException.Read(iprot_);
        iprot_.ReadMessageEnd();
        throw x;
      }
      ticketsSold_result result = new ticketsSold_result();
      result.Read(iprot_);
      iprot_.ReadMessageEnd();
      return;
    }

  }
  public class Processor : TProcessor {
    public Processor(ISync iface)
    {
      iface_ = iface;
      processMap_["login"] = login_Process;
      processMap_["findAllMeciWithTickets"] = findAllMeciWithTickets_Process;
      processMap_["findAllMeci"] = findAllMeci_Process;
      processMap_["ticketsSold"] = ticketsSold_Process;
    }

    protected delegate void ProcessFunction(int seqid, TProtocol iprot, TProtocol oprot);
    private ISync iface_;
    protected Dictionary<string, ProcessFunction> processMap_ = new Dictionary<string, ProcessFunction>();

    public bool Process(TProtocol iprot, TProtocol oprot)
    {
      try
      {
        TMessage msg = iprot.ReadMessageBegin();
        ProcessFunction fn;
        processMap_.TryGetValue(msg.Name, out fn);
        if (fn == null) {
          TProtocolUtil.Skip(iprot, TType.Struct);
          iprot.ReadMessageEnd();
          TApplicationException x = new TApplicationException (TApplicationException.ExceptionType.UnknownMethod, "Invalid method name: '" + msg.Name + "'");
          oprot.WriteMessageBegin(new TMessage(msg.Name, TMessageType.Exception, msg.SeqID));
          x.Write(oprot);
          oprot.WriteMessageEnd();
          oprot.Transport.Flush();
          return true;
        }
        fn(msg.SeqID, iprot, oprot);
      }
      catch (IOException)
      {
        return false;
      }
      return true;
    }

    public void login_Process(int seqid, TProtocol iprot, TProtocol oprot)
    {
      login_args args = new login_args();
      args.Read(iprot);
      iprot.ReadMessageEnd();
      login_result result = new login_result();
      try
      {
        result.Success = iface_.login(args.Username, args.Password, args.Host, args.Port);
        oprot.WriteMessageBegin(new TMessage("login", TMessageType.Reply, seqid)); 
        result.Write(oprot);
      }
      catch (TTransportException)
      {
        throw;
      }
      catch (Exception ex)
      {
        Console.Error.WriteLine("Error occurred in processor:");
        Console.Error.WriteLine(ex.ToString());
        TApplicationException x = new TApplicationException      (TApplicationException.ExceptionType.InternalError," Internal error.");
        oprot.WriteMessageBegin(new TMessage("login", TMessageType.Exception, seqid));
        x.Write(oprot);
      }
      oprot.WriteMessageEnd();
      oprot.Transport.Flush();
    }

    public void findAllMeciWithTickets_Process(int seqid, TProtocol iprot, TProtocol oprot)
    {
      findAllMeciWithTickets_args args = new findAllMeciWithTickets_args();
      args.Read(iprot);
      iprot.ReadMessageEnd();
      findAllMeciWithTickets_result result = new findAllMeciWithTickets_result();
      try
      {
        result.Success = iface_.findAllMeciWithTickets();
        oprot.WriteMessageBegin(new TMessage("findAllMeciWithTickets", TMessageType.Reply, seqid)); 
        result.Write(oprot);
      }
      catch (TTransportException)
      {
        throw;
      }
      catch (Exception ex)
      {
        Console.Error.WriteLine("Error occurred in processor:");
        Console.Error.WriteLine(ex.ToString());
        TApplicationException x = new TApplicationException      (TApplicationException.ExceptionType.InternalError," Internal error.");
        oprot.WriteMessageBegin(new TMessage("findAllMeciWithTickets", TMessageType.Exception, seqid));
        x.Write(oprot);
      }
      oprot.WriteMessageEnd();
      oprot.Transport.Flush();
    }

    public void findAllMeci_Process(int seqid, TProtocol iprot, TProtocol oprot)
    {
      findAllMeci_args args = new findAllMeci_args();
      args.Read(iprot);
      iprot.ReadMessageEnd();
      findAllMeci_result result = new findAllMeci_result();
      try
      {
        result.Success = iface_.findAllMeci();
        oprot.WriteMessageBegin(new TMessage("findAllMeci", TMessageType.Reply, seqid)); 
        result.Write(oprot);
      }
      catch (TTransportException)
      {
        throw;
      }
      catch (Exception ex)
      {
        Console.Error.WriteLine("Error occurred in processor:");
        Console.Error.WriteLine(ex.ToString());
        TApplicationException x = new TApplicationException      (TApplicationException.ExceptionType.InternalError," Internal error.");
        oprot.WriteMessageBegin(new TMessage("findAllMeci", TMessageType.Exception, seqid));
        x.Write(oprot);
      }
      oprot.WriteMessageEnd();
      oprot.Transport.Flush();
    }

    public void ticketsSold_Process(int seqid, TProtocol iprot, TProtocol oprot)
    {
      ticketsSold_args args = new ticketsSold_args();
      args.Read(iprot);
      iprot.ReadMessageEnd();
      ticketsSold_result result = new ticketsSold_result();
      try
      {
        iface_.ticketsSold(args.Meci, args.LoggedInClient);
        oprot.WriteMessageBegin(new TMessage("ticketsSold", TMessageType.Reply, seqid)); 
        result.Write(oprot);
      }
      catch (TTransportException)
      {
        throw;
      }
      catch (Exception ex)
      {
        Console.Error.WriteLine("Error occurred in processor:");
        Console.Error.WriteLine(ex.ToString());
        TApplicationException x = new TApplicationException      (TApplicationException.ExceptionType.InternalError," Internal error.");
        oprot.WriteMessageBegin(new TMessage("ticketsSold", TMessageType.Exception, seqid));
        x.Write(oprot);
      }
      oprot.WriteMessageEnd();
      oprot.Transport.Flush();
    }

  }


  #if !SILVERLIGHT
  [Serializable]
  #endif
  public partial class login_args : TBase
  {
    private string _username;
    private string _password;
    private string _host;
    private int _port;

    public string Username
    {
      get
      {
        return _username;
      }
      set
      {
        __isset.username = true;
        this._username = value;
      }
    }

    public string Password
    {
      get
      {
        return _password;
      }
      set
      {
        __isset.password = true;
        this._password = value;
      }
    }

    public string Host
    {
      get
      {
        return _host;
      }
      set
      {
        __isset.host = true;
        this._host = value;
      }
    }

    public int Port
    {
      get
      {
        return _port;
      }
      set
      {
        __isset.port = true;
        this._port = value;
      }
    }


    public Isset __isset;
    #if !SILVERLIGHT
    [Serializable]
    #endif
    public struct Isset {
      public bool username;
      public bool password;
      public bool host;
      public bool port;
    }

    public login_args() {
    }

    public void Read (TProtocol iprot)
    {
      iprot.IncrementRecursionDepth();
      try
      {
        TField field;
        iprot.ReadStructBegin();
        while (true)
        {
          field = iprot.ReadFieldBegin();
          if (field.Type == TType.Stop) { 
            break;
          }
          switch (field.ID)
          {
            case 1:
              if (field.Type == TType.String) {
                Username = iprot.ReadString();
              } else { 
                TProtocolUtil.Skip(iprot, field.Type);
              }
              break;
            case 2:
              if (field.Type == TType.String) {
                Password = iprot.ReadString();
              } else { 
                TProtocolUtil.Skip(iprot, field.Type);
              }
              break;
            case 3:
              if (field.Type == TType.String) {
                Host = iprot.ReadString();
              } else { 
                TProtocolUtil.Skip(iprot, field.Type);
              }
              break;
            case 4:
              if (field.Type == TType.I32) {
                Port = iprot.ReadI32();
              } else { 
                TProtocolUtil.Skip(iprot, field.Type);
              }
              break;
            default: 
              TProtocolUtil.Skip(iprot, field.Type);
              break;
          }
          iprot.ReadFieldEnd();
        }
        iprot.ReadStructEnd();
      }
      finally
      {
        iprot.DecrementRecursionDepth();
      }
    }

    public void Write(TProtocol oprot) {
      oprot.IncrementRecursionDepth();
      try
      {
        TStruct struc = new TStruct("login_args");
        oprot.WriteStructBegin(struc);
        TField field = new TField();
        if (Username != null && __isset.username) {
          field.Name = "username";
          field.Type = TType.String;
          field.ID = 1;
          oprot.WriteFieldBegin(field);
          oprot.WriteString(Username);
          oprot.WriteFieldEnd();
        }
        if (Password != null && __isset.password) {
          field.Name = "password";
          field.Type = TType.String;
          field.ID = 2;
          oprot.WriteFieldBegin(field);
          oprot.WriteString(Password);
          oprot.WriteFieldEnd();
        }
        if (Host != null && __isset.host) {
          field.Name = "host";
          field.Type = TType.String;
          field.ID = 3;
          oprot.WriteFieldBegin(field);
          oprot.WriteString(Host);
          oprot.WriteFieldEnd();
        }
        if (__isset.port) {
          field.Name = "port";
          field.Type = TType.I32;
          field.ID = 4;
          oprot.WriteFieldBegin(field);
          oprot.WriteI32(Port);
          oprot.WriteFieldEnd();
        }
        oprot.WriteFieldStop();
        oprot.WriteStructEnd();
      }
      finally
      {
        oprot.DecrementRecursionDepth();
      }
    }

    public override string ToString() {
      StringBuilder __sb = new StringBuilder("login_args(");
      bool __first = true;
      if (Username != null && __isset.username) {
        if(!__first) { __sb.Append(", "); }
        __first = false;
        __sb.Append("Username: ");
        __sb.Append(Username);
      }
      if (Password != null && __isset.password) {
        if(!__first) { __sb.Append(", "); }
        __first = false;
        __sb.Append("Password: ");
        __sb.Append(Password);
      }
      if (Host != null && __isset.host) {
        if(!__first) { __sb.Append(", "); }
        __first = false;
        __sb.Append("Host: ");
        __sb.Append(Host);
      }
      if (__isset.port) {
        if(!__first) { __sb.Append(", "); }
        __first = false;
        __sb.Append("Port: ");
        __sb.Append(Port);
      }
      __sb.Append(")");
      return __sb.ToString();
    }

  }


  #if !SILVERLIGHT
  [Serializable]
  #endif
  public partial class login_result : TBase
  {
    private string _success;

    public string Success
    {
      get
      {
        return _success;
      }
      set
      {
        __isset.success = true;
        this._success = value;
      }
    }


    public Isset __isset;
    #if !SILVERLIGHT
    [Serializable]
    #endif
    public struct Isset {
      public bool success;
    }

    public login_result() {
    }

    public void Read (TProtocol iprot)
    {
      iprot.IncrementRecursionDepth();
      try
      {
        TField field;
        iprot.ReadStructBegin();
        while (true)
        {
          field = iprot.ReadFieldBegin();
          if (field.Type == TType.Stop) { 
            break;
          }
          switch (field.ID)
          {
            case 0:
              if (field.Type == TType.String) {
                Success = iprot.ReadString();
              } else { 
                TProtocolUtil.Skip(iprot, field.Type);
              }
              break;
            default: 
              TProtocolUtil.Skip(iprot, field.Type);
              break;
          }
          iprot.ReadFieldEnd();
        }
        iprot.ReadStructEnd();
      }
      finally
      {
        iprot.DecrementRecursionDepth();
      }
    }

    public void Write(TProtocol oprot) {
      oprot.IncrementRecursionDepth();
      try
      {
        TStruct struc = new TStruct("login_result");
        oprot.WriteStructBegin(struc);
        TField field = new TField();

        if (this.__isset.success) {
          if (Success != null) {
            field.Name = "Success";
            field.Type = TType.String;
            field.ID = 0;
            oprot.WriteFieldBegin(field);
            oprot.WriteString(Success);
            oprot.WriteFieldEnd();
          }
        }
        oprot.WriteFieldStop();
        oprot.WriteStructEnd();
      }
      finally
      {
        oprot.DecrementRecursionDepth();
      }
    }

    public override string ToString() {
      StringBuilder __sb = new StringBuilder("login_result(");
      bool __first = true;
      if (Success != null && __isset.success) {
        if(!__first) { __sb.Append(", "); }
        __first = false;
        __sb.Append("Success: ");
        __sb.Append(Success);
      }
      __sb.Append(")");
      return __sb.ToString();
    }

  }


  #if !SILVERLIGHT
  [Serializable]
  #endif
  public partial class findAllMeciWithTickets_args : TBase
  {

    public findAllMeciWithTickets_args() {
    }

    public void Read (TProtocol iprot)
    {
      iprot.IncrementRecursionDepth();
      try
      {
        TField field;
        iprot.ReadStructBegin();
        while (true)
        {
          field = iprot.ReadFieldBegin();
          if (field.Type == TType.Stop) { 
            break;
          }
          switch (field.ID)
          {
            default: 
              TProtocolUtil.Skip(iprot, field.Type);
              break;
          }
          iprot.ReadFieldEnd();
        }
        iprot.ReadStructEnd();
      }
      finally
      {
        iprot.DecrementRecursionDepth();
      }
    }

    public void Write(TProtocol oprot) {
      oprot.IncrementRecursionDepth();
      try
      {
        TStruct struc = new TStruct("findAllMeciWithTickets_args");
        oprot.WriteStructBegin(struc);
        oprot.WriteFieldStop();
        oprot.WriteStructEnd();
      }
      finally
      {
        oprot.DecrementRecursionDepth();
      }
    }

    public override string ToString() {
      StringBuilder __sb = new StringBuilder("findAllMeciWithTickets_args(");
      __sb.Append(")");
      return __sb.ToString();
    }

  }


  #if !SILVERLIGHT
  [Serializable]
  #endif
  public partial class findAllMeciWithTickets_result : TBase
  {
    private List<MeciDTO> _success;

    public List<MeciDTO> Success
    {
      get
      {
        return _success;
      }
      set
      {
        __isset.success = true;
        this._success = value;
      }
    }


    public Isset __isset;
    #if !SILVERLIGHT
    [Serializable]
    #endif
    public struct Isset {
      public bool success;
    }

    public findAllMeciWithTickets_result() {
    }

    public void Read (TProtocol iprot)
    {
      iprot.IncrementRecursionDepth();
      try
      {
        TField field;
        iprot.ReadStructBegin();
        while (true)
        {
          field = iprot.ReadFieldBegin();
          if (field.Type == TType.Stop) { 
            break;
          }
          switch (field.ID)
          {
            case 0:
              if (field.Type == TType.List) {
                {
                  Success = new List<MeciDTO>();
                  TList _list0 = iprot.ReadListBegin();
                  for( int _i1 = 0; _i1 < _list0.Count; ++_i1)
                  {
                    MeciDTO _elem2;
                    _elem2 = new MeciDTO();
                    _elem2.Read(iprot);
                    Success.Add(_elem2);
                  }
                  iprot.ReadListEnd();
                }
              } else { 
                TProtocolUtil.Skip(iprot, field.Type);
              }
              break;
            default: 
              TProtocolUtil.Skip(iprot, field.Type);
              break;
          }
          iprot.ReadFieldEnd();
        }
        iprot.ReadStructEnd();
      }
      finally
      {
        iprot.DecrementRecursionDepth();
      }
    }

    public void Write(TProtocol oprot) {
      oprot.IncrementRecursionDepth();
      try
      {
        TStruct struc = new TStruct("findAllMeciWithTickets_result");
        oprot.WriteStructBegin(struc);
        TField field = new TField();

        if (this.__isset.success) {
          if (Success != null) {
            field.Name = "Success";
            field.Type = TType.List;
            field.ID = 0;
            oprot.WriteFieldBegin(field);
            {
              oprot.WriteListBegin(new TList(TType.Struct, Success.Count));
              foreach (MeciDTO _iter3 in Success)
              {
                _iter3.Write(oprot);
              }
              oprot.WriteListEnd();
            }
            oprot.WriteFieldEnd();
          }
        }
        oprot.WriteFieldStop();
        oprot.WriteStructEnd();
      }
      finally
      {
        oprot.DecrementRecursionDepth();
      }
    }

    public override string ToString() {
      StringBuilder __sb = new StringBuilder("findAllMeciWithTickets_result(");
      bool __first = true;
      if (Success != null && __isset.success) {
        if(!__first) { __sb.Append(", "); }
        __first = false;
        __sb.Append("Success: ");
        __sb.Append(Success);
      }
      __sb.Append(")");
      return __sb.ToString();
    }

  }


  #if !SILVERLIGHT
  [Serializable]
  #endif
  public partial class findAllMeci_args : TBase
  {

    public findAllMeci_args() {
    }

    public void Read (TProtocol iprot)
    {
      iprot.IncrementRecursionDepth();
      try
      {
        TField field;
        iprot.ReadStructBegin();
        while (true)
        {
          field = iprot.ReadFieldBegin();
          if (field.Type == TType.Stop) { 
            break;
          }
          switch (field.ID)
          {
            default: 
              TProtocolUtil.Skip(iprot, field.Type);
              break;
          }
          iprot.ReadFieldEnd();
        }
        iprot.ReadStructEnd();
      }
      finally
      {
        iprot.DecrementRecursionDepth();
      }
    }

    public void Write(TProtocol oprot) {
      oprot.IncrementRecursionDepth();
      try
      {
        TStruct struc = new TStruct("findAllMeci_args");
        oprot.WriteStructBegin(struc);
        oprot.WriteFieldStop();
        oprot.WriteStructEnd();
      }
      finally
      {
        oprot.DecrementRecursionDepth();
      }
    }

    public override string ToString() {
      StringBuilder __sb = new StringBuilder("findAllMeci_args(");
      __sb.Append(")");
      return __sb.ToString();
    }

  }


  #if !SILVERLIGHT
  [Serializable]
  #endif
  public partial class findAllMeci_result : TBase
  {
    private List<MeciDTO> _success;

    public List<MeciDTO> Success
    {
      get
      {
        return _success;
      }
      set
      {
        __isset.success = true;
        this._success = value;
      }
    }


    public Isset __isset;
    #if !SILVERLIGHT
    [Serializable]
    #endif
    public struct Isset {
      public bool success;
    }

    public findAllMeci_result() {
    }

    public void Read (TProtocol iprot)
    {
      iprot.IncrementRecursionDepth();
      try
      {
        TField field;
        iprot.ReadStructBegin();
        while (true)
        {
          field = iprot.ReadFieldBegin();
          if (field.Type == TType.Stop) { 
            break;
          }
          switch (field.ID)
          {
            case 0:
              if (field.Type == TType.List) {
                {
                  Success = new List<MeciDTO>();
                  TList _list4 = iprot.ReadListBegin();
                  for( int _i5 = 0; _i5 < _list4.Count; ++_i5)
                  {
                    MeciDTO _elem6;
                    _elem6 = new MeciDTO();
                    _elem6.Read(iprot);
                    Success.Add(_elem6);
                  }
                  iprot.ReadListEnd();
                }
              } else { 
                TProtocolUtil.Skip(iprot, field.Type);
              }
              break;
            default: 
              TProtocolUtil.Skip(iprot, field.Type);
              break;
          }
          iprot.ReadFieldEnd();
        }
        iprot.ReadStructEnd();
      }
      finally
      {
        iprot.DecrementRecursionDepth();
      }
    }

    public void Write(TProtocol oprot) {
      oprot.IncrementRecursionDepth();
      try
      {
        TStruct struc = new TStruct("findAllMeci_result");
        oprot.WriteStructBegin(struc);
        TField field = new TField();

        if (this.__isset.success) {
          if (Success != null) {
            field.Name = "Success";
            field.Type = TType.List;
            field.ID = 0;
            oprot.WriteFieldBegin(field);
            {
              oprot.WriteListBegin(new TList(TType.Struct, Success.Count));
              foreach (MeciDTO _iter7 in Success)
              {
                _iter7.Write(oprot);
              }
              oprot.WriteListEnd();
            }
            oprot.WriteFieldEnd();
          }
        }
        oprot.WriteFieldStop();
        oprot.WriteStructEnd();
      }
      finally
      {
        oprot.DecrementRecursionDepth();
      }
    }

    public override string ToString() {
      StringBuilder __sb = new StringBuilder("findAllMeci_result(");
      bool __first = true;
      if (Success != null && __isset.success) {
        if(!__first) { __sb.Append(", "); }
        __first = false;
        __sb.Append("Success: ");
        __sb.Append(Success);
      }
      __sb.Append(")");
      return __sb.ToString();
    }

  }


  #if !SILVERLIGHT
  [Serializable]
  #endif
  public partial class ticketsSold_args : TBase
  {
    private MeciDTO _meci;
    private ClientDTO _loggedInClient;

    public MeciDTO Meci
    {
      get
      {
        return _meci;
      }
      set
      {
        __isset.meci = true;
        this._meci = value;
      }
    }

    public ClientDTO LoggedInClient
    {
      get
      {
        return _loggedInClient;
      }
      set
      {
        __isset.loggedInClient = true;
        this._loggedInClient = value;
      }
    }


    public Isset __isset;
    #if !SILVERLIGHT
    [Serializable]
    #endif
    public struct Isset {
      public bool meci;
      public bool loggedInClient;
    }

    public ticketsSold_args() {
    }

    public void Read (TProtocol iprot)
    {
      iprot.IncrementRecursionDepth();
      try
      {
        TField field;
        iprot.ReadStructBegin();
        while (true)
        {
          field = iprot.ReadFieldBegin();
          if (field.Type == TType.Stop) { 
            break;
          }
          switch (field.ID)
          {
            case 1:
              if (field.Type == TType.Struct) {
                Meci = new MeciDTO();
                Meci.Read(iprot);
              } else { 
                TProtocolUtil.Skip(iprot, field.Type);
              }
              break;
            case 2:
              if (field.Type == TType.Struct) {
                LoggedInClient = new ClientDTO();
                LoggedInClient.Read(iprot);
              } else { 
                TProtocolUtil.Skip(iprot, field.Type);
              }
              break;
            default: 
              TProtocolUtil.Skip(iprot, field.Type);
              break;
          }
          iprot.ReadFieldEnd();
        }
        iprot.ReadStructEnd();
      }
      finally
      {
        iprot.DecrementRecursionDepth();
      }
    }

    public void Write(TProtocol oprot) {
      oprot.IncrementRecursionDepth();
      try
      {
        TStruct struc = new TStruct("ticketsSold_args");
        oprot.WriteStructBegin(struc);
        TField field = new TField();
        if (Meci != null && __isset.meci) {
          field.Name = "meci";
          field.Type = TType.Struct;
          field.ID = 1;
          oprot.WriteFieldBegin(field);
          Meci.Write(oprot);
          oprot.WriteFieldEnd();
        }
        if (LoggedInClient != null && __isset.loggedInClient) {
          field.Name = "loggedInClient";
          field.Type = TType.Struct;
          field.ID = 2;
          oprot.WriteFieldBegin(field);
          LoggedInClient.Write(oprot);
          oprot.WriteFieldEnd();
        }
        oprot.WriteFieldStop();
        oprot.WriteStructEnd();
      }
      finally
      {
        oprot.DecrementRecursionDepth();
      }
    }

    public override string ToString() {
      StringBuilder __sb = new StringBuilder("ticketsSold_args(");
      bool __first = true;
      if (Meci != null && __isset.meci) {
        if(!__first) { __sb.Append(", "); }
        __first = false;
        __sb.Append("Meci: ");
        __sb.Append(Meci== null ? "<null>" : Meci.ToString());
      }
      if (LoggedInClient != null && __isset.loggedInClient) {
        if(!__first) { __sb.Append(", "); }
        __first = false;
        __sb.Append("LoggedInClient: ");
        __sb.Append(LoggedInClient== null ? "<null>" : LoggedInClient.ToString());
      }
      __sb.Append(")");
      return __sb.ToString();
    }

  }


  #if !SILVERLIGHT
  [Serializable]
  #endif
  public partial class ticketsSold_result : TBase
  {

    public ticketsSold_result() {
    }

    public void Read (TProtocol iprot)
    {
      iprot.IncrementRecursionDepth();
      try
      {
        TField field;
        iprot.ReadStructBegin();
        while (true)
        {
          field = iprot.ReadFieldBegin();
          if (field.Type == TType.Stop) { 
            break;
          }
          switch (field.ID)
          {
            default: 
              TProtocolUtil.Skip(iprot, field.Type);
              break;
          }
          iprot.ReadFieldEnd();
        }
        iprot.ReadStructEnd();
      }
      finally
      {
        iprot.DecrementRecursionDepth();
      }
    }

    public void Write(TProtocol oprot) {
      oprot.IncrementRecursionDepth();
      try
      {
        TStruct struc = new TStruct("ticketsSold_result");
        oprot.WriteStructBegin(struc);

        oprot.WriteFieldStop();
        oprot.WriteStructEnd();
      }
      finally
      {
        oprot.DecrementRecursionDepth();
      }
    }

    public override string ToString() {
      StringBuilder __sb = new StringBuilder("ticketsSold_result(");
      __sb.Append(")");
      return __sb.ToString();
    }

  }

}
