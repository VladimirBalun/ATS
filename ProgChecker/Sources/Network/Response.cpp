#include "Response.h"

namespace Network
{

     /**
     * The method creates response for client in JSON format.
     * @param result result of checking task of the client.
     * @return response for client in JSON format.
     */
    std::string Response::createResponse(int result)
    {
        boost::property_tree::ptree root;
        root.put("resultChecking", result);
        std::stringstream response;
        boost::property_tree::write_json(response, root);
        return response.str();
    }

}