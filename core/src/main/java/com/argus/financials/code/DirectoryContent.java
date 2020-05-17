/*
 * DirectoryContent.java
 *
 * Created on 10 January 2003, 15:50
 */

package com.argus.financials.code;

import java.io.File;
import java.util.Collection;
import java.util.TreeSet;

import com.argus.financials.api.code.CodeComparator;
import com.argus.util.ReferenceCode;

public class DirectoryContent extends BaseCode {

    private static Collection codes;

    /** Creates new InterestCategoryCode */
    public DirectoryContent(String directory) {
        codes = new TreeSet(new CodeComparator());
        initCodes(directory);
    }

    public Collection getCodes() {
        return codes;
    }

    public Object[] getCodeDescriptions() {

        Collection codes = getCodes();
        if (codes != null)
            return codes.toArray();

        return new String[0];

    }

    private void initCodes(String directory) {

        codes.add(new ReferenceCode(1, "", ""));

        File dir = new File(directory);

        if (dir == null)
            return;

        File[] files = dir.listFiles();

        if (files == null)
            return;
        for (int i = 0; i < files.length; i++) {
            codes.add(new ReferenceCode(i + 1000, files[i].toString(), files[i]
                    .getName()));
        }
    }

}
