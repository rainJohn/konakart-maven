package is.tmsoftware;

import com.konakart.app.KKException;
import com.konakart.appif.ProductIf;
import com.konakartadmin.app.AdminCategory;
import com.konakartadmin.app.AdminProduct;
import com.konakartadmin.app.AdminProductAttribute;
import com.konakartadmin.app.AdminProductDescription;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author bjarnig
 */
public class UpdateProduct extends BaseApi
{
    public static void main(String[] args) {
        try {

            init(getEngineMode(), getStoreId(), getEngClassName(), isCustomersShared(),
                    isProductsShared(), isCategoriesShared(), null);

            AdminProduct prod = getAdminProduct();
            AdminProductDescription[] descriptions = getAdminProductDescriptions();

            prod.setDescriptions(descriptions);

            AdminCategory cat = new AdminCategory();

            cat.setId(2);
            AdminCategory[] catArray = new AdminCategory[1];
            catArray[0] = cat;
            prod.setCategories(catArray);

            AdminProductAttribute[] attrs = new AdminProductAttribute[2];

            AdminProductAttribute prodAttr1 = new AdminProductAttribute();
            prodAttr1.setOptionId(4);
            prodAttr1.setOptionValueId(1);
            prodAttr1.setPrice(new BigDecimal(20));
            prodAttr1.setPricePrefix("+");
            attrs[0] = prodAttr1;

            AdminProductAttribute prodAttr2 = new AdminProductAttribute();
            prodAttr2.setOptionId(3);
            prodAttr2.setOptionValueId(5);
            prodAttr2.setPrice(new BigDecimal(10));
            prodAttr2.setPricePrefix("-");
            attrs[1] = prodAttr2;

            prod.setAttributes(attrs);

            ProductIf productIf = getProductPerSku(sessionId, "123-XXX", 1);

            int productId = -1;
            if (productIf != null) {
                System.out.println("#######################################################");
                System.out.println("Updating existing product");
                System.out.println("Product Id: " + productIf.getId());
                System.out.println("#######################################################");

                prod.setId(productIf.getId());
                eng.editProduct(sessionId, prod);
            } else {
                System.out.println("#######################################################");
                System.out.println("Creating a new product");
                productId = eng.insertProduct(sessionId, prod);
                System.out.println("Product Id of inserted product = " + productId);
                System.out.println("#######################################################");
            }

            if (productIf == null) {
                prod = eng.getProduct(sessionId, productId);
                System.out.println("#######################################################");
                System.out.println("New product: " + prod.getId());
                System.out.println(prod.toStringBrief());
                System.out.println("#######################################################");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static AdminProduct getAdminProduct() {
        AdminProduct prod = new AdminProduct();
        prod.setManufacturerId(6);
        prod.setImage("test/300.jpg");
        prod.setModel("5D");
        prod.setPriceExTax(new BigDecimal(650));
        prod.setQuantity(20);
        prod.setStatus((byte) 1);
        prod.setTaxClassId(1);
        prod.setWeight(new BigDecimal(5));
        prod.setDateAvailable(new Date());
        prod.setCustom1("custom1");
        prod.setCustom2("custom2");
        prod.setCustom3("custom3");
        prod.setCustom4("custom4");
        prod.setCustom5("custom5");
        prod.setCanOrderWhenNotInStock(false);
        prod.setSku("123-XXX");
        return prod;
    }

    private static AdminProductDescription[] getAdminProductDescriptions() {
        AdminProductDescription[] descriptions = new AdminProductDescription[1];
        descriptions[0] = new AdminProductDescription();
        descriptions[0].setDescription("Canon EOS 5D - Full Frame Camera");
        descriptions[0].setLanguageId(1);
        descriptions[0].setName("Canon EOS 5D");
        descriptions[0].setUrl("www.nyherji.is");
        descriptions[0].setComparison("");
        return descriptions;
    }

    public static ProductIf getProductPerSku(String sessionId, String sku, int languageId) {
        ProductIf product = null;
        try {
            product = kkEng.getProductPerSku(sessionId, sku, languageId);
        } catch (KKException e) {
            e.printStackTrace();
        }
        return product;
    }
}
