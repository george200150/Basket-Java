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


#if !SILVERLIGHT
[Serializable]
#endif
public partial class MeciDTO : TBase
{
  private string _id;
  private string _home;
  private string _away;
  private long _date;
  private TipMeciDTO _tip;
  private int _numarBileteDisponibile;

  public string Id
  {
    get
    {
      return _id;
    }
    set
    {
      __isset.id = true;
      this._id = value;
    }
  }

  public string Home
  {
    get
    {
      return _home;
    }
    set
    {
      __isset.home = true;
      this._home = value;
    }
  }

  public string Away
  {
    get
    {
      return _away;
    }
    set
    {
      __isset.away = true;
      this._away = value;
    }
  }

  public long Date
  {
    get
    {
      return _date;
    }
    set
    {
      __isset.date = true;
      this._date = value;
    }
  }

  /// <summary>
  /// 
  /// <seealso cref="TipMeciDTO"/>
  /// </summary>
  public TipMeciDTO Tip
  {
    get
    {
      return _tip;
    }
    set
    {
      __isset.tip = true;
      this._tip = value;
    }
  }

  public int NumarBileteDisponibile
  {
    get
    {
      return _numarBileteDisponibile;
    }
    set
    {
      __isset.numarBileteDisponibile = true;
      this._numarBileteDisponibile = value;
    }
  }


  public Isset __isset;
  #if !SILVERLIGHT
  [Serializable]
  #endif
  public struct Isset {
    public bool id;
    public bool home;
    public bool away;
    public bool date;
    public bool tip;
    public bool numarBileteDisponibile;
  }

  public MeciDTO() {
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
              Id = iprot.ReadString();
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 2:
            if (field.Type == TType.String) {
              Home = iprot.ReadString();
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 3:
            if (field.Type == TType.String) {
              Away = iprot.ReadString();
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 4:
            if (field.Type == TType.I64) {
              Date = iprot.ReadI64();
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 5:
            if (field.Type == TType.I32) {
              Tip = (TipMeciDTO)iprot.ReadI32();
            } else { 
              TProtocolUtil.Skip(iprot, field.Type);
            }
            break;
          case 6:
            if (field.Type == TType.I32) {
              NumarBileteDisponibile = iprot.ReadI32();
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
      TStruct struc = new TStruct("MeciDTO");
      oprot.WriteStructBegin(struc);
      TField field = new TField();
      if (Id != null && __isset.id) {
        field.Name = "id";
        field.Type = TType.String;
        field.ID = 1;
        oprot.WriteFieldBegin(field);
        oprot.WriteString(Id);
        oprot.WriteFieldEnd();
      }
      if (Home != null && __isset.home) {
        field.Name = "home";
        field.Type = TType.String;
        field.ID = 2;
        oprot.WriteFieldBegin(field);
        oprot.WriteString(Home);
        oprot.WriteFieldEnd();
      }
      if (Away != null && __isset.away) {
        field.Name = "away";
        field.Type = TType.String;
        field.ID = 3;
        oprot.WriteFieldBegin(field);
        oprot.WriteString(Away);
        oprot.WriteFieldEnd();
      }
      if (__isset.date) {
        field.Name = "date";
        field.Type = TType.I64;
        field.ID = 4;
        oprot.WriteFieldBegin(field);
        oprot.WriteI64(Date);
        oprot.WriteFieldEnd();
      }
      if (__isset.tip) {
        field.Name = "tip";
        field.Type = TType.I32;
        field.ID = 5;
        oprot.WriteFieldBegin(field);
        oprot.WriteI32((int)Tip);
        oprot.WriteFieldEnd();
      }
      if (__isset.numarBileteDisponibile) {
        field.Name = "numarBileteDisponibile";
        field.Type = TType.I32;
        field.ID = 6;
        oprot.WriteFieldBegin(field);
        oprot.WriteI32(NumarBileteDisponibile);
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
    StringBuilder __sb = new StringBuilder("MeciDTO(");
    bool __first = true;
    if (Id != null && __isset.id) {
      if(!__first) { __sb.Append(", "); }
      __first = false;
      __sb.Append("Id: ");
      __sb.Append(Id);
    }
    if (Home != null && __isset.home) {
      if(!__first) { __sb.Append(", "); }
      __first = false;
      __sb.Append("Home: ");
      __sb.Append(Home);
    }
    if (Away != null && __isset.away) {
      if(!__first) { __sb.Append(", "); }
      __first = false;
      __sb.Append("Away: ");
      __sb.Append(Away);
    }
    if (__isset.date) {
      if(!__first) { __sb.Append(", "); }
      __first = false;
      __sb.Append("Date: ");
      __sb.Append(Date);
    }
    if (__isset.tip) {
      if(!__first) { __sb.Append(", "); }
      __first = false;
      __sb.Append("Tip: ");
      __sb.Append(Tip);
    }
    if (__isset.numarBileteDisponibile) {
      if(!__first) { __sb.Append(", "); }
      __first = false;
      __sb.Append("NumarBileteDisponibile: ");
      __sb.Append(NumarBileteDisponibile);
    }
    __sb.Append(")");
    return __sb.ToString();
  }

}

