package org.approvaltests.reporters.macosx;

import org.approvaltests.reporters.GenericDiffReporter;

import java.text.MessageFormat;

/**
 * Copy of KDiff3Reporter with path for MacOS.
 */
public class KDiff3Reporter extends GenericDiffReporter {
    private static final String DIFF_PROGRAM = "/usr/local/bin/kdiff3";
    private static final String MESSAGE = MessageFormat.format("Unable to find KDiff3 at {0}", DIFF_PROGRAM);
    public static final KDiff3Reporter INSTANCE = new KDiff3Reporter();

    public KDiff3Reporter() {
        super(DIFF_PROGRAM, "%s %s -m", MESSAGE, GenericDiffReporter.TEXT_FILE_EXTENSIONS);
    }
}
