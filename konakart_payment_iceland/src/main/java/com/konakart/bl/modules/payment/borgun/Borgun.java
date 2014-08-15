package com.konakart.bl.modules.payment.borgun;

import com.konakart.app.*;
import com.konakart.appif.KKEngIf;
import com.konakart.bl.modules.BaseModule;
import com.konakart.bl.modules.ordertotal.OrderTotalMgr;
import com.konakart.bl.modules.payment.BasePaymentModule;
import com.konakart.bl.modules.payment.PaymentInfo;
import com.konakart.bl.modules.payment.PaymentInterface;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * Borgun module. This payment module allows for credit card credentials to be collected
 * directly from a KonaKart page. All communication to the Borgun server is done from the
 * KonaKart server. It uses the Advanced Integration Method (AIM).
 * <p/>
 * Note that the following variables need to be defined via the Merchant Interface:
 * <ul>
 * <li>Merchant eMail address</li>
 * <li>Whether a confirmation eMail should be sent to the customer</li>
 * <li>The interface version (3.0, 3.1 etc.)</li>
 * </ul>
 */
public class Borgun extends BasePaymentModule implements PaymentInterface {
    // Module name must be the same as the class name although it can be all in lowercase
    private static String code = "borgun";

    private static String bundleName = BaseModule.basePackage + ".payment.borgun.Borgun";

    private static HashMap<Locale, ResourceBundle> resourceBundleMap = new HashMap<Locale, ResourceBundle>();

    /**
     * Hash Map that contains the static data
     */
    private static Map<String, StaticData> staticDataHM = Collections
            .synchronizedMap(new HashMap<String, StaticData>());

    private static String mutex = "borgunMutex";

    // Configuration Keys

    /**
     * Used to put the gateway online / offline
     */
    private final static String MODULE_PAYMENT_BORGUN_STATUS = "MODULE_PAYMENT_BORGUN_STATUS";

    /**
     * The Borgun zone, if greater than zero, should reference a GeoZone. If the
     * DeliveryAddress of the order isn't within that GeoZone, then we throw an exception
     */
    private final static String MODULE_PAYMENT_BORGUN_ZONE = "MODULE_PAYMENT_BORGUN_ZONE";

    /**
     * The order for displaying this payment gateway on the UI
     */
    private final static String MODULE_PAYMENT_BORGUN_SORT_ORDER = "MODULE_PAYMENT_BORGUN_SORT_ORDER";

    /**
     * The Borgun Url used to POST the payment request.
     * "https://secure.authorize.net/gateway/transact.dll"
     */
    private final static String MODULE_PAYMENT_BORGUN_URL = "MODULE_PAYMENT_BORGUN_URL";

    /**
     * The Borgun API Login ID for this installation
     */
    private final static String MODULE_PAYMENT_BORGUN_USERNAME = "MODULE_PAYMENT_BORGUN_USERNAME";

    /**
     * The Borgun transaction key for this installation
     */
    private final static String MODULE_PAYMENT_BORGUN_PASSWORD = "MODULE_PAYMENT_BORGUN_PASSWORD";

    /**
     * Borgun security code options
     */
    private final static String MODULE_PAYMENT_BORGUN_SECURITY = "MODULE_PAYMENT_BORGUN_SECURITY";

    // Message Catalogue Keys
    private final static String MODULE_PAYMENT_BORGUN_TEXT_TITLE = "module.payment.borgun.text.title";

    private final static String MODULE_PAYMENT_BORGUN_TEXT_DESCRIPTION = "module.payment.borgun.text.description";

    /**
     * Constructor
     *
     * @param eng
     * @throws KKException
     */
    public Borgun(KKEngIf eng) throws KKException {
        super.init(eng);

        StaticData sd = staticDataHM.get(getStoreId());

        if (sd == null) {
            synchronized (mutex) {
                sd = staticDataHM.get(getStoreId());
                if (sd == null) {
                    setStaticVariables();
                }
            }
        }
    }

