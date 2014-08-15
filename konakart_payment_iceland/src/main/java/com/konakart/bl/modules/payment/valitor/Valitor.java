//
// (c) 2006 DS Data Systems UK Ltd, All rights reserved.
//
// DS Data Systems and KonaKart and their respective logos, are 
// trademarks of DS Data Systems UK Ltd. All rights reserved.
//
// The information in this document is free software; you can redistribute 
// it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This software is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package com.konakart.bl.modules.payment.valitor;

import com.konakart.app.*;
import com.konakart.appif.KKEngIf;
import com.konakart.appif.SSOTokenIf;
import com.konakart.bl.modules.BaseModule;
import com.konakart.bl.modules.ordertotal.OrderTotalMgr;
import com.konakart.bl.modules.payment.BasePaymentModule;
import com.konakart.bl.modules.payment.PaymentInfo;
import com.konakart.bl.modules.payment.PaymentInterface;
import com.konakart.util.Utils;

import java.math.BigDecimal;
import java.util.*;

/**
 * valitor IPN module
 */
public class Valitor extends BasePaymentModule implements PaymentInterface
{
    // Module name must be the same as the class name although it can be all in lowercase
    private static String code = "valitor";

    private static String bundleName = BaseModule.basePackage + ".payment.valitor.valitor";

    private static HashMap<Locale, ResourceBundle> resourceBundleMap = new HashMap<Locale, ResourceBundle>();

    /** Hash Map that contains the static data */
    private static Map<String, StaticData> staticDataHM = Collections
            .synchronizedMap(new HashMap<String, StaticData>());

    private static String mutex = "valitorMutex";

    private static final String hostPortSubstitute = "host:port";

    // Configuration Keys

    private final static String MODULE_PAYMENT_valitor_STATUS = "MODULE_PAYMENT_valitor_STATUS";

    private final static String MODULE_PAYMENT_valitor_ID = "MODULE_PAYMENT_valitor_ID";

    private final static String MODULE_PAYMENT_valitor_ZONE = "MODULE_PAYMENT_valitor_ZONE";

    private final static String MODULE_PAYMENT_valitor_ORDER_STATUS_ID = "MODULE_PAYMENT_valitor_ORDER_STATUS_ID";

    private final static String MODULE_PAYMENT_valitor_SORT_ORDER = "MODULE_PAYMENT_valitor_SORT_ORDER";

    /**
     * Username and password used to log into the engine by the IPN call from valitor
     */
    private final static String MODULE_PAYMENT_valitor_CALLBACK_USERNAME = "MODULE_PAYMENT_valitor_CALLBACK_USERNAME";

    private final static String MODULE_PAYMENT_valitor_CALLBACK_PASSWORD = "MODULE_PAYMENT_valitor_CALLBACK_PASSWORD";

    /**
     * This URL is used by the valitor IPN functionality to call back into the application with the
     * results of the payment transaction. It must be a URl that is visible from the internet.
     */
    private final static String MODULE_PAYMENT_valitor_CALLBACK_URL = "MODULE_PAYMENT_valitor_CALLBACK_URL";

    /**
     * This URL is used by valitor to redirect the user's browser when returning from the payment
     * gateway. If it is in the form http://host:port/konakart/CheckoutFinished.do, then the string
     * host:port is substituted automatically with the correct value.
     */
    private final static String MODULE_PAYMENT_valitor_RETURN_URL = "MODULE_PAYMENT_valitor_RETURN_URL";

    /**
     * This URL is used by valitor to redirect the user's browser when returning from the payment
     * gateway after cancelling out of the operation. If it is in the form
     * http://host:port/konakart/CatalogCheckoutExternalPaymentErrorPage.do, then the string
     * host:port is substituted automatically with the correct value.
     */
    private final static String MODULE_PAYMENT_valitor_CANCEL_URL = "MODULE_PAYMENT_valitor_CANCEL_URL";

