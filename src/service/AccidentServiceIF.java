package service;

import domain.Accident;
import enumeration.accident.AccidentStatus;
import exception.EmptyListException;
import exception.NoDataException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface AccidentServiceIF  extends Remote{
    ArrayList<Accident> getAccidentList(AccidentStatus status) throws RemoteException, EmptyListException;

    ArrayList<Accident> getAccidentList() throws RemoteException, EmptyListException;

    Accident getAccident(int id) throws RemoteException, NoDataException;

    int reportAccident(Accident accident) throws RemoteException;

    boolean setStatus(int accidentId, AccidentStatus status) throws RemoteException;
}
