#include "Server.h"

namespace Network
{

    /**
     * Main method of the server. Here all the components of the server is
     * initilizated, after initialization runs two thread for listening and
     * proccessing of the clients.
     * @param address ipV4 address for server.
     * @param port port for server.
     */
    void Server::start(const std::string& ipAddress, int port)
    {
        try
        {
            init(ipAddress, port);
            listen();
            handle();
        }
        catch (std::exception& e)
        {
            throw Exceptions::NetworkException("Unknown error... Server can't continue its work.");
        }
    }

    /**
     * The method runs server on selected address and port.
     * @param address ipV4 address for server.
     * @param port port for server.
     */
    void Server::init(const std::string& ipAddress, int port)
    {
        try
        {
            _ioService = std::make_unique<IOService>();
            _serverAddress = std::make_unique<ServerAddress>(ServerAddress::from_string(ipAddress));
            _acceptor = std::make_unique<Acceptor>(*_ioService, TCP::endpoint(*_serverAddress, port));
            LOG_INFO(__FILE__, "Server was started.");
        }
        catch (std::exception& e)
        {
            throw Exceptions::NetworkException("Server wasn't started... Incorrect ip address or port.");
        }
    }

    /**
     * The method listens incomer connections with clients. After connection,
     * current client is added in queue for further processing.
     */
    void Server::listen()
    {
        std::thread threadListener([&] {
            const short BUFF_SIZE = 4096;
            char buff[BUFF_SIZE];
            while (true)
            {
                try
                {
                    UPtrSocket clientSocket = std::make_unique<Socket>(*_ioService);
                    _acceptor->accept(*clientSocket);

                    size_t lengthBuff = clientSocket->read_some(boost::asio::buffer(buff));
                    SystemChecking::ISystem::SPtrTask taskClient = Request::parseRequest(std::string(buff).substr(0, lengthBuff));
                    UPtrClient client = std::make_unique<Client>(clientSocket, taskClient);

                    std::unique_lock<std::mutex> lock{_mutex};
                    _clients.push(std::move(client));
                    _conditionVar.notify_one();
                }
                catch(std::exception& e)
                {
                    LOG_WARNING(__FILE__, e.what());
                    std::cerr << "Incorrect request from user. Cause: " << e.what() << std::endl;
                }
            }
        });
        threadListener.detach();
    }

    /**
     * The method handles connected clients in another thread. Here calls system checking tasks
     * for checking task of client. After result is sent to the client.
     */
    void Server::handle()
    {
        std::thread threadHandler([&] {
            while (true)
            {
                std::unique_lock<std::mutex> lock{_mutex};
                _conditionVar.wait(lock);
                SystemChecking::EResultChecking resultChecking = _systemChecking->checkTask(_clients.front()->getTask());
                std::string response = Response::createResponse(resultChecking);
                boost::asio::write(*_clients.front()->getClientSocket(), boost::asio::buffer(response));
                _clients.pop();
            }
        });
        threadHandler.join();
    }

}