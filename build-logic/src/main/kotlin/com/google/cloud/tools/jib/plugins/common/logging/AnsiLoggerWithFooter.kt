/*
 * Copyright 2018 Google LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.cloud.tools.jib.plugins.common.logging

import com.google.cloud.tools.jib.api.LogEvent
import com.google.cloud.tools.jib.plugins.common.logging.AnsiLoggerWithFooter
import com.google.cloud.tools.jib.plugins.common.logging.SingleThreadedExecutor

/**
 * Logs to a console supporting ANSI escape sequences and keeps an additional footer that always
 * appears below log messages.
 */
internal class AnsiLoggerWithFooter(
    messageConsumers: Map<LogEvent.Level?, (String) -> Unit>,
    singleThreadedExecutor: SingleThreadedExecutor,
    enableTwoCursorUpJump: Boolean
) : ConsoleLogger {
    private val messageConsumers: Map<LogEvent.Level?, (String) -> Unit>
    private val lifecycleConsumer: (String) -> Unit
    private val singleThreadedExecutor: SingleThreadedExecutor
    private var footerLines = emptyList<String>()

    // When a footer is erased, makes the logger go up two lines (and then down one line by calling
    // "accept()" once) before printing the next message. This is useful to correct an issue in Maven:
    // https://github.com/GoogleContainerTools/jib/issues/1952
    private val enableTwoCursorUpJump: Boolean

    /**
     * Creates a new [AnsiLoggerWithFooter].
     *
     * @param messageConsumers       map from each [Level] to a corresponding message logger
     * @param singleThreadedExecutor a [SingleThreadedExecutor] to ensure that all messages are
     * logged in a sequential, deterministic order
     * @param enableTwoCursorUpJump  allows the logger to move the cursor up twice at once. Fixes a
     * logging issue in Maven (https://github.com/GoogleContainerTools/jib/issues/1952) but causes
     * a problem in Gradle (https://github.com/GoogleContainerTools/jib/issues/1963)
     */
    init {
        lifecycleConsumer = requireNotNull(
            messageConsumers[LogEvent.Level.LIFECYCLE]
        ) {
            "Cannot construct AnsiLoggerFooter without LIFECYCLE message consumer"
        }
        this.messageConsumers = messageConsumers
        this.singleThreadedExecutor = singleThreadedExecutor
        this.enableTwoCursorUpJump = enableTwoCursorUpJump
    }

    override fun log(logLevel: LogEvent.Level, message: String) {
        val messageConsumer = messageConsumers[logLevel] ?: return
        singleThreadedExecutor.execute {
            val didErase = eraseFooter()
            // If a previous footer was erased, the message needs to go up a line.
            if (didErase) {
                if (enableTwoCursorUpJump) {
                    messageConsumer(String.format(CURSOR_UP_SEQUENCE_TEMPLATE, 2))
                    messageConsumer(message)
                } else {
                    messageConsumer(CURSOR_UP_SEQUENCE + message)
                }
            } else {
                messageConsumer(message)
            }
            printInBold(footerLines)
        }
    }

    /**
     * Sets the footer asynchronously. This will replace the previously-printed footer with the new
     * `footerLines`.
     *
     *
     * The footer is printed in **bold**.
     *
     * @param newFooterLines the footer, with each line as an element (no newline at end)
     */
    override fun setFooter(newFooterLines: List<String>) {
        val truncatedNewFooterLines = truncateToMaxWidth(newFooterLines)
        if (truncatedNewFooterLines == footerLines) {
            return
        }
        singleThreadedExecutor.execute {
            val didErase = eraseFooter()
            // If a previous footer was erased, the first new footer line needs to go up a line.
            if (didErase) {
                if (enableTwoCursorUpJump) {
                    lifecycleConsumer(String.format(CURSOR_UP_SEQUENCE_TEMPLATE, 2))
                    printInBold(truncatedNewFooterLines)
                } else {
                    printInBold(truncatedNewFooterLines, CURSOR_UP_SEQUENCE)
                }
            } else {
                printInBold(truncatedNewFooterLines)
            }
            footerLines = truncatedNewFooterLines
        }
    }

    /**
     * Erases the footer. Do *not* call outside of a task submitted to [ ][.singleThreadedExecutor].
     *
     * @return `true` if anything was erased; `false` otherwise
     */
    private fun eraseFooter(): Boolean {
        if (footerLines.isEmpty()) {
            return false
        }

        // Moves the cursor up to the start of the footer.
        val footerEraserBuilder =
            "${String.format(CURSOR_UP_SEQUENCE_TEMPLATE, footerLines.size)}$ERASE_DISPLAY_BELOW"
        lifecycleConsumer(footerEraserBuilder)
        return true
    }

    private fun printInBold(lines: List<String>, firstLinePrefix: String = "") {
        for (i in lines.indices) {
            lifecycleConsumer((if (i == 0) firstLinePrefix else "") + BOLD + lines[i] + UNBOLD)
        }
    }

    companion object {
        /**
         * Maximum width of a footer line. Having width too large can mess up the display when the console
         * width is too small.
         */
        private const val MAX_FOOTER_WIDTH = 50

        /**
         * ANSI escape sequence template for moving the cursor up multiple lines.
         */
        private const val CURSOR_UP_SEQUENCE_TEMPLATE = "\u001b[%dA"

        /**
         * ANSI escape sequence for moving the cursor up.
         */
        private val CURSOR_UP_SEQUENCE = String.format(CURSOR_UP_SEQUENCE_TEMPLATE, 1)

        /**
         * ANSI escape sequence for erasing to end of display.
         */
        private const val ERASE_DISPLAY_BELOW = "\u001b[0J"

        /**
         * ANSI escape sequence for setting all further characters to bold.
         */
        private const val BOLD = "\u001b[1m"

        /**
         * ANSI escape sequence for setting all further characters to not bold.
         */
        private const val UNBOLD = "\u001b[0m"

        /**
         * Makes sure each line of text in `lines` is at most [.MAX_FOOTER_WIDTH] characters
         * long. If a line of text exceeds [.MAX_FOOTER_WIDTH] characters, the line is truncated to
         * [.MAX_FOOTER_WIDTH] characters with the last 3 characters as `...`.
         *
         * @param lines the lines of text
         * @return the truncated lines of text
         */
        fun truncateToMaxWidth(lines: List<String>): List<String> = buildList {
            for (line in lines) {
                if (line.length > MAX_FOOTER_WIDTH) {
                    add(line.substring(0, MAX_FOOTER_WIDTH - 3) + "...")
                } else {
                    add(line)
                }
            }
        }
    }
}
