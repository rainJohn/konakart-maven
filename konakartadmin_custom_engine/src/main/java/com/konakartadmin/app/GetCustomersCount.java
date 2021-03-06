package com.konakartadmin.app;

import com.konakartadmin.appif.*;
import com.konakartadmin.bl.KKAdmin;

/**
 *  The KonaKart Custom Engine - GetCustomersCount - Generated by CreateKKAdminCustomEng
 */
@SuppressWarnings("all")
public class GetCustomersCount
{
    KKAdmin kkAdminEng = null;

    /**
     * Constructor
     */
     public GetCustomersCount(KKAdmin _kkAdminEng)
     {
         kkAdminEng = _kkAdminEng;
     }

     public int getCustomersCount(String sessionId, AdminCustomerSearch search) throws KKAdminException
     {
         return kkAdminEng.getCustomersCount(sessionId, search);
     }
}
