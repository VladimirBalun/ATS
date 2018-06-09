#ifndef _IINTERPRETER_H_
#define _IINTERPRETER_H_

#include <SystemChecking/Programs/IProgram.h>

namespace SystemChecking::Interpreters
{

    struct IInterpreter : public IProgram
    {
        typedef std::shared_ptr<Objects::TestData> SPtrTestData;
        int checkTask(const UPtrTask& task) override = 0;
        virtual int runProgram(const std::string& textProgram, std::vector<SPtrTestData>&& testDataForProgram) = 0;
    };

}

#endif
