package com.konakart.app;

/**
 *  The KonaKart Custom Engine - DoesCustomerExistForEmail - Generated by CreateKKCustomEng
 */
@SuppressWarnings("all")
public class DoesCustomerExistForEmail
{
    KKEng kkEng = null;

    /**
     * Constructor
     */
     public DoesCustomerExistForEmail(KKEng _kkEng)
     {
         kkEng = _kkEng;
     }

     public boolean doesCustomerExistForEmail(String emailAddr) throws KKException
     {
         return kkEng.doesCustomerExistForEmail(emailAddr);
     }
}
