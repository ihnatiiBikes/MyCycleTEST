#ifndef MADISON_MAPPER_H
#define MADISON_MAPPER_H

#include <vector>
#include <string>
#include <unordered_map>

//static constexpr bool DEBUG_ENABLED = false;

class MadisonMapper
{
public:
    MadisonMapper();

    void MapFields(const std::vector<std::string>& fields,
                   std::unordered_map<std::string, std::string>& outputProduct, bool stockstatus);

private:
    std::unordered_map<std::string, std::string> categoryMap_;
    std::unordered_map<std::string, std::string> brandMap_;
    std::vector<std::string> requiredHeaders_;

    void InitializeCategoryMaps()
    {
        categoryMap_ = {
                {"spre", "356,332,257"},
                {"c/ri", "131,95,257"},
                {"f/bo", "87,257"},
                {"cabl", "142,95,257"},
                {"brbx", "96,257"},
                {"whel", "87,257"},
                {"b/ca", "164,112"},
                {"brl", "96"},
                {"batt", "119"},
                {"brak", "96,257"},
                {"part", "257"},
                {"cart", "257"},
                {"cera", "257"},
                {"chai", "141,95,257"},
                {"glas", "104,23"},
                {"lube", "143"},
                {"clea", "143"},
                {"cass", "139,95,257"},
                {"helm", "24,23"},
                {"frh", "87"},
                {"p/re", "99"},
                {"tyre", "88,257"},
                {"tool", "99"},
                {"c/se", "131,95,257"},
                {"cran", "131,95,257"},
                {"frm", "138,95,257"},
                {"rrh", "137,95,257"},
                {"rrm", "137,95,257"},
                {"nutr", "118,23"},
                {"gear", "95,257"},
                {"gpsc", "151,35"},
                {"h/se", "89"},
                {"glov", "80,23"},
                {"jack", "63,23"},
                {"gile", "63,23"},
                {"bags", "94"},
                {"bag", "94"},
                {"jers", "79,23"},
                {"lens", "104,23"},
                {"shor", "78,23"},
                {"trou", "76,23"},
                {"sock", "82,23"},
                {"m/gu", "101,257"},
                {"stem", "169,89,257"},
                {"tape", "213,90"},
                {"peda", "100,257"},
                {"laze", "312"},
                {"bmx", "356"},
                {"sti", "96,257"},
                {"slim", "363"},
                {"vitt", "372"},
                {"sadd", "109,93,257"},
                {"sint", "96,257"},
                {"adap", "96,257"},
                {"roto", "96,257"},
                {"flui", "143"},
                {"shif", "142,95,257"},
                {"s/po", "110,93,257"},
                {"car", "230"},
                {"mik", "230"},
                {"ch/s", "221"},
                {"grav", "106"},
                {"axle", "87,257"},
                {"trai", "39"},
                {"gpscc", "407"}
        };
    }


    void InitializeBrandMaps()
    {
        brandMap_ = {
                //Bikes
                {"adventure", "287"},
                {"aeroe", "288"},
                {"airpop", "289,23"},
                {"amplitude", "290"},
                {"atranvelo", "291"},
                {"axa", "292"},
                {"aztec", "293"},
                {"basil", "294"},
                {"bikefitting", "295"},
                {"crudproducts", "296"},
                {"dtswiss", "297,257"},
                {"dugast", "298,257"},
                {"dynaplug", "299"},
                {"elite", "300"},
                {"finishline", "301"},
                {"fisthandwear", "302,23"},
                {"garmin", "303,233"},
                {"gorilla", "304"},
                {"gtechniq", "305"},
                {"hump", "306"},
                {"infini", "307"},
                {"k-edge", "308"},
                {"kingston", "309"},
                {"knog", "310"},
                {"kryptonite", "311"},
                {"lazer", "312,23"},
                {"lifesystems", "313,23"},
                {"lifeventure", "314"},
                {"mpart", "315,257"},
                {"mpartwheels", "316,257"},
                {"madison", "317"},
                {"madisoneyewear", "318,23"},
                {"madisonsaddles", "319,257"},
                {"maxxis", "320,257"},
                {"motionpro", "321"},
                {"nutrak", "322,23"},
                {"odi", "323"},
                {"ogio", "324"},
                {"pro", "325,257"},
                {"parktool", "326"},
                {"petzl", "327"},
                {"profiledesign", "328"},
                {"quadlock", "329"},
                {"rematiptop", "330"},
                {"scienceinsport", "331"},
                {"shimano", "332"},
                {"shimano105", "333"},
                {"shimanoacera", "334"},
                {"shimanoalfine", "335"},
                {"shimanoalivio", "336"},
                {"shimanoaltus", "337"},
                {"shimanocues", "338"},
                {"shimanoclaris", "339"},
                {"shimanoclothing", "340,79,23"},
                {"shimanodxr", "341"},
                {"shimanodeore", "342"},
                {"shimanodeorext", "343"},
                {"shimanodura-ace", "344"},
                {"shimanodura-acetrack", "345"},
                {"shimanoessa", "346"},
                {"shimanogrx", "347"},
                {"shimanonon-seriesdi2", "349,232,257"},
                {"shimanonon-seriesmtb", "350"},
                {"shimanonon-seriesroad", "351"},
                {"shimanoslx", "352"},
                {"shimanosteps", "353,419"},
                {"shimanosaint", "354"},
                {"shimanosora", "355"},
                {"shimanospares", "356,257,257"},
                {"shimanostiagra", "357"},
                {"shimanotourney/ty", "358"},
                {"shimanoultegra", "359"},
                {"shimanoworkshop", "360,99"},
                {"shimanoxtr", "361"},
                {"shimanozee", "362"},
                {"slime", "363,257"},
                {"smartwasher", "364"},
                {"storm", "365"},
                {"toocycling", "366"},
                {"thule", "367"},
                {"thulechariot", "368"},
                {"trelock", "369"},
                {"truflo", "370"},
                {"urbaniki", "371"},
                {"vittoria", "372,257"},
                {"wheelsmanufacturing", "373,257"},
                {"whitelightning", "374"},
                {"coros", "405,406"}
        };
    }

    void InitializeRequiredHeaders()
    {
        requiredHeaders_ = {
                "Product", "Description", "Barcode", "Colour", "UOM", "Size", "Brand", "Hierachy",
                "SRP", "VatRate", "WebFriendlyDescription", "ImageFilename", "PackContents",
                "DivisionCode", "DiscontinuedFlag",
        };
    };
};

#endif