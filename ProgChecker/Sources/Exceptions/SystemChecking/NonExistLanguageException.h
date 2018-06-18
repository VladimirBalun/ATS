#ifndef _NON_EXIST_LANGUAGE_EXCEPTION_H_
#define _NON_EXIST_LANGUAGE_EXCEPTION_H_

#include "SystemCheckingException.h"

namespace Exceptions
{

    /**
     * Exception is used when working with the ProgChecker
     * doesn't support selected language.
     */
    class NonExistLanguageException : public SystemCheckingException
    {
    public:
        NonExistLanguageException(const std::string& message) : SystemCheckingException(message) {}
        inline std::string what() override { return _message; }
    };

}

#endif