package com.konakartadmin.app;

import com.konakartadmin.appif.*;
import com.konakartadmin.bl.KKAdmin;

/**
 *  The KonaKart Custom Engine - UpdateMiscPrices - Generated by CreateKKAdminCustomEng
 */
@SuppressWarnings("all")
public class UpdateMiscPrices
{
    KKAdmin kkAdminEng = null;

    /**
     * Constructor
     */
     public UpdateMiscPrices(KKAdmin _kkAdminEng)
     {
         kkAdminEng = _kkAdminEng;
     }

     public void updateMiscPrices(String sessionId, AdminMiscPrice[] miscPrices) throws KKAdminException
     {
         kkAdminEng.updateMiscPrices(sessionId, miscPrices);
     }
}
