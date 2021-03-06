package com.konakartadmin.app;

import com.konakartadmin.appif.*;
import com.konakartadmin.bl.KKAdmin;

/**
 *  The KonaKart Custom Engine - GetProductOptions - Generated by CreateKKAdminCustomEng
 */
@SuppressWarnings("all")
public class GetProductOptions
{
    KKAdmin kkAdminEng = null;

    /**
     * Constructor
     */
     public GetProductOptions(KKAdmin _kkAdminEng)
     {
         kkAdminEng = _kkAdminEng;
     }

     public AdminProductOptionSearchResult getProductOptions(String sessionId, AdminProductOptionSearch search, int offset, int size) throws KKAdminException
     {
         return kkAdminEng.getProductOptions(sessionId, search, offset, size);
     }
}
