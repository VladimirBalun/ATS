#ifndef _NONEXIST_LANGUAGE_EXCEPTION_H_
#define _NONEXIST_LANGUAGE_EXCEPTION_H_

#include "SystemCheckingException.h"

namespace Exceptions
{

    class NonExistLanguageException : public SystemCheckingException
    {
    public:
        NonExistLanguageException(const std::string& message) : SystemCheckingException(message) {}
        inline std::string what() override { return _message; }
    };

}

#endif