    /**
     * Sets some static variables during setup
     *
     * @throws KKException
     */
    public void setStaticVariables() throws KKException {
        KKConfiguration conf;
        StaticData staticData = staticDataHM.get(getStoreId());
        if (staticData == null) {
            staticData = new StaticData();
            staticDataHM.put(getStoreId(), staticData);
        }

        conf = getConfiguration(MODULE_PAYMENT_BORGUN_URL);
        if (conf == null) {
            throw new KKException(
                    "The Configuration MODULE_PAYMENT_BORGUN_URL must be set to the URL for"
                            + " sending the request to Borgun. (e.g. https://borgun.com/quick_link)");
        }
        staticData.setBorgunRequestUrl(conf.getValue());

        conf = getConfiguration(MODULE_PAYMENT_BORGUN_USERNAME);
        if (conf == null) {
            throw new KKException(
                    "The Configuration MODULE_PAYMENT_BORGUN_USERNAME must be set to the"
                            + " Borgun username for this installation");
        }
        staticData.setBorgunUsername(conf.getValue());

        conf = getConfiguration(MODULE_PAYMENT_BORGUN_PASSWORD);
        if (conf == null) {
            throw new KKException(
                    "The Configuration MODULE_PAYMENT_BORGUN_PASSWORD must be set to the"
                            + " Borgun password for this installation");
        }
        staticData.setBorgunPassword(conf.getValue());

        conf = getConfiguration(MODULE_PAYMENT_BORGUN_SECURITY);
        if (conf == null) {
            throw new KKException(
                    "The Configuration MODULE_PAYMENT_BORGUN_SECURITY must be set to the"
                            + " Borgun security code for this installation");
        }
        staticData.setBorgunSecurityCode(conf.getValue());

        conf = getConfiguration(MODULE_PAYMENT_BORGUN_ZONE);
        if (conf == null) {
            staticData.setZone(0);
        } else {
            staticData.setZone(new Integer(conf.getValue()).intValue());
        }

        conf = getConfiguration(MODULE_PAYMENT_BORGUN_SORT_ORDER);
        if (conf == null) {
            staticData.setSortOrder(0);
        } else {
            staticData.setSortOrder(new Integer(conf.getValue()).intValue());
        }
    }

    /**
     * Return a payment details object for Borgun IPN module
     *
     * @param order
     * @param info
     * @return Returns information in a PaymentDetails object
     * @throws Exception
     */
    public PaymentDetails getPaymentDetails(Order order, PaymentInfo info) throws Exception {
        StaticData sd = staticDataHM.get(getStoreId());
        /*
         * The BorgunZone zone, if greater than zero, should reference a GeoZone. If the
         * DeliveryAddress of the order isn't within that GeoZone, then we throw an exception
         */
        if (sd.getZone() > 0) {
            checkZone(info, sd.getZone());
        }

        // Get the scale for currency calculations
        int scale = new Integer(order.getCurrency().getDecimalPlaces()).intValue();

        // Get the resource bundle
        ResourceBundle rb = getResourceBundle(mutex, bundleName, resourceBundleMap, info
                .getLocale());
        if (rb == null) {
            throw new KKException("A resource file cannot be found for the country "
                    + info.getLocale().getCountry());
        }

        PaymentDetails pDetails = new PaymentDetails();
        pDetails.setCode(code);
        pDetails.setSortOrder(sd.getSortOrder());
        pDetails.setPaymentType(PaymentDetails.SERVER_PAYMENT_GATEWAY);
        pDetails.setDescription(rb.getString(MODULE_PAYMENT_BORGUN_TEXT_DESCRIPTION));
        pDetails.setTitle(rb.getString(MODULE_PAYMENT_BORGUN_TEXT_TITLE));

        // Return now if the full payment details aren't required. This happens when the manager
        // just wants a list of payment gateways to display in the UI.
        if (info.isReturnDetails() == false) {
            return pDetails;
        }

        pDetails.setPostOrGet("post");
        pDetails.setRequestUrl(sd.getBorgunRequestUrl());

        // Borgun requires details of the final price - inclusive of tax.
        BigDecimal total = null;
        for (int i = 0; i < order.getOrderTotals().length; i++) {
            OrderTotal ot = (OrderTotal) order.getOrderTotals()[i];
            if (ot.getClassName().equals(OrderTotalMgr.ot_total)) {
                total = ot.getValue().setScale(scale, BigDecimal.ROUND_HALF_UP);
            }
        }

        if (total == null) {
            throw new KKException("An Order Total was not found");
        }

        List<NameValue> parmList = new ArrayList<NameValue>();

        parmList.add(new NameValue("dc_logon", encode(sd.getBorgunUsername())));
        parmList.add(new NameValue("dc_password", encode(sd.getBorgunPassword())));
        parmList.add(new NameValue("dc_transaction_type", encode("AUTHORIZATION_CAPTURE")));
        parmList.add(new NameValue("dc_version", encode("1.2")));
        parmList.add(new NameValue("dc_transaction_amount", encode(total.toString())));

        if (sd.getBorgunSecurityCode() != null && sd.getBorgunSecurityCode().length() > 1) {
            parmList.add(new NameValue("dc_security", encode(sd.getBorgunSecurityCode())));
        }

        // Put the parameters into an array
        NameValue[] nvArray = new NameValue[parmList.size()];
        parmList.toArray(nvArray);
        pDetails.setParameters(nvArray);

        // Set the fields that should be visible in the UI when gathering Credit Card details
        pDetails.setShowAddr(true);
        pDetails.setShowCVV(true);
        pDetails.setShowPostcode(true);
        pDetails.setShowType(false); // Borgun doesn't require the card type
        pDetails.setShowOwner(true);


        if (log.isDebugEnabled()) {
            log.debug(pDetails.toString());
        }

        return pDetails;
    }

