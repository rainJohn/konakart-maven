package com.konakartadmin.app;

import com.konakartadmin.appif.*;
import com.konakartadmin.bl.KKAdmin;

/**
 *  The KonaKart Custom Engine - InsertCouponForPromotion - Generated by CreateKKAdminCustomEng
 */
@SuppressWarnings("all")
public class InsertCouponForPromotion
{
    KKAdmin kkAdminEng = null;

    /**
     * Constructor
     */
     public InsertCouponForPromotion(KKAdmin _kkAdminEng)
     {
         kkAdminEng = _kkAdminEng;
     }

     public int insertCouponForPromotion(String sessionId, AdminCoupon coupon, int promotionId) throws KKAdminException
     {
         return kkAdminEng.insertCouponForPromotion(sessionId, coupon, promotionId);
     }
}
