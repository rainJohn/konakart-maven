package com.konakartadmin.app;

import com.konakartadmin.appif.*;
import com.konakartadmin.bl.KKAdmin;

/**
 *  The KonaKart Custom Engine - GetGiftCertificatesPerPromotion - Generated by CreateKKAdminCustomEng
 */
@SuppressWarnings("all")
public class GetGiftCertificatesPerPromotion
{
    KKAdmin kkAdminEng = null;

    /**
     * Constructor
     */
     public GetGiftCertificatesPerPromotion(KKAdmin _kkAdminEng)
     {
         kkAdminEng = _kkAdminEng;
     }

     public AdminProducts getGiftCertificatesPerPromotion(String sessionId, AdminProductSearch search, int offset, int size) throws KKAdminException
     {
         return kkAdminEng.getGiftCertificatesPerPromotion(sessionId, search, offset, size);
     }
}
