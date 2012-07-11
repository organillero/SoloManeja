/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/carlos/Documents/workspace/Solo Maneja/src/mx/ferreyra/solomaneja/service/Iservicio.aidl
 */
package mx.ferreyra.solomaneja.service;
public interface Iservicio extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements mx.ferreyra.solomaneja.service.Iservicio
{
private static final java.lang.String DESCRIPTOR = "mx.ferreyra.solomaneja.service.Iservicio";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an mx.ferreyra.solomaneja.service.Iservicio interface,
 * generating a proxy if needed.
 */
public static mx.ferreyra.solomaneja.service.Iservicio asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof mx.ferreyra.solomaneja.service.Iservicio))) {
return ((mx.ferreyra.solomaneja.service.Iservicio)iin);
}
return new mx.ferreyra.solomaneja.service.Iservicio.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_isRoute:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isRoute();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getCounterCrono:
{
data.enforceInterface(DESCRIPTOR);
long _result = this.getCounterCrono();
reply.writeNoException();
reply.writeLong(_result);
return true;
}
case TRANSACTION_startRoute:
{
data.enforceInterface(DESCRIPTOR);
this.startRoute();
reply.writeNoException();
return true;
}
case TRANSACTION_stopRoute:
{
data.enforceInterface(DESCRIPTOR);
this.stopRoute();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements mx.ferreyra.solomaneja.service.Iservicio
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public boolean isRoute() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isRoute, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public long getCounterCrono() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
long _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCounterCrono, _data, _reply, 0);
_reply.readException();
_result = _reply.readLong();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void startRoute() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startRoute, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void stopRoute() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopRoute, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_isRoute = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getCounterCrono = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_startRoute = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_stopRoute = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public boolean isRoute() throws android.os.RemoteException;
public long getCounterCrono() throws android.os.RemoteException;
public void startRoute() throws android.os.RemoteException;
public void stopRoute() throws android.os.RemoteException;
}
