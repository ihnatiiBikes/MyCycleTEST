#ifndef MYCYCLE_MAPPER_H
#define MYCYCLE_MAPPER_H

#include <string>
#include <vector>
#include <unordered_map>

class MyCycleMapper {
public:
    MyCycleMapper();
    void MapFields(const std::vector<std::string> &fields,
                   std::unordered_map<std::string, std::string> &outputProduct) const;

private:
    void InitializeRequiredHeaders();
    std::vector<std::string> requiredHeaders_;
};

#endif // MYCYCLE_MAPPER_H
