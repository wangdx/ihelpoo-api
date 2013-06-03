package com.ihelpoo.api.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * record say list.
 * @author dongxu.wang@acm.org
 * @version 1
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RecordSayList {

    /**
     * nested list.
     */
    private List<RecordSay> list;

    /**
     * constructor.
     */
    public RecordSayList() {
        list = new ArrayList<RecordSay>();
    }

    /**
     * get list.
     * @return a list
     */
    public List<RecordSay> getList() {
        return list;
    }

    /**
     * set list.
     * @param list some list
     */
    public void setList(final List<RecordSay> list) {
        this.list = list;
    }

}
