package ru.system.OLSystem.olympiad;

import ru.system.OLSystem.olympiad.pojo.Participant;

public interface Olympiad {

    void startOlympiad(String login);

    int checkProgram(String titleTask, String textProgram);

    Participant getCurrentParticipant();

    void finishOlympiad();

}
