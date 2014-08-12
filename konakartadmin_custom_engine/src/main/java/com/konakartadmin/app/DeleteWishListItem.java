package com.konakartadmin.app;

import com.konakartadmin.bl.KKAdmin;

/**
 *  The KonaKart Custom Engine - DeleteWishListItem - Generated by CreateKKAdminCustomEng
 */
@SuppressWarnings("all")
public class DeleteWishListItem
{
    KKAdmin kkAdminEng = null;

    /**
     * Constructor
     */
     public DeleteWishListItem(KKAdmin _kkAdminEng)
     {
         kkAdminEng = _kkAdminEng;
     }

     public void deleteWishListItem(String sessionId, int wishListItemId) throws KKAdminException
     {
         kkAdminEng.deleteWishListItem(sessionId, wishListItemId);
     }
}
