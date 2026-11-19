#ifndef CSV_PROCESSOR_H
#define CSV_PROCESSOR_H

#include <fstream>
#include <string>
#include <vector>
#include <unordered_map>

#include "mappers/brompton_mapper.h"
#include "mappers/cyclex_mapper.h"
#include "mappers/cube_mapper.h"
#include "mappers/madison_mapper.h"
#include "output/outputFileManager.h"

class TrekMapper;

class CSVProcessor {
public:
    CSVProcessor(TrekMapper &trekMapper, MadisonMapper &madisonMapper, BromptonMapper &bromptonMapper,
                 CyclexMapper &cyclexMapper,
                 CubeMapper &cubeMapper);

    void ProcessTrekFiles(const std::string &trekFile, OutputFileManager &outputManager) const;

    void ProcessBromptonFiles(const std::string &bromptonFile, OutputFileManager &outputManager) const;

    void ProcessMadisonFiles(const std::string &madisonFile, OutputFileManager &outputManager) const;

    void ProcessCyclexFiles(const std::string &cyclexFile, OutputFileManager &outputManager) const;

    void ProcessCubeFiles(const std::string &cubeFile, OutputFileManager &outputManager) const;

    static void CheckCodes();

    void WriteOutputHeader(std::ostream &out) const;

    void ProcessTrekLine(const std::vector<std::string> &fields, std::ostream &outFile, int &errorCount) const;

    void ProcessMadisonLine(const std::vector<std::string> &fields, std::ostream &out) const;

    void ProcessCyclexLine(const std::vector<std::string> &fields, std::ostream &out) const;
    void ProcessCubeLine(const std::vector<std::string> &fields, std::ostream &out) const;

    void ProcessBromptonLine(const std::vector<std::string> &fields, std::ostream &outFile) const;

    void WriteOutputRow(std::ostream &outFile, const std::unordered_map<std::string, std::string> &product) const;

private:
    TrekMapper &trekMapper_;
    MadisonMapper &madisonMapper_;
    BromptonMapper &bromptonMapper_;
    CyclexMapper &cyclexMapper_;
    CubeMapper &cubeMapper_;
    const std::vector<std::string> outputHeaders_ = {
        "productcode", "vendor_partno", "productname", "displaybegindate", "displayenddate",
        "hideproduct", "stockstatus", "lastmodified", "options_cloned_from", "photos_cloned_from",
        "share_stockstatus_with", "lastmodby", "ischildofproductcode", "ischildofproductcode_productid",
        "options_cloned_from_productid", "photos_cloned_from_productid", "share_stockstatus_with_productid",
        "productpopularity", "homepage_section", "autodropship", "donotallowbackorders", "warehouselocation",
        "warehouseaisle", "warehousebin", "warehousecustom", "upc_code", "productkeywords", "productnameshort",
        "productweight", "freeshippingitem", "minqty", "maxqty", "recurringstartprice", "recurringstartduration",
        "recurringprice", "recurringhowoften", "recurringduration", "allowpriceedit", "productprice", "listprice",
        "saleprice", "taxableproduct", "selectedoptionids", "giftwrapcost", "downloadfile", "availability",
        "metatag_title", "metatag_description", "discountedprice_level1", "discountedprice_level2",
        "discountedprice_level3", "discountedprice_level4", "discountedprice_level5", "photo_subtext",
        "photo_alttext", "discountedrecurringprice_level1", "discountedrecurringprice_level2",
        "discountedrecurringprice_level3", "discountedrecurringprice_level4", "discountedrecurringprice_level5",
        "fixed_shippingcost", "fixed_shippingcost_outside_localregion", "uses_product_keytypes", "price_subtext",
        "price_subtext_short", "listprice_name", "productprice_name", "saleprice_name", "hide_yousave",
        "hide_freeaccessories", "setupcost", "setupcost_name", "discountedsetupcost_level1",
        "discountedsetupcost_level2",
        "discountedsetupcost_level3", "discountedsetupcost_level4", "discountedsetupcost_level5", "setupcost_title",
        "stockreorderqty", "warehouses", "affiliate_commissionable_value", "addtocartbtn_replacement_text", "book_isbn",
        "productcondition", "estship_ground", "estship_2ndday", "estship_overnight", "customfield1", "customfield2",
        "customfield3", "customfield4", "customfield5", "shoppingdotcom_category", "yahoo_category", "yahoo_medium",
        "productmanufacturer", "hide_when_outofstock", "stocklowqtyalarm", "addtopo_now", "lastpo_qty", "lastpo_date",
        "howtogetsaleprice", "inv_verify_qtyonhand", "inv_lastverified", "enablemultichildaddtocart", "vendor_price",
        "enableoptions_inventorycontrol", "photourl_small", "photourl_large", "private_section_customers_only",
        "quickbooks_item_accnt", "quickbooks_item_assetaccnt", "quickbooks_item_cogsaccnt", "vat_percentage",
        "recurring_pricing_text", "google_product_type", "length", "width", "height", "ships_by_itself", "package_type",
        "oversized", "additional_handling_indicator", "reward_points_given_for_purchase", "productdescriptionshort",
        "productdescription", "productfeatures", "techspecs", "extinfo", "orderfinished_note", "metatag_keywords",
        "productdescription_abovepricing", "custom_metatags_override", "ean", "google_product_category", "google_size",
        "google_color", "google_gender", "google_age_group", "google_availability", "google_pattern", "google_material",
        "estdaystoship", "google_unique_identifier_exists", "google_adult_product", "google_sizetype",
        "google_sizesystem",
        "nonshippable", "custom_label_0", "custom_label_1", "custom_label_2", "custom_label_3", "custom_label_4",
        "createddate", "mpn","hazardousproduct", "categoryids", "optionids", "accessories", "freeaccessories"
    };
};

#endif