    /**
     * If set to true, the module will use the valitor sandbox. Otherwise the live URL will be used.
     */
    private final static String MODULE_PAYMENT_valitor_TEST_MODE = "MODULE_PAYMENT_valitor_TEST_MODE";

    // Message Catalogue Keys

    private final static String MODULE_PAYMENT_valitor_TEXT_TITLE = "module.payment.valitor.text.title";

    private final static String MODULE_PAYMENT_valitor_TEXT_DESCRIPTION = "module.payment.valitor.text.description";

    /**
     * Constructor
     * 
     * @param eng
     * 
     * @throws com.konakart.app.KKException
     */
    public Valitor(KKEngIf eng) throws KKException
    {
        super.init(eng);

        StaticData sd = staticDataHM.get(getStoreId());

        if (sd == null)
        {
            synchronized (mutex)
            {
                sd = staticDataHM.get(getStoreId());
                if (sd == null)
                {
                    setStaticVariables();
                }
            }
        }
    }

    /**
     * Sets some static variables during setup
     * 
     * @throws com.konakart.app.KKException
     * 
     */
    public void setStaticVariables() throws KKException
    {
        KKConfiguration conf;
        StaticData staticData = staticDataHM.get(getStoreId());
        if (staticData == null)
        {
            staticData = new StaticData();
            staticDataHM.put(getStoreId(), staticData);
        }

        conf = getConfiguration(MODULE_PAYMENT_valitor_ID);
        if (conf == null)
        {
            throw new KKException(
                    "The Configuration MODULE_PAYMENT_valitor_ID must be set to the valitor Id of the merchant.");
        }
        staticData.setvalitorId(conf.getValue());

        conf = getConfiguration(MODULE_PAYMENT_valitor_CALLBACK_URL);
        if (conf == null)
        {
            throw new KKException(
                    "The Configuration MODULE_PAYMENT_valitor_CALLBACK must be set to the Callback Url for the"
                            + " IPN functionality (i.e. https://myhost/konakart/valitorCallback.do).");
        }
        staticData.setvalitorCallbackUrl(conf.getValue());

        conf = getConfiguration(MODULE_PAYMENT_valitor_CALLBACK_USERNAME);
        if (conf == null)
        {
            throw new KKException(
                    "The Configuration MODULE_PAYMENT_valitor_CALLBACK_USERNAME must be set to the Callback Username for the"
                            + " IPN functionality.");
        }
        staticData.setvalitorCallbackUsername(conf.getValue());

        conf = getConfiguration(MODULE_PAYMENT_valitor_CALLBACK_PASSWORD);
        if (conf == null)
        {
            throw new KKException(
                    "The Configuration MODULE_PAYMENT_valitor_CALLBACK must be set to the Callback Password for the"
                            + " IPN functionality.");
        }
        staticData.setvalitorCallbackPassword(conf.getValue());

        conf = getConfiguration(MODULE_PAYMENT_valitor_RETURN_URL);
        if (conf == null)
        {
            throw new KKException(
                    "The Configuration MODULE_PAYMENT_valitor_RETURN_URL must be set to the return URL for"
                            + " when the customer leaves the payment gateway. (i.e. http://{host:port}/konakart/CheckoutFinished.do)");
        }
        staticData.setvalitorReturnUrl(conf.getValue());

        conf = getConfiguration(MODULE_PAYMENT_valitor_CANCEL_URL);
        if (conf == null)
        {
            throw new KKException(
                    "The Configuration MODULE_PAYMENT_valitor_CANCEL_URL must be set to the return URL for"
                            + " when the customer leaves the payment gateway by cancelling the operation. (i.e. http://{host:port}/konakart/CatalogCheckoutExternalPaymentErrorPage.do)");
        }
        staticData.setvalitorCancelUrl(conf.getValue());

        conf = getConfiguration(MODULE_PAYMENT_valitor_ZONE);
        if (conf == null)
        {
            staticData.setZone(0);
        } else
        {
            staticData.setZone(new Integer(conf.getValue()).intValue());
        }

        conf = getConfiguration(MODULE_PAYMENT_valitor_SORT_ORDER);
        if (conf == null)
        {
            staticData.setSortOrder(0);
        } else
        {
            staticData.setSortOrder(new Integer(conf.getValue()).intValue());
        }

        conf = getConfiguration(MODULE_PAYMENT_valitor_ORDER_STATUS_ID);
        if (conf == null)
        {
            staticData.setOrderStatusId(0);
        } else
        {
            staticData.setOrderStatusId(new Integer(conf.getValue()).intValue());
        }

        conf = getConfiguration(MODULE_PAYMENT_valitor_TEST_MODE);
        if (conf == null)
        {
            staticData.setvalitorTestMode(true);
        } else
        {
            if (conf.getValue().equalsIgnoreCase("false"))
            {
                staticData.setvalitorTestMode(false);
            } else
            {
                staticData.setvalitorTestMode(true);
            }
        }
    }

