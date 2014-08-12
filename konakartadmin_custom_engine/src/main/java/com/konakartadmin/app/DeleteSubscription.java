package com.konakartadmin.app;

import com.konakartadmin.bl.KKAdmin;

/**
 *  The KonaKart Custom Engine - DeleteSubscription - Generated by CreateKKAdminCustomEng
 */
@SuppressWarnings("all")
public class DeleteSubscription
{
    KKAdmin kkAdminEng = null;

    /**
     * Constructor
     */
     public DeleteSubscription(KKAdmin _kkAdminEng)
     {
         kkAdminEng = _kkAdminEng;
     }

     public int deleteSubscription(String sessionId, int id) throws KKAdminException
     {
         return kkAdminEng.deleteSubscription(sessionId, id);
     }
}