    /**
     * Returns true or false
     *
     * @throws KKException
     */
    public boolean isAvailable() throws KKException {
        return isAvailable(MODULE_PAYMENT_BORGUN_STATUS);
    }

    /**
     * URL-Encodes a value
     *
     * @param value Value to be URL-encoded
     * @return URL-encoded value
     */
    private String encode(String value) {
        try {
            // return URL encoded string
            if (value != null && value.length() > 1) {
                return URLEncoder.encode(value, "UTF-8");
            }
        } catch (Exception e) {
            log.warn("Error URL-encoding '" + value + "' : " + e);
        }
        return "";
    }

    /**
     * Used to store the static data of this module
     */
    protected class StaticData {
        private int sortOrder = -1;

        // The Borgun URL used to POST the payment request.
        private String borgunRequestUrl;

        // Username
        private String borgunUsername;

        // Password
        private String borgunPassword;

        // Security Code Options
        private String borgunSecurityCode;

        // zone where Borgun will be made available
        private int zone;

        /**
         * @return the sortOrder
         */
        public int getSortOrder() {
            return sortOrder;
        }

        /**
         * @param sortOrder the sortOrder to set
         */
        public void setSortOrder(int sortOrder) {
            this.sortOrder = sortOrder;
        }

        /**
         * @return the borgunRequestUrl
         */
        public String getBorgunRequestUrl() {
            return borgunRequestUrl;
        }

        /**
         * @param borgunRequestUrl the borgunRequestUrl to set
         */
        public void setBorgunRequestUrl(String borgunRequestUrl) {
            this.borgunRequestUrl = borgunRequestUrl;
        }

        /**
         * @return the borgunUsername
         */
        public String getBorgunUsername() {
            return borgunUsername;
        }

        /**
         * @param borgunUsername the borgunUsername to set
         */
        public void setBorgunUsername(String borgunUsername) {
            this.borgunUsername = borgunUsername;
        }

        /**
         * @return the borgunPassword
         */
        public String getBorgunPassword() {
            return borgunPassword;
        }

        /**
         * @param borgunPassword the borgunPassword to set
         */
        public void setBorgunPassword(String borgunPassword) {
            this.borgunPassword = borgunPassword;
        }

        /**
         * @return the borgunSecurityCode
         */
        public String getBorgunSecurityCode() {
            return borgunSecurityCode;
        }

        /**
         * @param borgunSecurityCode the borgunSecurityCode to set
         */
        public void setBorgunSecurityCode(String borgunSecurityCode) {
            this.borgunSecurityCode = borgunSecurityCode;
        }

        /**
         * @return the zone
         */
        public int getZone() {
            return zone;
        }

        /**
         * @param zone the zone to set
         */
        public void setZone(int zone) {
            this.zone = zone;
        }

    }
}