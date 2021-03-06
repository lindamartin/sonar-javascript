/*
 * SonarQube JavaScript Plugin
 * Copyright (C) 2011 SonarSource and Eriks Nukis
 * sonarqube@googlegroups.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.javascript.checks;

import org.junit.Test;
import org.sonar.javascript.checks.utils.TreeCheckTest;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

public class NewOperatorMisuseCheckTest extends TreeCheckTest {

  private NewOperatorMisuseCheck check = new NewOperatorMisuseCheck();

  @Test
  public void default_without_jsdoc() {
    CheckMessagesVerifier.verify(getIssues("src/test/resources/checks/newOperatorMisuse.js", check))
      .next().atLine(36)
      .next().atLine(37)
      .next().atLine(38)
      .next().atLine(43)
      .next().atLine(44)
    .noMore();
  }

  @Test
  public void custom_with_jsdoc() {
    check.considerJSDoc = true;

    CheckMessagesVerifier.verify(getIssues("src/test/resources/checks/newOperatorMisuse.js", check))
      .next().atLine(36)
      .next().atLine(37)
      .next().atLine(38)
      .next().atLine(43)
      .next().atLine(44)

      // Function without JSDoc @constructor tag
      .next().atLine(49)
      .next().atLine(50)
      .next().atLine(57) // False-positive=>SONARJS-454
      .next().atLine(58)
      .next().atLine(83)
      .noMore();

  }
}