    /**
     * Return a payment details object for valitor IPN module
     * 
     * @param order
     * @param info
     * @return Returns information in a PaymentDetails object
     * @throws Exception
     */
    public PaymentDetails getPaymentDetails(Order order, PaymentInfo info) throws Exception
    {
        StaticData sd = staticDataHM.get(getStoreId());

        /*
         * The valitorZone zone, if greater than zero, should reference a GeoZone. If the
         * DeliveryAddress of the order isn't within that GeoZone, then we throw an exception
         */
        if (sd.getZone() > 0)
        {
            checkZone(info, sd.getZone());
        }

        // Get the scale for currency calculations
        int scale = new Integer(order.getCurrency().getDecimalPlaces()).intValue();

        // Get the resource bundle
        ResourceBundle rb = getResourceBundle(mutex, bundleName, resourceBundleMap,
                info.getLocale());
        if (rb == null)
        {
            throw new KKException("A resource file cannot be found for the country "
                    + info.getLocale().getCountry());
        }

        PaymentDetails pDetails = new PaymentDetails();
        pDetails.setCode(code);
        pDetails.setSortOrder(sd.getSortOrder());
        pDetails.setOrderStatusId(sd.getOrderStatusId());
        pDetails.setPaymentType(PaymentDetails.BROWSER_PAYMENT_GATEWAY);
        pDetails.setDescription(rb.getString(MODULE_PAYMENT_valitor_TEXT_DESCRIPTION));
        pDetails.setTitle(rb.getString(MODULE_PAYMENT_valitor_TEXT_TITLE));

        // Return now if the full payment details aren't required
        if (info.isReturnDetails() == false)
        {
            return pDetails;
        }

        pDetails.setPostOrGet("post");
        if (sd.isvalitorTestMode())
        {
            pDetails.setRequestUrl("https://www.sandbox.valitor.com/cgi-bin/webscr");
        } else
        {
            pDetails.setRequestUrl("https://www.valitor.com/cgi-bin/webscr");
        }

        List<NameValue> parmList = new ArrayList<NameValue>();
        parmList.add(new NameValue("cmd", "_xclick"));
        parmList.add(new NameValue("item_name", "Order #" + order.getId() + " from "
                + info.getStoreName()));

        BigDecimal total = null;
        for (int i = 0; i < order.getOrderTotals().length; i++)
        {
            OrderTotal ot = (OrderTotal) order.getOrderTotals()[i];
            if (ot.getClassName().equals(OrderTotalMgr.ot_total))
            {
                total = ot.getValue().setScale(scale, BigDecimal.ROUND_HALF_UP);
            }
        }

        if (total == null)
        {
            throw new KKException(
                    "An Order Total was not found so the payment could not be processed through valitor.");
        }

        /*
         * Create a session here which will be used by the IPN callback
         */
        SSOTokenIf ssoToken = new SSOToken();
        String sessionId = getEng().login(sd.getvalitorCallbackUsername(),
                sd.getvalitorCallbackPassword());
        if (sessionId == null)
        {
            throw new KKException(
                    "Unable to log into the engine using the valitor Callback Username and Password");
        }
        ssoToken.setSessionId(sessionId);
        ssoToken.setCustom1(String.valueOf(order.getId()));
        /*
         * Save the SSOToken with a valid sessionId and the order id in custom1
         */
        String uuid = getEng().saveSSOToken(ssoToken);

        parmList.add(new NameValue("amount", total.toString()));
        parmList.add(new NameValue("business", sd.getvalitorId()));
        parmList.add(new NameValue("currency_code", order.getCurrencyCode()));
        parmList.add(new NameValue("custom", uuid));
        //parmList.add(new NameValue("no_shipping", "2"));
        parmList.add(new NameValue("no_note", "1"));
        parmList.add(new NameValue("notify_url", sd.getvalitorCallbackUrl()));

        sd.setvalitorReturnUrl(sd.getvalitorReturnUrl().replaceFirst(hostPortSubstitute,
                info.getHostAndPort()));
        sd.setvalitorCancelUrl(sd.getvalitorCancelUrl().replaceFirst(hostPortSubstitute,
                info.getHostAndPort()));
        parmList.add(new NameValue("return", sd.getvalitorReturnUrl()));
        parmList.add(new NameValue("cancel_return", sd.getvalitorCancelUrl()));

        // Added for anti-fraud operations

        // Set the billing name. If the name field consists of more than two strings, we take the
        // last one as being the surname.
        // String bName = order.getBillingName();
        // if (bName != null)
        // {
        // String[] names = splitNameIntoFirstAndLastNames(bName);
        // parmList.add(new NameValue("first_name", names[0]));
        // parmList.add(new NameValue("last_name", names[1]));
        // }

        // parmList.add(new NameValue("address1", order.getBillingStreetAddress()));
        // parmList.add(new NameValue("address2", order.getBillingStreetAddress1()));
        // parmList.add(new NameValue("city", order.getBillingCity()));
        // parmList.add(new NameValue("state", order.getBillingState()));
        // parmList.add(new NameValue("country", order.getBillingCountry()));
        // parmList.add(new NameValue("zip", order.getBillingPostcode()));

        // Set the delivery names from the delivery address Id

        String[] dNames = getFirstAndLastNamesFromAddress(order.getDeliveryAddrId());
        if (dNames != null)
        {
            parmList.add(new NameValue("first_name", dNames[0]));
            parmList.add(new NameValue("last_name", dNames[1]));
        }

        parmList.add(new NameValue("address1", order.getDeliveryStreetAddress()));
        if (!Utils.isBlank(order.getDeliveryStreetAddress1()))
        {
            parmList.add(new NameValue("address2", order.getDeliveryStreetAddress1()));
        }
        parmList.add(new NameValue("city", order.getDeliveryCity()));
        parmList.add(new NameValue("state", order.getDeliveryState()));
        
        String iso2Country = getISO2CountryCodeFromCountryString(order.getDeliveryCountry());
        if (iso2Country != null)
        {
            parmList.add(new NameValue("country", iso2Country));
        }
        parmList.add(new NameValue("zip", order.getDeliveryPostcode()));

        parmList.add(new NameValue("address_override", "1"));

        // Put the parameters into an array
        NameValue[] nvArray = new NameValue[parmList.size()];
        parmList.toArray(nvArray);
        pDetails.setParameters(nvArray);

        if (log.isDebugEnabled())
        {
            log.debug(pDetails.toString());
        }

        return pDetails;
    }

