package com.konakartadmin.app;

import com.konakartadmin.appif.*;
import com.konakartadmin.bl.KKAdmin;

/**
 *  The KonaKart Custom Engine - GetPromotionsCount - Generated by CreateKKAdminCustomEng
 */
@SuppressWarnings("all")
public class GetPromotionsCount
{
    KKAdmin kkAdminEng = null;

    /**
     * Constructor
     */
     public GetPromotionsCount(KKAdmin _kkAdminEng)
     {
         kkAdminEng = _kkAdminEng;
     }

     public int getPromotionsCount(String sessionId, AdminPromotionSearch search) throws KKAdminException
     {
         return kkAdminEng.getPromotionsCount(sessionId, search);
     }
}
