package com.helpdesk_ticketing_system.tickets_data_management.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;


@Component
class IdGenerator
{
    private final String TICKET_ID_PREFIX;
    private final Calendar calendar;
    private final Random random;
    private final Integer randomNumberRange;
    private final Integer num_of_digits_to_extract_from_p;

    @Autowired
    public IdGenerator(
            String solutionIdPrefix,
            Calendar calendar,
            Random random,
            Integer randomNumberRange,
            @Qualifier("digitsToExtract") Integer numOfDigitsToExtractFromP
    ) {
        TICKET_ID_PREFIX = solutionIdPrefix;
        this.calendar = calendar;
        this.random = random;
        this.randomNumberRange = randomNumberRange;
        num_of_digits_to_extract_from_p = numOfDigitsToExtractFromP;
    }

    public String generateId()
    {
        long p = System.currentTimeMillis();
        calendar.setTimeInMillis(p);

        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);

        int extractedDigitsOfPostedOn = (int) (p%Math.pow(10,num_of_digits_to_extract_from_p));
        int randomNumberSuffix = random.nextInt(randomNumberRange);

        // pattern : TIC <yyyy> <mm> <dd> <last few digits of postedOn> <random integer>
        AtomicReference<StringBuilder> issueID = new AtomicReference<>(new StringBuilder(TICKET_ID_PREFIX));
        issueID.get()
                .append(year).append(month).append(date)
                .append(extractedDigitsOfPostedOn)
                .append(randomNumberSuffix);

        return issueID.toString();
    }
}