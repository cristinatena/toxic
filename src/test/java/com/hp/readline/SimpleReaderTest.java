package com.hp.readline;

import com.hp.readfile.SimpleProcessor;
import junit.framework.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import java.io.IOException;

/**
 * Tests SimpleProcessor.
 */
public class SimpleReaderTest {
    @Test
    public void testTask() {
        String line = "R\t2011\tNO\tNO\tPRESIDENT\tCHRISTIAN NICKUM\tELECTRONIC\t2013-05-21\t83333RCKYM1030A\tROCKY MOUNTAIN HARDWARE INC (HAILEY)\t1020 AIRPORT WAY\tHAILEY\tBLAINE\tID\t83333\tROCKY MOUNTAIN HARDWARE INC (HAILEY)\t1020 AIRPORT WAY\tHAILEY\tID\t\t83333\tYES\tNO\tNO\tNO\tANNETTE GOICOECHEA\t2087882013\t\t\t\t\t\t\t\t332510\t\t\t\t\t\t43.511667\t-114.305833\t122334329\t\t\t\t\t\t\t\tROCKY MOUNTAIN HARDWARE INC\t\t1311209972025\tN100\tCOPPER COMPOUNDS\tTRI\tPounds\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tNO\tNO\tNO\tNO\tNO\tNO\tYES\tNO\tYES\tNO\tNO\tNO\tNO\tNO\t04\tNA\t\t0\t\t1.01\t\t1.01\tO \t1.01\tNA\t\t\t0\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t0\t0\tNA\t\t0\t\tNA\t\t0\t\t0\tNA\t\t0\t\tNA\t\t0\t\tNA\t\t0\t\t0\t\t0\t\tNA\t\t0\t\t0\t0\t\t0\t0\t0\t.17\t0\t0\t0\t0\t0\t0\t0\t0\t1004.7\t0\t1004.7\t0\t0\t13752.2\t0\t461.3\t0\t0\t0\t0\t0\t0\t0\t0\t0\t14213.5\t0\t0\t0\t0\tNA\t\t\t\tNA\t\t\t\t\t\t\t\t\t\tNA\t\t0\t\tNA\t\t0\t\t0\t0\t0\t0\tNO\tAGOICOECHEA@ROCKYMOUNTAINHARDWARE.COM\tRR5\t\tYES\t";
        SimpleProcessor.Task task = new SimpleProcessor.Task();
        int c = new SimpleProcessor.Task().perform(line);
        Assert.assertEquals(c, 1);
    }


    @Test
    public void testTaskNegativeFacility() {
        String line = "R	2011	NO	NO	PLANT MANAGER	ALBERT DADAH	ELECTRONIC	2012-06-26	29730MPNCB200IN	TE CONNECTIVITY	200 INTERCONNECT DR	ROCK HILL	YORK	SC	29730	TE CONNECTIVITY	200 INTERCONNECT DR	ROCK HILL	SC		29730	YES	NO	NO	NO	AL DADAH	8039858503								334417						34.88711	-80.99402	003012549								TE CONNECTIVITY LTD		1311209335900	N420	LEAD COMPOUNDS	PBT	Pounds																		YES	NO	YES	NO	NO	NO	YES	NO	NO	NO	NO	NO	NO	NO	01	NA		0		5		5	E2	5	NA			0																																	0	0	NA		0		NA		0		0	NA		0		NA		0		NA		0		0		0		NA		0		0	0		0	5	0	0	0	0	0	0	0	0	0	50	0	0	55	0	4	0	0	0	0	0	0	0	0	0	0	0	0	4	0	0	0	0	NA				NA										NA		0		NA		0		0	0	0	0	NO	DADAHA@TE.COM			YES	";
        SimpleProcessor.Task task = new SimpleProcessor.Task();
        int c = new SimpleProcessor.Task().perform(line);
        Assert.assertEquals(c,0);

    }

    @Test
    public void testTaskNegativeID() {
        String line = "R	2011	NO	NO	PLANT MANAGER	ALBERT DADAH	ELECTRONIC	2012-06-26	29730MPNCB200IN	TE CONNECTIVITY	200 INTERCONNECT DR	ROCK HILL	YORK	SC	29730	TE CONNECTIVITY	200 INTERCONNECT DR	ROCK HILL	SC		29730	YES	NO	NO	NO	AL DADAH	8039858503								334417						34.88711	-80.99402	003012549								TE CONNECTIVITY LTD		1311209335900	N420	LEAD COMPOUNDS	PBT	Pounds																		YES	NO	YES	NO	NO	NO	YES	NO	NO	NO	NO	NO	NO	NO	01	NA		0		5		5	E2	5	NA			0																																	0	0	NA		0		NA		0		0	NA		0		NA		0		NA		0		0		0		NA		0		0	0		0	5	0	0	0	0	0	0	0	0	0	50	0	0	55	0	4	0	0	0	0	0	0	0	0	0	0	0	0	4	0	0	0	0	NA				NA										NA		0		NA		0		0	0	0	0	NO	DADAHA@TE.COM			YES	";
        SimpleProcessor.Task task = new SimpleProcessor.Task();
        int c = new SimpleProcessor.Task().perform(line);
        Assert.assertEquals(c,0);

    }

    @Test
    public void testProcessor() throws IOException {
        SimpleProcessor p = new SimpleProcessor();
        int counter = 0;

        URL resource = getClass().getResource("/sample.log");
        try {
            File dataFile = new File(resource.toURI());
            counter = p.process(dataFile.getAbsolutePath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(2,counter);
    }

}
