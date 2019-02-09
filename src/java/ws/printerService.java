/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import db.Status;
import db.Transactions;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author FAZOUL
 */
@WebService(serviceName = "printerService")
public class printerService {

    @PersistenceContext(unitName = "printerServerPU")
    private EntityManager em;
    @Resource
    private UserTransaction utx;

    @WebMethod(operationName = "hello")
    public String hello() {
        StringBuilder br = new StringBuilder();
        List<Transactions> transactionsList = new ArrayList<>();
        try {
            transactionsList = em.createQuery("select t from Transactions t where t.statusID.idstatus = '1'").getResultList();
            for (Transactions t : transactionsList) {
                br.append("                            " + t.getOutletID().getAddress() + "                             " + t.getCreatedOn() + "                             " + t.getAmount() + "                           " + "=====================                                " + t.getMpesastatement());
                t.setStatusID(new Status(0));
                utx.begin();
                em.merge(t);
                utx.commit();
            }
        } catch (Exception ex) {
            br.append(ex.getMessage());
        }
        return br.toString();
    }
}