    private String getISO2CountryCodeFromCountryString(String country)
    {
        try
        {  
            Country ctry = getTaxMgr().getCountryPerName(country);

            if (ctry == null)
            {
                return null;
            }

            return ctry.getIsoCode2();
        } catch (Exception e)
        {
            // We just ignore this for now
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Returns true or false
     * 
     * @throws com.konakart.app.KKException
     */
    public boolean isAvailable() throws KKException
    {
        return isAvailable(MODULE_PAYMENT_valitor_STATUS);
    }

    /**
     * Used to store the static data of this module
     */
    protected class StaticData
    {
        private int sortOrder = -1;

        private String valitorId;

        private String valitorCallbackUrl;

        private String valitorCallbackUsername;

        private String valitorCallbackPassword;

        private String valitorReturnUrl;

        private String valitorCancelUrl;

        private boolean valitorTestMode;

        private int zone;

        private int orderStatusId;

        /**
         * @return the sortOrder
         */
        public int getSortOrder()
        {
            return sortOrder;
        }

        /**
         * @param sortOrder
         *            the sortOrder to set
         */
        public void setSortOrder(int sortOrder)
        {
            this.sortOrder = sortOrder;
        }

        /**
         * @return the valitorId
         */
        public String getvalitorId()
        {
            return valitorId;
        }

        /**
         * @param valitorId
         *            the valitorId to set
         */
        public void setvalitorId(String valitorId)
        {
            this.valitorId = valitorId;
        }

        /**
         * @return the valitorCallbackUrl
         */
        public String getvalitorCallbackUrl()
        {
            return valitorCallbackUrl;
        }

        /**
         * @param valitorCallbackUrl
         *            the valitorCallbackUrl to set
         */
        public void setvalitorCallbackUrl(String valitorCallbackUrl)
        {
            this.valitorCallbackUrl = valitorCallbackUrl;
        }

        /**
         * @return the valitorReturnUrl
         */
        public String getvalitorReturnUrl()
        {
            return valitorReturnUrl;
        }

        /**
         * @param valitorReturnUrl
         *            the valitorReturnUrl to set
         */
        public void setvalitorReturnUrl(String valitorReturnUrl)
        {
            this.valitorReturnUrl = valitorReturnUrl;
        }

        /**
         * @return the valitorCancelUrl
         */
        public String getvalitorCancelUrl()
        {
            return valitorCancelUrl;
        }

        /**
         * @param valitorCancelUrl
         *            the valitorCancelUrl to set
         */
        public void setvalitorCancelUrl(String valitorCancelUrl)
        {
            this.valitorCancelUrl = valitorCancelUrl;
        }

        /**
         * @return the valitorTestMode
         */
        public boolean isvalitorTestMode()
        {
            return valitorTestMode;
        }

        /**
         * @param valitorTestMode
         *            the valitorTestMode to set
         */
        public void setvalitorTestMode(boolean valitorTestMode)
        {
            this.valitorTestMode = valitorTestMode;
        }

        /**
         * @return the zone
         */
        public int getZone()
        {
            return zone;
        }

        /**
         * @param zone
         *            the zone to set
         */
        public void setZone(int zone)
        {
            this.zone = zone;
        }

        /**
         * @return the orderStatusId
         */
        public int getOrderStatusId()
        {
            return orderStatusId;
        }

        /**
         * @param orderStatusId
         *            the orderStatusId to set
         */
        public void setOrderStatusId(int orderStatusId)
        {
            this.orderStatusId = orderStatusId;
        }

        /**
         * @return the valitorCallbackUsername
         */
        public String getvalitorCallbackUsername()
        {
            return valitorCallbackUsername;
        }

        /**
         * @param valitorCallbackUsername
         *            the valitorCallbackUsername to set
         */
        public void setvalitorCallbackUsername(String valitorCallbackUsername)
        {
            this.valitorCallbackUsername = valitorCallbackUsername;
        }

        /**
         * @return the valitorCallbackPassword
         */
        public String getvalitorCallbackPassword()
        {
            return valitorCallbackPassword;
        }

        /**
         * @param valitorCallbackPassword
         *            the valitorCallbackPassword to set
         */
        public void setvalitorCallbackPassword(String valitorCallbackPassword)
        {
            this.valitorCallbackPassword = valitorCallbackPassword;
        }
    }
}
